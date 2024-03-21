
6. 玩转Elasticsearch零停机索引重建
建议的功能方案：数据库 + MQ + 应用模块 + Elasticsearch，可以在MQ控制台发送MQ消息来触发重 导数据，按批次对数据进行导入，整个过程异步化处理
详细操作步骤： 
1. 通过MQ的web控制台或cli命令行，发送指定的MQ消息
 2. MQ消息被微服务模块的消费者消费，触发ES数据重新导入功能 
3. 微服务模块从数据库里查询数据的总数及批次信息，并将每个数据批次的分页信息重新发送给MQ 消息，分页信息包含查询条件和偏移量，此MQ消息还是会被微服务的MQ消息者接收处理。
4. 微服务根据接收的查询条件和分页信息，从数据库获取到数据后，根据索引结构的定义，将数据组 装成ES支持的JSON格式，并执行bulk命令，将数据发送给Elasticsearch集群。

方案二:基于scroll+bulk+索引别名方案
假设原索引名称是book，新的索引名称为book_new，Java客户端使用别名book_alias连接 Elasticsearch，该别名指向原索引book。
1.若Java客户端没有使用别名，需要给客户端分配一个: PUT /book/_alias/book_alias 
2. 新建索引book_new，将mapping信息，settings信息等按新的要求全部定义好。 
3. 使用scroll api将数据批量查询出来 为了使用 scroll，初始搜索请求应该在查询中指定 scroll 参数，这可以告诉 Elasticsearch 需要保持搜索的上下文环境多久,1m 就是一分钟。
GET /book/_search?scroll=1m {
"query": { "match_all": {}
}, "sort": ["_doc"], "size": 2
}
4. 采用bulk api将scoll查出来的一批数据，批量写入新索引
POST /_bulk { "index": { "_index": "book_new", "_id": "对应的id值" }}
{ 查询出来的数据值 }
5. 反复执行修改后的步骤3和步骤4，查询一批导入一批，以后可以借助Java Client或其他语言的API 支持。注意做3时需要指定上一次查询的 scroll_id
6. 切换别名book_alias到新的索引book_new上面，此时Java客户端仍然使用别名访问，也不需要修 改任何代码，不需要停机。
POST /_aliases {
"actions": [ { "remove": { "index": "book", "alias": "book_alias" }}, { "add":
{ "index": "book_new", "alias": "book_alias" }} ]
}

方案三:Reindex API方案： Elasticsearch v6.3.1已经支持Reindex API，它对scroll、bulk做了一层封装，能够 对文档重建索引而不 需要任何插件或外部工具。
POST _reindex {"source": { "index": "book"}, "dest": { "index": "book_new"}}


7. 玩转Elasticsearch Suggester智能搜索建议

玩转Elasticsearch之企业级高可用分布式集群
