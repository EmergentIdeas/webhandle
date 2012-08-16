package com.emergentideas.logging;

/**
 * Provides a very generic way to capture msgs.
 * @author kolz
 *
 */
public interface Logger {
	
	public void error(String msg);
	
	public void warn(String msg);
	
	public void info(String msg);
	
	public void debug(String msg);
	
	public void error(String msg, Throwable t);
	
	public void warn(String msg, Throwable t);
	
}
