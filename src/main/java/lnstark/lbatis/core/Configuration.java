package lnstark.lbatis.core;

import javax.sql.DataSource;

public class Configuration {

    private DataSource dataSource;

    public Configuration(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
