### dockerFile
1. DockerFile是构建Docker镜像的脚本文件，由一系列指令构成
2. 通过docker build命令创建镜像，会由上到下依次执行，每条指令都会构建出一个镜像。
### 编写DockerFile 文件，通过命令 docker build -t name . 就可以通过DockerFile文件编写出一个镜像，可以是一个包含c语言的程序

1. from scratch：是从空镜像，只能从Dockerfile被继承
2. ADD： 复制宿主机里面的文件到src文件中到dest目录中。src里面的文件是一个压缩文件。
3. COPY：功能和ADD是一样的,SRC不能是一个URL，不会自动解压
4. ONBUILD：用于指定当前镜像的子镜像进行构建的时候要执行的命令
5. WORKDIR：容器打开之后的相对路径，可以设置多路径
6. RUN: <command,param1>使用shell指定运行的cmd    执行过程中会调用第一个参数EXECUTABLE指定程序，后面是参数
7. CMD: 容器启动之后，执行完成run之后执行CMD命令
    CMD ["EXECUTABLE","PARAMS","PARAMS2"]
    CMD COMMAND PARAM1 PARAM2   执行shell命令
    CMD ["PARAM1","PARAM2"]     提供给 ENTRYPOINT的参数
ENTRYPOINT：和CMD差不多的。


### 使用docker build -f 指定Dockerfle文件 -it启动终端 镜像名称:版本号
### Dockerfile中的[command]或者[”executable“]如果通过CMD指定的，docker run不能带参数。如果Dockerfle中式通过ENTRYPOINT指定的，可以带参数ENRETPOINT式不能被替换的。   
结论：无论使用CMD还是ENTRYPOINT使用["executable"]的方式通用性会很高。

docker cache：大量应用了镜像之间的分层关系，下层作为上层的父镜像出现。镜像分层：（镜像文件系统和json文件）、
LABEN：将json文件中添加LABEL标签
COPY: 将宿主机的文件复制到容器目录。修改镜像层文件大小，json文件中镜像ID也会发生变化
