package cn.szkedun.websocket.utils;

import java.lang.reflect.Method;

public class Base64 {

	/***
	 * encode by Base64
	 */
	public static String encodeBase64(String str) {
		byte[] input = str.getBytes();
		Class<?> clazz;
		String result = "";
		try {
			clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
			Method mainMethod = clazz.getMethod("encode", byte[].class);
			mainMethod.setAccessible(true);
			Object retObj = mainMethod.invoke(null, new Object[] { input });
			result = retObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/***
	 * decode by Base64
	 */
	public static String decodeBase64(String input) {
		String result = "";
		try {
			Class<?> clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
			Method mainMethod = clazz.getMethod("decode", String.class);
			mainMethod.setAccessible(true);
			Object retObj = mainMethod.invoke(null, input);
			byte[] obj = (byte[]) retObj;
			result = new String(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(encodeBase64("123"));
		System.out.println(decodeBase64("MTIz"));
	}
}
