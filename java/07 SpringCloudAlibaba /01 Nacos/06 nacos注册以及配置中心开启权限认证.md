## 在之前搭建的项目中，Nacos注册服务时，没有开启权限认证，实际上线后，如果别人知道了Nacos地址，随便注册一个相同服务名的实例，则会导致大问题出现，所以开启权限认证及配置规范很重要。
1. 新建项目命名空间，不要使用默认public
2. 修改bootstrap.properties为bootstrap.yml,更加简洁
3. 在配置的命名空间下重新添加配置，舍弃各个dev等环境，1是文件太多，2是暴露个各个环境的配置也不安全,此配置只保留本项目需要的spring及自定义配置，其他可共享配置放在共项配置中.
4. 在nacos的application.properties文件中添加登录认证   nacos.core.auth.enbale=true
5. 修改配置bootstrap.yml，namespace值在控制台查找，或者新建时自己输入。
6. 添加分组信息和


```
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>com.alibaba.nacos</groupId>
                    <artifactId>nacos-client</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>com.alibaba.nacos</groupId>
            <artifactId>nacos-client</artifactId>
            <version>1.3.2</version>
        </dependency>

```
```
spring:
  application:
    # 服务名
    name: pearl-test
  cloud:
    nacos:
      config:
        # 是否开启配置中心 默认true
        enabled: true
        # 配置中心地址
        server-addr: localhost:8848
        # 配置文件后缀
        file-extension: yml
        # 配置对应的分组
        group: PEARL_GROUP
        # 命名空间 常用场景之一是不同环境的配置的区分隔离，例如开发测试环境和生产环境的资源（如配置、服务）隔离等
        namespace: ba42e722-81aa-48f1-9944-9dca57d5f396
        group: GROUPNAME
        username: nacos 
        password: 123456
```

