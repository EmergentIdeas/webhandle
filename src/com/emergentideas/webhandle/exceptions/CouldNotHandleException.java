package com.emergentideas.webhandle.exceptions;

/**
 * An exception to indicate that the handler could not actually handle the request.  This is 
 * a sort of no-fault exception indicating that it was correctly called and claimed that it
 * could handle the url but after investigating whatever data sources it uses has determined
 * that somebody else could probably do a better job.  This can be used when multiple handlers
 * may have to share handling for the same name space, for example, html documents served from 
 * a file system or a database sharing the same name space.
 * @author kolz
 *
 */
public class CouldNotHandleException extends RuntimeException {

}
