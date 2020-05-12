package lnstark.lbatis.core;

import lnstark.lbatis.exception.SqlSessionException;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class SqlSession implements Closeable {

//	private DataSource dataSource = null;
	private static final String XML = ".xml";
	private Connection conn = null;
	
	public <T> T getMapper(Class<T> clz) {
		String clzPath = clz.getResource("").getPath();
		String xmlMapperPath = clzPath + clz.getSimpleName() + XML;
		File xmlMapper = new File(xmlMapperPath);
		if(!xmlMapper.exists())
			throw new SqlSessionException(xmlMapperPath + " not found!");

		System.out.println(xmlMapperPath);

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

	private <T> T jdkProxy(Object obj) {
		InvocationHandler lp = new LProxy(obj);
		return (T) Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), lp);
	}

	private <T> T jdkProxy(Class<T> clz) {
		InvocationHandler lp = new LProxy(null);
		return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, lp);
	}

	class LProxy implements InvocationHandler {

		private Object subject;

		/**
		 * 构造方法，给我们要代理的真实对象赋初值
		 */
		public LProxy(Object subject) {
			this.subject = subject;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

			System.out.println("proxy: " + LProxy.class + " execute method");

			Object result = executeMethod(method, args);

			// subject is null
			if(subject != null)
				return method.invoke(subject, args);

			return result;
		}
		
	}

	private Object executeMethod(Method method, Object[] args) {
		String sql = "";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void close() throws IOException {
		try {
			if(conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	public DataSource getDataSource() {
//		return dataSource;
//	}
//
//	public void setDataSource(DataSource dataSource) {
//		this.dataSource = dataSource;
//	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

}
