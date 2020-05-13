package lnstark.lbatis.util;

import java.util.Date;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Log (combine Factory and Log)
 * 
 * @author 	Zaoji_Lai   
 * @since 	1.0
 * @date 	2020年5月13日
 */
public class LLog {

	private static Map<Class<?>, LLog> cache = new HashMap<>();
	
	private static int level = 4;
	
	public static final int 
					DEBUG_LEVEL = 4, 
					INFO_LEVEL 	= 3, 
					WARN_LEVEL 	= 2,
					ERROR_LEVEL = 1;
	
	private String clzName;
	
	public static LLog getInstace(Class<?> clz) {
		LLog llog = cache.get(clz);
		if(llog == null)
			llog = new LLog(clz);
		cache.put(clz, llog);
		return llog;
	}
	
	private LLog(Class<?> clz) {
		String sp = ".", clzName = "";
		String fullName = clz.getName();
		String[] paths = fullName.split("\\" + sp);
		for (int i = 0; i < paths.length - 1; i++) {
			clzName += (paths[i].charAt(0) + sp);
		}
		clzName += clz.getSimpleName();
		this.clzName = clzName;
	}
	
	private String constructMsg(String msg, String level) {
		StringBuilder sb = new StringBuilder();
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
		sb.append(time)
			.append(String.format("%6s", level))
			.append(String.format("%6s", getProcessID()))
			.append("  ")
			.append(String.format("%1$-35s", clzName))
			.append(": ")
			.append(msg);
		return sb.toString();
	}

	public void debug(String msg) {
		if (level < DEBUG_LEVEL)
			return;
		System.out.println(constructMsg(msg, "DEBUG"));
	}
	
	public void info(String msg) {
		if (level < INFO_LEVEL)
			return;
		System.out.println(constructMsg(msg, "INFO"));
	}
	
	public void warn(String msg) {
		if (level < WARN_LEVEL)
			return;
		System.out.println(constructMsg(msg, "WARN"));
	}

	public void error(String msg) {
		if (level < ERROR_LEVEL)
			return;
		System.out.println(constructMsg(msg, "ERROR"));
	}
	
	public static void setLevel(int l) {
		level = l;
	}
	
	public static final int getProcessID() {  
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0])  
                .intValue();  
    }
}
