package lnstark.lbatis.core.mapper;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lnstark.lbatis.exception.MapperParseException;
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

	/**
	 * parse result set
	 * 
	 * @param resultSet
	 * @param method
	 * @return
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException 
	 */
//	@SuppressWarnings("rawtypes")
	public Object parseResult(ResultSet resultSet, Method method) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Object resultObj = null;
		List resultList = null;
		boolean singleResult = false;
		Type returnType = method.getGenericReturnType();
		String returnTypeName = returnType.getTypeName();
		Class<?> beanClz;
		Class<?> returnClz = Class.forName(removeGeneric(returnTypeName));
		if (returnClz == List.class || returnClz == ArrayList.class || returnClz == LinkedList.class) {
			Type[] actualTypeArguments = ((ParameterizedType) returnType).getActualTypeArguments();
			String typeName = actualTypeArguments[0].getTypeName();
			beanClz = Class.forName(removeGeneric(typeName));
			if(returnClz == List.class)
				returnClz = ArrayList.class;
			resultList = (List) returnClz.newInstance();
		} else {
			singleResult = true;
			beanClz = returnClz;
		}
		// get resultSet's size
		int size = 0;
		resultSet.last();
		size = resultSet.getRow();
		resultSet.beforeFirst();
		log.debug("<==      Total: " + size);
		if (size < 2 && !singleResult)
			throw new MapperParseException("query result is more than one.");
		
		if (!singleResult) {
			if (beanClz == Map.class || beanClz == HashMap.class || beanClz == LinkedHashMap.class) {
				if (beanClz == Map.class)
					beanClz = HashMap.class;
			
				while (resultSet.next()) {
					ResultSetMetaData md = resultSet.getMetaData();
					int columnCount = md.getColumnCount();
					
					Map result = null;
					result = (Map) beanClz.newInstance();
					
					for (int i = 1; i <= columnCount; i++) {
						String columnName = md.getColumnName(i);
						Object value = resultSet.getObject(i);
						result.put(columnName, value);
					}
					resultList.add(result);
				}
				return resultList;
			}
			
		}
			
		return resultObj;
	}

	public List<Map<String, Object>> get() {
		return null;
	}
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		Method m = MapperWrapper.class.getDeclaredMethod("get");
		List<Map<String, Object>> l = new ArrayList<>();
		Type type = m.getGenericReturnType();
		if (type instanceof ParameterizedType) {
			Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
			String typeName = actualTypeArguments[0].getTypeName();
			System.out.println(typeName);
		}
		
	}
	
	private String removeGeneric(String name) {
		int firstLTIndex = name.indexOf("<");
		return firstLTIndex == -1 ? name : name.substring(0, firstLTIndex);
	}
	
	
	
}
