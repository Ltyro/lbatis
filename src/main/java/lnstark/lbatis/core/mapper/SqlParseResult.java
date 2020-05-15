package lnstark.lbatis.core.mapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * sql parse result
 * 
 * @author 	Lnstark   
 * @since 	1.0
 * @date 	2020年5月15日
 */
public class SqlParseResult {
	
	private String prepareSql;

	private List<Object> params;
	
	public SqlParseResult(String prepareSql, List<Object> params) {
		this.prepareSql = prepareSql;
		this.params = params;
	}
	
	public String getPrepareSql() {
		return prepareSql;
	}

	public void setPrepareSql(String prepareSql) {
		this.prepareSql = prepareSql;
	}

	public void fillStatement(PreparedStatement statement) {
		
		try {
			for (int i = 0; i < params.size(); i++) {
				setParam(statement, params.get(i), i + 1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private void setParam(PreparedStatement st, Object o, int i) throws SQLException {
		
		if (o == null)
			return;
		
		if (o instanceof String || o instanceof Character) {
			st.setString(i, o.toString());
		} else if (o instanceof Boolean) {
			st.setBoolean(i, (Boolean) o);
		} else if (o instanceof Byte) {
			st.setByte(i, (Byte) o);
		} else if (o instanceof Short) {
			st.setShort(i, (Short) o);
		} else if (o instanceof Integer) {
			st.setInt(i, (Integer) o);
		} else if (o instanceof Float) {
			st.setFloat(i, (Float) o);
		} else if (o instanceof Long) {
			st.setLong(i, (Long) o);
		} else if (o instanceof Double) {
			st.setDouble(i, (Double) o);
		}
		
	}
	
	
}
