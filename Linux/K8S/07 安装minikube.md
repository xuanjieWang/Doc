云原生是一种构建和运行应用程序的方法，旨在充分利用云计算的优势。以下是一些常用的云原生技术：

1. 容器化：将应用程序及其依赖项打包为容器镜像，以实现跨平台和可移植性。Docker 是最常用的容器化工具之一。

2. 容器编排：用于管理和编排容器化应用程序的工具。Kubernetes 是最流行的容器编排工具，它提供了自动化部署、扩展、故障恢复和负载均衡等功能。

3. 微服务架构：将应用程序拆分为小型、独立的服务单元，每个服务单元都可以独立开发、部署和扩展。微服务架构有助于实现敏捷开发、可伸缩性和灵活性。

4. 持续集成/持续部署（CI/CD）：通过自动化构建、测试和部署流程，实现快速、可靠的软件交付。常用的工具包括 Jenkins、GitLab CI、Travis CI 等。

5. 服务网格：用于管理和监控微服务之间通信的技术。Istio 和 Linkerd 是常用的服务网格实现，它们提供流量控制、故障恢复、安全认证等功能。

6. 无服务器计算：将应用程序的开发和运行从基础设施细节中解放出来，通过使用云提供商的无服务器平台，开发人员可以专注于编写业务逻辑而无需管理服务器。AWS Lambda、Azure Functions 和 Google Cloud Functions 是常见的无服务器计算平台。

7. 基础设施即代码（IaC）：使用代码来定义和管理基础设施资源，例如虚拟机、网络和存储等。常用的工具有Terraform和AWS CloudFormation。

这些技术共同构成了云原生应用程序的基础设施和开发工具链，帮助开发人员和运维团队更好地利用云计算的优势，实现高效、可伸缩和可靠的应用程序交付。

### 安装minikber（默认容器运行时）
1. 下载地址https://minikube.sigs.k8s.io/docs/start/
2. 将镜像仓库设置为cn，将容器运行时设置为containerd， k8s1.24之后不再支持docker，不设置命令启动会出错
3. 启动成功会在docker软件中看到
4. 使用kubectl查看节点的状态，kubectl get node  kubectl get node 查看节点  kubectl get pod -A查看pod，apiserver，controllerManager，
5. 使用minikubectl ssh进入到容器中

### 什么是k3s  控制平面和管理节点
1. k3s是一个轻量级的完全兼容k8s发行版本
2. k3s将所有k8s控制平面组件都封装在单个二进制文件和进程中，文件<100m。暂用资源小
3. 适合边缘计算，物联网，嵌入式和ARM的场景
4. 在三台机器上面安装k3s集群

### containerd镜像加速
1. 一般在/etc/containerd/config.yml中间

### pod
1. pod是一个或者多个容器的容器组，是k8s中创建和管理的最小对象
2. pod是最小的调度单位，k8s管理pod不是容器
3. 同一个pod安排在同一节点
4. pod是逻辑主机，共享存储网络和配置生命
5. 每一个pod有唯一的ip地址，ip地址分配给pod，在同一个pod内部所有的容器共享一个ip和端口，pod可以使用localhost互相通信

### service（微服务的抽象）
1. 将一组Pod上的应用程序公开为网络服务的抽象方法
2. 为一组Pod提供相同的DNS名，可以负载均衡
3. k8s为Pod分配IP地址，但是IP地址可能会变化
4. 集群内的容器可以通过service名称访问服务，而不需要担心Pod的ip发生变化

### service外部访问
 集群外部访问，通过Clister-ip访问服务，有三种方式
6. nodePod：通过主机ip和静态端口暴露服务，集群外部主机可以使用节点IP和NodePort访问服务
7. externalName：将集群外部的网络引入集群内部
8. loadBalancer：使用云提供商的负载均衡器向外部暴露服务

### 声明式配置
1. 使用kubectl命令创建和管理k8s对象
2. 命令行是口头传达，简单，快速，高效
3. 声明式配置难度大吗，配置麻烦是适合操作复杂的对象，使用yaml配置 
4. 可以添加lable作为标签，在筛选的时候通过kubectl pod -l "标签信息"

### 容器运行时接口CRI与镜像导入导出
1. kubelet运行在每个node上面，管理和维护pod和容器的状态
2. CRI是kubelet和容器运行时之间的通信协议，docker没有CRI端口
3. 使用crictl是一个兼容CRI的容器运行时命令
4. 将docker的image导入到containerd容器中
5. 使用docker save 可以将image保存为一个tar，使用scp保存镜像

### 灰度发布 istio灰度发布
1. 使用服务网格技术灰度发布

### 使用自动化部署mysql集群
1. 使用服务挂在，在yam中进行配置，使用参数不存在就挂载出来
2. 在docker中，使用绑定改在的方式将配置文件挂在到容器中，在k8s中，容器可能调度在任意接待你，配置文件需要在节点上访问，分发，更新
3. configMap： 在键值对数据库中保存非加密的数据，一般保存配置文件，可以作为环境变量，命令行参数或者存储卷。
4. 不适合保存大量的数据。只适合去设置配置文件，比如mysql的字符集编码
5. configmap是自动更新的。修改之后就会更新

### Secret, docker Secret
1. Secret保存机密数据对象，一般用于保存密码，令牌或者密钥
2. data字段存放base64编码数据
3. stringData存储为编码的字符串
4. Secret不需要再应用程序中包含机密数据，减少机密数据泄露的风险
5. Secret和configMap一样用作环境变量，命令行参数或者存储卷技术

 ### 数据卷
 1. 数据卷存储在容器中，一旦容器删除，数据也会删除
 2. 卷是独立于容器之外的一块区域，通过挂载的方式共Pod容器使用
 3. 使用场景，卷可以在多个容器之间共享数据，可以将 容器数据存储在云服务器上
 4. 卷更容易备份和迁移
 5. 临时数据卷EV：enptyDir，缓存或存储日志，configMap，Secret，DownWardApi-pod注入数据
 6. 持久卷： 本地存储local，hostPath，网络存储NFS，分布式存储Ceph。
 7. 卷的状态：可用，已绑定，失败，以释放（pod删除，pv删除手动删除）
 8. 访问模式： 
 9. 投射卷：projected卷可以将多个卷映射到同一个目录上
 10. 使用helm自动化部署

#### 初始化容器
1. 初始化容器时一种特殊的容器，在pod容器内应用启动之前运行
2. 生成配置文件
3. 执行初始化命令或者脚本
4. 执行健康检查Read 或者 Health

#### 编车容器
1. 容器启动之后，初始化容器之后，编车容器将持久卷推送到其他容器
2. 其他容器获取历史数据

### 端口转发
1. kubectl Port-forword pods/mysql0 容器端口: 主机端口

### helm
1. Helm时k8s下面的包管理工具
2. 使用chart封装k8s的taml文件，只需要设置参数就可以快速部署应用
3. Chart：Helm包，包含所有资源和依赖，相当于模板，类似与maben中的pom.xml
4. repository，存放和共享charts
5. release时运行chart的实例，一个chart可以在集群中创建多次，release name不能重复
6. 安装Helm二进制文件，/usr/local/bin/helm目录下面，k3s
7. 和docker hub下面的仓库是差不多的，
8. 查询安装 helm get values my-mysql
9. 删除安装： helm delete my-mysql

### 使用helm部署集群
1. 使用 --set添加参数
2. 使用 -f 使用yaml文件覆盖默认配置，可以指定多次，优先使用最右边的文件
3. 如果使用两种方式，则--set会合并到yaml中，--set参数优先级会更高
