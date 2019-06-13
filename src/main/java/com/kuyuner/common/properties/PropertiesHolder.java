package com.kuyuner.common.properties;

import org.springframework.core.env.Environment;

/**
 * propertities配置文件属性获取
 *
 * @author tangzj
 */
public class PropertiesHolder {
    private static Environment environment;

    public static void init(Environment environment) {
        PropertiesHolder.environment = environment;
    }

    public static String getString(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    public static String getString(String key) {
        return getString(key, null);
    }

    public static int getInt(String key, int defaultValue) {
        return environment.getProperty(key, Integer.class, defaultValue);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return environment.getProperty(key, Boolean.class, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }
}
