## 安装jenkins
1. docker pull jenkins/jenkins:2.387.1
3. chmod -R 777 /var/jenkins_home
3. docker run --name jenkins --restart always -p 8080:8080 -p 50000:50000 -v /var/jenkins_home/:var/jenkins_home -d jenkins/jenkins:2.387.1
4. 将maven和jdk移动到jenkins_home目录下面
5. 安装jenkins插件Publish Over SSH，和Git Parameter
6. 进入虚拟机jenkins文件夹：修改Jenkins下载地址   vim hudson.model.UpdateCenter.xml
<url>https://mirrors.tuna.tsinghua.edu.cn/jenkins/updates/current/update-center.json</url>

## 安装jenkins 整合 sonar-scanner 安装插件整合sonarqube
1. 下载sonar-scanner在官网下载，源码文件里面，传到服务器上面，移动到/var/jenkins_home服务器挂载目录，进入conf目录，修改properties文件
2. 解压，移动sonar-scanner移动到jenkins_home下面
3. 进入到sonar-scanner配置文件，vim conf/sonar-scanner.properties，
4. 配置端口号和url，字符集编码
5. 源码存放的位置：sonal.source=./
6. 需要编译过的二进制文件目录(jar包)：sonar.java.binaries=./target
7. jenkins安装sonarqube scanner插件，代码扫描工具，扫描代码的质量，将代码结果扫描到服务器上面。
8. jenkins添加目标服务器，ssh的方式连接

## idea集成gitlab，实现本地代码推送
1. gitlab: 9999-root  jenkins-8080-xj  Sonar-Qube:9000-admin   harbar:80-admin
2. 将gitlab和harbar集成到jenkins中。完成持续交付和持续部署。将代码推送到gitlab，jenkins在项目中，将项目拉取到jenkins本地，自动打包，构建镜像（docker file），docker compose跑项目
3. 在gitlab中创建git仓库，获取到http连接的地址
4. 使用idea创建本地git，提交的时候url是gitlab的仓库地址，点击提交添加gitlab登录密码
5. git config --global user.name "Adminstrator"
6. git config --global user.email "2071985621@qq.com"
7. 代码成功推送到gitlab

## jenkins集成gitlab
1. 将代码提交到远程的gitlab
2. 在jenkins中创建任务，创建自由风格的项目，在设置中添加源码管理，添加git地址
3. 创建的项目之后需要打包，添加构建后的步骤，添加mavne，添加步骤清理和打包clean package--DskipTests
4. 点击构建项目，构建项目成功。可以看见在/var/jenkins_home目录下面生成了target目录。

## jenkins添加代码监测Sonar Qube
1. 创建Sonar Qube中将提交的代码进行代码检测。会出现悬虚镜像，push 到gitlab中，每一次提交都会打包会新增一个悬虚镜像，
2. 执行代码(手工检测)：/var/jenkins_home/sonarScanner/bin/sonar-scanner Dsonar.login=admin Dsonar.password=wxj150038 Dsonar.projectKey=xj_jenkins
3. 配置自动检测：Jenkins中添加构建后的流程，Execute SonarQube Scanner 指定jdk
4. 在Analysis properties执行参数中添加  sonar.login=admin  sonar.password=wxj50038  sonar.projectKey=${JOB_NAME}  (当前项目的名称可以指定)
5. 需要在jenkins首页配置sonarQube的地址，确保构建后的sonarQube登录信息是否正确。
6. 删除构建之后的隐藏文件.scannerwork

## 将jenkins中代码检测之后的代码 添加构建后操作，将target使用ssh推到目标服务器
1. 因为是对其他服务器推送target
2. 添加构建后的操作，添加send build artifacts over SSH
3. 添加SSH Servier Name 是Jenkins配置中的远程SSH
4. 添加Transfer Ser Source files中的第一个  target/*.jar

## 实现将推送过去的jar包实现自动化部署
1. 在项目中添加dockerCompose文件（需要远程服务器有jdk环境）
2. 添加dockerfile文件，dockerfile中的FROM一定是一个docker image
3. 将之间运行的容器删除,清除悬虚镜像，每一次提交过去之后都会覆盖前一次的镜像：docker image prune -f

### Dockerfile
``` yml
FROM openjdk:8u102
LABEL auth="wangxuanjie"
COPY helloJenkins-0.0.1-SNAPSHOT.jar /usr/local/jenkins/jks.jar
WORKDIR /usr/local/jenkins
ENTRYPOINT ["java","-jar","jks.jar"]
```
### dockercompose
``` yml
services:
  helloJks:
    build: ./
    image: jks
    container_name: myhellojks
    ports:
      - 8080:8080
```

## jenkins容器化：在jenkins中使用docker构建镜像
1. 在生产环境中推送jar包很浪费资源，将docker build环节在jenkins中完成，直接推送到harbor中，其他的服务器进行拉取
2. 实现方案，1DIOD在容器内部安装docker（不适用），2DOOD与宿主机共享docker。
   ``` shell
   //修改docker.sock权限：
   cd /var/run/
   chown root:root docker.sock
   chmod o+rw docker.so ck

   // jenkins容器化步骤
   docker run --name jenkins \
   --restart=always \
   -p 8080:8080 \
   -p 50000:50000 \
   -v /var/jenkins_home:/var/jenkins_home \
   -v /var/run/docker.sock:/var/run/docker.sock \
   -v /usr/bin/docker:/usr/bin/docker \
   -v /etc/docker/daemo.json:/etc/docker/daemo.json \
   -d jenkins/jenkins:2.387.1-lts
   ```

1. 修改配置文件：
2. 将target目录下面的jar包移动到docker目录：mv target/*.jar docker
3. 构建镜像：docker build -d helldjks.docker /
4. 登录harbor： docker login -u amdin -p 1111
5. docker tar hellojks
6. 清除悬虚镜像：docker image prune -f
7. 将镜像推送到harbor: docker push
8. jenkins通知目标服务器：使用kenkins通知目标服务器。定义脚本文件
9. 流水线任务helloworld：流水线是根据jenkins构建阶段的清晰显示，用户可以通过可视化的操作方式轻松查看。

