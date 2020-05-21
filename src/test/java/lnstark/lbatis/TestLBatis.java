package lnstark.lbatis;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import junit.framework.TestCase;
import lnstark.lbatis.core.configuration.Configuration;
import lnstark.lbatis.core.configuration.LDataSource;
import lnstark.lbatis.core.session.SqlSession;
import lnstark.lbatis.core.session.SqlSessionFactory;
import lnstark.lbatis.entity.HzrRiver;
import lnstark.lbatis.entity.Song;
import lnstark.lbatis.mapper.BlogMapper;
import lnstark.lbatis.core.util.LLog;

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
//            param.put("name", "天");
//            param.put("id", 1);
//            List<Song> l = mapper.selectSongByMap(param);
            
            param.put("name", "角港");
            param.put("id", "034c12921ebf472c8351d66a10323f6e");
            HzrRiver l = mapper.selectSingleBeanByMap(param);
            log.info(l);
//            for (Map<String, Object> m : l) {
//                log.info(m);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
