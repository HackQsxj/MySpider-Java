package com.test.util;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
public class EncodeUtils {

	public static final String encodeURL(String str,String enc) {
		try {
			return URLEncoder.encode(str, enc);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	public static final String decodeURL(String str,String enc) {
		try {
			return URLDecoder.decode(str, enc);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String unicdoeToGB2312(String str) {
		String res = null;
		if(str==null ){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		try {
			while (str.length() > 0) {
				if (str.startsWith("\\u")) {
					int x = 0;
					try{
						x = Integer.parseInt(str.substring(2, 6), 16);
					}catch(Exception ex){
						x=  0;
					}
					sb.append((char) x);
					str = str.substring(6);
				} else {
					sb.append(str.charAt(0));
					str = str.substring(1);
				}
			}
			res = sb.toString();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		res=res.replaceAll("\\\\r", "")
			.replaceAll("\\\\n", "")
			.replaceAll("\\\\t", "")
			.replaceAll("&nbsp;", "")
			.replaceAll("&gt", "")
			.replaceAll("\\[", "\"")
			.replaceAll("\\]", "\"");
		return res;
	}
	
	public static String unicodeTogb2312(String str) {
		String res = null;
		StringBuffer sb = new StringBuffer();
		try {
			while (str.length() > 0) {
				if (str.startsWith("\\u")) {
					int x = Integer.parseInt(str.substring(2, 6), 16);
					sb.append((char) x);
					str = str.substring(6);
				} else {
					sb.append(str.charAt(0));
					str = str.substring(1);
				}
			}
			res = sb.toString();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		res=res.replaceAll("\\\\r", "")
				.replaceAll("\\\\t", "")
				.replaceAll("&nbsp;", "")
				.replaceAll("&gt", "")
			   .replaceAll("\\\\n", "");
		return res;
	}
}
