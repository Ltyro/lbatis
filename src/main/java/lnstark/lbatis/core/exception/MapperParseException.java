package lnstark.lbatis.core.exception;

import java.lang.reflect.Method;

public class MapperParseException extends RuntimeException {
	
	public MapperParseException(String msg) {
		super(msg);
	}

	public MapperParseException(String msg, Method method) {
		super(msg + "(occured while executing " + method.getDeclaringClass().getName() + "." + method.getName() + ")");
	}
}
