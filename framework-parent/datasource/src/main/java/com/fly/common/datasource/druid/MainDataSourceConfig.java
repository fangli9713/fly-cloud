package com.fly.common.datasource.druid;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
  * @description: TODO
  * @author fanglinan
  * @date 2019/5/28
  */
@Configuration
@Import(DruidConfig.class)
@MapperScan(basePackages = MainDataSourceConfig.PACKAGE, sqlSessionFactoryRef = MainDataSourceConfig.REF)
public class MainDataSourceConfig {

    /**
     * 精确到 master 目录，以便跟其他数据源隔离
     */
    static final String PACKAGE = "com.fly.operate.dao";
    static final String MAPPER_LOCATION = "classpath*:mapper/operate/*.xml";
    static final String REF = "mainSqlSessionFactory";

    @Bean
    public SqlSessionFactory mainSqlSessionFactory()
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(ds);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        // 使用上面配置的Factory
        SqlSessionTemplate template = new SqlSessionTemplate(mainSqlSessionFactory());
        return template;
    }


    @Resource(name = "mainDataSource")
    private DataSource ds;



}
