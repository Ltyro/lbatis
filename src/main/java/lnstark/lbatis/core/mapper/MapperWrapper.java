package lnstark.lbatis.core.mapper;

import java.lang.reflect.InvocationTargetException;
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

import lnstark.lbatis.core.exception.MapperParseException;
import lnstark.lbatis.core.exception.ResultParseException;
import lnstark.lbatis.core.util.ClassUtil;
import lnstark.lbatis.core.util.LLog;
import lnstark.lbatis.core.util.StringUtil;

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
		String sourceSql = mapperResolver.getSql(methodName, args);
		return sourceSql;
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
	public Object parseResult(ResultSet resultSet, Method method) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, InvocationTargetException {
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
		log.debug("<==      Total: " + size, method);
		if (size > 1 && singleResult)
			throw new MapperParseException("query result is more than one.", method);
		Map<String, Method> methodMap = ClassUtil.getMethodMap(beanClz);
		
		boolean isMap = beanClz == Map.class || beanClz == HashMap.class || beanClz == LinkedHashMap.class;
		
		if (beanClz == Map.class)
			beanClz = LinkedHashMap.class;
		
		if (!singleResult) {
			if (size == 0)
				return resultList;
			while (resultSet.next()) {
				Object result = null;
				result = isMap ? constructMap(resultSet, beanClz) 
							: constructBean(resultSet, beanClz, methodMap);
				resultList.add(result);
			}
			return resultList;
		}
		if (size == 0)
			return null;
		resultSet.next();
		resultObj = isMap ? constructMap(resultSet, beanClz) 
					: constructBean(resultSet, beanClz, methodMap);
			
		return resultObj;
	}

	/**
	 * construct map data
	 */
	private Map constructMap(ResultSet resultSet, Class<?> beanClz) 
			throws InstantiationException, IllegalAccessException, SQLException {
		ResultSetMetaData md = resultSet.getMetaData();
		int columnCount = md.getColumnCount();
		Map result = (Map) beanClz.newInstance();
		for (int i = 1; i <= columnCount; i++) {
			String columnName = md.getColumnName(i);
			Object value = resultSet.getObject(i);
			result.put(columnName, value);
		}
		return result;
	}
	
	/**
	 * construct bean data
	 */
	private Object constructBean(ResultSet resultSet, Class<?> beanClz, Map<String, Method> methodMap) 
			throws InstantiationException, IllegalAccessException, SQLException, IllegalArgumentException, InvocationTargetException {
		ResultSetMetaData md = resultSet.getMetaData();
		int columnCount = md.getColumnCount();
		Object result = beanClz.newInstance();
		for (int i = 1; i <= columnCount; i++) {
			String columnName = md.getColumnName(i);
			Object value = resultSet.getObject(i);
			String methodName = StringUtil.getSetter(columnName);
			Method setterMethod = null;
			if ((setterMethod = methodMap.get(methodName)) != null) {
				setterMethod.invoke(result, value);
			}
		}
		return result;
		
	}
	
	private String removeGeneric(String name) {
		int firstLTIndex = name.indexOf("<");
		return firstLTIndex == -1 ? name : name.substring(0, firstLTIndex);
	}

	
}
