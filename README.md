# SASS/SCSS Smart Compiler

## Description

Compiles a set of SASS/SCSS files to CSS files.

Since built through an Ant task, it can be easily added to Eclipse so that it compiles SASS/SCSS files on the fly (see [http://help.eclipse.org](http://help.eclipse.org/luna/topic/org.eclipse.platform.doc.user/gettingStarted/qs-93_project_builder.htm)).

## How-to

Build a JAR from these sources.

Create a target:

	<target name="scss-compile">
		<taskdef name="scssCompile" classname="com.boogiedev.scss.ScssCompilerTask" classpath="ant/boogiedev-scss-1.1.jar" />
		<scssCompile cssDir="${css.dir}" scssDir="${scss.dir}" sassExe="${sass.exe}" />
	</target>
