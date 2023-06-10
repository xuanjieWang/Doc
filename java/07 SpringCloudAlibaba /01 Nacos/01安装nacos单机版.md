## 简介
注册中心	将两个模块中的方法在注册中心中进行注册，就可以实现服务之间的调用。在Linux中使用单节点加载的时候需要将配置文件startup.sh mode修改为standalone单机模式（Linux中Nacos默认是集群启动，Windows中则是单机）

**
- 早期的注册中心：Eureka最原始的注册中心，2.0有性能瓶颈
- Zookeeper：支持，专业独立的产品，dubbo
- Consul：go语言开发的

**
Ncaos和eureka和zookeeper的区别
C：一致性，每个节点同一时刻查询的数据是一致的
A：可用性：集群节点中部分节点宕机的情况下，集群依旧可用
P：分区容错性：网络出现问题（脑裂的问题），与集体失去联系无法形成集群。
·zookeeper（CP）：保证了一致性和分区容错性，底层使用的是ZAB+两阶段提交的协议保证数据的一致性。Zookeeper只要当前节点总数小于总节点的1/2，zookeeper最好节点数是奇数
Zookeeper中有三种角色：master，follow，observer

Eureka（AP）：采用了去中心化的思想，保证了可用性和分区容错性。当还有一台节点启动的时候集群就可用。
Nacos（ap和cp之间的切换）：
服务的注册中心：spring-cloud-starter-alibaba-nacos-discover
服务的配置中心：spring-cloud-starter-alibaba-nacos-config

1.	在/scr/main/resources/bootstrap.properties配置nacos元数据（优先于application.properties加载）
2.	配置文件中添加服务发现的名称和服务发现的地址。配置中心默认添加一个叫数据集Data id规则就是应用名.properties。添加任何配置。
Spring.cloud.nacos.config.namespace=
3.	使用@RefreshScope：动态刷新配置	@Value(“${配置项的值}”)：获取配置文件中的值。
4.	配置中心连接本地mysql：

命名空间：配置隔离，默认public	可以基于环境进行隔离还可以基于微服务进行隔离
配置集：
配置集ID：文件名Data Id：
配置分组：

## 使用docker安装nacos

docker pull nacos/nacos-server:v2.0.4

docker run --env MODE=standalone --name nacos -d -p 8848:8848 -p 9848:9848 -p 9849:9849 nacos/nacos-server:v2.0.4

## 数据卷挂载
```
 docker run -d -e MODE=standalone -e JVM_XMS=256m -e JVM_XMX=256m -e JVM_XMN=256m -v /xj/soft/nacos/conf/conf:/home/nacos/conf -v /xj/soft/nacos/logs:/home/nacos/logs -v /xj/soft/nacos/data:/home/nacos/data -p 8848:8848 -p 9848:9848 -p 9849:9849 --name ruoyi-nacos --link ruoyi-mysql:ruoyi-mysql --restart=always nacos/nacos-server:v2.0.3
```

## sprinbCloud整合nacos
### 环境
**
- JDK-1.8
- Spring cloud-Hoxton.SR7
- Spring Boot-2.3.2.RELEASE
- Nacos-1.3.2
**
## 启动类添加@EnableDiscoveryClient

## 配置文件配置
```
# Spring
spring:
  application:
    # 应用名称
    name: ruoyi-gateway
  profiles:
    # 环境配置
    active: dev
  cloud:
    nacos:
      discovery:
        # 服务注册地址
        server-addr: 172.27.173.253:8848
      config:
        # 配置中心地址
        server-addr: 172.27.173.253:8848
        # 配置文件格式
        file-extension: yml
        # 共享配置
        shared-configs:
          - application-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
```

## 切换ap和cp模式
curl -X PUT '$NACOS_SERVER:8848/nacos/v1/ns/operator/switches?entry=serverMode&value=CP'
