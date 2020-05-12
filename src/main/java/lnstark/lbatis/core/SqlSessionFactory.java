package lnstark.lbatis.core;

import java.sql.SQLException;

import javax.sql.DataSource;

public class SqlSessionFactory {
	
	private Configuration configuration;

	public SqlSessionFactory(Configuration configuration) {
		super();
		this.configuration = configuration;
	}
	
	public SqlSession openSession() {
		SqlSession sqlSession = new SqlSession();
		DataSource dataSource = configuration.getDataSource();
		try {
			sqlSession.setConn(dataSource.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sqlSession;
	}
	
}
