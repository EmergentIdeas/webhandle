package com.emergentideas.webhandle.bootstrap;

import java.util.regex.Pattern;

/**
 * Holds information about an atomizer which can be used to parse configuration lines.  Each
 * configuration has a pattern which match the type part of the configuration.  If it
 * matches, the {@link Atomizer} will then be applied and a {@link ConfigurationAtom}
 * created.
 * @author kolz
 *
 */
public class AtomizerConfiguration {
	
	protected Pattern typePattern;
	
	protected Atomizer atomizer;

	public Pattern getTypePattern() {
		return typePattern;
	}

	public void setTypePattern(Pattern typePattern) {
		this.typePattern = typePattern;
	}

	public Atomizer getAtomizer() {
		return atomizer;
	}

	public void setAtomizer(Atomizer atomizer) {
		this.atomizer = atomizer;
	}
	
	

}
