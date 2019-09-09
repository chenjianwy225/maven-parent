package com.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cglib.beans.BeanMap;

import com.alibaba.fastjson.JSONObject;
import com.maven.model.user.User;

public class Ts {

	public static void main(String[] args) throws Exception {
		User user = new User();
		user.setKeyId("1");
		user.setUserName("梅西");

		Map<String, Object> map = new HashMap<String, Object>();
		BeanMap beanMap = BeanMap.create(user);
		for (Object key : beanMap.keySet()) {
			map.put(key + "", beanMap.get(key));
		}

		System.out.println(new JSONObject(map));
	}
}