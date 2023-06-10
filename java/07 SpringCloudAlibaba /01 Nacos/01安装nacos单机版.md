## 使用docker安装nacos

docker pull nacos/nacos-server:v2.0.4

docker run --env MODE=standalone --name nacos -d -p 8848:8848 -p 9848:9848 -p 9849:9849 nacos/nacos-server:v2.0.4

## 数据卷挂载
```
 docker run -d -e MODE=standalone -e JVM_XMS=256m -e JVM_XMX=256m -e JVM_XMN=256m -v /xj/soft/nacos/conf/conf:/home/nacos/conf -v /xj/soft/nacos/logs:/home/nacos/logs -v /xj/soft/nacos/data:/home/nacos/data -p 8848:8848 -p 9848:9848 -p 9849:9849 --name ruoyi-nacos --link ruoyi-mysql:ruoyi-mysql --restart=always nacos/nacos-server:v2.0.3
```


