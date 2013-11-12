package com.emergentideas.webhandle.transformers;

import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueTransformer;
import com.emergentideas.webhandle.exceptions.TransformationException;

/**
 * Converts a {@link FileItem} to String, providing the file name
 * @author kolz
 *
 */
public class FileItemToStringTransformer implements ValueTransformer<String, FileItem, String[]> {

	public String[] transform(InvocationContext context,
			Map<String, String> transformationProperties,
			Class finalParameterClass, String parameterName, FileItem... source)
			throws TransformationException {
		
		String[] result = new String[source.length];
		for(int i = 0; i < source.length; i++) {
			result[i] = source[i].getName();
		}
		
		return result;
	}

	
	

}
