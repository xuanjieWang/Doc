## DOcker网络
Docker网络原理是Linux网络空间，Linux内核提供的用于实现网络虚拟化的功能，可以创建多个隔离的网络空间。

1. CNM：网络模型：一个网络空间就代表了一个 独立主机，CNM是有个规范，
2. bridge网络：加入网络的容器有独立的地址，网络接口地址，和ip
3. none网络：也有独立的namespace，但是没有连接外网的接口
4. hostL: 没有独立的namespace，加入网络的容器无需暴露端口号，其端口号直接在宿主机的namespace中

## Docker 隧道技术 连接不同docker容器之间的主机，跨主机通信，自动路由，跨网络环境
1. Docker Overlay 网络：Overlay 网络是 Docker 提供的一种网络驱动程序，它使用 VXLAN（Virtual Extensible LAN）等技术将不同主机上的容器连接到一个共享网络中。这样，容器就可以通过 Overlay 网络进行跨主机的通信，实现了隧道化的效果。、
2. Calico：Calico 是一种开源的容器网络解决方案，可以为 Docker 容器提供强大的网络隔离和路由功能。它使用 BGP（Border Gateway Protocol）协议来建立容器之间的隧道，实现可靠的跨主机通信。
3. Flannel：Flannel 是另一种流行的容器网络解决方案，它使用不同的后端技术（如 VXLAN、GRE、Host-GW 等）来构建虚拟网络。Flannel 可以为 Docker 容器提供网络隧道，使容器能够在不同主机之间进行通信。

## Ci/Cd和devops
1. Ci/cd是持续集成，持续交付，将代码交付到共同的仓库中，构建测试验证
2. devops：是软件开发文化，持续交互，自动化，弹性基础设置，


### CI/CD架构
1. idea(git push)
2. gitlab(git pull)
3. jenkins(使用maven package构建，代码质量监测SonarQube，SonarScanner（代码监测的客户端，本地对代码监测）)
4. 构建成镜像Docker build(docker file)
5. 推送到镜像中心Harbor  docker push
6. 目标服务器，拉取到镜像文件，通过ssh拉取到jenkins镜像，使用docker run运行
7. 构建结果提醒到钉钉
8. 
