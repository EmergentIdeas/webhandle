package com.emergentideas.webhandle.json;

import javax.annotation.Resource;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;

@Integrate
public class JSONSerializerIntegrator implements Integrator {

	@Resource
	protected AnnotationDrivenJSONSerializer annotationDrivenJSONSerializer;
	
	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(focus != null) {
			JSONSerializer json = ReflectionUtils.getAnnotationOnClass(focus.getClass(), JSONSerializer.class);
			if(json != null && focus instanceof ObjectSerializer) {
				annotationDrivenJSONSerializer.add((ObjectSerializer)focus);
			}
		}
	}

	public AnnotationDrivenJSONSerializer getAnnotationDrivenJSONSerializer() {
		return annotationDrivenJSONSerializer;
	}

	public void setAnnotationDrivenJSONSerializer(
			AnnotationDrivenJSONSerializer annotationDrivenJSONSerializer) {
		this.annotationDrivenJSONSerializer = annotationDrivenJSONSerializer;
	}
}
