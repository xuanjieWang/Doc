## mybatsi允许你在已经映射语句执行过程中能进行拦截调用
1. 拦截执行器的方法：Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
2. 拦截参数的处理：ParameterHandler (getParameterObject, setParameters)
3. 拦截结果集的处理：ResultSetHandler (handleResultSets, handleOutputParameters)
4. 拦截sql语句构建的处理：StatementHandler (prepare, parameterize, batch, update, query)

#### @Signature 注解是用于定义拦截器签名的注解，它包含三个属性：
1. type、指定目标方法所在的接口或者类
2. method： 指定方法的名称
3. args： 指定目标方法的参数

```java
package cn.itcast.mp.plugins; import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement; import org.apache.ibatis.plugin.*;
import java.util.Properties;

@Intercepts({@Signature( type= Executor.class, method = "update", args = {MappedStatement.class,Object.class})})
public class MyInterceptor implements Interceptor {
  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    //拦截方法，具体业务逻辑编写的位置
    return invocation.proceed();
  }

  @Override
  public Object plugin(Object target) {
     //创建target对象的代理对象,目的是将当前拦截器加入到该对象中
      return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {
    //属性设置
  }
}

/** * 自定义拦截器 */
@Bean public MyInterceptor myInterceptor(){ return new MyInterceptor();}
```

## 性能分析插件
#### 1. maven依赖
```java
<!-- 引入 MyBatis Plus -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>最新版本</version>
</dependency>

<!-- 引入性能分析插件 -->
<dependency>
    <groupId>p6spy</groupId>
    <artifactId>p6spy</artifactId>
    <version>最新版本</version>
</dependency>
```
#### 2. 配置文件配置插件
```java
@Configuration
public class MyBatisPlusConfig {

    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor interceptor = new PerformanceInterceptor();
        // 设置格式化 SQL，这样输出的 SQL 会更加易读，默认为 false
        interceptor.setFormat(true);
        // 设置最大执行时间，超过该时间的 SQL 将被输出，默认为 0，表示不限制
        interceptor.setMaxTime(1000);
        return interceptor;
    }
}
```

#### 3. 配置文件中配置启动插件
```java
@Configuration
@MapperScan("com.example.mapper")
public class MyBatisConfig extends MybatisPlusConfigurerAdapter {

    @Autowired
    private PerformanceInterceptor performanceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(performanceInterceptor);
    }

}
```
