1. 第一部分：MongoDB系统结构 NoSQL和MongoDB MongoDB体系结构 和RDBMS的对比 BSON MongoDB中对BSON支持 的类型 Linux下安装 启动参数说明 mongo shell GUI工具(Compass NoSqlBooster)
2. 第二部分：MongoDB命令 MongoDB的基础操作 集合数据的CURD 聚合操作(聚合管道和MapReduce)
3. 第三部分：MongoDB索引Index 索引类型 索引和explain分析 慢查询分析 MongoDB索引的底层实现原理
4. 第四部分：MongoDB应用实战 MongoDB的适用场景 和 场景具体应用 如何抉择是否使用MongoDB 程序访问MongoDB (java Spring SpringBoot)
5. 第五部分：MongoDB架构 MongoDB逻辑结构 数据模型 存储引擎(WiredTiger) 存储引擎实现原理
6. 第六部分：MongoDB集群高可用 主从架构的缺陷 复制集 replica set（什么是复制集 为什么用 复制集原理 如何搭建 ） 分片 shard （什么是分片 为什么用 分片原理(片键 区块 分片策略) 分片集群搭建）
7. 第七部分：MongoDB安全认证为什么要安全认证 用户相关操作 角色 单机安全认证 分片集群的安全认证

#### 第一部分 MongoDB体系结构
1. NoSQL数据库四大家族 列存储 Hbase,
2. 键值(Key-Value)存储 Redis,
3. 图像存储 Neo4j,
4. 文档存储 MongoDB
5. MongoDB 是一个基于分布式文件存储的数据库，由 C++ 编写，可以为 WEB 应用提供可扩展、 高性能、易部署的数据存储解决方案。MongoDB 是一个介于关系数据库和非关系数据库之间的产品，是非关系数据库中功能最丰富、最像关系数据库的。在高负载的情况下，通过添加更多的节点，可以保证服务器性能。

### 什么是BSON
1. BSON是一种类json的一种二进制形式的存储格式，简称Binary JSON，它和JSON一样，支持内嵌的文 档对象和数组对象，但是BSON有JSON没有的一些数据类型，如Date和Binary Data类型。
2. BSON可以 做为网络数据交换的一种存储形式,是一种schema-less的存储形式，它的优点是灵活性高，但它的缺点 是空间利用率不是很理想。
3. {key:value,key2:value2} 这是一个BSON的例子，其中key是字符串类型,后面的value值，它的类型一般是字符串,double,Array,ISODate等类型。

### MongoDB
1. MongoDB的基本操作 查看数据库 show dbs;
2. 切换数据库 如果没有对应的数据库则创建 use 数据库名;
3. 创建集合 db.createCollection("集合名")
4. 查看集合 show tables; show collections;
5. 删除集合 db.集合名.drop();
6. 删除当前数据库db.dropDatabase();

### 聚合操作分类
1. MongoDB 聚合操作分类 单目的聚合操作(Single Purpose Aggregation Operation)
2. 聚合管道(Aggregation Pipeline)
3. MapReduce 编程模型

### 索引：相当于是目录，可以快速的查询出所需要的内容所在的位置
1. 索引是一种单独的、物理的对数据库表中一列或多列的值进行排序的一种存储结构，它是某个表 中一列或若干列值的集合和相应的指向表中物理标识这些值的数据页的逻辑指针清单。
2. 索引的作 用相当于图书的目录，可以根据目录中的页码快速找到所需的内容。索引目标是提高数据库的查 询效率，没有索引的话，查询会进行全表扫描（scan every document in a collection）
3. 数据量 大时严重降低了查询效率。默认情况下Mongo在一个集合（collection）创建时，自动地对集合的_id创建了唯一索引。

### 索引类型 3.2.1 单键索引 (Single Field) 
1. MongoDB支持所有数据类型中的单个字段索引，并且可以在文档的任何字段上定义。 对于单个字段索引，索引键的排序顺序无关紧要，因为MongoDB可以在任一方向读取索引。
   - 单个例上创建索引： db.集合名.createIndex({"字段名":排序方式}) 特殊的单键索引
   - 过期索引 TTL （ Time To Live）TTL索引是MongoDB中一种特殊的索引， 可以支持文档在一定时间之后自动过期删除，目前TTL索引 只能在单字段上建立，并且字段类型必须是日期类型。db.集合名.createIndex({"日期字段":排序方式}, {expireAfterSeconds: 秒数})
   - 复合索引(Compound Index）
     通常我们需要在多个字段的基础上搜索表/集合，这是非常频繁的。 如果是这种情况，我们可能会考虑 在MongoDB中制作复合索引。 复合索引支持基于多个字段的索引，这扩展了索引的概念并将它们扩展 到索引中的更大域。
制作复合索引时要注意的重要事项包括：字段顺序与索引方向。
db.集合名.createIndex( { "字段名1" : 排序方式, "字段名2" : 排序方式 } )
   - 多键索引（Multikey indexes） 针对属性包含数组数据的情况，MongoDB支持针对数组中每一个element创建索引，Multikeyindexes支持strings，numbers和nested documents
   - 地理空间索引（Geospatial Index） 针对地理空间坐标数据创建索引。 2dsphere索引，用于存储和查找球面上的点2d索引，用于存储和查找平面上的点
   - 全文索引 MongoDB提供了针对string内容的文本查询，Text Index支持任意属性值为string或string数组元素的索引查询。注意：一个集合仅支持最多一个Text Index，中文分词不理想 推荐ES。
   - 哈希索引 Hashed Index 针对属性的哈希值进行索引查询，当要使用Hashed index时，MongoDB能够自动的计算hash值，无需程序计算hash值。注：hash index仅支持等于查询，不支持范围查询。
     
### MongoDB 索引底层实现原理分析
1. B-树的特点: (1) 多路 非二叉树 (2) 每个节点 既保存数据 又保存索引 (3) 搜索时 相当于二分查找
2. B+树是B-树的变种B+ 树的特点: （1） 多路非二叉 （2） 只有叶子节点保存数据 （3） 搜索时 也相当于二分查找（4） 增加了 相邻节点指针
3. 一个是数据的保存位置，一个是相邻节点的指向。就是这 俩造成了MongoDB和MySql的差别
4. B+树相邻接点的指针可以大大增加区间访问性，可使用在范围查询等，而B-树每个节点 key 和 data 在一起 适合随机读写 ，而区间查找效率很差。
5. B+树更适合外部存储，也就是磁盘存储，使用B-结构的话，每次磁盘预读中的很多数据是用不上 的数据。因此，它没能利用好磁盘预读的提供的数据。由于节点内无 data 域，每个节点能索引的范围更大更精确。
6. 注意这个区别相当重要，是基于（1）（2）的，B-树每个节点即保存数据又保存索引 树的深度 小，所以磁盘IO的次数很少，B+树只有叶子节点保存，较B树而言深度大磁盘IO多，但是区间访问比较 好。第四部分 MongoDB 应用实战

### MongoDB的行业具体应用场景
1. 游戏场景，使用 MongoDB 存储游戏用户信息，用户的装备、积分等直接以内嵌文档的形式存 储，方便查询、更新。
2. 物流场景，使用 MongoDB 存储订单信息，订单状态在运送过程中会不断更新，以 MongoDB 内 嵌数组的形式来存储，一次查询就能将订单所有的变更读取出来。
3. 社交场景，使用 MongoDB 存储存储用户信息，以及用户发表的朋友圈信息，通过地理位置索引 实现附近的人、地点等功能。
4. 物联网场景，使用 MongoDB 存储所有接入的智能设备信息，以及设备汇报的日志信息，并对这 些信息进行多维度的分析。
5. 直播，使用 MongoDB 存储用户信息、礼物信息等。
