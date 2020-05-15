package lnstark.lbatis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import junit.framework.TestCase;
import lnstark.lbatis.core.LDataSource;

public class TestJDBC extends TestCase {
	
	public void testApp() {
		
        LDataSource ds = new LDataSource();
        Connection conn;
        try {
			conn = ds.getConnection();
			String sql = "select name, create_time from hzr_river where name like '%' + ? and create_time like ?";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, "河");
			st.setString(2, "2019%");
			ResultSet result = st.executeQuery();
			while (result.next()) {
				ResultSetMetaData md = result.getMetaData();
				int columnCount = md.getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = md.getColumnName(i);
					Object n = result.getObject(i);
					System.out.print(columnName + " " + n + " ");
				}
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
    }
}
