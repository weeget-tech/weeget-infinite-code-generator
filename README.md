# weeget-infinite-code-generator
代码生成器
# 代码生成
【数据库表名】基于自定义Velocity模板来生成service层所有代码，包含基本的增删改查和分页接口，接口可直接投产。

## 操作步骤
1、指定作者和包名

2、指定表名

3、导入表结构

注：只需要导入一次即可。
```text
执行方法：
cn.weeget.code.generator.GeneratorTest.importTableSave()
```
4、生成代码
```text
执行方法：
cn.weeget.code.generator.GeneratorTest.genFeignCode()
cn.weeget.code.generator.GeneratorTest.genServiceCode()
```
5、生成后将code.zip解压，然后copy到对应的工程目录下


## 解释
```html
Velocity是一个基于java的模板引擎（template engine）。
它允许任何人仅仅简单的使用模板语言（template language）来引用由java代码定义的对象。
```
