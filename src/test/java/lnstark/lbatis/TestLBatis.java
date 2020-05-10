package lnstark.lbatis;

import junit.framework.TestCase;
import lnstark.lbatis.core.Configuration;
import lnstark.lbatis.core.LDataSource;
import lnstark.lbatis.core.SqlSession;
import lnstark.lbatis.core.SqlSessionFactory;
import lnstark.lbatis.mapper.BlogMapper;

import javax.sql.DataSource;
import java.io.IOException;

public class TestLBatis extends TestCase {

    public void test() {
        DataSource dataSource = new LDataSource();
        Configuration config = new Configuration(dataSource);
        SqlSessionFactory sessionFactory = new SqlSessionFactory(config);
        try (SqlSession session = sessionFactory.openSession()) {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            mapper.selectBean("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
