#### CI: 持续集成，将持续不断的更新的代码经过构建，测试之后也持续不断的集成到主干分支CD：持续部署，持续交付
#### DevOps：是一组过程，方法，系统的开发运维测试，通过自动化软件交付和架构更变的流程来使得构建，测试，发布软件更加便捷。是一种思想和管理模式

### CI/CD架构
1. idea(git push)
2. gitlab(git pull)
3. jenkins(使用maven package构建，代码质量监测SonarQube，SonarScanner（代码监测的客户端，本地对代码监测）)
4. 构建成镜像Docker build(docker file)
5. 推送到镜像中心Harbor  docker push
6. 目标服务器，拉取到镜像文件，通过ssh拉取到jenkins镜像，使用docker run运行
7. 构建结果提醒到钉钉

---java
### 安装gitlab 需要安装docker 和dockercompose
docker 安装gitlab： docker pull gitlab/gitlab-ce
docker exec -it gitlab bash
查看dockerlab密码： cat /etc/gitlab/initial_root_password

编写docker compose文件：
 ### // SonarQube安装：目标服务器安装，是一个代码扫描平台，质量检测，代码量安全隐患，编写规范，还需要安装postfres数据库和sonarqube镜像
services:
  postgres:
    image: postgres
    container_name: pgdb
    restart: always
    ports:
      - 5432:5432
    environment:
      POSTGRES_USER: sonar
      POSTGRES_PASSWORD: sonar

  sonarqube:
    image: sonarqube:9.9-community
    container_name: sonarqb
    restart: always
    depends_on:
      - postgres
    ports:
      - 9000:9000
    environment:
      SONAR_JDBC_URL: jdbc:postgressql://pgdb:5432/sonar
      SONAR_JDBC_USERNAME: sonar
      SONAR_JDBC_PASSWORD: sonar
      
 DockerCompose编写完成之后需要修改虚拟机内存大小  /etc/sysctl.conf    vm.max_map_count=262144  sysctl -p
 
 ---
 Harbor（镜像中心）和target的服务器的安装
 harbor： 从github上面下载，解压到指定目录      
 修改配置文件：mv harbor.yml.tmpl harbor.yml       vim harbar.yml         1.修改hostName：为虚拟机地址   2.注释https，    3.配置harbor密码
 运行 ./prepare   运行 ./install.sh
 访问 80端口，新建一个项目
 
 在目标服务器，安装docker compose，创建jenkins文件夹用于接收jenkins数据，
 
 
 
 
 




