package lnstark.lbatis.util;

import java.util.Date;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Log (combine Factory and Log)
 * 
 * @author 	Lnstark   
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
		this.clzName = getNameByClz(clz);
	}
	
	private String getNameByClz(Class<?> clz) {
		String sp = ".", clzName = "";
		String fullName = clz.getName();
		String[] paths = fullName.split("\\" + sp);
		for (int i = 0; i < paths.length - 1; i++) {
			clzName += (paths[i].charAt(0) + sp);
		}
		clzName += clz.getSimpleName();
		return clzName;
	}
	
	private String getNameByMethod(Method method) {
		String sp = ".";
		StringBuilder sb = new StringBuilder();
		Class<?> clz = method.getClass();
		String fullName = clz.getName();
		String[] paths = fullName.split("\\" + sp);
		for (int i = 0; i < paths.length - 1; i++) {
			sb.append(paths[i].charAt(0)).append(sp);
		}
		sb.append(clz.getSimpleName().substring(0, 1).toUpperCase()).append(sp)
			.append(method.getName());
		return sb.toString();
	}
	
	private String constructMsg(String msg, String level, Method method) {
		StringBuilder sb = new StringBuilder();
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
		sb.append(time)
			.append(String.format("%6s", level))
			.append(String.format("%6s", getProcessID()))
			.append(" --- [")
			.append(String.format("%15s", Thread.currentThread().getName()))
			.append("] ")
			.append(String.format("%1$-35s", method == null ? clzName : getNameByMethod(method)))
			.append(": ")
			.append(msg);
		return sb.toString();
	}

	public void debug(Object msg) {
		if (level < DEBUG_LEVEL)
			return;
		System.out.println(constructMsg(msg.toString(), "DEBUG", null));
	}
	
	public void debug(Object msg, Method method) {
		if (level < DEBUG_LEVEL)
			return;
		System.out.println(constructMsg(msg.toString(), "DEBUG", method));
	}
	
	public void info(Object msg) {
		if (level < INFO_LEVEL)
			return;
		System.out.println(constructMsg(msg.toString(), "INFO", null));
	}

	public void info(Object msg, Method method) {
		if (level < INFO_LEVEL)
			return;
		System.out.println(constructMsg(msg.toString(), "INFO", method));
	}
	
	public void warn(Object msg) {
		if (level < WARN_LEVEL)
			return;
		System.out.println(constructMsg(msg.toString(), "WARN", null));
	}

	public void warn(Object msg, Method method) {
		if (level < WARN_LEVEL)
			return;
		System.out.println(constructMsg(msg.toString(), "WARN", method));
	}
	
	public void error(Object msg) {
		if (level < ERROR_LEVEL)
			return;
		System.out.println(constructMsg(msg.toString(), "ERROR", null));
	}
	
	public void error(Object msg, Method method) {
		if (level < ERROR_LEVEL)
			return;
		System.out.println(constructMsg(msg.toString(), "ERROR", method));
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
