## 使用go创建网站
1. kvWeb.go使用
2. SetDeadline设置网络连接的超时时间，可以设置读取的超时时间和写入的超时时间

## go的标准库——net
1. net.Dial()方法作为客户端连接网络，第一个参数是协议名称（tcp），第二个是ip端口
2. net.Listen()方法作为服务端告诉go程序接收网络连接

## 远程调用RPC
RPC是使用TCP/ip进程之间的，客户端-服务端
1. sharedRPC.go 安装
2. 使用rpc.Dial()进行连接RPC服务器


```go
package main

import (
	"fmt"
	"net/http"
	"os"
	"time"
)

//处理函数配置url
func myHandler(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "Serving: %s\n", r.URL.Path)
	fmt.Printf("Served: %s\n", r.Host,"用户访问myHandler接口")
}

//第二个函数的处理方式
func timeHandler(w http.ResponseWriter, r *http.Request) {
	t := time.Now().Format(time.DateTime)
	Body := "现在的时间是： "
	fmt.Fprintf(w, "<h1 align=\"center\">%s</h1>", Body)
	fmt.Fprintf(w, "<h2 align=\"center\">%s</h2>\n", t)
	fmt.Fprintf(w, "访问的路径是：: %s\n", r.URL.Path)
	fmt.Fprintf(w, "访问的端口是：: %s\n", r.Host)
}
func main() {
	PORT := ":8001"
	arguments := os.Args
	if len(arguments) == 1 {
		fmt.Println("Using default port number: ", PORT)
	} else {
		PORT = ":" + arguments[1]
	}
	http.HandleFunc("/time", timeHandler)
	http.HandleFunc("/", myHandler)
	err := http.ListenAndServe(PORT, nil)
	if err != nil {
		fmt.Println(err)
		return
	}
}

```

```go
srv := &http.Server { Addr: PORT, Handler: m, ReadTimeout: 3 * time.Second, WriteTimeout: 3 * time.Second,
}
```
