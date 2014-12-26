/*
 * Copyright (c) boogiedev.com, all rights reserved.
 * This code is licensed under the LGPL 3.0 license,
 * available at the root application directory.
 */

package com.boogiedev.scss;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.slf4j.helpers.MarkerIgnoringBase;

/**
 * Ant Task logger:
 * <ul>
 * <li>Calls {@link Task#log(String, int)} or {@link Task#log(String, Throwable, int)}.</li>
 * <li>Uses {@link String#format(String, Object...)} when formatting a string template with arguments.</li>
 * </ul>
 */
public class TaskLogger extends MarkerIgnoringBase {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/** The Ant task. */
	private final Task task;

	/**
	 * Constructor.
	 * 
	 * @param task
	 *          The Ant task.
	 */
	public TaskLogger(Task task) {
		this.task = task;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTraceEnabled() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String msg) {
		task.log(msg, Project.MSG_VERBOSE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String format, Object arg) {
		trace(String.format(format, arg));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String format, Object arg1, Object arg2) {
		trace(String.format(format, arg1, arg2));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String format, Object... arguments) {
		trace(String.format(format, arguments));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String msg, Throwable t) {
		task.log(msg, t, Project.MSG_VERBOSE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(String msg) {
		task.log(msg, Project.MSG_DEBUG);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(String format, Object arg) {
		debug(String.format(format, arg));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(String format, Object arg1, Object arg2) {
		debug(String.format(format, arg1, arg2));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(String format, Object... arguments) {
		debug(String.format(format, arguments));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(String msg, Throwable t) {
		task.log(msg, t, Project.MSG_DEBUG);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInfoEnabled() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String msg) {
		task.log(msg, Project.MSG_INFO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String format, Object arg) {
		info(String.format(format, arg));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String format, Object arg1, Object arg2) {
		info(String.format(format, arg1, arg2));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String format, Object... arguments) {
		info(String.format(format, arguments));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String msg, Throwable t) {
		task.log(msg, t, Project.MSG_INFO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWarnEnabled() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String msg) {
		task.log(msg, Project.MSG_WARN);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String format, Object arg) {
		warn(String.format(format, arg));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String format, Object arg1, Object arg2) {
		warn(String.format(format, arg1, arg2));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String format, Object... arguments) {
		warn(String.format(format, arguments));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String msg, Throwable t) {
		task.log(msg, t, Project.MSG_WARN);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isErrorEnabled() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String msg) {
		task.log(msg, Project.MSG_ERR);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String format, Object arg) {
		error(String.format(format, arg));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String format, Object arg1, Object arg2) {
		error(String.format(format, arg1, arg2));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String format, Object... arguments) {
		error(String.format(format, arguments));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String msg, Throwable t) {
		task.log(msg, t, Project.MSG_ERR);
	}

}
