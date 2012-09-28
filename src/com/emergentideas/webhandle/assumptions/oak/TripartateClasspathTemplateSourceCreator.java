package com.emergentideas.webhandle.assumptions.oak;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Create;
import com.emergentideas.webhandle.bootstrap.Creator;
import com.emergentideas.webhandle.bootstrap.Loader;
import com.emergentideas.webhandle.templates.TripartateClasspathTemplateSource;

@Create("template-classpath")
public class TripartateClasspathTemplateSourceCreator implements Creator {

	public Object create(Loader loader, Location location,
			ConfigurationAtom atom) {
		return new TripartateClasspathTemplateSource(atom.getValue());
	}

}
