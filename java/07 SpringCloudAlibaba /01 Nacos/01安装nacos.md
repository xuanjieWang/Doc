## 使用docker安装nacos

docker pull nacos/nacos-server:v2.0.4

docker run --env MODE=standalone --name nacos -d -p 8848:8848 -p 9848:9848 -p 9849:9849 nacos/nacos-server:v2.0.4

