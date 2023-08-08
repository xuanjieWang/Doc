### node.js中第三方模块就是包

格式化时间：创建方法，定义格式化时间的方法，导出

使用npm包管理器，添加moment，使用require()导入格式化的包，
```js
const moment=require('moment)
const dt = moment().format('YYYY-MM-DD HH:mm:ss')
```

将包发布到npm
1. 进入npm首页，注册账号
2. 终端中转换为npm官方服务器，npm login进行登录
3. 运行 npm publish命令就可以发布包

模块的加载机制：
1. 模块在第一次加载之后会被缓存，多次调用require()之后被执行多次
2. 按照确切的名字加载，补全.js加载，补全.json加载，补全.node加载，加载失败报错
