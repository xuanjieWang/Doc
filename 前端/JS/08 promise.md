Promise: 是一门新技术
     * 1. 从语法上来说：Promise 是一个构造函数
     * 2. 从功能上来说：promise 对象用来封装一个异步操作并可以获取其成功/失败的结果值
     * 流程：
     
     * 1. 创建 promise 对象 (pending 状态), 指定执行器函数
     * 2. 在执行器函数中启动异步任务
     *    2.1 如果成功了，调用 resolve(), 指定成功的 value, 变为 fulfilled 状态
     *    2.2 如果失败了，调用 reject(), 指定失败的 error, 变为 rejected 状态
       
     * 1. 三个状态：
     *    1.1 pending: 未确定的，起始时的状态
     *    1.2 fulfilled: 成功的，调用 resolve() 后的状态
     *    1.3 rejected: 失败的，调用 reject() 后的状态
     * 2. 2 种状态改变的形式
     *    2.2 pending ==> fulfilled
     *    2.3 pending ==> rejected
     * 3. 状态只能改变一次

promise：解决回调地狱，处理异步更加灵活

执行顺序
1. 首先执行主线程上面的同步任务
2. 遇到异步代码时，如果是宏任务（如 setTimeout/setInterval），则将其放入宏任务队列；如果是微任务（如 Promise.then/nextTick），则将其放入微任务队列
3. 当主线程上的同步代码执行完毕后，在 Event Loop 开始之前，会先检查微任务队列，如果有未执行的微任务，则依次执行完所有的微任务
4. 然后再检查 DOM 渲染，如果有需要更新的 DOM，则进行渲染
5. 最后再从宏任务队列中取出一个宏任务并执行，然后重复上述过程


