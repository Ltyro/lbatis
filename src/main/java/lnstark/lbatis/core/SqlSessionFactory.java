package lnstark.lbatis.core;

public class SqlSessionFactory {
	private Configuration configuration;

	public SqlSessionFactory(Configuration configuration) {
		super();
		this.configuration = configuration;
	}
	
	public SqlSession openSession() {
		return new SqlSession();
	}
}
