## DockerCOmpose容器编排
1. DockerCompose：在生产的时候，每一个开启容器的时候是有顺序的，是一个Docker容器编排外部工具，不是通过脚本组织起来的，通过声明式的将应用组织起来的。启动创建多个容器
2. compose文件中包含6个属性：version ,service（必须的） ,network ,volumes ,configs ,secrets很多属性
3. docker compose的安装：curl -SL https://github.com/docker/compose/releases/download/v2.18.1/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose
4. 添加为可执行文件： chmod +x /usr/local/bin/docker-compose

 ```java
 使用dockerfile将进行构建为镜像
 FROM openjdk:8U102
 LABLE auth:xuanjie
 COPY xxx.jar xxx.jar
 ENTRYPOINT ["java","jar","xxx.jar"]
 EXPOSE 8080

```

1.  在应用中，打包的jar包只能在本地进行运行，手工启动项目不仅繁琐易错，还存在一个致命的问题。redis和mysql都是在配置文件中写死的，使用Docker compose可以解决问题
2.   检查缩写的compose.yml文件是不是有错误：docker-compose config -q
3.   docker-compose启动命令；docker-compose up -d
``` java
  将文件的名称设置为
 services:
  xjapp:
    build: ./
    container_name: myapp
    networks:
      - ab
    ports:
      - 9000:8080
    volumes:
      - ./logs:/var/applogs
    depends_on:
      - xjmysql
      - xjredis

  xjmysql:
    image: mysql:5.7
    container_name: mymysql
    environment:
      MYSQL_ROOT_PASSWORD: 111
    ports:
      - 3306:3306
    volumes:
      - /root/mysql/log:/var/log/mysql
      - /root/mysql/data:/var/lib/mysql
      - /root/mysql/conf:/etc/mysql/conf.d

  xjredis:
    image: redis:7.0
    container_name: myredis
    ports:
      - 6379:6379
    volumes:
      - /root/redis/reids.conf:/etc/redis/redis.conf
      - /root/redis/data:data
    command: redis-server /etc/redis/redis.conf
    
  networks:
   ab:
 ```
 
 

