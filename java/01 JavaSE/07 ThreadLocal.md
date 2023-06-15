## ThreadLocal是什么?
ThreadLocal，即线程本地变量。如果你创建了一个ThreadLocal变量，那么访问这个变量的每个线程都 会有这个变量的一个本地拷贝，多个线程操作这个变量的时候，实际是操作自己本地内存里面的变量，
从而起到线程隔离的作用，避免了线程安全问题。
```java
//创建一个ThreadLocal变量 static 
ThreadLocal<String> localVariable = new ThreadLocal<>();
```

ThreadLocal的应用场景有
1. 数据库连接池
2. 会话管理中使用

## ThreadLocal的实现原理
1. Thread类有一个类型为ThreadLocal.ThreadLocalMap的实例变量threadLocals，即每个线程都有 一个属于自己的ThreadLocalMap。
2. ThreadLocalMap内部维护着Entry数组，每个Entry代表一个完整的对象，key是ThreadLocal本 身，value是ThreadLocal的泛型值。
3. 每个线程在往ThreadLocal里设置值的时候，都是往自己的ThreadLocalMap里存，读也是以某个 ThreadLocal作为引用，在自己的map里找对应的key，从而实现了线程隔离

Thread对象中持有一个ThreadLocal.ThreadLocalMap的成员变量。
ThreadLocalMap内部维护了Entry数组，每个Entry代表一个完整的对象，key是ThreadLocal本 身，value是ThreadLocal的泛型值。

## 知道ThreadLocal 内存泄露问题吗？
弱引用比较容易被回收。因此，如果ThreadLocal（ThreadLocalMap的Key）被垃圾回收器回收了，但 是因为ThreadLocalMap生命周期和Thread是一样的，它这时候如果不被回收，就会出现这种情况： ThreadLocalMap的key没了，value还在，这就会「造成了内存泄漏问题」 。
如何「解决内存泄漏问题」 ？使用完ThreadLocal后，及时调用remove()方法释放内存空间。
