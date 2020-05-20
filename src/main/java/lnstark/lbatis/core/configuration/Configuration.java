package lnstark.lbatis.core.configuration;

import javax.sql.DataSource;

public class Configuration {

    private DataSource dataSource;

    public Configuration(DataSource dataSource) {
    	if (dataSource == null)
    		throw new NullPointerException();
        this.dataSource = dataSource;
    }

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
    
    
}
