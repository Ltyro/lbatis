package lnstark.lbatis.exception;

public class SqlParseException extends RuntimeException {
	public SqlParseException(String msg, String sql) {
        super(msg + "(Occured while parsing SQL \"" + sql + "\")");
    }
}
