package com.emergentideas.location;

/**
 * A class capable of producing values by name or type.  This is the case when we already have a value
 * or are creating one without input from something like a request.  If we have an indication of some
 * sort of value that should be loaded, perhaps a ValutTransformer is what you want. 
 * @author kolz
 *
 * @param <E>
 */
public interface ValueSource<E> {

	/**
	 * Returns a value for the name or type.  If a name is passed, the type returned need not be 
	 * a value that is assignable to T
	 * @param name
	 * @param type
	 * @param context
	 * @return
	 */
	public <T extends E> E get(String name, Class<T> type, InvocationContext context);
	
	public <T> boolean canGet(String name, Class<T> type, InvocationContext context);
}
