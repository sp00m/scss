/*
 * Copyright (c) boogiedev.com, all rights reserved.
 * This code is licensed under the LGPL 3.0 license,
 * available at the root application directory.
 */

package com.boogiedev.scss;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * SASS/SCSS compiler Ant task.
 */
public class ScssCompilerTask extends Task {

	/** The SASS/SCSS directory path, containing the SASS/SCSS file to compile. */
	private String scssDirPath;

	/** The CSS directory path, that will contain the CSS compiled files. */
	private String cssDirPath;

	/** Path to the SASS executable. */
	private File sassExe;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() throws BuildException {
		try {
			ScssCompiler scssCompiler = new ScssCompiler(scssDirPath, cssDirPath, sassExe);
			scssCompiler.setLogger(new TaskLogger(this));
			scssCompiler.compile();
		} catch (ScssCompilerException e) {
			throw new BuildException(e);
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
