package lnstark.lbatis.mapper;

import java.util.List;
import java.util.Map;

import lnstark.lbatis.entity.HzrRiver;
import lnstark.lbatis.entity.Song;
import org.apache.ibatis.annotations.Param;

public interface BlogMapper {

	List<Map<String, Object>> selectBean(@Param("id") String id);

	List<Map<String, Object>> selectLBatisBean(@lnstark.lbatis.core.annotation.Param("id") String id);
	
	List<Map<String, Object>> selectLBatisBeanByMap(Map<String, Object> param);
	
	Map<String, Object> selectSingleMapByMap(Map<String, Object> param);
	
	HzrRiver selectSingleBeanByMap(Map<String, Object> param);

	List<Song> selectSongByMap(Map<String, Object> param);
	
	List<HzrRiver> selectRiverByMap(Map<String, Object> param);
}
