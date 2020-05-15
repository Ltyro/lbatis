package lnstark.lbatis.util;

/**
 * tool class
 * 
 * @author 	Lnstark   
 * @since 	1.0
 * @date 	2020年5月13日
 */
public class Validator {

	/**
	 * judge is null or is "" if it is String
	 */
	public static boolean isNull(Object o) {
		if (o == null)
			return true;
		
		if (o instanceof String)
			return o.toString().equals("");
		
		return false;
	}
	
	/**
	 * judge if the Class clz implement the interface itface
	 * @param clz
	 * @param itface
	 * @return
	 */
	public static boolean implementInterface(Class<?> clz, Class<?> itface) {
		Class<?>[] is = clz.getInterfaces();
		for (Class<?> i : is) {
			if(i == itface)
				return true;
		}
		return false;
	}
}
