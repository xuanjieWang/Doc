## 实际生产环境，存在多网卡，此时使用nacos可能会存在IP不正确问题，针对此情况，nacos服务端IP，客户端注册IP应该怎么配置？
当前部署为单机环境nacos,版本1.3.2

1. 找到application.properties文件
2. 修改固定ip   nacos.inetutils.ip-address=xxx.xxx.xxx
3. 其他配置项
```
# 可以让nacos使用局域网ip，这个在nacos部署的机器有多网卡时很有用，可以让nacos选择局域网网卡
nacos.inetutils.use-only-site-local-interfaces=true
# 支持网卡数组，可以让nacos忽略多个网卡
nacos.inetutils.ignored-interfaces[0]=eth0
nacos.inetutils.ignored-interfaces[1]=eth1
# nacos优先选择匹配的ip，支持正则匹配和前缀匹配
nacos.inetutils.preferred-networks[0]=30.5.124.
nacos.inetutils.preferred-networks[0]=30.5.124.(25[0-5]|2[0-4]\\d|((1d{2})|([1-9]?\\d))),30.5.124.(25[0-5]|2[0-4]\\d|((1d{2})|([1-9]?\\d)))
```
修改网卡
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
        ip: xxx.xxx.xxx
        # 配置文件格式
        file-extension: yml
        # 共享配置
        shared-configs:
          - application-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
```





