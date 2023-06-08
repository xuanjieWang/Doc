## mapping映射入门
自动或者手动的为每一个索引创建数据结构和相关的配置。相当于是动态映射，确定字段和字段的值

搜索方法：get website/_maping      get website/_search     搜索索引中的所有数据
搜索一个数据： get website/_search?q=2019    查询所有数据中的2019

精准匹配和全文搜索的对比分析
精确匹配 exact value：将对应字段的值全部搜索（data时间是精确匹配）
全文检索 full text（缩写和全称，格式转化，大小写，同义词）：关键词搜索（text字段进行全文检索）

全文检索下排索引核心原理：normalization建立倒排索引的时候，拆分出各个单词相应的处理，以提升后面搜索的时候查询到的概率。

分词器:analyzer切分词语进行正规化操作
给es一段句子，将句子拆分成为一个一个的单词，同时对每个单词进行normalition(时态转化，单复数转化) recall：召回率，搜索的时候增加能够搜索到的结果的数量

analyzer：
1. 预处理，过滤html标签
2. 分词
3. 将所有的分出来的词进行处理，转换，大小写，单复数的转换
4. 标准分词器：
5. 简化分词器：
6. 空格分词器：根据空格分词
7. 特定语言分词器：按照语言大小写分词

创建映射mapping：创建索引之后应该手动创建映射。如果是自动创建会根据数据类型创建
1. text文本类型，analyzer属性指定分词器 english
2. index：属性指定是否索引   false：不进行创建索引
3. store：是否在source之外进行存储    

```
添加新字段   PUT /book/_mapping
{
  "properties": {
    "new_field":{
      "type": "text",
      "index": "false"
    }
  }
}
scalling_factor:100   如果是一个小数的话存储整数
复杂数据类型

```




