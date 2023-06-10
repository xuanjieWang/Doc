## 简介
注册中心	将两个模块中的方法在注册中心中进行注册，就可以实现服务之间的调用。在Linux中使用单节点加载的时候需要将配置文件startup.sh mode修改为standalone单机模式（Linux中Nacos默认是集群启动，Windows中则是单机）

**
- 早期的注册中心：Eureka最原始的注册中心，2.0有性能瓶颈
- Zookeeper：支持，专业独立的产品，dubbo
- Consul：go语言开发的
**


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
## 启动类添加@Enable
