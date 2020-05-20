package lnstark.lbatis.core.util;

/**
 * String util
 * 
 * @author 	Lnstark   
 * @since 	1.0
 * @date 	2020年5月15日
 */
public class StringUtil {
	
	/**
     * 转义正则特殊字符 （$()*+.[]?\^{}
     * \\需要第一个替换，否则replace方法替换时会有逻辑bug
     */
    public static String transRegExp2Str(String str) {
    	
        if(Validator.isNull(str)){
            return str;
        }

        return str.replace("\\", "\\\\").replace("*", "\\*")
                .replace("+", "\\+").replace("|", "\\|")
                .replace("{", "\\{").replace("}", "\\}")
                .replace("(", "\\(").replace(")", "\\)")
                .replace("^", "\\^").replace("$", "\\$")
                .replace("[", "\\[").replace("]", "\\]")
                .replace("?", "\\?").replace(",", "\\,")
                .replace(".", "\\.").replace("&", "\\&");
    }

    public static String getSetter(String m) {
        if (Validator.isNull(m))
            throw new RuntimeException(m + "should not be empty");
        char c = m.charAt(0);
        return new StringBuilder()
                .append("set")
                .append(Character.toUpperCase(c))
                .append(m.substring(1))
                .toString();
    }


}
