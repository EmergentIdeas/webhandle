package com.emergentideas.webhandle.bootstrap;

import java.util.regex.Pattern;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.Location;

@Integrate
public class AtomizerIntegrator implements Integrator {

	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(focus == null) {
			return;
		}
		
		Atomize anno = ReflectionUtils.getAnnotationOnClass(focus.getClass(), Atomize.class);
		if(anno != null && focus instanceof Atomizer) {
			for(String regex : anno.value()) {
				AtomizerConfiguration conf = new AtomizerConfiguration();
				conf.setTypePattern(Pattern.compile(regex));
				conf.setAtomizer((Atomizer)focus);
				loader.getAtomizerConfigurations().add(0, conf);
			}
		}
	}

}
