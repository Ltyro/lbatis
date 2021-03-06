package lnstark.lbatis.core.session;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lnstark.lbatis.core.mapper.MapperWrapper;
import lnstark.lbatis.core.mapper.SqlParseResult;
import lnstark.lbatis.core.mapper.SqlParser;
import lnstark.lbatis.core.util.LLog;

public class SqlSession implements Closeable {

	private LLog log = LLog.getInstace(SqlSession.class);
	
	private Connection conn = null;
	
	private MapperWrapper mapperWrapper;
	
	public <T> T getMapper(Class<T> clz) {
		
		mapperWrapper = new MapperWrapper(clz);

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

			log.info("proxy: " + LProxy.class + " execute method");

			Object result = executeMethod(method, args);

			// subject is null
			if(subject != null)
				return method.invoke(subject, args);

			return result;
		}
		
	}

	private Object executeMethod(Method method, Object[] args) {
		String sql = mapperWrapper.getSql(method, args);
		SqlParseResult sqlParseResult = new SqlParser(sql, method, args).parse();
		log.debug(sqlParseResult.getPreparedSqlForLog(), method);
		log.debug(sqlParseResult.getParametersForLog(), method);
		Object result = null;
		try {
			PreparedStatement statement = conn.prepareStatement(sqlParseResult.getPrepareSql(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			sqlParseResult.fillStatement(statement);
			
			ResultSet resultSet = statement.executeQuery();
			result = mapperWrapper.parseResult(resultSet, method);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
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

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

}
