package lnstark.lbatis;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import lnstark.lbatis.mapper.BlogMapper;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
//		DataSource dataSource = BlogDataSourceFactory.getBlogDataSource();
//		TransactionFactory transactionFactory = new JdbcTransactionFactory();
//		Environment environment = new Environment("development", transactionFactory, dataSource);
//		Configuration configuration = new Configuration(environment);
//		configuration.addMapper(BlogMapper.class);
//		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
	}
}
