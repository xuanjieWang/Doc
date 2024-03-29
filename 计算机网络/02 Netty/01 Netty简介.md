### Netty 是 一个异步事件驱动的网络应用程序框架，用于快速开发可维护的高性能协议服务器和客户端。
1. BIO，同步阻塞IO，阻塞整个步骤，如果连接少，他的延迟是最低的，因为一个线程只处理一个连接，适用于少连接且延迟低的场景，比如说数据库连接。
2. NIO，同步非阻塞IO，阻塞业务处理但不阻塞数据接收，适用于高并发且处理简单的场景，比如聊天软件。
3. 多路复用IO，他的两个步骤处理是分开的，也就是说，一个连接可能他的数据接收是线程a完成的，数据处理是线程b完成的，他比BIO能处理更多请求。
4. 信号驱动IO，这种IO模型主要用在嵌入式开发，不参与讨论。
5. 异步IO，他的数据请求和数据处理都是异步的，数据请求一次返回一次，适用于长连接的业务场景。

### IO线程模型
1. react线程模型
   和之前的nio是类似的,用一个单线程去轮询监听连接事件,当有读写的请求过来的时候,会调用子线程去处理读写请求
   问题: 子线程可能因为任务数量太多,导致并发不够,使用一个主线程去监听连接时间会有瓶颈
2. react多线程模型
   使用一个主线程去监听连接事件,当时读写请求的时候,使用handle去处理时间,并调用底层的work线程池去处理读写请求
   问题: 使用一个主线程去监听连接事件会有连接上面的瓶颈问题
3. 主从react线程模型
   主React负责去监听连接事件,当建立建立之后将请求转交给从节点的React线程, 并分配到handle去处理相对应的请求.每一个从react都有自己的work线程池.

### netyy使用的线程模型
client -> ServerSocketChannel -> accept -> socketChannel -> nioSocketChannel -> selector(WorkerGroup)
1. BoosGroup:使用线程池轮询客户端建立连接 select轮询io连接事件,处理accept事件与客户端建立简介,生成NIOSocketChannel注册到WorkerGroup上面的线程上的Selector
2. WorkerGroup:使用线程池轮询和处理IO事件,select轮询注册到nioSocketChannel上面的read/write事件,在对应的NIoSocketChannel处理read/write事件

### 详细版本的netty模型
1. BoosGroup, nioeventLoop(selecto(轮询)r+TaskQueue(任务线程))(单个线程),只关注连接事件,去除事件的key,处理key ProcessSelectKeys可以得到通道,将通道注册到Work Group上面.
2. WorkerGroup, nioeventLoop(selector(轮询)+TaskQueue(任务线程))(单个线程),监听事件处理key,添加到runAllTasks去执行事件线程
3. BoosNioEventLoopGroup: 事件循环组包含NioEventLoop很多的事件循环线程池中的一个线程

### netty是一Nio的封装,可以实现http,websocket协议

常见的编解码协议: 内置的协议
1. 行编码解码器,一行一个请求
2. netty提供了内置的常用解码器,包括行编码器一行一个请求
3. 前缀长度编码器,前N个字节定请求的字节长度
4. 可重放解码器,记录半包消息的状态
5. HTTp编解码器,
6. wenbsocket消息编解码器

### netty提供了生命周期回调接口
当完整的一个

### netty为什么快
1. netty使用了零拷贝
2. 一般我们的数据如果需要从IO读取到堆内存，中间需要经过Socket缓冲区，也就是说一个数据会被拷贝两次才能到达他的的终点，如果数据量大，就会造成不必要的资源浪费。
3. Netty针对这种情况，使用了NIO中的另一大特性——零拷贝，当他需要接收数据的时候，他会在堆内存之外开辟一块内存，数据就直接从IO读到了那块内存中去，在netty里面通过ByteBuf可以直接对这些数据进行直接操作，从而加快了传输速度。
原本使用: data => 页缓存 => 应用程序缓冲区  => socket缓存  => DMA传输到网卡
使用零拷贝: 机械硬盘  => 应用程序缓冲区 => 网卡传输

## Channel
1. 数据传输流，与channel相关的概念有以下四个，上一张图让你了解netty里面的Channel。
2. Channel，表示一个连接，可以理解为每一个请求，就是一个Channel。
3. ChannelHandler，核心处理业务就在这里，用于处理业务请求。
4. ChannelHandlerContext，用于传输业务数据。
5. ChannelPipeline，用于保存处理过程需要用到的ChannelHandler和ChannelHandlerContext。

NioSocketChannel： 异步非阻塞的客户端 TCP Socket 连接。
NioServerSocketChannel： 异步非阻塞的服务器端 TCP Socket 连接。
OioSocketChannel： 同步阻塞的客户端 TCP Socket 连接。
OioServerSocketChannel： 同步阻塞的服务器端 TCP Socket 连接。

## ByteBuf
ByteBuf是一个存储字节的容器，最大特点就是使用方便，它既有自己的读索引和写索引，方便你对整段字节缓存进行读写，也支持get/set，方便你对其中每一个字节进行读写，他的数据结构如下图所示：
1. Heap Buffer 堆缓冲区,堆缓冲区是ByteBuf最常用的模式，他将数据存储在堆空间。
2. Direct Buffer 直接缓冲区, 直接缓冲区是ByteBuf的另外一种常用模式，他的内存分配都不发生在堆，jdk1.4引入的nio的ByteBuffer类允许jvm通过本地方法调用分配内存，这样做有两个好处
3. 通过免去中间交换的内存拷贝, 提升IO处理速度; 直接缓冲区的内容可以驻留在垃圾回收扫描的堆区以外。
4. DirectBuffer 在 -XX:MaxDirectMemorySize=xxM大小限制下, 使用 Heap 之外的内存, GC对此”无能为力”,也就意味着规避了在高负载下频繁的GC过程对应用线程的中断影响.
5. Composite Buffer 复合缓冲区 复合缓冲区相当于多个不同ByteBuf的视图，这是netty提供的，jdk不提供这样的功能。

### Codec编码和解码
#### java的编码和解码,将原始数据和自定义的消息对象进行转化,使用字节传输,网络传输,对象持久化,无法跨语言,序列化之后太大,性能低
1. Netty中的编码/解码器，通过他你能完成字节与pojo、pojo与pojo的相互转换，从而达到自定义协议的目的。
2. 在Netty里面最有名的就是HttpRequestDecoder和HttpResponseEncoder了。
3. Netty中的编解码器,实现了ChannelHandlerAdapter,也是一种特殊的ChannelHandler,依赖于CHannelPieLine.

### 解码器Decoder
1. 解码器是入栈数据的体现,是抽象ChannelInboundHandler的实现,将解码器放在pipeLine中
2. netty中提供了,ByteToMessageDecoder(字节转换为消息)和MessagetoMessageDecoder(pojo转换为pojo),httpServerCodec
3. 



