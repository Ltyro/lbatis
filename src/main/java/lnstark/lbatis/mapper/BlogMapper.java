package lnstark.lbatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BlogMapper {

	List<Map<String, Object>> selectBean(@Param("id") String id);

}
