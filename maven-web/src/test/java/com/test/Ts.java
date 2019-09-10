package com.test;

import java.util.Arrays;
import java.util.List;

public class Ts {

	public static void main(String[] args) throws Exception {
		String str = "1";
		String str1 = "";
		String[] strs = new String[]{};
		System.out.println(strs.length);

		List<String> list = Arrays.asList(strs);
		System.out.println(list.contains(str));
	}
}