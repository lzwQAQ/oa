package com.kuyuner.core.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.kuyuner.common.persistence.BaseEntity;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author administrator
 */
@Configuration
@MapperScan(basePackages = "com.kuyuner", annotationClass = MyBatisDao.class)
public class MyBatisConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.druid")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * 因为TypeAliasesSuperType无法通过配置文件进行配置，这里通过代码初始化
     *
     * @param druidDataSource
     * @return
     * @throws IOException
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DruidDataSource druidDataSource, Environment environment) throws IOException {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setTypeAliasesSuperType(BaseEntity.class);
        factoryBean.setDataSource(druidDataSource);
        factoryBean.setMapperLocations(getMapperLocations(environment));
        factoryBean.setTypeAliasesPackage(environment.getProperty("mybatis.type-aliases-package"));

        //配置分页插件
        //org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        //PageInterceptor pageHelper = new PageInterceptor();
        //Properties properties = new Properties();
        //properties.setProperty("supportMethodsArguments", "true");
        //properties.setProperty("params", "pageNum=pageNumKey;pageSize=pageSizeKey;");
        //pageHelper.setProperties(properties);
        //configuration.addInterceptor(pageHelper);
        //
        //factoryBean.setConfiguration(configuration);
        return factoryBean;
    }

    /**
     * jar包当中的mapper文件的优先级较低，项目中的优先级较高，可以避免mapper重负加载导致的问题
     *
     * @param environment
     * @return
     * @throws IOException
     */
    private Resource[] getMapperLocations(Environment environment) throws IOException {
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(environment.getProperty("mybatis.mapper-locations"));
        Map<String, Resource> maps = new HashMap<>();

        //遍历jar包当中的mapper文件
        for (Resource resource : resources) {
            if (resource instanceof UrlResource) {
                String file = resource.getFilename();
                maps.put(file, resource);
            }
        }

        //遍历项目中的mapper文件
        for (Resource resource : resources) {
            if (resource instanceof FileSystemResource) {
                String file = resource.getFilename();
                maps.put(file, resource);
            }
        }
        Resource[] rs = new Resource[maps.size()];
        List<Resource> list = new ArrayList<>();
        Iterator<String> keys = maps.keySet().iterator();
        while (keys.hasNext()) {
            list.add(maps.get(keys.next()));
        }
        list.toArray(rs);
        return rs;
    }
}