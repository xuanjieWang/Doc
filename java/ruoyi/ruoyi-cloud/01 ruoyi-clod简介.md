## tuoyi-cloud简介

### 项目启动（使用docker安装） [文档地址](https://blog.csdn.net/zht3306/article/details/124687425)
#### docker安装nacos
```
docker pull nacos/nacos-server:v2.0.4
docker run --env MODE=standalone --name nacos -d -p 8848:8848 -p 9848:9848 -p 9849:9849 nacos/nacos-server:v2.0.4
```
#### docker安装nginx
```
docker pull nginx
docker run -di --name nginx -p 80:80 nginx
mkdir -p /home/ruoyi/nginx
# 将容器内的配置文件拷贝到指定目录
docker cp nginx:/etc/nginx /home/ruoyi/nginx/conf
docker stop nginx
docker rm nginx
docker run -di --name nginx -p 80:80 -v /home/ruoyi/nginx/conf:/etc/nginx nginx
```
#### docker安装mysql
```
docker pull mysql:8.0
mkdir -p /home/mysql/data /home/mysql/logs /home/mysql/conf
#此处的password为即将设置的数据库密码建议修改成为自己的密码
docker run -p 3306:3306 --name mysql -v /home/mysql/conf:/etc/mysql/conf.d -v /home/mysql/logs:/logs -v /home/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0

docker exec -it mysql /bin/bash		#未挂载镜像时进入MySQL方式
//docker exec -it 122c297e7298 bash  #挂载后进入容器方式,容器id 使用此命令进入mysql需要先执行docker ps 找到mysql镜像id
mysql -uroot -p
use mysql
flush privileges;
ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
flush privileges;
exit

docker ps			#找到mysql镜像id
docker exec -it MySQL镜像ID bash
mysql -uroot -p
use mysql
flush privileges;
ALTER USER 'root'@'%' IDENTIFIED BY 'root' PASSWORD EXPIRE NEVER; #修改加密规则
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'root'; #修改密码
flush privileges; #刷新数据

```
#### docker安装redis
```
docker pull redis
docker run -di --name redis -p 6379:6379 redis

```


