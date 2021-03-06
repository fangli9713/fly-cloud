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

import javax.sql.DataSource;

/**
  * @description: TODO
  * @author fanglinan
  * @date 2019/5/28
  */
@Configuration
@Import(DruidConfig.class)
@MapperScan(basePackages = RobotDataSourceConfig.PACKAGE, sqlSessionFactoryRef = RobotDataSourceConfig.REF)
public class RobotDataSourceConfig{

    /**
     * 精确到 master 目录，以便跟其他数据源隔离
     */
    static final String PACKAGE = "com.fly.web.dao";
    static final String MAPPER_LOCATION = "classpath*:mapper/web/*.xml";
    static final String REF = "robotSqlSessionFactory";

    @Bean
    public SqlSessionFactory robotSqlSessionFactory()
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(ds);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MAPPER_LOCATION));
        return sessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate2() throws Exception {
        // 使用上面配置的Factory
        SqlSessionTemplate template = new SqlSessionTemplate(robotSqlSessionFactory());
        return template;
    }

    @Autowired
    @Qualifier("robotDataSource")
    private DataSource ds;




}
