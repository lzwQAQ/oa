package com.kuyuner.common.io;

import com.kuyuner.common.lang.ObjectUtils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

/**
 * Properties工具类， 可载入多个properties、yml文件，
 * 相同的属性在最后载入的文件中的值将会覆盖之前的值，
 * 取不到从System.getProperty()获取。
 *
 * @author administrator
 */
public class PropertiesUtils {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    private static final Properties properties = new Properties();

    public static void init(String locations) {
        locations = locations == null ? "classpath*:/config/*.*" : locations;
        Resource[] resources = null;
        try {
            resources = new PathMatchingResourcePatternResolver().getResources(locations);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return;
        }

        for (Resource resource : resources) {
            String ext = FileUtils.getFileExtension(resource.getFilename());
            if ("properties".equals(ext)) {
                InputStreamReader is = null;
                try {
                    is = new InputStreamReader(resource.getInputStream(), "UTF-8");
                    properties.load(is);
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                } finally {
                    IOUtils.closeQuietly(is);
                }
            } else if ("yml".equals(ext)) {
                YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
                bean.setResources(resource);
                for (Map.Entry<Object, Object> entry : bean.getObject().entrySet()) {
                    properties.put(ObjectUtils.toString(entry.getKey()),
                            ObjectUtils.toString(entry.getValue()));
                }
            }
        }
    }

    /**
     * 获取当前加载的属性
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * 取出String类型的Property
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 取出String类型的Property
     */
    public static String getProperty(String key) {
        return properties.getProperty(key, null);
    }

}
