package org.tyco.common.util;

import java.util.Arrays;


/**
 * Utility class for handling strings.
 * @author Tiago Cogumbreiro <cogumbreiro@users.sf.net>
 *
 */
public class Strings {
	/**
	 * Creates a string concatenating every element on <code>Iterable</code>
	 * <code>elems</code> (calling <code>toString</code>) and putting between
	 * each element the string <code>inter</code>.
	 * 
	 * @param inter the string that between each element
	 * @param elems the elements that will be converted to strings and concatenated
	 * @return the string with the string representation of the elements
	 */
	public static String joinAll(String inter, Object... elems) {
		return join(inter, Arrays.asList(elems));
	}
	
	/**
	 * Creates a string concatenating every element on <code>Iterable</code>
	 * <code>elems</code> (calling <code>toString</code>) and putting between
	 * each element the string <code>inter</code>.
	 * 
	 * @param inter the string that between each element
	 * @param elems the elements that will be converted to strings and concatenated
	 * @return the string with the string representation of the elements
	 */
	public static String join(String inter, Iterable<?> elems) {
		String ret = "";
		for (Object obj: elems) {
			ret += obj + inter;
		}
		if (ret.length() == 0) {
			return "";
		}
		return ret.substring(0, ret.length() - inter.length());
	}
	
	/**
	 * Replaces all ocurrences of <code>oldStr</code> with <code>newStr</code>
	 * on <code>target</code>.
	 * 
	 * @param target the string where the replacement will occour
	 * @param oldStr the string that will be replaced
	 * @param newStr the string that will be used as a replacement
	 * @return
	 */
	public static String replace(String target, String oldStr, String newStr) {
		if (!target.contains(oldStr)) {
			return target;
		}
		
		int index = target.indexOf(oldStr);
		String start = target.substring(0, index);
		String finish = target.substring(index + oldStr.length(), target.length());
		return replace(start, oldStr, newStr) + newStr + replace(finish, oldStr, newStr);
	}
	/**
	 * Escapes a string.
	 * @param raw the string to be escaped
	 * @return the escaped string
	 */
	public static String escape(final String raw) {
		String ret = raw;
		ret = replace(ret, "\\", "\\\\");
		ret = replace(ret, "\n", "\\n");
		ret = replace(ret, "\t", "\\t");
		ret = replace(ret, "\"", "\\\"");
		ret = replace(ret, "\r", "\\r");
		return '"' + ret + '"';
	}

	/**
	 * Implements the multiplication operator used on Python.
	 * @param data
	 * @param count
	 * @return
	 */
	public static String multiplyString(String data, int count) {
		String ret = "";
		for (int counter = 0; counter < count; counter++) {
			ret += data;
		}
		return ret;
	}
	
	/**
	 * Verifies if a string starts with another
	 * @param data the string you want to test
	 * @param other the other string
	 * @return if it starts with the other string
	 */
	public static boolean startsWith(String data, String other) {
		if (data.length() < other.length()) {
			return false;
		}
		
		String result = data.substring(0, other.length());
		return result.equals(other);
	}
}
