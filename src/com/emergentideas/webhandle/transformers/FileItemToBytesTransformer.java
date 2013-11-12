package com.emergentideas.webhandle.transformers;

import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueTransformer;
import com.emergentideas.webhandle.exceptions.TransformationException;

/**
 * Converts a {@link FileItem} content to bytes, providing the content.
 * @author kolz
 *
 */
public class FileItemToBytesTransformer implements ValueTransformer<String, FileItem, Byte[]> {

	public Byte[] transform(InvocationContext context,
			Map<String, String> transformationProperties,
			Class finalParameterClass, String parameterName, FileItem... source)
			throws TransformationException {
		
		if(source != null) {
			if(source.length > 0) {
				return convert(source[0].get());
			}
		}
		
		return null;
	}

	protected Byte[] convert(byte[] data) {
		Byte[] result = new Byte[data.length];
		for(int i = 0; i < result.length; i++) {
			result[i] = Byte.valueOf(data[i]);
		}
		return result;
	}
	

}
