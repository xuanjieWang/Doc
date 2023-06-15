## 说一说什么是AQS
1. AQS 是一个锁框架，它定义了锁的实现机制，并开放出扩展的地方，让子类去实现，比如我们在 lock 的时候，AQS 开放出 state 字段，让子类可以根据 state 字段来决定是否能够获得锁，
   对于获 取不到锁的线程 AQS 会自动进行管理，无需子类锁关心，这就是 lock 时锁的内部机制，封装的很好，又暴露出子类锁需要扩展的地方；
3. AQS 底层是由同步队列 + 条件队列联手组成，同步队列管理着获取不到锁的线程的排队和释放， 条件队列是在一定场景下，对同步队列的补充，比如获得锁的线程从空队列中拿数据，肯定是拿不到数据的，
   这时候条件队列就会管理该线程，使该线程阻塞；
5. AQS 围绕两个队列，提供了四大场景，分别是：获得锁、释放锁、条件队列的阻塞，条件队列的唤 醒，分别对应着 AQS 架构图中的四种颜色的线的走向。

## AQS使用了哪些设计模式？
1. 使用者继承AbstractQueuedSynchronizer并重写指定的方法。（这些重写方法很简单，无非是对 于共享资源state的获取和释放）
2. 将AQS组合在自定义同步组件的实现中，并调用其模板方法，而这些模板方法会调用使用者重写的 方法

## 了解AQS中同步队列的数据结构吗
1. 当前线程获取同步状态失败，同步器将当前线程机等待状态等信息构造成一个Node节点加入队 列，放在队尾，同步器重新设置尾节点
2. 加入队列后，会阻塞当前线程
3. 同步状态被释放并且同步器重新设置首节点，同步器唤醒等待队列中第一个节点，让其再次获取同 步状态

## 了解AQS 对资源的共享方式吗
1. Exclusive(独占)只有一个线程能执行，如ReentrantLock。又可分为公平锁和非公平锁：
2. Share（共享）：多个线程可同时执行，如Semaphore/CountDownLatch。Semaphore、 CountDownLatCh、 CyclicBarrier、ReadWriteLock 我们都会在后面讲到。
ReentrantReadWriteLock 可以看成是组合式，因为ReentrantReadWriteLock也就是读写锁允许多个线 程同时对某一资源进行读。

## AQS 组件了解吗
1. Semaphore(信号量)-允许多个线程同时访问： synchronized 和 ReentrantLock 都是一次只允 许一个线程访问某个资源，Semaphore(信号量)可以指定多个线程同时访问某个资源。
2. CountDownLatch （倒计时器） ： CountDownLatch是一个同步工具类，用来协调多个线程之 间的同步。这个工具通常用来控制线程等待，它可以让某一个线程等待直到倒计时结束，再开始执行。
3. CyclicBarrier(循环栅栏)： CyclicBarrier 和 CountDownLatch 非常类似，它也可以实现线程间 的技术等待，但是它的功能比 CountDownLatch 更加复杂和强大。主要应用场景和 CountDownLatch 类似。CyclicBarrier 的字面意思是可循环使用（Cyclic）的屏障（Barrier）。它 要做的事情是，让一组线程到达一个屏障（也可以叫同步点）时被阻塞，直到最后一个线程到达屏 障时，屏障才会开门，所有被屏障拦截的线程才会继续干活。CyclicBarrier默认的构造方法是 CyclicBarrier(int parties)，其参数表示屏障拦截的线程数量，每个线程调用await方法告诉
CyclicBarrier 我已经到达了屏障，然后当前线程被阻塞。


