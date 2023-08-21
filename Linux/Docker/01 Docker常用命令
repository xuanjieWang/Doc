## Docker常用命令
```rust
docker pull redis   docker rmi redis      docker ps -a  docker stop myredis
docker run --name myredis -d redis
伪终端的方式运行：docker run --name myredis -it -p tomcat    -it 伪终端方式   -d守护进程方式

Docker运行的本质：与用户交互的状态或者等待状态。docker run 直接在后台运行  -e(-env-file) 设置环境变量  -i 交互方式 -t 给出客户端  -w:公共目录
Docker退出：-it启动容器之后，exit直接退出 Ctrl+P+Q退出容器后台运行    使用docker exec进入容器之后容器是不会停止的，需要后台结束
Docker进入容器：docker exec -it name  进入到容器的内部

Docker attach 和docker exec（线程隔离的）
docker attach：就是一个进程，多个客户端进入到一个容器的时候是操作指令都可以看见的。可以看见日志

docker top name ：查看正在运行的进程
docker cp ： 将一个文件拷贝到镜像下面的一个目录中  docker cp mytar myredis:/root
docker rm ： 删除镜像命令 docker rm names

提交容器为镜像：docker commit -a 作者 -m 提交的信息 -p 暂停容器执行(默认是true)
导出镜像,为一个压缩包     docker save -o my.tar zookeeper3.7  导入镜像包(压缩包): docker load -i my.tar
导出容器：docker export -o（指定输出的文件）    导入容器：docker import -c（添加镜像指令） -m（提交信息）
export和save：容器和镜像导出导出的区别：export一次只能对一个容器操作，save一次可以对多个镜像操作。 save原封不动的保存文件系统，export只是将文件系统快照保存，丢失原镜像历史记录
impoer和load：最终都是镜像，import恢复镜像只是当前一层，load恢复的镜像和原来是相同的

docker system df ：查看容器磁盘占用    docker system event ：查看启动日志   docker system info：查看信息  docker system prune(移除所有的未使用镜像)

docker pause：可以暂停容器对外提供服务
docker create：创建一个容器但是不运行

容器退出的状态码    0：正常退出  1-128：容器内部错误    129-255：非正常退出，收到终止信号之后进行退出


```








