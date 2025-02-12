## docker启动es必须是单机而且关闭安全模式  后面使用挂载的形式挂载ik分词器
参数
``` yml
      discovery.type=single-node
      xpack.security.enabled=false
```

"PATH=/usr/share/elasticsearch/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin",


### 全文数据，全文数据的查询方法，全文检索和倒排索引

第一部分：Lucene基础回顾 传统搜索的问题 改进之后的搜索 全文数据 全文数据的查询方法 全文检索和倒排索引 Lucene 相关产品 特性 逻辑模块 第二部分：Lucene 应用实战
索引创建和搜索的流程 索引创建和搜索的代码实现 luke工具 Field域的使用 索引的维护 分 词器
Query子类查询 TermQuery BooleanQuery 范围查询 短语查询 跨度查询 模糊查询 数值查询 QueryParser MultiFieldQueryParser StandardQueryParser 第三部分：Lucene 底层高级
底层的存储结构 相关度排序 相关度排序公式 VSM BM25 Lucene优化 使用的注意事项

## 顺序扫描。
1. 全文检索，每个词建立索引，使用倒排索引实现
2. 正排索引：将数据存储到字段中，字段比对进行搜索
3. 倒排索引：单词在文档中的位置的映射，出现次数，出现位置（单词词典和倒排文件组成）

查询->系列词->查询树->将索引读取到内存->查询树搜索索引，每个词的文档列表，得到结果文档相关性进行排序
1.Field属性 是否分词，是否索引，是否存储，字符串类型，文本类型，数字类型，多种类型

每一个文档创建四种属性的域的document，每一条内容被包含文档（对文档中的属性内容都对应成这四种属性）
创建分词器，标准过滤，大小写过滤，停用词过滤

查询：
Query子类查询： 短词查询（关键词精准匹配），布尔查询（组合查询，多条记录一起查询（交集，并集）），短语查询，多重短语查询，临近查询，词项范围查询，模糊查询，数值查询
解析查询表达式： QueryParser和MultiFieldQueryParser，StandardQueryParser

QueryParser搜索： 基础查询，范围查询，组合条件查询（多条件 and or not）



索引index：一个目录一个索引，
段segment：一个索引多个段，独立，每添加10个文档就生成一个段，每10个段合成10个段，
文档document：对象
field属性：对象的属性
term词：语法分析和语言处理的字符串

数据结构： 数组，跳表，trie，二叉树，三叉树，

### 优化： 过滤请求，使用布隆过滤器，通过设置IndexWriter的参数优化，分词器，屏蔽打分排序机制
1. 关键词区分大小写：
2. 同一时刻只能有一个对索引的写操作，在写的同时可以进行搜索
3. 索引的过程中强行退出将在tmp目录留下一个lock文件，使以后的写操作无法进行，可以将 其手工删除
4. ucene只支持一种时间格式yyMMddHHmmss，所以你传一个yy-MM-dd HH:mm:ss的时间给 lucene它 是不会当作时间来处理的。
设置权重： Query termQuery = new TermQuery(new Term("name","lucene")); BoostQuery query =new BoostQuery(termQuery, 3.5f); 
