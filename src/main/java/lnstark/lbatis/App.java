package lnstark.lbatis;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.logging.jdk14.Jdk14LoggingImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.xml.sax.SAXException;


import lnstark.lbatis.mapper.BlogMapper;
import lnstark.lbatis.mapper.MyInterceptor;
import lnstark.lbatis.mapper.MyLog;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		DataSource dataSource = new MyDataSource();
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		LogFactory.useCustomLogging(MyLog.class);
		Environment environment = new Environment("development", transactionFactory, dataSource);
		Configuration configuration = new Configuration(environment);
		configuration.addInterceptor(new MyInterceptor());
		configuration.addMapper(BlogMapper.class);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		
		try (SqlSession session = sqlSessionFactory.openSession()) {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			List<Map<String, Object>> bean = mapper.selectBean("55ba691e1528445d80381dad9806cae9");
			System.out.println(bean);
		}
	}

	static class MyDataSource implements DataSource {

		private static final String dirverClassName = "net.sourceforge.jtds.jdbc.Driver";
		private static final String url = "jdbc:jtds:sqlserver://172.16.254.56:1433;DatabaseName=WENV_HZR;SelectMethod=Cursor;useLOBs=false";
		private static final String user = "sa";
		private static final String pswd = "fpi@123456";

		//连接池 
        private static Vector<Connection> pool = new Vector<>();
        
		static {
			try {
				Class.forName(dirverClassName);
			} catch (ClassNotFoundException e) {
				System.out.println("driver not found");
				e.printStackTrace();
			}
		}

		public MyDataSource() {
		}

		@Override
		public PrintWriter getLogWriter() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setLogWriter(PrintWriter out) throws SQLException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setLoginTimeout(int seconds) throws SQLException {
			// TODO Auto-generated method stub

		}

		@Override
		public int getLoginTimeout() throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Logger getParentLogger() throws SQLFeatureNotSupportedException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> T unwrap(Class<T> iface) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Connection getConnection() throws SQLException {
			synchronized (pool) {
                if (pool.size() > 0) 
                	return pool.remove(0); 
                return DriverManager.getConnection(url, user, pswd);
			}
		}

		@Override
		public Connection getConnection(String username, String password) throws SQLException {
			return DriverManager.getConnection(url, username, password);
		}

	}
	
}
