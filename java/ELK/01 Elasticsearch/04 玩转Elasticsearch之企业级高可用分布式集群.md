## 概念
### 集群（Cluster） 一个Elasticsearch集群由多个节点（Node）组成，每个集群都有一个共同的集群名称作为标识
### 节点（Node）
1. 一个Elasticsearch实例即一个Node，一台机器可以有多个实例，正常使用下每个实例都应该 会部署在不同的机器上。
2. Elasticsearch的配置文件中可以通过node.master、node.data来设 置节点类型。
3. node.master：表示节点是否具有成为主节点的资格 true代表的是有资格竞选主节点 false代表的是没有资格竞选主节点
4. node.data：表示节点是否存储数据
5. 
### Node节点组合 主节点+数据节点（master+data） 默认 节点既有成为主节点的资格，又存储数据 master节点参加选举和存储数据
node.master: true node.data: true

### 数据节点（data） 节点没有成为主节点的资格，不参与选举，只会存储数据
node.master: false node.data: true

### 客户端节点（client） 不会成为主节点，也不会存储数据，主要是针对海量请求的时候可以进行负载均衡
node.master: false node.data: false

### 分片 
1. 每个索引有1个或多个分片，每个分片存储不同的数据。分片可分为主分片（primary shard）和复制分片（replica shard）
2. 复制分片是主分片的拷贝。默认每个主分片有一个复 制分片，每个索引的复制分片的数量可以动态地调整，
3. 复制分片从不与它的主分片在同一个节点上。复制分片和主分片在同一个节点上面

### 副本 这里指主分片的副本分片（主分片的拷贝）

## Elasticseasrch的架构遵循其基本概念：一个采用Restful API标准的高扩展性和高可用性的实时数据分 析的全文搜索引擎。
1. 高扩展性：体现在Elasticsearch添加节点非常简单，新节点无需做复杂的配置，只要配置好集群信 息将会被集群自动发现。
2. 高可用性：因为Elasticsearch是分布式的，每个节点都会有备份，所以宕机一两个节点也不会出现 问题，集群会通过备份进行自动复盘。
3. 实时性：使用倒排索引来建立存储结构，搜索时常在百毫秒内就可完成。

Elasticsearch支持的索引快照的存储格式，es默认是先把索引存放到内存中，当内存满了之后再持久 化到本地磁盘。
gateway对索引快照进行存储，当Elasticsearch关闭再启动的时候，它就会从这个 gateway里面读取索引数据；支持的格式有：本地的Local FileSystem、分布式的Shared FileSystem、 Hadoop的文件系统HDFS、Amazon（亚马逊）的S3。 
第二层 —— Lucene框架： Elasticsearch基于Lucene（基于Java开发）框架。
第三层 —— Elasticsearch数据的加工处理方式： Index Module（创建Index模块）、Search Module（搜索模块）、Mapping（映射）、River 代表
es的一个数据源（运行在Elasticsearch集群内部的一个插件，主要用来从外部获取获取异构数据，然后 在Elasticsearch里创建索引；
常见的插件有RabbitMQ River、Twitter River）。
第四层 —— Elasticsearch发现机制、脚本：
Discovery 是Elasticsearch自动发现节点的机制的模块，Zen Discovery和 EC2 discovery。EC2：亚 马逊弹性计算云 EC2 discovery主要在亚马云平台中使用。Zen Discovery作用就相当于solrcloud中的 zookeeper。zen Discovery 从功能上可以分为两部分，第一部分是集群刚启动时的选主，或者是新加 入集群的节点发现当前集群的Master。第二部分是选主完成后，Master 和 Folower 的相互探活。
Scripting 是脚本执行功能，有这个功能能很方便对查询出来的数据进行加工处理。
3rd Plugins 表示Elasticsearch支持安装很多第三方的插件，例如elasticsearch-ik分词插件、 elasticsearch-sql sql插件。 
第五层 —— Elasticsearch的交互方式： 有Thrift、Memcached、Http三种协议，默认的是用Http协议传输
第六层 —— Elasticsearch的API支持模式：

``` yml
cluster.name: my-es
#集群名称 ---
node.name: node-1
# 节点名称
node.master: true
#当前节点是否可以被选举为master节点，是：true、否：false
--network.host: 0.0.0.0
http.port: 9200
transport.port: 9300
# ---
#初始化一个新的集群时需要此配置来选举master cluster.initial_master_nodes: ["node-1","node-2","node-3"] #写入候选主节点的设备地址 --discovery.seed_hosts: ["127.0.0.1:9300", "127.0.0.1:9301","127.0.0.1:9302"] http.cors.enabled: true
http.cors.allow-origin: "*"
```
### 配置多台的机器，只需要修改node.name: node-3     http.port: 9202    transport.port: 9302

## 分布式集群调优策略
###  Index(写)调优
1. 首次灌入数据，可以将副本数写成0 ，节省了索引创建的时间
2. 使用uuid，使用自定义的id创建的时候需要判断有没有，使用生成的就不用考虑
3. 合理设置mapping，不需要建立索引的字段index属性设置为不分词，减少字段内容长度，使用不同的分析器
4. 调整_source字段，可以不存储数据
5. 对分词的字段关闭评分，对analyzed的字段禁用norms
6. 调整索引的刷新间隔：
7. 批处理：将多个请求和成一次请求
8. Document的路由处理，线程降低了但是单批的处理耗时了增加了
9. Search(读)调优  es存储日志，日志的索引基于天的，使用小范围查询
10. 使用Filter代替Query。不需熬打分机制，查询多个字段，其中的一个字段进行打分，其他字段不进行打分
11. ID字段定义为keyword，keyword会被优化不会被range范围查询，性能提升30%
12. 别让用户的无约束的输入拖累了ES集群的性能，如果是*

### ES数据建模Mapping设置
mapping属性：
1. enable，仅仅存储不做搜索和聚合分析  !!
2. index：是否构建倒排索引  !!
3. index_option：存储倒排索引的哪些信息
4. norms：存储相关参数
5. doc——value用户聚合和排序分析  !!
6. fileddata：是否为text类型启动fieldddata实现排序和聚合分析
7. store：是否存储该字段的数据  ！!
8. coerce：字段数据类型转换
9. dynamic：自动更新

### ES关联关系处理
1. Application-side joins
2. Data denormalization（数据的非规范化）
3. Nested objects（嵌套文档)
4. Parent/child relationships（父子文档）
