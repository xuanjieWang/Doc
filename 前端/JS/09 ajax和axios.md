1. ajax是原生的方式XMLHttpRequest对象进行异步通信，需要手动设置请求头，处理响应和错误，而Axios是基于Promise的封装库
2. axios。请求和响应的拦截器，自动转换json，请求的取消，并发请求，axios可以通过catch方法捕获错误，可以设置全局的错误处理器

使用express的get和post方法
Express 提供了一组常用的方法，用于定义路由、处理请求和响应等操作。下面列举了 Express 中的一些常用方法：

1. app.get(path, callback)：定义一个处理 HTTP GET 请求的路由，指定路径 path 和对应的回调函数 callback。
2. app.post(path, callback)：定义一个处理 HTTP POST 请求的路由，指定路径 path 和对应的回调函数 callback。
3. app.put(path, callback)：定义一个处理 HTTP PUT 请求的路由，指定路径 path 和对应的回调函数 callback。
4. app.delete(path, callback)：定义一个处理 HTTP DELETE 请求的路由，指定路径 path 和对应的回调函数 callback。
5. app.use(middleware)：使用中间件 middleware，可以是一个函数或函数数组。中间件在请求被路由处理之前执行，可以用于处理请求的预处理、添加额外的功能等。
6. app.set(setting, value)：设置全局配置项，可以通过 app.get(setting) 获取配置项的值。
7. app.listen(port, callback)：启动服务器，监听指定的端口 port，并在服务器成功启动后执行回调函数 callback。
8. req.params：获取通过路由中的动态参数传递的值。
9. req.query：获取 URL 查询参数。
10. req.body：获取请求体的内容，通常用于处理 POST 请求中的表单数据。

跨域：协议，ip，端口号需要完全相同


**2.后台配置cors解决跨域**
  以Node为例：
	res.set('Access-Control-Allow-Origin', 'http://localhost:63342');

 **3.使用代理服务器**
		例如：nginx等

 nginx解决跨域的方法
```
http {
  ...
  # 允许跨域请求
  server {
    listen       80;
    server_name  yourdomain.com;

    location /api {
      add_header 'Access-Control-Allow-Origin' '*';
      add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS';
      add_header 'Access-Control-Allow-Headers' 'DNT,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type';
    }

    ...
  }
  ...
}
```

1. Access-Control-Allow-Origin 设置为 * 表示允许任何来源的跨域请求。如果你希望仅允许特定的源进行跨域请求，可以将 * 替换为相应的域名。
2. Access-Control-Allow-Methods 设置允许的请求方法，这里包括了常用的 GET、POST 和 OPTIONS。你也可以根据实际需要添加其他请求方法。
3. Access-Control-Allow-Headers 设置允许的请求头，这里包括了一些常见的请求头。你可以根据实际情况添加或删除请求头。

允许跨域的请求，允许跨域的方法，允许跨域的请求头

## 前端的jsonp解决跨域
1. 原理是通过script标签解决跨域，请求不同源的限制
2. 动态构建script节点，利用src属性，发送get请求绕过ajax引擎
3. 只能解决get请求，

请求行（url，方法），请求头（host，Cookie），请求体（body）
响应状态码，响应头，响应体
### 5. post请求体参数格式
    1). Content-Type: application/x-www-form-urlencoded;charset=utf-8
        用于键值对参数，参数的键值用=连接, 参数之间用&连接
        例如: name=%E5%B0%8F%E6%98%8E&age=12
    2). Content-Type: application/json;charset=utf-8
        用于json字符串参数
        例如: {"name": "%E5%B0%8F%E6%98%8E", "age": 12}
    3). Content-Type: multipart/form-data
        用于文件上传请求
    
浏览器端发请求: 只有XHR或fetch发出的才是ajax请求, 其它所有的都是非ajax请求

## 4. axios常用语法
    axios(config): 通用/最本质的发任意类型请求的方式
    axios(url[, config]): 可以只指定url发get请求
    axios.request(config): 等同于axios(config)
    axios.get(url[, config]): 发get请求
    axios.delete(url[, config]): 发delete请求
    axios.post(url[, data, config]): 发post请求
    axios.put(url[, data, config]): 发put请求
    
    axios.defaults.xxx: 请求的默认全局配置
    axios.interceptors.request.use(): 添加请求拦截器
    axios.interceptors.response.use(): 添加响应拦截器

    axios.create([config]): 创建一个新的axios(它没有下面的功能)
    
    axios.Cancel(): 用于创建取消请求的错误对象
    axios.CancelToken(): 用于创建取消请求的token对象
    axios.isCancel(): 是否是一个取消请求的错误
    axios.all(promises): 用于批量执行多个异步请求
    axios.spread(): 用来指定接收所有成功数据的回调函数的方法

 1). axios.create(config) 
        a. 根据指定配置创建一个新的axios, 也就就每个新axios都有自己的配置
        b. 新axios只是没有取消请求和批量发请求的方法, 其它所有语法都是一致的
        c. 为什么要设计这个语法?
            需求: 项目中有部分接口需要的配置与另一部分接口需要的配置不太一样, 如何处理
            解决: 创建2个新axios, 每个都有自己特有的配置, 分别应用到不同要求的接口请求中

    2). 拦截器函数/ajax请求/请求的回调函数的调用顺序
        a. 说明: 调用axios()并不是立即发送ajax请求, 而是需要经历一个较长的流程
        b. 流程: 请求拦截器2 => 请求拦截器1 => 发ajax请求 => 响应拦截器1 => 响应拦截器2 => 请求的回调
        c. 注意: 此流程是通过promise串连起来的, 请求拦截器传递的是config, 响应拦截器传递的是response
                错误流程控制与错误处理

    3). 取消请求
        a. 基本流程: 
            配置cancelToken对象
            缓存用于取消请求的cancel函数
            在后面特定时机调用cancel函数取消请求
            在错误回调中判断如果error是cancel, 做相应处理
        b. 实现功能
            点击按钮, 取消某个正在请求中的请求
            在请求一个接口前, 取消前面一个未完成的请求





