## pom添加Redis，把连接信息配置在共享配置中
```java
<dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

```
## pearl命名空间下添加pearl-common.yml，用于存放共有配置信息，我把Nacos注册中心和Redis配置都放在这里
```java
spring:
  redis:
    host: 127.0.0.1
    port: 6379
    database: 0
    timeout: 5000 #连接超时 毫秒
    password: 123456
    jedis:
      pool:
        maxActive: 3
        maxIdle: 3
        minIdle: 1
        maxWait: -1 #连接池最大等行时间 -1没有限制
  cloud:
    nacos:
      discovery:
        enabled: true
        server-addr: localhost:8848
          # Nacos 认证用户
        username: nacos
          # Nacos 认证密码
        password: 123456
aaa: pearl-common.yml
```
## 新建pearl-custom.yml，用于存放一些自定义配置，实际可以不要，此处主要是为了测试
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
        # Nacos 认证用户
        username: nacos
        # Nacos 认证密码
        password: 123456
        # 支持多个共享 Data Id 的配置，优先级小于extension-configs,自定义 Data Id 配置 属性是个集合，内部由 Config POJO 组成。Config 有 3 个属性，分别是 dataId, group 以及 refresh
        shared-configs[0]:
          data-id: pearl-common.yml # 配置文件名-Data Id
          group: PEARL_GROUP   # 默认为DEFAULT_GROUP
          refresh: false   # 是否动态刷新，默认为false
        shared-configs[1]:
          data-id: pearl-custom.yml
          group: PEARL_GROUP
          refresh: true
```
