
## 事件
1. 可以是没有参数的事件
2. 可以是传递一个或者多个参数的事件
3. 可以是没有参数，但是使用event可以获取到：test4(params,$event) 可以获取到当前参数的的内容

### 事件修饰符
阻止事件的冒泡:        event.stopPropagation()     简写：在定义的方法上面使用           @click.stop="click"
阻止事件的默认行为:     event。preventDefault()     简写：在定义的方法上面使用 @click.prevent="click"

### 案件修饰符
input标签获取到值：通过event.targar.value获取到          @keyup.enter="click"   在按下enter的时候进行触发
if（event.keyCode === 13）{}                      简写：@keyup.13="click"

### 声明周期:     beforeCreate() -> create()  -> beforeMount() -> mounted()  -> beforeUpdate()  -> update()

let var const 都是生命变量的关键字

在 JavaScript 中，let、var 和 const 都是用于声明变量的关键字，它们有以下的区别：

1. var 会在当前作用域下声明一个变量，并将其初始化为 undefined。而 let 和 const 声明变量后不会被自动初始化。

2. var 声明的变量作用域是函数作用域或全局作用域（如果没有包含在任何函数中）。而 let 和 const 声明的变量作用域是块级作用域（例如 if、for 和 while 等语句块）。

3. 使用 var 声明的变量可以在同一作用域内重复声明，而 let 和 const 只允许声明一次。同时，
在同一作用域内使用 let 或 const 声明的变量名不能与函数名或参数名相同，而这种情况在 var 中是允许的。

4. 使用 let 声明的变量可以被重新赋值，而使用 const 声明的变量不能被重新赋值，但是 const 声明的对象和数组中的值可以被修改。  因为数组是引用数据类型，地址值没有发生变化。深拷贝和浅拷贝的区别

5. 在 JavaScript 的严格模式下，使用未声明的变量会抛出错误。使用 var 声明的变量没有这个问题，因为它会被自动提升到作用域的顶部。
但是，如果使用 let 或 const 声明的变量在声明之前被访问，会抛出 ReferenceError 错误。

以上是 let、var 和 const 的一些主要区别。在实际开发中，可以根据具体情况选择合适的关键字来声明变量。
建议在全局作用域下尽可能使用 const 来声明常量，在局部作用域下使用 let 来声明变量。而 var 声明变量的用法已经越来越少，推荐尽可能使用 let 和 const 来代替它。

	5). vue包含一系列的扩展插件(库):
		* vue-cli: vue脚手架
		* vue-resource(axios): ajax请求
		* vue-router: 路由
		* vuex: 状态管理
		* vue-lazyload: 图片懒加载
		* vue-scroller: 页面滑动相关
		* mint-ui: 基于vue的组件库(移动端)
		* element-ui: 基于vue的组件库(PC端)
  
