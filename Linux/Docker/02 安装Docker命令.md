# 安装docker
1.	需要一点Linux,Centos7,需要先安装yum，使用xshell连接远程服务进行操作
环境查看（系统内核是3.10以上的）
查看系统的详细信息(Centos7) cat /etc/os-release

# 1. 卸载旧的docker版本
 sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
                  
# 2. 安装需要的安装包
yum install -y yum-utils

# 3. 改镜像仓库，改成阿里云的
sudo yum-config-manager --add-repo  https://download.docker.com/linux/centos/docker-ce.repo  # 默认国外，太慢，不用
使用这个：yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

# 更新yum软件包索引
yum makecache fast

# 4. 安装Docker相关的依赖    docker-ce社区版   -ee企业版
sudo yum install docker-ce docker-ce-cli containerd.io

# 5. 启动docker
sudo systemctl start docker

# 6. 查看是否安装成功
docker version

# 7. 测试hello world
docker run hello-world

# 8.查看一下下载的这个 hello-world
  可以利用docker images最重要的一步一定是配置阿里云的加速器
  
6-查看是否安装成功 docker version
 了解卸载docker
 
#1. 卸载依赖
yum remove docker-ce docker-ce-cli containerd.io

#2. 删除资源
rm -rf /var/lib/docker

1.查看内核版本 <Docker 要求 CentOS 系统的内核版本高于 3.10>
   uname -r         本机<内核版本: 3.10.0-327.el7.x86_64>
2.把yum包更新到最新
  sudo yum update
3.安装需要的软件包, yum-util 提供yum-config-manager功能，另外两个是devicemapper驱动依赖的
  sudo yum install -y yum-utils device-mapper-persistent-data lvm2

4.设置yum源
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

5.查看仓库中docker版本 
  yum list docker-ce --showduplicates | sort -r

6. 安装docker
  sudo yum install docker-ce

7.启动Docker,设置开机启动,停止Docker
  sudo systemctl start docker
  sudo systemctl enable docker
  sudo systemctl stop docker   
8.查看版本
 docker version
