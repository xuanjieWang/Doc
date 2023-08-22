## 安装jenkins

1. docker pull jenkins/jenkins:2.387.1
3. chmod -R 777 /var/jenkins_home
3. docker run --name jenkins --restart always -p 8080:8080 -p 50000:50000 -v /var/jenkins_home/:var/jenkins_home -d jenkins/jenkins:2.387.1
4. 将maven和jdk移动到jenkins_home目录下面
5. 安装jenkins插件Publish Over SSH，和Git Parameter
6. 进入虚拟机jenkins文件夹：修改Jenkins下载地址   vim hudson.model.UpdateCenter.xml
<url>https://mirrors.tuna.tsinghua.edu.cn/jenkins/updates/current/update-center.json</url>

## 安装sonar-scanner
1. 下载sonar-scanner在官网下载，源码文件里面，传到服务器上面，移动到jenkins服务器挂载目录，进入conf目录，修改properties文件
2. 解压，移动sonar-scanner移动到jenkins_home下面
3. 进入到sonar-scanner配置文件，vim conf/sonar-scanner.properties，
4. 配置端口号和url，字符集编码
5. 源码存放的位置：sonal.source=./
6. 需要编译过的二进制文件目录(jar包)：sonar.java.binaries=./target
7. jenkins安装sonarqube scanner插件，代码扫描工具，扫描代码的质量，将代码结果扫描到服务器上面。

## 集成gitlab
1. gitlab: 9999-root  jenkins-8080-xj  Sonar-Qube:9000-admin   harbar:80-admin
2. 将gitlab和harbar集成到jenkins中。完成持续交付和持续部署。将代码推送到gitlab，jenkins在项目中，将项目拉取到jenkins本地，自动打包，构建镜像（docker file），docker compose跑项目
3. 搭建一个springBoot项目。在gitlab中添加项目。gitlab创建用户
4. PS C:\Users\WXJ\Project\hellojks> git config --global user.name "Adminstrator"
5. PS C:\Users\WXJ\Project\hellojks> git config --global user.email "2071985621@qq.com"

1. 将代码提交到远程的gitlab
2. 在jenkins中创建任务，创建自由风格的项目
3. 创建 的项目之后需要打包，添加构建步骤，clean package--DskipTests
4. 创建Sonar Qube中将提交的代码进行代码检测。会出现悬虚镜像，push 到gitlab中，每一次提交都会打包会新增一个悬虚镜像，

## jenkins容器化：实现方案，在 容器内部安装docker（不适用），与宿主机共享docker。
## 修改docker.sock权限：/var/run/docker.sock  chown root:root docker.sock              chmod o+rw docker.sock

1. 修改配置文件：
2. 将target目录下面的jar包移动到docker目录：mv target/*.jar docker
3. 构建镜像：docker build -d helldjks.docker /
4. 登录harbor： docker login -u amdin -p 1111
5. docker tar hellojks
6. 清除悬虚镜像：docker image prune -f
7. 将镜像推送到harbor: docker push
8. jenkins通知目标服务器：使用kenkins通知目标服务器。定义脚本文件
9. 流水线任务helloworld：流水线是根据jenkins构建阶段的清晰显示，用户可以通过可视化的操作方式轻松查看。

