### es：索引：  一个索引是一个关系型数据库
类型: 代表的是一个表
映射：定义了每个字段的类型和信息，相当于关系型数据库中的表结构
集成ik分词器，需要和es的版本是一致 的不然会闪退，使用docker ps进行安装，在更新kinba中的配置文件配置中文 ik_max_word (常用，最细粒度拆分)	ik_smart（最粗粒度拆分）
扩展词典应用： 进入到 config/analysis-ik/(插件命令安装方式) 或 plugins/analysis-ik/config(安装包安装方式) 目录 下, 新增自定义词典 vim xxx.dic IKAnalyzer.cfg.xml中配置 <entry key="ext_dict">lagou_ext_dict.dic</entry> 扩展词典的路径
配置同义词典，
‘

## 索引操作：
1. 创建索引库，setting可以设置分片数，副本数。
2. 判断索引是否存在： HEAD /lagou-company-index
3. 查看索引： get /xxx
4. 批量查看索引： GET /索引名称1,索引名称2,索引名称3,...
5. 查看所有索引： GET _all
PUT /索引名称 {
"settings": { "属性名": "属性值"
}
}
6. 打开索引： POST /索引名称/_open
7. 关闭索引： POST /索引名称/_close
8. 删除索引： DELETE /索引名称1,索引名称2,索引名称3...			
		
## 映射操作
PUT /索引库名/_mapping {
"properties": { "字段名": { "type": "类型", "index": true， "store": true， "analyzer": "分词器"
} }
}
type： text、long、short、date、integer、object	indexl: 是否是索引，默认为true	store：是否存储，默认为false	analyzer：分词器
1. 查看索引的映射关系： GET /索引名称/_mapping
2. 查看所有索引的映射关系： GET _mapping
3. 修改索引映射关系： PUT /索引库名/_mapping {
"properties": { "字段名": { "type": "类型", "index": true， "store": true， "analyzer": "分词器"
} }
}
4. 一次性创建索引和映射： PUT /lagou-employee-index {
"settings": {}, "mappings": { "properties": { "name": { "type": "text", "analyzer": "ik_max_word"
} } }
}

## 文档操作
1. 文档增删改查和局部刷新： 文档是索引库中的数据，会根据规则创建索引，将用于搜索，可以类比成数据库中的一行数据
2. 新增文档： POST /索引名称/_doc/{id} （手动指定id）	POST /索引名称/_doc {"field":"value"} 自动生成id，id是文档的唯一标识
3. 查看单个文档： GET /索引名称/_doc/{id}
4. 查看所有文档： POST /索引名称/_search {"query":{ "match_all": { }}}
_source：定制返回结果：GET /lagou-company-index/_doc/1?_source=name,job
5. 更新文档（全部更新）： put /index/_doc/id 没有就新增，有就更新
6. 更新文档（局部更新）： 使用post   POST /索引名/_update/{id} {"doc":{ "field":"value"}}
7. 删除文档： DELETE /索引名/_doc/{id}
8. 条件删除： OST /索引库名/_delete_by_query {"query": { "match": { "字段名": "搜索关键字"} }}
9. 删除所有文档： POST 索引名/_delete_by_query {"query": { "match_all": {}}}

## 地理坐标数据类型
地理坐标点数据类型： 使用geo_point字段类型，经纬度格式可以是字符串类型，对象类型"name":"Sina", "location":{ "lat":40.722, "lon":73.989}，数组类型："name":"Baidu", "location":[73.983,40.719]
1. 地理坐标过滤：找出落在指定举行中的点geo_bounding_box，geo_distance
2. 找出与指定位置在给定距离内的点，geo_distance_range
3. 找出与指定点距离在给定最小距离和最大距离之间的点，
4. geo_polygon 找出落在多边形中的点。 这个过滤器使用代价很大 。当你觉得自己需要使用它，最好先看看 geo-shapes 。

## 动态映射
动态映射：mapping的dynamic有如下设置： true：遇到陌生字段就执行dynamic mapping处理机制 false：遇到陌生字段就忽略。 索引层的"dynamic": "strict",开启严格模式，遇到陌生的字段报错
1. 自定义动态映射： 运行时增加新的字段，启动动态映射，然而有时候我们可以自定义规则，适用数据
2. 日期检测： 日期检测可以通过在根对象上设置 date_detection 为 false 来关闭，使用这个映射，字符串将始终作为 string 类型。如果需要一个 date 字段，必须手动添加。 Elasticsearch 判断字符串为日期的规则可以通过 dynamic_date_formats setting 来设置。
3. dynamic_templates： 可以控制新生成的字段映射，通过正则表达式指定字段使用哪个分词器

## 符合查询
Query DSL： 基于JSOn的完整查询sql，定义查询，叶子查询，复合查询，
OST /索引库名/_search {
"query":{ "查询类型":{ "查询条件":"查询条件值"
} }
}		
1. 查询类型： match_all（查询所有）， match（通过分词器查询（再创建索引的时候指定字段的分词器），term（词条搜索）， range（范围查询）
2. 全文搜索(full-text query)： 全文搜索可以搜索出分析的文本字段，电子邮件正文，商品描述，使用索引应应用于字段统一分析器处理查询字符串。
3. 匹配搜索(match query): 全文查询的标准查询，可以对一个字段进行模糊，短语查询，match queries接收。对他们进行分词分析，再组织成一个booelan查询，可以通过operator指定bool组合
4. 精确查找：对于使用了分词器，使用精确查找POST /lagou-property/_search {"query": {"match": { "title": {"query": "小米电视4A","operator": "and"}}} 使用and属性
5. 短语搜索(match phrase query)： GET /lagou-property/_search {"query": {"match_phrase": {"title": "小米电视"}}} 指定一个字段进行短语查询
6. query_string查询：无需指定某字段而对文档全文进行匹配搜索的查询"query_string" : { "query" : "2699","default_field" : "title"}
7. 逻辑查询： "query_string" : { "query" : "手机 OR 小米","default_field" : "title"}
8. 模糊查询： "query": { "query_string" : { "query" : "大米~1","default_field" : "title"}
9. 多字段支持： "multi_match" : { "query":"2699","fields": [ "title","price"]}
    
## 词条搜索
term 查询用于查询指定字段包含某个词项的文档（词条搜索）
1. 词条级搜索(term-level queries)： POST /book/_search {"query": { "term" : { "name" : "solr" }}}
2. 词条集合搜索： GET /book/_search {"query": { "terms" : { "name" : ["solr", "elasticsearch"]}}}
3. 范围搜索： gte：大于等于 gt：大于 lte：小于等于 lt：小于boost：查询权重	GET /book/_search {"query": { "range" : { "price" : { "gte" : 10, "lte" : 200, "boost" : 2.0} } }}
4. 不为空搜索： GET /book/_search {"query": { "exists" : { "field" : "price" }}}
5. 词项前缀搜索(prefix query)： GET /book/_search { "query": { "prefix" : { "name" : "so" }}}
6. 通配符搜索(wildcard query): GET /book/_search {"query": { "wildcard" : { "name" : "so*r" }}}
7. 正则搜索(regexp query): 最好匹配前缀，{ "query": { "regexp":{ "name": "s.*"} }}
8. 模糊搜索(fuzzy query)： GET /book/_search {"query": { "fuzzy" : { "name" : "so" }}}
9. ids搜索(id集合查询)： GET /book/_search {"query": { "ids" : { "type" : "_doc", "values" : ["1", "3"]} }}

## 复合搜索(compound query)： 
1. 布尔搜索： must：必须满足 filter：必须满足，但执行的是filter上下文，不参与、不影响评分 should：或must_not：必须不满足，在filter上下文中执行，不参与、不影响评分
2. minimum_should_match代表了最小匹配精度，如果设置minimum_should_match=1，那么should 语句中至少需要有一个条件满足。
3. 排序： 相关性评分排序，默认情况下最相关的是再最前面的，POST /book/_search {"query": { "match": {"description":"solr"}}, "sort": [{"_score": {"order": "asc"}} ]}
4. 多级排序（多个字段排序）： POST /book/_search {"query":{ "match_all":{}}, "sort": [{ "price": { "order": "desc" }}, { "timestamp": { "order": "desc" }}]}
5. 分页： POST /book/_search {"query": { "match_all": {}}, "size": 2, "from": 0} size: 条数，from：当前页的起始索引
6. 高亮： POST /book/_search {"query": { "match": { "name": "elasticsearch"}}, "highlight": { "pre_tags": "<font color='pink'>","post_tags": "</font>","fields": [{"name":{}}] }}

## 文档批量操作（bulk（批量查询） 和 mget（批量增删改））： 只需要一次请求
1. 同一索引下面的批量查询：POST /book/_search {"query": { "ids" : { "values" : ["1", "4"]} }}
2. 批量增删该（）bulk： 删除1，新增5，修改2： 
POST /_bulk  
{ "delete": { "_index": "book", "_id": "1" }} 
{ "create": { "_index": "book", "_id": "5" }} { "name": "test14","price":100.99 } 
{ "update": { "_index": "book", "_id": "2"} }{ "doc" : {"name" : "test"} }
3. Filter DSL，所有的查询都会触发相关度得分的计算，以过滤器的形式提供了另一种功能的查询。过滤器不会计算的得分，效率高，被缓存到内存中


## 聚合分析：
找出某字段 （或计算表达式的结果）的最大值、最小值，计算和、平均值等。Elasticsearch作为搜索引擎兼数据库，同样提供了强大的聚合分析能力  "aggregations"： aggs表聚合操作
1. 聚合指标max min sum avg，文档计数count，value_count 统计某字段有值的文档数，cardinality值去重计数 基数，stats 统计 count max min avg sum 5个值，
2. 找出文档中的max_price max 最大的树： POST /book/_search {"size": 0, "aggs": { "max_price": { "max": { "field": "price"} } }}
3. Extended stats： 高级统计，比stats多4个统计结果： 平方和、方差、标准差、平均值加/减两个标准差的区间
4. Percentiles 占比百分位对应的值统计
5. 指定分位值： POST /book/_search?size=0 {"aggs": { "price_percents": { "percentiles": { "field": "price", "percents" : [75, 99, 99.9]} } }}  price_percents： 字段	percentiles：统计百分比
6. Percentiles rank 统计值小于等于指定值的文档占比： POST /book/_search?size=0 {"aggs": { "gge_perc_rank": { "percentile_ranks": { "field": "price", "values": [ 100,200] } } }}
7. 桶聚合： 它执行的是对文档分组的操作（与sql中的group by类似），把满足相关特性的文档分到一个桶里，即 桶分，输出结果往往是一个个包含多个文档的桶（一个桶就是一个group）
8. bucket：一个数据分组 metric：对一个数据分组执行的统计
9. 实现having 效果： 

