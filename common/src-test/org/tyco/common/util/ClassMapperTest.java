package org.tyco.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.tyco.common.util.ClassMapper.getMRO;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class ClassMapperTest {
	ClassMapper<Object> map;
	
	@SuppressWarnings("boxing")
	@Test
	public void empty() {
		map = new ClassMapper<Object>();
		try {
			map.get(null);
			fail();
		} catch (NullPointerException e) {/* exception should be thrown */}
		
		assertNull(map.get(1));
		assertNull(map.get("a"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void mro() {
		assertEquals(Arrays.asList(Integer.class, Comparable.class,
				Number.class, Serializable.class),
				getMRO(Integer.class));
		assertEquals(Arrays.asList(String.class, Serializable.class,
				Comparable.class, CharSequence.class), getMRO(String.class));
	}
	
	@SuppressWarnings({ "boxing", "unchecked" })
	@Test
	public void simpleTypes() {
		map = new ClassMapper<Object>();
		assertNull(map.get(1));
		
		map.register(Integer.class, "foo");
		assertSame("foo", map.get(1));
		assertSame("foo", map.get(10));
		assertNull(map.get("bar"));
		
		map.register(Map.class, "bar");
		assertSame("bar", map.get(new HashMap()));
		assertSame("bar", map.get(new TreeMap()));
		assertSame("bar", map.get(new Hashtable()));
		assertNull(map.get("bar"));
		
		map.register(HashMap.class, "spam");
		assertSame("spam", map.get(new HashMap()));
		assertSame("bar", map.get(new TreeMap()));
		assertSame("bar", map.get(new Hashtable()));
		assertNull(map.get("bar"));
	}
}
