package cn.weeget.code.generator.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.weeget.code.generator.consts.GenConstants;
import cn.weeget.code.generator.domain.GenTable;
import cn.weeget.code.generator.domain.GenTableColumn;
import cn.weeget.code.generator.mapper.GenTableColumnMapper;
import cn.weeget.code.generator.mapper.GenTableMapper;
import cn.weeget.code.generator.util.GenUtils;
import cn.weeget.code.generator.util.StringUtils;
import cn.weeget.code.generator.util.VelocityInitializer;
import cn.weeget.code.generator.util.VelocityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 业务 服务层实现
 *
 * @author ruoyi
 */
@Service
public class GenTableServiceImpl implements GenTableService {
    private static final Logger log = LoggerFactory.getLogger(GenTableServiceImpl.class);

    @Autowired
    private GenTableMapper genTableMapper;

    @Autowired
    private GenTableColumnMapper genTableColumnMapper;

    @Override
    public GenTable queryGenTableColumn(String tableName) {
        // 查询表信息
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        if (null == table) {
            throw new RuntimeException("表不存在 " + tableName);
        }
        // 设置主键列信息
        setPkColumn(table, table.getColumns());
        return table;
    }

    @Deprecated
    @Override
    public GenTable parseTable(String tableName) {
        // 查询表信息
        GenTable table = genTableMapper.selectDbTableByName(tableName);
        GenUtils.initTable(table, "system");
        // 查询列信息
        List<GenTableColumn> columns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
        for (GenTableColumn column : columns) {
            GenUtils.initColumnField(column, table);
        }
        // 设置主键列信息
        setPkColumn(table, columns);
        table.setColumns(columns);
        log.info("table={}", JSON.toJSONString(table));
        return table;
    }

    @Override
    public void importTableSave(String[] tableNames) {
        // 查询表信息
        List<GenTable> tableList = genTableMapper.selectDbTableListByNames(tableNames);
        if (CollectionUtils.isEmpty(tableList)) {
            log.info("[导入表结构]未查询到表");
            return;
        }
        for (GenTable table : tableList) {
            try {
                log.info("[导入表结构][{}]start", table.getTableName());
                String tableName = table.getTableName();
                GenUtils.initTable(table, "system");
                int row = genTableMapper.insertGenTable(table);
                if (row > 0) {
                    // 保存列信息
                    List<GenTableColumn> genTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
                    for (GenTableColumn column : genTableColumns) {
                        GenUtils.initColumnField(column, table);
                        genTableColumnMapper.insertGenTableColumn(column);
                    }
                }
                log.info("[导入表结构][{}]end", table.getTableName());
            } catch (Exception e) {
                log.info("[导入表结构失败][" + table.getTableName() + "]", e);
            }
        }
    }

    /**
     * 预览代码
     *
     * @param tableName 表编号
     * @return 预览数据列表
     */
    public Map<String, String> previewCode(String tableName) {
        Map<String, String> dataMap = new LinkedHashMap<>();

        // 查询表信息
        GenTable table = this.queryGenTableColumn(tableName);

        // 初始化vm方法
        VelocityInitializer.initVelocity();

        // 设置模板变量信息
        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, GenConstants.UTF8);
            tpl.merge(context, sw);
            log.info("{}", template);
            log.info("{}", sw.toString());
            dataMap.put(template, sw.toString());
        }
        return dataMap;
    }

    /**
     * 生成代码
     *
     * @param tableName 表名称
     * @return 数据
     */
    @Override
    public byte[] generatorCode(String tableName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        generatorCode(tableName, zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 批量生成代码
     *
     * @param tableNames 表数组
     * @return 数据
     */
    @Override
    public byte[] generatorCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {
            if (StringUtils.isBlank(tableName)) {
                continue;
            }
            generatorCode(tableName, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    @Override
    public void generatorCode(String[] tableNames, String outZipPath) {
        try {
            FileOutputStream outputStream = new FileOutputStream(outZipPath);
            ZipOutputStream zip = new ZipOutputStream(outputStream);
            for (String tableName : tableNames) {
                if (StringUtils.isBlank(tableName)) {
                    continue;
                }
                generatorCode(tableName, zip);
            }
            IOUtils.closeQuietly(zip);
        } catch (FileNotFoundException e) {
            log.error("生成异常", e);
        }
    }

    /**
     * 查询表信息并生成代码
     */
    private void generatorCode(String tableName, ZipOutputStream zip) {
        // 查询表信息
        GenTable table = this.queryGenTableColumn(tableName);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, GenConstants.UTF8);
            tpl.merge(context, sw);
            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(sw.toString(), zip, GenConstants.UTF8);
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                log.error("渲染模板失败，表名：" + table.getTableName(), e);
            }
        }
    }

    /**
     * 设置主键列信息
     *
     * @param table   业务表信息
     * @param columns 业务字段列表
     */
    public void setPkColumn(GenTable table, List<GenTableColumn> columns) {
        for (GenTableColumn column : columns) {
            column.setGetterMethodName("get" + StringUtils.upperCamelCase(column.getJavaField() + "()"));
            column.setSetterMethodName("set" + StringUtils.upperCamelCase(column.getJavaField() + "()"));
            if (column.isPk()) {
                table.setPkColumn(column);
                break;
            }
        }
        if (Objects.isNull(table.getPkColumn())) {
            table.setPkColumn(columns.get(0));
        }
    }

    /**
     * 设置代码生成其他选项值
     *
     * @param genTable 设置后的生成对象
     */
    public void setTableFromOptions(GenTable genTable) {
        JSONObject paramsObj = JSONObject.parseObject(genTable.getOptions());
        if (!Objects.isNull(paramsObj)) {
            String treeCode = paramsObj.getString(GenConstants.TREE_CODE);
            String treeParentCode = paramsObj.getString(GenConstants.TREE_PARENT_CODE);
            String treeName = paramsObj.getString(GenConstants.TREE_NAME);
            genTable.setTreeCode(treeCode);
            genTable.setTreeParentCode(treeParentCode);
            genTable.setTreeName(treeName);
        }
    }
}