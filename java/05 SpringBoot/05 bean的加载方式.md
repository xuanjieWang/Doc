## bean的加载方式
1. xml声明式加载
2. xml+注解方式声明
3. 配置类+扫描+注解的方式
4. 使用@import导入bean的类

```java

//xml声明式加载

<?xml version="1.0" encoding="UTF-8"?> <beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
<!--声明自定义bean--> <bean id="bookService" class="com.itheima.service.impl.BookServiceImpl" scope="singleton"/>
<!--声明第三方开发bean--> <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource"/>
</beans>

// xml+注解方式声明
```java
<?xml version="1.0" encoding="UTF-8"?> <beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context" xsi:schemaLocation="http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd"> <context:component-scan base-package="com.itheima"/>
</beans>

//使用@component方式加载
Service public class BookServiceImpl implements BookService {}

@Component
public class DbConfig {
  @Bean
public DruidDataSource getDataSource(){
    DruidDataSource ds = new DruidDataSource();
    return ds;
  }
}

//使用注解的方式声明
@Configuration
@ComponentScan("com.itheima")
public class SpringConfig {
  @Bean public DruidDataSource getDataSource(){
  DruidDataSource ds = new DruidDataSource(); return ds;
}
}
```

## @EnableConfigurationProperties(CartoonProperties.class) 设置使用属性类的时候加载bean（避免强制加载，根据导入之后加载）

## 自动装配原理
1. 条件注入@conditionOnBean
2. 组件扫描@component @service @bean
3. 自动装配注解：@autowire和@inject注解实现自动装配
4. 根据条件自动装配，使用@enableConfigurationProperties在需要的时候才进行注入
5. 自定义装配规则：用户自定义：使用@configuretion 注解
