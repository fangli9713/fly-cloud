package com.fly.finance.mybatisplus;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * mybatis-plus代码生成器
 * mybatis-plus官网：https://mp.baomidou.com/
 * Author chenpiqian Date 2019/3/11
 */
public class MybatisPlusGenerator {


    static final String TABLE_NAME = "ashare_transaction";
    // final String MODULE_NAME = "charge";
    static final String AUTHOR = "fanglinan";

    //修改TABLE_NAME、MODULE_NAME、AUTHOR后运行main方法
    //注意，会覆盖MODULE_NAME下的所有代码
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir")+"/finance"; //项目目录
        System.out.println(projectPath);
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        globalConfig.setOpen(false);  //生成后打开文件目录
        globalConfig.setFileOverride(true);  //覆盖文件
        globalConfig.setBaseResultMap(true);  //生成BaseResultMap
        globalConfig.setBaseColumnList(true);  //生成BaseColumnList
        globalConfig.setServiceName("%sService");  //自定义service接口名
        globalConfig.setAuthor(AUTHOR);
        globalConfig.setDateType(DateType.ONLY_DATE);  //使用java.util.Date
        autoGenerator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://47.106.153.232:3306/dd_main0?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("fangln");
        dsc.setPassword("fangln1991!");
        autoGenerator.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.fly.finance");
        pc.setService("service");
        pc.setServiceImpl("service.impl");  //在service.impl包下生成Impl
        pc.setXml("mapper.finance");  //在mapper生成xml
        //pc.setModuleName(MODULE_NAME);
        autoGenerator.setPackageInfo(pc);


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //数据库表映射到实体的命名策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //是否为lombok模型
        strategy.setEntityLombokModel(true);
        //生成 @RestController 控制器
        strategy.setRestControllerStyle(true);
        //表前缀
        //strategy.setTablePrefix("t");
        //需要包含的表名，允许正则表达式（与exclude二选一配置）
        strategy.setInclude(TABLE_NAME);
        autoGenerator.setStrategy(strategy);

        //autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-rb");
                this.setMap(map);
            }
        };

        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return "D:\\personal-project\\fly-cloud\\finance\\src\\main\\resources" + "/mapper/finance/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        cfg.setFileOutConfigList(focList);
        autoGenerator.setCfg(cfg);

        // 关闭默认 xml 生成，调整生成 至 根目录
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);
        autoGenerator.setTemplate(tc);

        autoGenerator.execute();

    }
}