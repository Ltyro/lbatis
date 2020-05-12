package lnstark.lbatis.core;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class LDataSource implements DataSource {

	private static String dirverClassName = "net.sourceforge.jtds.jdbc.Driver";
	private String url = "jdbc:jtds:sqlserver://172.16.254.56:1433;DatabaseName=WENV_HZR;SelectMethod=Cursor;useLOBs=false";
	private String username = "sa";
	private String password = "fpi@123456";

//	private static String dirverClassName = "com.mysql.cj.jdbc.Driver";
//	private String url = "jdbc:mysql://127.0.0.1:3306/mydb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
//	private String username = "root";
//	private String password = "123456";

	//连接池 
    private Vector<Connection> pool = new Vector<>();
    
	static {
		try {
			Class.forName(dirverClassName);
		} catch (ClassNotFoundException e) {
			System.out.println("driver not found");
			e.printStackTrace();
		}
	}

	public LDataSource() {
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
            return DriverManager.getConnection(url, username, password);
		}
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	public Vector<Connection> getPool() {
		return pool;
	}

	public void setPool(Vector<Connection> pool) {
		this.pool = pool;
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
}
