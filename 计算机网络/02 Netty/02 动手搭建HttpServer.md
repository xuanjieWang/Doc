## 搭建HttpServr
三大优点：并发高，传输快，封装好。在这一章我们来用Netty搭建一个HttpServer，从实际开发中了解netty框架的一些特性和概念。

Http请求: HttpRequest头部信息 => HttpContent包含数据西悉尼  =>  LastHttpContent: 包含结束,同时包含头部的尾部信息
http相应: HttpResponse包含头信息 => HttpContent 里面包含的是数据，可以后续有多个 HttpContent 部分

1. HttpRequestDecoder，用于解码request
2. HttpResponseEncoder，用于编码response
3. aggregator，消息聚合器（重要）。为什么能有FullHttpRequest这个东西，就是因为有他，HttpObjectAggregator，如果没有他，就不会有那个消息是FullHttpRequest的那段Channel，同样也不会有FullHttpResponse。如果我们将z'h
4. HttpObjectAggregator(512 * 1024)的参数含义是消息合并的数据大小，如此代表聚合的消息内容长度不超过512kb。
5. 添加对接口的处理

### 什么是Decoder和Encoder: 
1. Channel,一个客户端与服务端通信的通道
2. ChannelHandler,业务逻辑处理器,分别是ChannelinBoundHandler和ChannelOutboundHandler
   ChannelInboundHandler，输入数据处理器
   ChannelOutboundHandler，输出业务处理器
3. ChannelPipeline，用于存放ChannelHandler的容器
4. ChannelContext，通信管道的上下文

事件传递给 ChannelPipeline 的第一个 ChannelHandler
ChannelHandler 通过关联的 ChannelHandlerContext 传递事件给 ChannelPipeline 中的 下一个
ChannelHandler 通过关联的 ChannelHandlerContext 传递事件给 ChannelPipeline 中的 下一个

#### Encoder: ​ Encoder最重要的实现类是MessageToByteEncoder<T>，这个类的作用就是将消息实体T从对象转换成byte，写入到ByteBuf，然后再丢给剩下的ChannelOutboundHandler传给客户端
将消息抓换为字节的操作

#### Decoder,在服务端接收到数据的时候,将字节流转换为实体对象Message,
decode 和 decodeLast: 调用时机不同,decodeLast只有实在Channel的生命周期结束的时候才会带哦用,默认是调用decode方法
因为不知道这次请求发送多少次数据,每次判断数据够不够4,介绍一个常用的解码器ReplayingDecoder

ReplayingDecoder:
ReplayingDecoder 是 byte-to-message 解码的一种特殊的抽象基类，读取缓冲区的数据之前需要检查缓冲区是否有足够的字节，使用ReplayingDecoder就无需自己检查；若ByteBuf中有足够的字节，则会正常读取；若没有足够的字节则会停止解码。

​ RelayingDecoder在使用的时候需要搞清楚的两个方法是checkpoint(S s)和state()，其中checkpoint的参数S，代表的是ReplayingDecoder所处的状态，一般是枚举类型。RelayingDecoder是一个有状态的Handler，状态表示的是它目前读取到了哪一步，checkpoint(S s)是设置当前的状态，state()是获取当前的状态。

​ 在这里我们模拟一个简单的Decoder，假设每个包包含length:int和content:String两个数据，其中length可以为0，代表一个空包，大于0的时候代表content的长度。代码如下：
