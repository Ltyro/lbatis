package lnstark.lbatis.mapper;

import org.apache.ibatis.logging.Log;

public class MyLog implements Log {
	
	public MyLog(String s) {
		
	}
	
	@Override
	public void warn(String s) {
		System.out.println(s);
	}
	
	@Override
	public void trace(String s) {
		System.out.println(s);
	}
	
	@Override
	public boolean isTraceEnabled() {
		return true;
	}
	
	@Override
	public boolean isDebugEnabled() {
		return true;
	}
	
	@Override
	public void error(String s, Throwable e) {
		System.out.println(s);
		e.printStackTrace();
	}
	
	@Override
	public void error(String s) {
		
	}
	
	@Override
	public void debug(String s) {
		System.out.println(s);
	}
}
