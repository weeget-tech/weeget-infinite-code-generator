package cn.weeget.code.generator.util;

import com.google.common.base.CaseFormat;

/**
 * @author chenck
 * @date 2020/4/9 10:48
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 驼峰式命名法 例如：user_name->userName
     * 下划线 转 驼峰(首字母小写)
     */
    public static String lowerCamelCase(String value) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, value);
    }

    /**
     * 驼峰式命名法 例如：user_name->UserName
     * 下划线 转 驼峰(首字母大写)
     */
    public static String upperCamelCase(String value) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, value);
    }
}
