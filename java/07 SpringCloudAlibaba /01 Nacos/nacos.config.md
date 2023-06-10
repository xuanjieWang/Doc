```java
spring:
  profiles:
    # 对应环境
    active: dev
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
        # 当要上阿里云时，阿里云上面的一个云账号名
        access-key: accessKey
        # 当要上阿里云时，阿里云上面的一个云账号密码
        secret-key: secretKey
        # Nacos Server 对外暴露的 context path
        context-path: nacos
        # 读取的配置内容对应的编码 默认UTF-8
        encode: ISO-8859-1
        # 配置文件后缀
        file-extension: yml
        # 配置对应的分组
        group: PEARL_GROUP
        # 命名空间 常用场景之一是不同环境的配置的区分隔离，例如开发测试环境和生产环境的资源（如配置、服务）隔离等
        namespace: 771d3d1a-374b-47fe-b88b-c53a0b271acf
        # 文件名前缀 默认为 ${spring.appliction.name}
        prefix: prefix
        # 客户端获取配置的超时时间(毫秒) 默认3000
        timeout: 5000
        # 配置成Nacos集群名称
        #cluster-name: clusterName
        # Nacos 认证用户
        username: nacos
        # Nacos 认证密码
        password: 123456
        # 长轮询的重试次数 默认3
        max-retry: 5
        # 长轮询任务重试时间，单位为毫秒
        config-retry-time: 1000
        # 长轮询的超时时间，单位为毫秒
        config-long-poll-timeout: 1000
        # 监听器首次添加时拉取远端配置 默认false
        enable-remote-sync-config: true
        # 地域的某个服务的入口域名，通过此域名可以动态地拿到服务端地址
        #endpoint: localhost
        # 是否开启监听和自动刷新
        refresh-enabled: true
        # 支持多个共享 Data Id 的配置，优先级小于extension-configs,自定义 Data Id 配置 属性是个集合，内部由 Config POJO 组成。Config 有 3 个属性，分别是 dataId, group 以及 refresh
        shared-configs[0]:
          data-id: pearl-common.yml
          group: DEV_GROUP   # 默认为DEFAULT_GROUP
          refresh: true   # 是否动态刷新，默认为false
        shared-configs[1]:
          data-id: pearl-test.yml
          group: DEV_GROUP
          refresh: true
        # 支持多个扩展 Data Id 的配置 ，优先级小于prefix+dev.yaml
        # extension-configs:

```
