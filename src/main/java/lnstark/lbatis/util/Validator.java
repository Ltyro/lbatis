package lnstark.lbatis.util;

public class Validator {

	public static boolean isNull(Object o) {
		if(o == null)
			return true;
		
		if(o instanceof String)
			return o.toString().equals("");
		
		return false;
	}
	
}
