## zookeeper分布式协调工具（强一致性，只会存在一个节点）
1. 是一个分布式协调服务，将分布式一致性服务封装起来构成可靠的高效的原语集。
2. 是一个分布式数据一致性解决方案，数据订阅，发布，负载均衡，命名服务，分布式锁，分布式队列
3. leader，follower，observe，观察者不参与写操作的过半写成功机制
4. session会话一个tcp连接
5. Znode节点数据： 机器节点和数据单元，数据存储到内存中
6. 事件监听器，zookeeper允许在一些节点上面注册监听器，特定事件触发的时候zk将事件通知到客户端。机制是分布式协调机制的重要性
7. ACL： 权限控制策略：创建，获取更新，删除，设置节点acl权限

### 节点
1. 持久节点，节点创建之后一直存在服务器，直到删除主动清除
2. 持久顺序节点，有顺序的持久节点，创建节点的时候，会在节点后面添加数字后缀，表示顺序
3. 临时顺序节点，

### watcher监听机制
1. 客户端线程，客户端watcherManager，Zookeeper服务器三部分
2. 客户端在向Zookeeper服务器注册的同时，会将Watcher对象存储在客户端的 WatcherManager当中。
3. 当Zookeeper服务器触发Watcher事件后，会向客户端发送通知，客户端线程从WatcherManager中取出对应的Watcher对象来执⾏回调逻辑。

### ACL保障数据的安全
1. ACL权限控制机制保证数据的安全，权限模式，授权对象，权限
2. ip：IP模式通过ip地址粒度控制权限
3. digest： Digest加密是使用username：password的形式权限来权限配置
4. world： 开放权限模式，对所有用户权限开放
5. SUper： 超级用户可以对所有的数据节点进行操作

### 权限
1. 数据节点的创建，create    create [-s][-e] path data acl     节点更新 set path data [version]
2. 子节点的删除权限，delete  delete path [version]
3. 数据节点的更新权限，write  
4. 数据节点的管理权限，admin

### Zookeeper-开源客户端
1. zkclient
2. curator

``` java
<dependency>
  <groupId>com.101tec</groupId>
  <artifactId>zkclient</artifactId>
  <version>0.2</version>
</dependency>
<dependency>
  <groupId>org.apache.curator</groupId>
  <artifactId>curator-framework</artifactId>
  <version>2.12.0</version>
</dependency>
```
``` java

```

### zookeeper应用场景
1. ZooKeeper是⼀个典型的发布/订阅模式的分布式数据管理与协调框架，我们可以使⽤它来进⾏分布式 数据的发布与订阅。
2. 另⼀⽅⾯，通过对ZooKeeper中丰富的数据节点类型进⾏交叉使⽤，配合Watcher 事件通知机制，可以⾮常⽅便地构建⼀系列分布式应⽤中都会涉及的核⼼功能
3. 如数据发布/订阅、命名 服务、集群管理、Master选举、分布式锁和分布式队列等。

#### 数据发布订阅
数据发布/订阅（Publish/Subscribe）系统，即所谓的配置中⼼，顾名思义就是发布者将数据发布到 ZooKeeper的⼀个或⼀系列节点上，供订阅者进⾏数据订阅，进⽽达到动态获取数据的⽬的，实现配置
信息的集中式管理和数据的动态更新。
1. 发布定于的拉pull和推push
2. 推模式，服务端主动推送数据到客户端，拉模式，客户端主动发起请求来获取最新的数据，轮询拉取
3. ZooKeeper 采⽤的是推拉相结合的⽅式：客户端向服务端注册⾃⼰需要关注的节点，⼀旦该节点的数据 发⽣变更，那么服务端就会向相应的客户端发送Watcher事件通知，客户端接收到这个消息通知之后，需要主动到服务端获取最新的数据。
4. 如果将配置信息存放到ZooKeeper上进⾏集中管理，应⽤在启动的时候都会主动到 ZooKeeper服务端上进⾏⼀次配置信息的获取，同时，在指定节点上注册⼀个Watcher监听，但凡配置信息发⽣变更，服务端都会实时通知到所有订阅的客户端，从⽽达到实时获取最新配置信息的⽬的。
5. 配置变更，在系统运行的过程中，出现数据库切换的情况，这个时候可以使用配置变更，借助zookeeper。对zookeeper配置节点上面的内容进行更新，将信息发送到客户端。客户端接收到信息就可以重新获取数据

#### 命名服务
1. 在分布式系统中，被命名的实体通常可以是集群中的机器、提供的服务地址或远程对象等——这些 我们都可以统称它们为名字（Name），
2. 其中较为常⻅的就是⼀些分布式服务框架（如RPC、RMI）中 的服务地址列表，通过使⽤命名服务，客户端应⽤能够根据指定名字来获取资源的实体、服务地址和提供者的信息等。

#### 集群管理
1. 统计出生产环境中所有的机器
2. 统计出机器上下线的情况
3. 实时监听集群中每台主机的状态
4. 客户端如果对Zookeeper的数据节点注册Watcher监听，那么当该数据节点的内容或是其⼦节点 列表发⽣变更时，Zookeeper服务器就会向订阅的客户端发送变更通知。
5. Zookeeper上创建的临时节点，⼀旦客户端与服务器之间的会话失效，那么临时节点也会被⾃动删除利⽤其两⼤特性。
6. 可以在一个节点上面添加监听节点，进行创建节点就会创建一个临时节点，动态的监测机器变动的情况

### 分布式日志采集系统
1. 在不同的机器上面采集日志机器，日志源机器是不断变化的，变化的收集器机器
2. 注册收集机器，使用zookeeper进行日志系统收集器的注册，收集机器在启动的时候在收集器下面创建自己的节点
3. 任务分发，所有收集机器创建自己对应的节点之后，系统收集采集器的节点个数，将分组之后的机器列表写到创建的子节点。
4. 状态汇报： 收集器机器注册和分发之后，收集器的汇报机制，在对应的子节点上创建一个状态子节点，心跳机制
5. 动态分配： 在小范围进行数据的分，如果一个机器挂了就分配之前的任务重新分配给负载低的机器上面

### master选举

### 分布式锁排他锁和共享锁
1. 排他锁：写锁，只有一个对象可见
2. 共享锁：读锁，数据对所有事务可见
3. 一般是在顺序最小的时候才获取到锁，每一个节点只关注前面的最小节点进行注册watcher，如果他消失了，自己就是最小的节点。就获取到了锁

### 分布式队列，先进先出的队列fifo和Barrier
1. fifo队列请求操作完成之后才会操作下一个，和分布式锁一致，监听前面的一个节点，如果是最小节点就进行操作
2. Barrier队列。如果所有的节点>10才进行操作

### zab协议（原子广播协议）
主备模式的系统架构保持系统中各副本之间数据的一致性，表现形式是，使用一个单一的主进程接收客户端的所有请求，使用zab原子广播协议，将服务器状态变更以事务的形式广播到所有副本。
将事务分发给所有子节点，一般超过半数得到正确的反馈之后，leader就会向所有的follower分发commit  消息。
1. 进⼊崩溃恢复模式： 当整个服务框架启动过程中，或者是Leader服务器出现⽹络中断、崩溃退出或重启等异常情况时，ZAB 协议就会进⼊崩溃恢复模式，同时选举产⽣新的Leader服务器。
2. 当选举产⽣了新的Leader服务器，同 时集群中已经有过半的机器与该Leader服务器完成了状态同步之后，ZAB协议就会退出恢复模式，其 中，所谓的状态同步 就是指数据同步，⽤来保证集群中过半的机器能够和Leader服务器的数据状态保持⼀致
3. 进⼊消息⼴播模式： 当集群中已经有过半的Follower服务器完成了和Leader服务器的状态同步，那么整个服务框架就可以进⼊消息⼴播模式，当⼀台同样遵守ZAB协议的服务器启动后加⼊到集群中，如果此时集群中已经存在⼀ 个Leader服务器在负责进⾏消息⼴播
4. 那么加⼊的服务器就会⾃觉地进⼊数据恢复模式：找到Leader所在的服务器，并与其进⾏数据同步，然后⼀起参与到消息⼴播流程中去。Zookeeper只允许唯⼀的⼀leader进行事务的请求

### 消息广播
1. 原子广播协议，二阶段的提交过程，针对客户端的事务请求，leader生产事务没发送给集群中的所有机器，分别收集各自的选票，最后进行事务提交
2. 在ZAB的⼆阶段提交过程中，移除了中断逻辑，所有的Follower服务器要么正常反馈Leader提出的事务 Proposal，要么就抛弃Leader服务器，同时，ZAB协议将⼆阶段提交中的中断逻辑移除意味着我们可以 在过半的Follower服务器已经反馈Ack之后就开始提交事务Proposal了
3. ⽽不需要等待集群中所有的 Follower服务器都反馈响应，但是，在这种简化的⼆阶段提交模型下，⽆法处理因Leader服务器崩溃退 出⽽带来的数据不⼀致问题。
4. 因此ZAB采⽤了崩溃恢复模式来解决此问题，另外，整个消息⼴播协议是 基于具有FIFO特性的TCP协议来进⾏⽹络通信的，因此能够很容易保证消息⼴播过程中消息接受与发送的顺序性。
5. 
### 服务器角色

### 服务器启动

### leader选举

### zookeeper源码分析
