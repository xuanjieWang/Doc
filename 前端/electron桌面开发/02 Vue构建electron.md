### 1. 先对文件中的图标进行更换    /  dist_electron > bundle > favicon.ico  尺寸是256X256

### 2. 添加系统文件
#### 2.1 electron安装包文件的复制防止路径  C:\Users\Administrator\AppData\Local\electron\Cache
1. electron-v1.8.2-win32-x64.zip下载失败 进入官网，然后将该包直接下载下来，然后放置到项目打包所依赖的文件目录中即可
2. winCodeSign-1.9.0.7z 官网找到所依赖的包的源码地址，然后将该包直接下载下来，然后放置到项目打包所依赖的文件目录中即可


#### 2.2 其余的下载的安装包文件和txt文件安装在下述路径 C:\Users\Administrator\AppData\Local\electron-builder\Cache
1. app-builder-v0.6.1-x64.7z 到官网找到所依赖的包的源码地址，然后将该包直接下载下来，然后放置到项目打包所依赖的文件目录中即可
2. nsis-3.0.1.13.7z,
3. nsis-resources-3.3.0.7z

```
