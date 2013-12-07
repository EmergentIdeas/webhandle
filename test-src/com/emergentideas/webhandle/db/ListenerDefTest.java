package com.emergentideas.webhandle.db;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.emergentideas.webhandle.db.ObjectChangeListener.ChangeType;

public class ListenerDefTest {
	
	@Test
	public void testClassChoices() {
		
		final List<String> done = new ArrayList<String>();
		
		
		ObjectEventInterchange oei = new ObjectEventInterchange();
		
		ObjectChangeListener<Number> ocl1 = new ObjectChangeListener<Number>() {
			@Override
			public ChangeType[] listenFor() {
				return new ChangeType[] {ChangeType.POST_PERSIST};
			}

			@Override
			public void changeEvent(ChangeType type, Number t) {
				done.add("one");
			}
		};
		
		oei.addListener(ocl1);
		
		ObjectChangeListener<Integer> ocl2 = new ObjectChangeListener<Integer>() {
			@Override
			public ChangeType[] listenFor() {
				return new ChangeType[] {ChangeType.POST_PERSIST};
			}

			@Override
			public void changeEvent(ChangeType type, Integer t) {
				done.add("two");
			}
		};
		
		oei.addListener(ocl2);
		
		oei.changeEvent(ChangeType.POST_PERSIST, new Integer(3));
		
		assertEquals(2, done.size());

		done.clear();
		
		oei.changeEvent(ChangeType.POST_PERSIST, new Double(3));
		
		assertEquals(1, done.size());
		
	}
}
