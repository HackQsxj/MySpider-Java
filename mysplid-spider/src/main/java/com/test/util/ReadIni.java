package com.test.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ReadIni {

	public static Map<String, String> getDbini(String file) {
		Map<String, String> map = new HashMap<String, String>();
		InputStreamReader isr = null;
		try{
			isr = new InputStreamReader(new FileInputStream(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(isr);
		String s = null;
		try {
			s = br.readLine();
			while (s != null) {
				if (s.trim().length() > 0) {
					String[] s1 = getIni(s);
					map.put(s1[0], s1[1]);
					s = br.readLine();
				}
			}
			br.close();
			isr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static String[] getIni(String str) {
		String[] temp = str.split("=");
		return temp;
	}

}
