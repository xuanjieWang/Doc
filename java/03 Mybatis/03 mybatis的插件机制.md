## mybatsi允许你在已经映射语句执行过程中能进行拦截调用,使用动态代理来执行的
1. 拦截执行器的方法：Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
2. 拦截参数的处理：ParameterHandler (getParameterObject, setParameters,prepare(拦截用于创建PreparedStatement对象，并设置超时时间。))
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

### mybatis插件原理
1. 每个创建出来的对象,都不是直接返回的,通过interceptorChain,.pluginAll(paranmeterHandler)
2. 获取到所有的拦截器,调用interceptor.plug方法进行操作,通过返回target
3. 都是执行invoke方法,完成逻辑的增强
4. 自定义插件,对核心方法进行拦截,

## 自定义MybatisPlus插件
1. 实现intercepts接口
2. 创建一个Java类，实现MyBatis的Interceptor接口。
3. 在实现类中重写intercept方法，该方法用于拦截MyBatis的方法调用。
4. 在intercept方法中，可以对拦截到的方法进行处理，并返回处理后的结果。
5. 可以通过使用@Intercepts注解，指定要拦截的方法。例如：@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})。
6. 在MyBatis配置文件中配置自定义插件。例如：<plugins><plugin interceptor="com.example.MyInterceptor"></plugin></plugins>。
