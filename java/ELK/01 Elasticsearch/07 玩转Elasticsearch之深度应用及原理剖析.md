## 索引文档写入和近实时搜索原理
### Segments in Lucene
1. 存储的基本单元是shard，es中的一个index分为多个shard，事实上每个shard都是一个Lucence的index，并且每个 Lucence Index 由多个 Segment 组成， 每个 Segment 事实上是一些倒排索引的集合，
2. 每次创建一个新的 Document， 都会归属于一个新的 Segment， 而不会去修改原来的 Segment 。且每次的文档删除操作，会仅仅标记 Segment 中该文档为删除状态， 而不会真正的立马物理删除， 所以说 ES 的 index 可以理解为一个抽象的概念
3. 文档被索引，首先写入到内存中，存 buffer 和 translog 文件。每个 shard 都对应一个 translog 文件

### Translog
1. 新文档被索引意味着文档会被首先写入内存 buffer 和 translog 文件。每个 shard 都对应一个 translog 文件
2. 在 Elasticsearch 中， _refresh 操作默认每秒执行一次， 意味着将内存 buffer 的数据写入到一个新 的 Segment 中，这个时候索引变成了可被检索的。写入新Segment后 会清空内存buffer。

### Flush in Elasticsearch
1. Flush 操作意味着将内存 buffer 的数据全都写入新的 Segments 中， 并将内存中所有的 Segments 全部刷盘， 并且清空 translog 日志的过程。

### 近实时搜索
1. 提交（Commiting）一个新的段到磁盘需要一个 fsync 来确保段被物理性地写入磁盘，这样在断电的时 候就不会丢失数据。
2. 但是 fsync 操作代价很大; 如果每次索引一个文档都去执行一次的话会造成很大的性能问题。

### 持久化变更
如果没有用 fsync 把数据从文件系统缓存刷（flush）到硬盘，我们不能保证数据在断电甚至是程序正 常退出之后依然存在。为了保证 Elasticsearch 的可靠性，需要确保数据变化被持久化到磁盘。
在动态更新索引时，我们说一次完整的提交会将段刷到磁盘，并写入一个包含所有段列表的提交点。 Elasticsearch 在启动或重新打开一个索引的过程中使用这个提交点来判断哪些段隶属于当前分片。
即使通过每秒刷新（refresh）实现了近实时搜索，我们仍然需要经常进行完整提交来确保能从失败中 恢复。但在两次提交之间发生变化的文档怎么办？我们也不希望丢失掉这些数据。 Elasticsearch 增加了一个 translog ，或者叫事务日志，在每一次对 Elasticsearch 进行操作时均进行了 日志记录。通过 translog ，整个流程看起来是下面这样： 1. 一个文档被索引之后，就会被添加到内存缓冲区，并且 追加到了 translog 
