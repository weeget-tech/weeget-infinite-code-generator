# 代码生成
将【数据库表名】基于自定义Velocity模板来生成service层所有代码。

## 操作步骤
1、指定表名

2、导入表结构
```java
执行方法：cn.weeget.code.generator.GeneratorTest.importTableSave()
```
3、生成代码
```java
执行方法：cn.weeget.code.generator.GeneratorTest.generatorCode()
```
4、生成后将code.zip解压，然后copy到对应的工程目录下


## 解释
```html
Velocity是一个基于java的模板引擎（template engine）。
它允许任何人仅仅简单的使用模板语言（template language）来引用由java代码定义的对象。
```
