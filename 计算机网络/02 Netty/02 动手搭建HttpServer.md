## 搭建HttpServr
三大优点：并发高，传输快，封装好。在这一章我们来用Netty搭建一个HttpServer，从实际开发中了解netty框架的一些特性和概念。

Http请求: HttpRequest头部信息 => HttpContent包含数据西悉尼  =>  LastHttpContent: 包含结束,同时包含头部的尾部信息
http相应: HttpResponse包含头信息 => HttpContent 里面包含的是数据，可以后续有多个 HttpContent 部分

1. HttpRequestDecoder，用于解码request
2. HttpResponseEncoder，用于编码response
3. aggregator，消息聚合器（重要）。为什么能有FullHttpRequest这个东西，就是因为有他，HttpObjectAggregator，如果没有他，就不会有那个消息是FullHttpRequest的那段Channel，同样也不会有FullHttpResponse。如果我们将z'h
4. HttpObjectAggregator(512 * 1024)的参数含义是消息合并的数据大小，如此代表聚合的消息内容长度不超过512kb。
5. 添加对接口的处理

### processSelectedKeys处理连接事件,生成NioSocketChannel,注册到WorkNioEventLoop中,会使用到pipeline,获取到对应的channel
1. pipeline,中维护了很多的处理器(拦截处理器,过滤处理器,自定义处理器)

### ChannelHandler
1. 定义了很多处理事件的方法,可以在事件就绪,事件发送, 事件出现异常的时候进行调用
2. 客户端给服务端发送消息的时候,读消息出栈
3. 服务端消息接受,消息入栈的处理器,查找CHannelpipeLine中的handle,出栈
4. ChannelHandlerContext: 处理器上下文,关闭,刷新,开始处理出栈,,将数据数据写道ChannelPipe中的下一个ChanelHandler

### ChannelOption: 设置Channel的参数Socket的标准参数
1. ChannelOption.SO_BACKLOG: 设置初始化可连接的队列大小
2. ChannelOption.SO_KEEPALIVE: 设置活动探测报文

### ChannelFuure: 异步返回结果,所有的IO都是异步的
1. channel channel()获取当前正在进行IO操作的通道
2. ChannelFuture sync() 等待异步操作完成改为同步

### EventLoopGropu和NioEventLoop
1. 相当于是线程池和线程的作用
2. NioEventLoop是EventLoop的实现类

### ServerBootstrap和BootStrap
1. ServerBootStrap是服务端启动助手(两个线程组),BootStrap是客户端(一个线程组)
2. channel
3. option   BoosEventLoopGroup
4. childOption workerEventLoopGroup
5. childHandler   工作线程池出来的逻辑

### Unpooled类
1. 操作缓冲区的类

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
