### 消息发送
1. 生产者向消息队列里写入消息，不同的业务场景需要生产者采用不同的写入策略。比如同步发送、 异步发送、Oneway发送、延迟发送、发送事务消息等。 默认使用的是DefaultMQProducer类，
2. 设置Producer的GroupName。
3. 2）设置InstanceName，当一个Jvm需要启动多个Producer的时候，通过设置不同的 InstanceName来区分，不设置的话系统使用默认名称“DEFAULT”。
4. 设置发送失败重试次数，当网络出现异常的时候，这个次数影响消息的重复投递次数。想保证 不丢消息，可以设置多重试几次。
5. 设置NameServer的地址
6. 组装消息并进行发送

#### 消息发送之后的状态
1. FLUSH_DISK_TIMEOUT：表示没有在规定时间内完成刷盘（需要Broker的刷盘策略被设置成 SYNC_FLUSH才会报这个错误）。
2. FLUSH_SLAVE_TIMEOUT：表示在主备方式下，并且Broker被设置成SYNC_MASTER方式， 没有在设定时间内完成主从同步。
3. SLAVE_NOT_AVAILABLE：这个状态产生的场景和FLUSH_SLAVE_TIMEOUT类似，表示在主 备方式下，并且Broker被设置成SYNC_MASTER，但是没有找到被配置成Slave的Broker。
4. SEND_OK：表示发送成功，发送成功的具体含义，比如消息是否已经被存储到磁盘？消息是 否被同步到了Slave上？消息在Slave上是否被写入磁盘？需要结合所配置的刷盘策略、主从策略来定。这个状态还可以简单理解为，没有发生上面列出的三个问题状态就是SEND_OK。


#### 提升写入的性能
1. 采用oneway的方式实现，只发送到客户端的socket缓冲区就进行返回
2. 增加producer的并发量，使用多个生产和进行发送，RocketMQ引入了一个并发窗口，在窗 口内消息可以并发地写入DirectMem中，然后异步地将连续一段无空洞的数据刷入文件系统当中。
3. 在Linux操作系统层级进行调优，推荐使用EXT4文件系统，IO调度算法使用deadline算法。

#### 消息消费
1. 消息消费方式（Pull和Push）
2. 消息消费的模式（广播模式和集群模式）
3. 流量控制（可以结合sentinel来实现，后面单独讲）
4. 并发线程数设置
5. 消息的过滤（Tag、Key） TagA||TagB||TagC * null

### 提升消费的性能
1. 在同一个消费者组下面，提升消费者的实例，总的Consumer数量不要超过Topic下Read Queue数量，超过的Consumer实例接收 不到消息。此外，通过提高单个Consumer实例中的并行处理的线程数，可以在同一个Consumer内增加并行度来提高吞吐量（设置方法是修改consumeThreadMin和consumeThreadMax）。
2. 以批量方式进行消费，多条消息同时处理
3. 检测延时的情况，跳过非重要的消息

#### 消息存储
1. 目前业界较为常用的几款产品（RocketMQ/Kafka/RabbitMQ）均采用的是消息刷盘至所部署虚拟 机/物理机的文件系统来做持久化（刷盘一般可以分为异步刷盘和同步刷盘两种模式）。
2. 消息刷盘为消 息存储提供了一种高效率、高可靠性和高性能的数据持久化方式。
3. 除非部署MQ机器本身或是本地磁盘挂了，否则一般是不会出现无法持久化的故障问题。
4. 使用顺序写，效率是随机写的6000杯
5. RocketMQ消息的存储是由ConsumeQueue和CommitLog配合完成 的，消息真正的物理存储文件 是CommitLog，ConsumeQueue是消息的逻辑队列，类似数据库的索引文件，存储的是指向物理存储的地址。每 个Topic下的每个Message Queue都有一个对应的ConsumeQueue文件。

#### 存储文件
1.  CommitLog消息主体以及元数据的存储主体，1G，文件名长度为20位
2. ConsumeQueue消费者队列，引入的目的主要是提高消息消费的性能 RocketMQ是基于主题topic的订阅模式，消息消费是针对主题进行，如果要遍历commitlog文件根据topic检索消息是非常低效。
3. Consumer即可根据ConsumeQueue来查找待消费的消息。 其中，ConsumeQueue（逻辑消费队列）作为消费消息的索引：保存了指定Topic下的队列消息在CommitLog中的起始物理偏移量offset 2. 消息大小size消息Tag的HashCode值。
3. IndexFile：IndexFile（索引文件）提供了一种可以通过key或时间区间来查询消息的方法。底层使用hashmap实现的

#### 过滤消息
1. 是在Consumer端订阅消息时 再做消息过滤的。
2. RocketMQ这么做是在于其Producer端写入消息和Consumer端订阅消息采用分离存储的机制来实 现的，Consumer端订阅消息是需要通过ConsumeQueue这个消息消费的逻辑队列拿到一个索引，然后再从CommitLog里面读取真正的消息实体内容，所以说到底也是还绕不开其存储结构。
3. 其ConsumeQueue的存储结构如下，可以看到其中有8个字节存储的Message Tag的哈希值，基 于Tag的消息过滤正式基于这个字段值的。
4. Tag过滤方式：Consumer端在订阅消息时除了指定Topic还可以指定TAG，如果一个消息有多 个TAG，可以用||分隔
5. SQL92的过滤方式

#### 零拷贝机制

#### 内存映射文件


#### 同步复制和异步复制

#### 高可用机制

####  刷盘机制
#### 负载均衡

#### 消息重试

#### 死信队列

#### 顺序消息

#### 事务消息

#### 消息查询

#### 消息优先级
#### 底层网络通信 - Netty高性能之道
#### 限流
####
####
####
####
####
####
####
####
####
####
####
####
####
####
####



