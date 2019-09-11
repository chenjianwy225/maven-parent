package com.test;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class Ts {

	public static void main(String[] args) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "messi");
		map.put("age", 20);
		
		System.out.println(map);
		System.out.println(JSON.toJSONString(map));
	}
}