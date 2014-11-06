package com.emergentideas.webhandle.eventbus;

import org.junit.Test;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.CallSpec;

import static org.junit.Assert.*;

public class SimpleEventBusTest {

	
	@Test
	public void determineFirstParameterType() throws Exception {
		SimpleEventBus bus = new SimpleEventBus();
		Listener1 l1 = new Listener1();
		CallSpec spec = ReflectionUtils.getFirstMethodCallSpec(l1, "listen");
		assertEquals(Number.class, bus.determineFirstParameterType(spec));
		assertEquals("n", bus.determineFirstParameterName(spec, Number.class));
	}

	@Test
	public void listenerNoEvent() throws Exception {
		SimpleEventBus bus = new SimpleEventBus();
		Listener1 l1 = new Listener1();
		bus.register(l1);
		assertFalse(l1.isTriggered());
		bus.emit("/hello", null);
		assertTrue(l1.isTriggered());
	}

	@Test
	public void listenerWithEvent() throws Exception {
		SimpleEventBus bus = new SimpleEventBus();
		Listener1 l1 = new Listener1();
		bus.register(l1);
		assertFalse(l1.isTriggered());
		bus.emit("/hello", new Integer(42));
		assertTrue(l1.isTriggered());
	}
	
	@Test
	public void listenerWrongEvent() throws Exception {
		SimpleEventBus bus = new SimpleEventBus();
		Listener1 l1 = new Listener1();
		bus.register(l1);
		assertFalse(l1.isTriggered());
		bus.emit("/hello", "world");
		assertFalse(l1.isTriggered());
	}
	
	@Test
	public void noArgumentListener() throws Exception {
		SimpleEventBus bus = new SimpleEventBus();
		NoEventListener l1 = new NoEventListener();
		bus.register(l1);
		assertFalse(l1.isTriggered());
		bus.emit("/hello", "world");
		assertTrue(l1.isTriggered());
		
		bus = new SimpleEventBus();
		l1 = new NoEventListener();
		bus.register(l1);
		assertFalse(l1.isTriggered());
		bus.emit("/hello", null);
		assertTrue(l1.isTriggered());
	}
	
	@Test
	public void testArgumentsListener() throws Exception {
		SimpleEventBus bus = new SimpleEventBus();
		
		ParameterListener l1 = new ParameterListener();
		bus.register(l1);
		
		bus.emit("/hello/3", new Double(4));
		
		assertEquals(3, (int)l1.getTwo());
		assertEquals("hello", l1.getOne());
		assertEquals(4.0, l1.getEvent());
		
	}

	@Test
	public void testMultiCallListener() throws Exception {
		SimpleEventBus bus = new SimpleEventBus();
		MultiCallListener l = new MultiCallListener();
		
		bus.register(l);
		
		bus.emit("/one", null);
		assertEquals(1, l.getCount());
	}

	@Test
	public void testMultiMethodListener() throws Exception {
		SimpleEventBus bus = new SimpleEventBus();
		MultiCallMultiMethodListener l = new MultiCallMultiMethodListener();
		
		bus.register(l);
		
		bus.emit("/one", null);
		assertEquals(1, l.getCount());
	}
	

	@Test
	public void testRemoveListener() throws Exception {
		SimpleEventBus bus = new SimpleEventBus();
		MultiCallListener l = new MultiCallListener();
		
		bus.register(l);
		
		bus.emit("/one", null);
		assertEquals(1, l.getCount());
		
		bus.emit("/one", null);
		assertEquals(2, l.getCount());
		
		bus.unregister(l);
		bus.emit("/one", null);
		assertEquals(2, l.getCount());
		
	}

}
