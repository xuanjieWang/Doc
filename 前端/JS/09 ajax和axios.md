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
