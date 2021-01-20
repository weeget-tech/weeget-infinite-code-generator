package cn.weeget.code.generator;

import cn.weeget.code.generator.config.GenConfig;
import cn.weeget.code.generator.service.GenTableService;
import cn.weeget.code.generator.util.StringUtils;
import cn.weeget.code.generator.util.VelocityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author chenck
 * @date 2021/1/14 15:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GeneratorTest {

    @Autowired
    GenTableService genTableService;

    @Autowired
    GenConfig genConfig;

    /**
     * 生成配置
     */
    @Bean
    public GenConfig genConfig() {
        GenConfig genConfig = new GenConfig();
        genConfig.setAuthor("chenck");// 作者
        genConfig.setServiceName("weeget-bullet-goods-service");// 服务名
        genConfig.setServicePort("9391");// 服务端口
        genConfig.setPackageName("cn.weeget.service.goods");// 默认生成包路径 goods，需改成自己的模块名称，如 coupon/order等
        genConfig.setAutoRemovePre(true);// 自动去除表前缀，默认是true
        genConfig.setTablePrefix("");// 表前缀（生成类名不会包含表前缀，多个用逗号分隔）
        return genConfig;
    }

    /**
     * 第一步：指定表名
     */
    String[] tableNames = {
            "goods",
    };

    /**
     * 第二步：导入表结构（将表信息和列信息插入DB）
     * 注：只需要导入一次即可。
     */
    @Test
    public void importTableSave() {
        genTableService.importTableSave(tableNames);
    }

    /**
     * 第三步：代码生成【fegin】
     * 注：生成后将code.zip解压，然后copy到对应的工程目录下
     */
    @Test
    public void genFeignCode() {
        // 显示指定feign的包路径
        genConfig.setPackageName("cn.weeget.service.goods.zax");// 默认生成包路径 goods，需改成自己的模块名称，如 coupon/order等
        // 指定模板
        VelocityUtils.addTemplate("vm/weeget/entityDTO.java.vm");
        VelocityUtils.addTemplate("vm/weeget/entityQueryDTO.java.vm");
        VelocityUtils.addTemplate("vm/weeget/feignClient.java.vm");
        VelocityUtils.addTemplate("vm/weeget/feignClientHystrix.java.vm");

        String zipName = "feign_code.zip";
        String outZipPath = GeneratorTest.class.getResource("/").getPath() + zipName;
        System.out.println("[feign]代码生成路径： " + outZipPath);
        genTableService.generatorCode(tableNames, outZipPath);
    }

    /**
     * 第四步：代码生成【service层】
     * 注：生成后将code.zip解压，然后copy到对应的工程目录下
     */
    @Test
    public void genServiceCode() {
        // 指定模板
        VelocityUtils.addTemplate("vm/weeget/controller.java.vm");
        VelocityUtils.addTemplate("vm/weeget/controllerTest.java.vm");
        VelocityUtils.addTemplate("vm/weeget/entity.java.vm");
        VelocityUtils.addTemplate("vm/weeget/mapper.java.vm");
        VelocityUtils.addTemplate("vm/weeget/mapper.xml.vm");
        VelocityUtils.addTemplate("vm/weeget/service.java.vm");
        VelocityUtils.addTemplate("vm/weeget/serviceImpl.java.vm");

        String zipName = "service_code.zip";
        String outZipPath = GeneratorTest.class.getResource("/").getPath() + zipName;
        System.out.println("[service]代码生成路径： " + outZipPath);
        genTableService.generatorCode(tableNames, outZipPath);
    }

}
