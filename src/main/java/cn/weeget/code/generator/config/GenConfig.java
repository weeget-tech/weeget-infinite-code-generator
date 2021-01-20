package cn.weeget.code.generator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取代码生成相关配置
 */
@Component
@ConfigurationProperties(prefix = "gen")
public class GenConfig {
    /**
     * 作者
     */
    public static String author;

    /**
     * 生成包路径
     */
    public static String packageName;

    /**
     * 自动去除表前缀，默认是true
     */
    public static boolean autoRemovePre;

    /**
     * 表前缀(类名不会包含表前缀)
     */
    public static String tablePrefix;

    /**
     * 服务名
     */
    public static String serviceName;

    /**
     * 服务端口
     */
    public static String servicePort;

    public static String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        GenConfig.author = author;
    }

    public static String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        GenConfig.packageName = packageName;
    }

    public static boolean getAutoRemovePre() {
        return autoRemovePre;
    }

    public void setAutoRemovePre(boolean autoRemovePre) {
        GenConfig.autoRemovePre = autoRemovePre;
    }

    public static String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        GenConfig.tablePrefix = tablePrefix;
    }

    public static String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        GenConfig.serviceName = serviceName;
    }

    public static String getServicePort() {
        return servicePort;
    }

    public void setServicePort(String servicePort) {
        GenConfig.servicePort = servicePort;
    }
}
