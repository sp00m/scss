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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * SASS/SCSS compiler Ant task.
 */
public class ScssCompiler extends Task {

	/** File separator, defaulted to "/". */
	private static final String FILE_SEPARATOR = System.getProperty("file.separator", "/");

	/** The CSS directory path, containing the file the compile. */
	private String cssDirPath;

	/** Path to the SASS executable. */
	private File sassExe;

	/** The SASS/SCSS directory path, that will contain the SASS/SCSS compiled files. */
	private String scssDirPath;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() throws BuildException {
		try {
			List<File> cssFiles = scanAndCompile(FILE_SEPARATOR);
			cleanCssFiles(new File(cssDirPath), cssFiles);
			File cacheDir = new File(".sass-cache");
			if (cacheDir.exists()) {
				delete(cacheDir);
			}
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

	/**
	 * Recursively scans the directory, and compiles any SASS/SCSS file if needed (based on the last modified date).
	 *
	 * @param scssDir
	 *          The directory containing SASS/SCSS files.
	 * @return The compiled (or already existing) corresponding CSS files.
	 * @throws Exception
	 *           If an error occurred while compiling.
	 */
	private List<File> scanAndCompile(String scssDir) throws Exception {
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
						log("Already up-to-date: " + cssFile.getAbsolutePath());
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
	 * @throws Exception
	 *           If an error occurred while compiling.
	 */
	private void compile(File scssFile, File cssFile) throws Exception {
		prepare(cssFile);
		String scssFilePath = scssFile.getAbsolutePath();
		String cssFilePath = cssFile.getAbsolutePath();
		ProcessBuilder builder = new ProcessBuilder(sassExe.getAbsolutePath(), scssFilePath, cssFilePath);
		builder.directory(sassExe.getParentFile());
		Process process = builder.start();
		int exitValue = process.waitFor();
		if (exitValue != 0) {
			logErrorStream(process);
			throw new IllegalStateException("Compiling " + scssFilePath + " to " + cssFilePath + " exited with code " + exitValue);
		}
		log("Compiled: " + cssFilePath);
	}

	/**
	 * Creates a file and its parent directory if needed.
	 *
	 * @param cssFile
	 *          The file to create.
	 * @throws IOException
	 *           If an error occured while creating the file.
	 */
	private void prepare(File cssFile) throws IOException {
		cssFile.getParentFile().mkdirs();
		cssFile.createNewFile();
	}

	/**
	 * Cleans the CSS directory: delete any found CSS file if it has not been generated during this compilation.
	 *
	 * @param cssDir
	 *          The CSS directory.
	 * @param cssFiles
	 *          The CSS files that have been generated during this compilation.
	 * @throws Exception
	 *           If an error occurred while deleting the directory.
	 */
	private void cleanCssFiles(File cssDir, List<File> cssFiles) throws Exception {
		if (cssDir.isHidden() || cssDir.getName().startsWith(".")) {
			return;
		}
		for (File cssFile : cssDir.listFiles()) {
			if (cssFile.isDirectory()) {
				cleanCssFiles(cssFile, cssFiles);
			} else if (cssFile.getName().matches(".*(?i)\\.css") && !cssFiles.contains(cssFile)) {
				if (!cssFile.delete()) {
					throw new IllegalStateException("Could not delete file " + cssFile.getAbsolutePath());
				}
				log("Deleted: " + cssFile.getAbsolutePath());
			}
		}
		if (cssDir.list().length == 0) {
			if (!cssDir.delete()) {
				throw new IllegalStateException("Could not delete dir " + cssDir.getAbsolutePath());
			}
			log("Deleted: " + cssDir.getAbsolutePath());
		}
	}

	/**
	 * Recursively deletes a directory.
	 *
	 * @param dir
	 *          The directory to delete.
	 */
	private void delete(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				delete(file);
			} else {
				if (!file.delete()) {
					throw new IllegalStateException("Could not delete file " + file.getAbsolutePath());
				}
			}
		}
		if (!dir.delete()) {
			throw new IllegalStateException("Could not delete dir " + dir.getAbsolutePath());
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
			if (scanner.useDelimiter("\\A").hasNext()) {
				log(scanner.next(), Project.MSG_ERR);
			}
		}
	}

	/**
	 * Sets the CSS directory path, containing the file the compile.
	 *
	 * @param cssDirPath
	 *          The CSS directory path, containing the file the compile.
	 */
	public void setCssDir(String cssDirPath) {
		this.cssDirPath = cssDirPath.trim().replaceAll("[/\\\\]$", "");
	}

	/**
	 * Sets the path to the SASS executable.
	 *
	 * @param cssDirPath
	 *          The path to the SASS executable.
	 */
	public void setSassExe(String sassExe) {
		this.sassExe = new File(sassExe);
	}

	/**
	 * Sets the SASS/SCSS directory path, that will contain the SASS/SCSS compiled files.
	 *
	 * @param cssDirPath
	 *          The SASS/SCSS directory path, that will contain the SASS/SCSS compiled files.
	 */
	public void setScssDir(String scssDirPath) {
		this.scssDirPath = scssDirPath.trim().replaceAll("[/\\\\]$", "");
	}

}
