## 主进程和渲染进程
1. main.js: 主进程
2. index.html: 渲染进程 渲染可以是多个,在同一个主进程之间可以同时渲染多个窗口应用

#### globalShortcut模块可以在操作系统中,注册/注销全局快捷键,,只能在主进程中使用.注册快捷键和回调函数.
1. 在初始化的时候进行注册,注册所有的快捷键
2. 

#### 复制粘贴板对象,clipboard,进程:主进程+渲染进程


#### webView对象,需要在main.js中创建窗口的时候,在webPreferences中配置webViewTag:true
1. webView中可以插入js,css文件对象, did-start-loading   did-stop-loading 加载插件和脚本
2. 在主进程中使用webView标签,添加src,

#### shell对象,链接导向外部
