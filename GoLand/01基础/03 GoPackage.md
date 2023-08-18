1. 在 Go 中所有东西都以 packages传递。Go package是Go 源文件，
2. 以 package 关键字后面跟着这个 package 的名字。一些 packages，net package有几个子目录，分别是net，http, mail, rpc, smtp, textproto 和 url ，
3. 他们分别依 net/http,net/mail, net/rpc, net/smtp, net/textproto 和 net/url引入
4. package不是子控制程序并且不能被编译为可执行文件，他们需要在main调用使用


### go函数
1. 匿名函数，不需要名称直接定义，也是闭包，当函数适合一个功能的时候是很方便和容易的
2. 多返回值的函数，strconv.atoi()
3. 可命名函数的返回值，虽然没有使用return进行返回，但是会根据顺序返回
4. 参数为指针的函数，返回值为指针的函数
5. 闭包，一个函数的返回值是一个函数
6. 函数作为参数：Go函数可以接收另一个Go函数作为参数，这个功能是很广泛的，
```go
//多返回值的函数
func aFunction() (int, int, float64, string){ }
//可命名函数的返回值
func namedMinMax(x, y int) (min, max int) {
if x > y{
  min = y max = x
} else {
  min = x max = y
}
  return
}

//这个方法接收一个参数为指针的值
func getPtr(v *float64) float64 {
  return *v * *v
}

//函数闭包，funReturnFun()函数返回一个匿名函数
func funReturnFun() func() int {
  i := 0
  return func() int {
    i++
    return i * i }
}

//函数作为参数，接收两个参数一个是f，一个是int值
func funFun(f func(int) int, v int) int {
  return f(v)
}
```

### 设计自己的package
1. 定义一个packages
2. 使用，需要一个带有main()函数的包创建执行文件，使用import将包导入


```go
//安装自己的包
mkdir ～/go/src/aPackage
$cp aPackage.go ~/go/src/aPackage/
$go install aPackage
$cd ~/go/pkg/dawin_amd64/
$ls -l aPackage.a
-rw-r--r-- 1 mtsouk staff 4980 Dec 22 06:12 aPackage.a
```

### 类型方法  func (a twoInts)类型方法 method方法名 (b twoInts)参数 twoInts返回值 {
### 类型断言
1. 类型断言的表示方法是x.(T)，其中x是接口类型的变量，T是判断的类型。
2. 类型断言，检查接口类型变量是否是特定的类型
3. 允许使用存储在接口中的具体值或将分配给新变量
4. switch用于类型判断

### 接口
### 反射  类型reflect.value,reflect.type
1. 动态的获取任意对象的类型和结构信息
2. 通过reflect包提供用于处理反射    fmt 、 text/template 和 html/template ’
3. 首先声明一个名 为 x 的变量，然后调用 reflect.ValueOf(&x).Elem() 方法。
4. 接下调用 xRefl.Type() 以获取存在 xType 中的变量类型。

1. 反射程序难以理解和阅读
2. 反收的go代码是的程序运行变慢
3. 反射错误在编译时无法捕获

### "flag"
### bufio包 缓冲输入输出适合读取文本文件
### 从/dev/random中读取 系统参数

### 复合数据类型 String
1. 创建只读的数据类型String.NewReader()
2. 
