#### 在mybatisPlus中，通过AbstractSqlInjector将BaseMapper中的方法注入到了Mybatis容器，这样这些方法才 可以正常执行
1. 通过自己编写myBaseMapper继承baseMapper,定义接口方法
2. 编写mySqlInject继承defaultSqlInject
3. 编写类实现这个方法，并继承abstractMethod
### metaObjectHandler mybatis自动填充的操作接口   定义类实现这个接口，


#### 添加MySqlInjector，实现mybatis的sqlInjector
```java
public class MySqlInjector extends DefaultSqlInjector {
  @Override
  public List<AbstractMethod> getMethodList() {
    List<AbstractMethod> methodList = super.getMethodList();
    methodList.add(new FindAll());
    // 再扩充自定义的方法
    list.add(new FindAll());
    return methodList;
} }
```
#### 实现FildAll()方法，继承abstractMethod
```java

@component
public class FindAll extends AbstractMethod {
  @Override
  public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?>
modelClass, TableInfo tableInfo) {
  String sqlMethod = "findAll";
  String sql = "select * from " + tableInfo.getTableName();
  SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql,modelClass); return this.addSelectMappedStatement(mapperClass, sqlMethod, sqlSource,
modelClass, tableInfo);
}
}
```
