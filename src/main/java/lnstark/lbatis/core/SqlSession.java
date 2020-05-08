package lnstark.lbatis.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SqlSession {

	public <T> T getMapper(Class<T> clz) {
		if(!clz.isInterface()) {
			try {
				return clz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			return jdkProxy(clz);
		}
		return null;
	}

	private <T> T jdkProxy(Class<T> clz) {
		MyProxy mp = new MyProxy();
		return (T) Proxy.newProxyInstance(SqlSession.class.getClassLoader(), clz.getInterfaces(), mp);
	}
	
	class MyProxy implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return method.invoke(proxy, args);
		}
		
	}
	
}
