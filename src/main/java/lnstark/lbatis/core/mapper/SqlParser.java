package lnstark.lbatis.core.mapper;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lnstark.lbatis.core.annotation.Param;
import lnstark.lbatis.exception.SqlParseException;
import lnstark.lbatis.util.LLog;
import lnstark.lbatis.util.StringUtil;
import lnstark.lbatis.util.Validator;

/**
 * lbatis sql parser
 * 
 * @author 	Lnstark   
 * @since 	1.0
 * @date 	2020年5月15日
 */
public class SqlParser {

	private String sourceSql;
	
	private String prepareSql;
	
	private Map<String, Object> param2ValueMap = new HashMap<>();
	
	private LLog log = LLog.getInstace(SqlParser.class);
	
	public SqlParser(String sourceSql, Method m, Object[] args) {
		this.sourceSql = sourceSql;
		// get parameters
		Parameter[] params = m.getParameters();
		if(params.length != args.length)
			log.error("the length of method params is not equal with args'");
		for (int i = 0; i < params.length; i++) {
			Parameter p = params[i];
			Object o = args[i];
			Class<?> c = p.getType();
			Param pa = p.getAnnotation(Param.class);
			if (Validator.implementInterface(c, Map.class)) {// if the parameter is a Map, putAll of it
				param2ValueMap.putAll((Map) o);
			} else if (pa != null) { // else put param by the Annotation @Param
				String pName = pa.value();
				param2ValueMap.put(pName, o);
			}
		}
	}

	public SqlParseResult parse() {
		String prepareSql = "";
		List<Object> params = new ArrayList<>();
		
		final char sharp = '#', dollar = '$', lb = '{', rb = '}', qm = '?', comma = ',';
		
		String tempSql = sourceSql;
		tempSql = tempSql.replace("\n", " ");// replace "\n" with space
		int fromIndex = 0;
		int start = 0, end = 0;
		// parse #{XXX}
		while ((start = tempSql.indexOf(sharp, fromIndex)) > -1) {
			if (tempSql.charAt(start + 1) != lb) {
				fromIndex = ++start;
				continue;
			}
			end = tempSql.indexOf(rb, start);
			if (end == -1)
				throw new SqlParseException("A \"#{\" is not closed.", sourceSql);
			fromIndex = start;
			// the content of #{XXX}
			String braceContent = tempSql.substring(start + 2, end);
			if (!Validator.isNull(braceContent)) {
				String pr = braceContent;
				if (pr.indexOf(comma) > -1)
					pr = pr.split(String.valueOf(comma))[0];
				params.add(param2ValueMap.get(pr));
			}

			String contentWithBrace = new StringBuilder().append(sharp).append(lb).append(braceContent).append(rb).toString();
			tempSql = tempSql.replaceFirst(StringUtil.transRegExp2Str(contentWithBrace), String.valueOf(qm));
		}
		
		// parse ${XXX}
		fromIndex = start = end = 0;
		while ((start = tempSql.indexOf(dollar, fromIndex)) > -1) {
			if (tempSql.charAt(start + 1) != lb) {
				fromIndex = ++start;
				continue;
			}
			end = tempSql.indexOf(rb, start);
			if (end == -1)
				throw new SqlParseException("A \"${\" is not closed.", sourceSql);
			fromIndex = start;
			// the content of ${XXX}
			String braceContent = tempSql.substring(start + 2, end);

			Object contentObj = param2ValueMap.get(braceContent);
			if(contentObj == null)
				continue;
			String contentWithBrace = new StringBuilder().append(sharp).append(lb).append(braceContent).append(rb).toString();
			tempSql = tempSql.replaceFirst(StringUtil.transRegExp2Str(contentWithBrace), contentObj.toString());
			
		}
		prepareSql = tempSql;
		return new SqlParseResult(prepareSql, params);
	}
	
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		Object m = new LinkedHashMap<String, Object>();
		Map mm = (Map) m;
	}
	
}
