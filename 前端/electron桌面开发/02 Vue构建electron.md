### 1. 先对文件中的图标进行更换    /  dist_electron > bundle > favicon.ico  尺寸是256X256

### 2. 添加系统文件
#### 2.1 electron安装包文件的复制防止路径  C:\Users\Administrator\AppData\Local\electron\Cache
1. electron-v1.8.2-win32-x64.zip下载失败 进入官网，然后将该包直接下载下来，然后放置到项目打包所依赖的文件目录中即可
2. winCodeSign-1.9.0.7z 官网找到所依赖的包的源码地址，然后将该包直接下载下来，然后放置到项目打包所依赖的文件目录中即可


#### 2.2 其余的下载的安装包文件和txt文件安装在下述路径 C:\Users\Administrator\AppData\Local\electron-builder\Cache
1. app-builder-v0.6.1-x64.7z 到官网找到所依赖的包的源码地址，然后将该包直接下载下来，然后放置到项目打包所依赖的文件目录中即可
2. nsis-3.0.1.13.7z,
3. nsis-resources-3.3.0.7z

### 3. 需要做的一些项目配置
1. 将生产环境的地址转换  VUE_APP_BASE_API = '/prod-api'  如果没有web:VUE_APP_BASE_API = 'http://localhost:8080'  如果有web: VUE_APP_BASE_API = 'http://IP/prod-api'
2. 注释掉v-clipboard 文字复制剪贴
3. ruoyi-ui/src/router/index.js  将原来的hastory切换为hash模式
``` js
   # 原配置
export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

# 改为：
# 路由模式改为hash意味着在URL中使用hash（#）来表示路由路径
export default new Router({
  mode: 'hash', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

```
4.  Cookies为localStorage（重要）
   ```
  全局搜索Cookies.get并替换为localStorage.getItem
  全局搜索Cookies.set并替换为localStorage.setItem
  全局搜索Cookies.remove并替换为localStorage.removeItem
  ruoyi-ui/src/layout/components/Navbar.vue中进行修改      location.href = '/index';  修改为  this.$router.push('/login')

然后找到：ruoyi-ui/src/views/login.vue
localStorage.setItem("username", this.loginForm.username, { expires: 30 });
localStorage.setItem("password", encrypt(this.loginForm.password), { expires: 30 });
localStorage.setItem('rememberMe', this.loginForm.rememberMe, { expires: 30 });
修改为： 
localStorage.setItem("username", this.loginForm.username);
localStorage.setItem("password", encrypt(this.loginForm.password));
localStorage.setItem('rememberMe', this.loginForm.rememberMe);
   ```
5. 路径切换
``` js
安装依赖  "path-browserify": "^1.0.1",
全局修改path.resolve为path.posix.resolve    解决页面跳转404的问题
vue-config.js       const path = require("path-browserify")
// import path from 'path'  ->   import path from 'path-browserify';
```
   
7.  ruoyi-ui/package.json
    ``` js
      "main": "background.js",
  "scripts": {
    "dev": "vue-cli-service electron:serve",
    "build": "vue-cli-service electron:build --win --ia32",
    "build:win32": "vue-cli-service electron:build --win --ia32",
    "preview": "node build/index.js --preview",
    "lint": "eslint --ext .js,.vue src"
  },
    ```
6.  vue.config.js  添加页面的图标
```js
  pluginOptions: {
    electronBuilder: {
      builderOptions: {
        win: {
          icon: './public/favicon.ico'
        },
        mac: {
          icon: './public/favicon.ico'
        },
        productName: 'AppDemo'
      }
    }
  }
```


