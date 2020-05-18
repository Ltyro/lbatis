package lnstark.lbatis;

import java.io.IOException;
import java.util.HashMap;
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
import lnstark.lbatis.util.StringUtil;

public class TestLBatis extends TestCase {

	private static LLog log = LLog.getInstace(TestLBatis.class);
	
	public static void main(String[] args) {
//		String s = "asfas#{asd, jdbcType=VARCHAR}ASD";
//		log.info(s.replaceFirst(StringUtil.transRegExp2Str("#{asd, jdbcType=VARCHAR}"), "?"));
		testLbatis();
	}
	
    public static void testLbatis() {
        DataSource dataSource = new LDataSource();
        Configuration config = new Configuration(dataSource);
        SqlSessionFactory sessionFactory = new SqlSessionFactory(config);
        try (SqlSession session = sessionFactory.openSession()) {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            Map<String, Object> param = new HashMap<>();
            param.put("name", "天");
            param.put("id", 1);
            List<Map<String, Object>> l = mapper.selectLBatisBeanByMap(param);
            for (Map<String, Object> m : l) {
                log.info(m);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
