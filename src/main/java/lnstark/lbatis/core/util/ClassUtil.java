package lnstark.lbatis.core.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClassUtil {
	
	public static Map<String, Method> getMethodMap(Class<?> clz) {
		Map<String, Method> map = new HashMap<>();
		Method[] ms = clz.getDeclaredMethods();
		for (Method m : ms) {
			map.put(m.getName(), m);
		}
		return map;
	}

}
