### redis原理
1. 系统在某个时刻访问量剧增（热点新闻），造成数据库压力剧增甚至崩溃，怎么办？
2. 什么是缓存雪崩、缓存穿透和缓存击穿，会造成什么问题，如何解决？
3. 什么是大Key和热Key，会造成什么问题，如何解决？
4. 如何保证 Redis 中的数据都是热点数据？
5. 缓存和数据库数据是不一致时，会造成什么问题，如何解决？
6. 什么是数据并发竞争，会造成什么问题，如何解决？
7. 单线程的Redis为什么这么快？ Redis哨兵和集群的原理及选择？
8. 在多机Redis使用时，如何保证主从服务器的数据一致性？

### redis 的用途
1. 分布式session
2. 做分布式锁，乐观锁，
3. http缓存使用Cache-control 在headler中添加缓存可以配置最大容量和超时时间

### 高并发中的缓存脏读
1. 先更新数据库后更新缓存    x
2. 先删除缓存再更新数据库    x
3. 先更新数据库，再删除缓存  对
4. 负载均衡  nginx缓存  本地缓存    redis缓存  数据库集群

### 分布式缓存
1. 简单数据类型 Value是字符串或整数 Value的值比较大（大于100K） 只进行setter和getter 可采用Memcached Memcached纯内存缓存多线程
2. 复杂数据类型 Value是hash、set、list、zset 需要存储关系，聚合，计算可采用Redis
3. 要做集群 分布式缓存集群方案（Redis） 哨兵+主从 ，RedisCluster
4. 数据库表和缓存是一一对应的 缓存的字段会比数据库表少一些，缓存的数据是经常访问的

### redis数据类型和应用场景
1. Redis是一个Key-Value的存储系统，使用ANSI C语言编写。
2. key的类型是字符串。 value的数据类型有：常用的：string字符串类型、list列表类型、set集合类型、sortedset（zset）有序集合类型、hash类型。
3. 不常见的：bitmap位图类型、geo地理位置类型。
4. string字符串类型： Redis的String能表达3种值的类型：字符串、整数、浮点数 100.01 是个六位的串 incr：递增数字，可用于实现乐观锁 watch(事务)，setnx用于分布式锁
5. list列表类型可以存储有序、可重复的元素，作为栈或队列使用，可用于各种列表，比如用户列表、商品列表、评论列表等。
6. set集合类型，无序、唯一元素： 适用于不能重复的且不需要顺序的数据结构，，关注的用户，还可以通过spop进行随机抽奖
7. sortedset有序集合类型：排行榜实现，zadd hit:1 100 item1 20 item2 45 item3
8. Hash类型（散列表）对象的存储 ，表数据的映射
9. bitmap位图类型： 用户每月签到，用户id为key ， 日期作为偏移量 1表示签到，统计活跃用户, 日期为key，用户id为偏移量 1表示活跃，查询用户在线状态， 日期为key，用户id为偏移量 1表示在线
10. geo地图
11. stream数据流类型： 消息ID的序列化生成 消息遍历，消息的阻塞和非阻塞读取 消息的分组消费 未完成消息的处理 消息队列监控，每个Stream都有唯一的名称，它就是Redis的key，首次使用xadd指令追加消息时自动创建。
