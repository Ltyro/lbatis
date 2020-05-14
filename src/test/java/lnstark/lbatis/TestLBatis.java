package lnstark.lbatis;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import junit.framework.TestCase;
import lnstark.lbatis.core.Configuration;
import lnstark.lbatis.core.LDataSource;
import lnstark.lbatis.core.SqlSession;
import lnstark.lbatis.core.SqlSessionFactory;
import lnstark.lbatis.mapper.BlogMapper;
import lnstark.lbatis.util.LLog;

public class TestLBatis extends TestCase {

	private LLog log = LLog.getInstace(TestLBatis.class);
	
    public void test() {
        DataSource dataSource = new LDataSource();
        Configuration config = new Configuration(dataSource);
        SqlSessionFactory sessionFactory = new SqlSessionFactory(config);
        try (SqlSession session = sessionFactory.openSession()) {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            List<Map<String, Object>> l = mapper.selectBean("");
            for (Map<String, Object> m : l) {
            	log.info(m);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
