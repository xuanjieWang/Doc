### 缓存cache

### cache
1. 创建缓存接口： 属性：id
2. 方法： 存入，取出，删除，获得大小，清空，获取读写锁

### CacheKey
1. MyBatis 对于其 Key 的生成采取规则为：[mappedStementId + offset + limit + SQL + queryParams + environment]生成一个哈希码
2. MyBatis 中的 CacheKey 类是用来作为缓存键的，用于标识缓存中的唯一对象。CacheKey 的实现方式比较简单，主要包括以下几个关键点：
3. 构造方法：CacheKey 类的构造方法接收了多个参数，包括 SQL 语句、参数对象、环境 ID 等，这些参数会被用来生成一个唯一的缓存键。
4. hashCode 方法：CacheKey 类重写了 hashCode 方法，通过将各个参数拼接成一个字符串，并计算其 hashCode 值来生成缓存键的 hashCode。这样可以确保相同的参数组合生成相同的 hashCode。
5. equals 方法：CacheKey 类重写了 equals 方法，用于比较两个 CacheKey 对象是否相等。在 equals 方法中，会逐个比较各个参数，只有所有参数都相等时才认为两个 CacheKey 对象相等。
6. toString 方法：CacheKey 类重写了 toString 方法，用于返回 CacheKey 对象的字符串表示形式，方便调试和日志输出。
7. 使用构造器传入[mappedStementId + offset + limit + SQL + queryParams + environment]
8. 属性：    hashcode = multiplier（） * hashcode + baseHashCode;  count属性个数，checksum检查的总数，basehashcode
9. 重写equals方法，==比较内存地址，判断类型，先比hashcode，checksum，count，理论上可以快速比出来，再检查每一项的hashcode的值

### TransactionalCacheManager 事务缓存管理器
1. clear(Cache)：清空指定缓存对象中的所有缓存数据。
2. commit()：提交当前事务。在事务提交时，会将当前事务中修改过的缓存数据同步到持久化缓存（例如二级缓存）中，以确保缓存数据的一致性。
3. rollback()：回滚当前事务。在事务回滚时，会撤销当前事务中修改过的缓存数据，恢复到事务开始前的状态。
4. TransactionalCacheManager 接口的实现类通常会根据具体的缓存策略和需求来实现上述方法，以管理事务缓存的生命周期和操作。通过 TransactionalCacheManager 的使用，可以确保在事务控制下对缓存数据的正确管理，保证缓存数据与数据库数据的一致性。

### CacheException extends PersistenceException 缓存异常类

1. BlockingCache，BlockingCache 是一个特殊的缓存装饰器（Cache Decorator），它实现了阻塞机制来保证在多线程环境下的数据一致性。底层是使用的ReentrantLock，存放到currenthashmap中。key是唯一的
2. FIFOCache 是一种基于先进先出（FIFO）原则的缓存实现，使用双向链表，超过1023，就移除第一个
3.  LoggingCache 的组件，它是 MyBatis 中用于缓存 SQL 语句执行结果的一种缓存实现，并且支持记录缓存的命中率、命中次数等相关信息。默认实现
4.  LruCache，最近最少使用缓存
5.  ScheduledCache： 定时任务缓存，目的是每一小时清空一下缓存
6.  SerializedCache： 序列化缓存，用途是先将对象序列化成2进制，再缓存,好处是将对象压缩了，省内存
7.  SoftCache： 软应用缓存
8.  SynchronizedCache： 同步缓存，弃用
9. TransactionalCache： 事务缓存，


### SoftCache软应用缓存
1. 创建对象和软引用：
2. 当需要缓存某个对象时，将该对象与一个软引用关联起来，通过 SoftReference 类实现。同时，将该软引用对象注册到一个 ReferenceQueue 中，以便在对象被回收时接收通知。
3. 访问缓存对象：
4. 当需要获取缓存对象时，首先检查软引用是否为空，如果软引用不为空，则通过软引用获取缓存对象。如果软引用为空，可能表示缓存对象已被回收或者尚未加载，根据具体情况可以重新加载对象或执行相应的处理逻辑。
5. 垃圾回收处理：
6. 垃圾回收器在内存不足时会尝试回收软引用指向的对象。当软引用指向的对象被回收时，对应的软引用对象会被添加到 ReferenceQueue 中。定期清理缓存：
7. 应用程序可以定期轮询 ReferenceQueue，检查其中是否有软引用对象，即被回收的对象的软引用。当发现软引用对象时，可以根据需要进行相应的清理操作，如从缓存中移除对应的键值对。
8. 缓存策略：
9. 在实现软引用缓存时，还需要考虑缓存策略，如何设置缓存大小、淘汰策略等。常见的缓存策略包括基于访问频率、时间戳等来淘汰不常用的缓存对象，以保持缓存大小在一个可控范围内。

