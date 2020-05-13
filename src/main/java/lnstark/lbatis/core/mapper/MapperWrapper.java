package lnstark.lbatis.core.mapper;

import java.lang.reflect.Method;

import lnstark.lbatis.util.LLog;

/**
 * For resolving mapper.xml and binding interface
 * 
 * @author 	Lnstark
 * @since 	1.0
 * @date 	2020年5月13日
 */
public class MapperWrapper {

	private static final String XML = ".xml";
	
	private LLog log = LLog.getInstace(MapperWrapper.class);
	
	MapperResolver mapperResolver;
	
	public <T> MapperWrapper(Class<T> clz) {
		String clzPath = clz.getResource("").getPath();
		String xmlMapperPath = clzPath + clz.getSimpleName() + XML;
		mapperResolver = new MapperResolver(xmlMapperPath);
		
		log.debug(xmlMapperPath);
	}

	public String getSql(Method method, Object[] args) {
		String methodName = method.getName();
		return mapperResolver.getSql(methodName, args);
	}

	
	
	
	
	
	
	
}
