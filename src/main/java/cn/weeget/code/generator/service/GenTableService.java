package cn.weeget.code.generator.service;


import cn.weeget.code.generator.domain.GenTable;

import java.util.Map;

/**
 * 业务 服务层
 *
 * @author ruoyi
 */
public interface GenTableService {

    /**
     * 查询表和列信息
     */
    public GenTable queryGenTableColumn(String tableName);

    /**
     * 解析table
     *
     * @param tableName 表名称
     */
    @Deprecated
    public GenTable parseTable(String tableName);

    /**
     * 导入表结构（保存）
     */
    public void importTableSave(String[] tableNames);

    /**
     * 预览代码
     *
     * @param tableName 表名
     * @return 预览数据列表
     */
    public Map<String, String> previewCode(String tableName);

    /**
     * 生成代码
     *
     * @param tableName 表名称
     * @return 数据
     */
    public byte[] generatorCode(String tableName);

    /**
     * 批量生成代码
     *
     * @param tableNames 表数组
     * @return 数据
     */
    public byte[] generatorCode(String[] tableNames);

    /***
     * 生成代码到指定zip文件
     *
     * @param tableNames 表数组
     * @param outZipPath 输出zip文件路径
     * @return void
     * @author chenck
     * @date 2020/4/14 15:17
     */
    public void generatorCode(String[] tableNames, String outZipPath);

}
