# SASS/SCSS Smart Compiler

## Description

Compiles a set of SASS/SCSS files to CSS files.

Since built through an Ant task, it can be easily added to Eclipse so that it compiles SASS/SCSS files on the fly (see [http://help.eclipse.org](http://help.eclipse.org/luna/topic/org.eclipse.platform.doc.user/gettingStarted/qs-93_project_builder.htm)).

## How-to

Build a JAR from these sources.

Create a target:

	<target name="scss-compile">
		<taskdef name="scssCompiler" classname="com.boogiedev.scss.ScssCompiler" classpath="path/to/ScssCompiler.jar" />
		<scssCompiler cssDir="${css.dir}" scssDir="${scss.dir}" sassExe="${sass.exe}" />
	</target>
