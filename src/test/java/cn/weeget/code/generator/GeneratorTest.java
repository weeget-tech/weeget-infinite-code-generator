package cn.weeget.code.generator;

import cn.weeget.code.generator.service.GenTableService;
import cn.weeget.code.generator.util.StringUtils;
import cn.weeget.code.generator.util.VelocityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    // 指定表名
    String[] tableNames = {
            "goods",
    };

    /**
     * 第一步：导入表结构（将表信息和列信息插入DB）
     * 注：只需要导入一次即可。
     */
    @Test
    public void importTableSave() {
        genTableService.importTableSave(tableNames);
    }

    /**
     * 第二步：生成代码
     * 注：生成后将code.zip解压，然后copy到对应的工程目录下
     */
    @Test
    public void generatorCode() {
        // 指定模板
        VelocityUtils.addTemplate("vm/weeget/controller.java.vm");
        VelocityUtils.addTemplate("vm/weeget/controllerTest.java.vm");
        VelocityUtils.addTemplate("vm/weeget/entity.java.vm");
        VelocityUtils.addTemplate("vm/weeget/entityDTO.java.vm");
        VelocityUtils.addTemplate("vm/weeget/entityQueryDTO.java.vm");
        VelocityUtils.addTemplate("vm/weeget/mapper.java.vm");
        VelocityUtils.addTemplate("vm/weeget/mapper.xml.vm");
        VelocityUtils.addTemplate("vm/weeget/service.java.vm");
        VelocityUtils.addTemplate("vm/weeget/serviceImpl.java.vm");
        VelocityUtils.addTemplate("vm/weeget/feignClient.java.vm");
        VelocityUtils.addTemplate("vm/weeget/feignClientHystrix.java.vm");

        String zipName = "code.zip";
        String outZipPath = GeneratorTest.class.getResource("/").getPath() + zipName;
        System.out.println("代码生成路径： " + outZipPath);
        genTableService.generatorCode(tableNames, outZipPath);
    }

}
