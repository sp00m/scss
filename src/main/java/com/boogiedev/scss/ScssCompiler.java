/*
 * Copyright (c) boogiedev.com, all rights reserved.
 * This code is licensed under the LGPL 3.0 license,
 * available at the root application directory.
 */

package com.boogiedev.scss;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SASS/SCSS compiler.
 */
public class ScssCompiler {

	/** Default logger. */
	private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(ScssCompiler.class);

	/** File separator, defaulted to "/". */
	private static final String FILE_SEPARATOR = System.getProperty("file.separator", "/");

	/** The SASS/SCSS directory path, containing the SASS/SCSS file to compile. */
	private final String scssDirPath;

	/** The CSS directory path, that will contain the CSS compiled files. */
	private final String cssDirPath;

	/** Path to the SASS executable. */
	private final File sassExe;

	/** Logger. */
	private Logger logger;

	/**
	 * Constructor.
	 *
	 * @param scssDirPath
	 *          The SASS/SCSS directory path, that will contain the SASS/SCSS compiled files.
	 * @param cssDirPath
	 *          The CSS directory path, containing the file the compile.
	 * @param sassExe
	 *          Path to the SASS executable.
	 */
	public ScssCompiler(String scssDirPath, String cssDirPath, File sassExe) {
		this.scssDirPath = scssDirPath;
		this.cssDirPath = cssDirPath;
		this.sassExe = sassExe;
		logger = DEFAULT_LOGGER;
	}

	/**
	 * Launches SASS/SCSS compilation.
	 *
	 * @throws ScssCompilerException
	 *           If an error occurred while compiling a file.
	 */
	public void compile() throws ScssCompilerException {
		List<File> cssFiles = scanAndCompile(FILE_SEPARATOR);
		cleanCssFiles(new File(cssDirPath), cssFiles);
		File cacheDir = new File(".sass-cache");
		if (cacheDir.exists()) {
			delete(cacheDir);
		}
	}

	/**
	 * Recursively scans the directory, and compiles any SASS/SCSS file if needed (based on the last modified date).
	 *
	 * @param scssDir
	 *          The directory containing SASS/SCSS files.
	 * @return The compiled (or already existing) corresponding CSS files.
	 * @throws ScssCompilerException
	 *           If an error occurred while compiling a file.
	 */
	private List<File> scanAndCompile(String scssDir) throws ScssCompilerException {
		List<File> cssFiles = new ArrayList<>();
		for (File scssFile : new File(scssDirPath + scssDir).listFiles()) {
			if (scssFile.isDirectory()) {
				cssFiles.addAll(scanAndCompile(scssDir + scssFile.getName() + FILE_SEPARATOR));
			} else {
				String scssFileName = scssFile.getName();
				if (scssFileName.matches("[^_].*(?i)\\.s[ac]ss")) {
					File cssFile = new File(cssDirPath + scssDir + scssFileName.replaceAll("(?i)\\.s[ac]ss$", ".css"));
					if (!cssFile.exists() || scssFile.lastModified() > cssFile.lastModified()) {
						compile(scssFile, cssFile);
					} else {
						logger.info("Already up-to-date: " + cssFile.getAbsolutePath());
					}
					cssFiles.add(cssFile);
				}
			}
		}
		return cssFiles;
	}

	/**
	 * Compiles a SASS/SCSS file to a CSS file.
	 *
	 * @param scssFile
	 *          The SASS/SCSS file to compile.
	 * @param cssFile
	 *          The output CSS file to create.
	 * @throws ScssCompilerException
	 *           If an error occurred while compiling the file.
	 */
	private void compile(File scssFile, File cssFile) throws ScssCompilerException {
		prepare(cssFile);
		String scssFilePath = scssFile.getAbsolutePath();
		String cssFilePath = cssFile.getAbsolutePath();
		ProcessBuilder builder = new ProcessBuilder(sassExe.getAbsolutePath(), scssFilePath, cssFilePath);
		builder.directory(sassExe.getParentFile());
		try {
			Process process = builder.start();
			int exitValue = process.waitFor();
			if (exitValue != 0) {
				logErrorStream(process);
				throw new ScssCompilerException("Compiling " + scssFilePath + " to " + cssFilePath + " exited with code " + exitValue);
			}
		} catch (IOException | InterruptedException e) {
			throw new ScssCompilerException("An error occurred while compiling " + scssFilePath + " to " + cssFilePath, e);
		}
		logger.info("Compiled: " + cssFilePath);
	}

	/**
	 * Creates a file and its parent directory if needed.
	 *
	 * @param cssFile
	 *          The file to create.
	 * @throws ScssCompilerException
	 *           If an error occurred while initializing the CSS file.
	 */
	private void prepare(File cssFile) throws ScssCompilerException {
		try {
			cssFile.getParentFile().mkdirs();
			cssFile.createNewFile();
		} catch (IOException e) {
			throw new ScssCompilerException("An error occurred while creating " + cssFile, e);
		}
	}

	/**
	 * Cleans the CSS directory: delete any found CSS file if it has not been generated during this compilation.
	 *
	 * @param cssDir
	 *          The CSS directory.
	 * @param cssFiles
	 *          The CSS files that have been generated during this compilation.
	 * @throws ScssCompilerException
	 *           If an error occurred while deleting a CSS file/directory.
	 */
	private void cleanCssFiles(File cssDir, List<File> cssFiles) throws ScssCompilerException {
		if (cssDir.isHidden() || cssDir.getName().startsWith(".")) {
			return;
		}
		for (File cssFile : cssDir.listFiles()) {
			if (cssFile.isDirectory()) {
				cleanCssFiles(cssFile, cssFiles);
			} else if (cssFile.getName().matches(".*(?i)\\.css") && !cssFiles.contains(cssFile)) {
				if (!cssFile.delete()) {
					throw new ScssCompilerException("Could not delete file " + cssFile);
				}
				logger.info("Deleted: " + cssFile.getAbsolutePath());
			}
		}
		if (cssDir.list().length == 0) {
			if (!cssDir.delete()) {
				throw new ScssCompilerException("Could not delete dir " + cssDir);
			}
			logger.info("Deleted: " + cssDir.getAbsolutePath());
		}
	}

	/**
	 * Recursively deletes a directory.
	 *
	 * @param dir
	 *          The directory to delete.
	 * @throws ScssCompilerException
	 *           If an error occurred while deleting the directory.
	 */
	private void delete(File dir) throws ScssCompilerException {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				delete(file);
			} else {
				if (!file.delete()) {
					throw new ScssCompilerException("Could not delete file " + file);
				}
			}
		}
		if (!dir.delete()) {
			throw new ScssCompilerException("Could not delete dir " + dir);
		}
	}

	/**
	 * Logs a process error stream.
	 *
	 * @param process
	 *          The process from which the error stream will be read.
	 */
	private void logErrorStream(Process process) {
		try (Scanner scanner = new Scanner(process.getErrorStream())) {
			scanner.useDelimiter("\\A");
			while (scanner.hasNext()) {
				logger.error(scanner.next());
			}
		}
	}

	/**
	 * Sets the logger to use.
	 *
	 * @param logger
	 *          The logger to use.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
