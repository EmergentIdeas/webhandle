package com.emergentideas.webhandle.handlers;

import java.util.List;
import java.util.Map;

import com.emergentideas.webhandle.CallSpec;
import com.emergentideas.webhandle.OutputResponseInvestigator;
import com.emergentideas.webhandle.output.Respondent;

/**
 * Provides a configuration interface for the common web request, handle, response life cycle.
 * @author kolz
 *
 */
public interface ResponseLifecycleHandler extends Respondent {
	
	/**
	 * Returns a map where the key are the exceptions and the value is a call spec that will be 
	 * used to handle the exception of that type.
	 * @return
	 */
	public Map<Class, CallSpec> getExceptionHandlers();

	/**
	 * Gets the object that will determine which method to call for a given request
	 * @return
	 */
	public HandlerInvestigator getHandlerInvestigator();

	/**
	 * Sets the object that will determine which method to call for a given request
	 * @param handlerInvestigator
	 */
	public void setHandlerInvestigator(HandlerInvestigator handlerInvestigator);

	/**
	 * Get the object that will determine which templates if any should be used to
	 * respond to the request.
	 * @return
	 */
	public OutputResponseInvestigator getOutputInvestigator();

	/**
	 * Get the object that will determine which templates if any should be used to
	 * respond to the request.
	 * @param outputInvestigator
	 */
	public void setOutputInvestigator(OutputResponseInvestigator outputInvestigator);
	
	/**
	 * Gets the list of calls that will be made before the handler is chosen and invoked.
	 * @return
	 */
	public List<CallSpec> getPreRequestCalls();
	
	/**
	 * Gets the list of calls that will be made after the handler has been invoked but
	 * before the response is created.  At this point, it is still possible to throw
	 * an exception which changes the output shown to the user.
	 * @return
	 */
	public List<CallSpec> getPreResponseCalls();
	
	/**
	 * Gets the list of calls that will be made after the response is generated and
	 * committed.  Any exceptions thrown here can not be shown to the user.
	 * @return
	 */
	public List<CallSpec> getPostResponseCalls();
	
	/**
	 * If no handler can be found which does not return an error or an exception is thrown
	 * during the handling, an Exception handler will be chosen.  Before that exception
	 * handler is invoked these calls will be.
	 * @return
	 */
	public List<CallSpec> getNormalHanlderFailedCalls();

}
