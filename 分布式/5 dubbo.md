### dubbo和springcloud的区别是什么
1. Dubbo基于rpc的远程过程调用，SpringCloud是基于http协议和restful风格的api实现服务之间的通信
2. 生态系统： Dubbo是阿里巴巴开源的项目，其生态系统较为成熟，拥有丰富的插件和工具支持。Spring Cloud是Spring官方提供的项目，它与Spring框架紧密集成，可以更好地利用Spring的生态系统和开发工具。
3. 服务注册： Dubbo使用ZooKeeper或其他注册中心来管理服务的注册和发现。Spring Cloud使用Eureka、Consul等注册中心，同时还提供了自己的服务注册与发现组件——Spring Cloud Netflix Eureka。，nacos也是使用的rpc调用
4. 服务网关： Dubbo没有内置的服务网关，需要结合其他组件来实现。Spring Cloud提供了Zuul和Gateway等服务网关组件，可以进行路由、负载均衡、过滤等功能。
5. 配置管理： Dubbo需要手动配置服务的URL和参数。Spring Cloud提供了分布式配置管理组件——Spring Cloud Config，可以集中管理和动态更新配置

### PRC和HTTP
1. 简化架构： 使用HTTP调用可以减少与其他组件的依赖，从而简化整体架构。RPC调用通常需要使用专门的RPC框架，而HTTP调用可以直接使用标准的HTTP协议，降低了技术栈的复杂性。
2. 跨语言支持：HTTP是一种通用的协议，几乎所有的编程语言都能够方便地进行HTTP调用。这意味着Nacos可以与各种语言的应用程序进行通信，而不仅限于特定的编程语言。
3. 易于扩展：使用HTTP调用可以更容易地对Nacos进行扩展。因为HTTP调用是基于标准的RESTful风格，可以使用各种开源工具和框架来构建和扩展Nacos的功能。

### restful，grpc，GraphQL
1. 资源定位： 每个资源都有唯一的URL标识，通过URL可以访问和操作资源
2. 统一接口： 使用统一的HTTP方法（GET、POST、PUT、DELETE）对资源进行操作，通过不同的HTTP方法和URL来表示不同的操作。
3. 无状态性：服务器不保存客户端的状态信息，每次请求都是独立的，客户端需要携带所有必要的信息
4. 可缓存性：通过合理设置HTTP头部，客户端和服务器可以共同决定是否缓存响应，提高性能和可扩展性。
5. 分层系统：通过分层系统可以实现更好的可伸缩性和可靠性，每个层次都只关注自己的功能。

###  dubbo
1. 高性能轻量级开源的RPC框架，面向接口远程调用，容错和负载均衡，自动注册和发现
2. dubbo面向接口远程调用，使用动态代理调用转发Dubbo远程框架，将调用信息序列化为网络字节流，通过网络传输到服务提供
3. 容错和负载均衡，使用hytrix实现，使用fallback方法执行，
4. 自动注册和发现使用zookeeper

### SPI机制
1. mate-info/service下面的配置文件实现，命名方式是接口全类名
2. 扩展点接口：是一种插件共享机制，
3. dubbo中的过滤器，filter机制是专门为提供。在每次的远程方法执行完成中后，实现ip黑白名单，监控功能，日志记录，实现rpc下面的Filter，可以打印出一个服务执行的时间，在resources下面的Mate-info文件夹下面的文件夹。                                                 
4. 实现org.apache.dubbo.rpc.Filter接口，指定生产端注册，计算方法运行时间的代码实现，在META-INF.dubbo中创建org.apache.dubbo.rpc.filter文件

### doubbo实现负载均衡
1. 在服务端的配置文件中添加负载均衡策略，通过loadbanlance机制来执行负载均衡策略
2. 处理使用配置文件中来操作负载均衡策略，还可以使用注解来指定负载均衡的
3. xml可以使用方法级别控制负载均衡，也可以使用标注，默认的负载均衡是随机的，调用多次。
4. 在消费端设置负载均的策略（随机，轮询，最少活跃数，一致性hash）

@Reference(interfaceClass = com.example.UserService.class, loadbalance = "roundrobin")
public interface UserService {
    //...
}

### 开发自己的负载均衡规则
1. org.apache.dubbo.rpc.cluster.loadBalance，可以通过实现这个接口来自定义负载均衡规则
2. 自定义负载均衡，创建dubbo-spi-loadbalance的maven模块，创建负载onltFirstLoadBalance
3. 配置负载均衡器，META-info文件夹下创建dubbo.rpc.clutster.loadbalance文件将当前的类名写入 onlyFirst=包名+负载均衡器
4. 测试负载均衡的效果

### dubbo提供了异步调用方法
1. dubbo不仅提供了同步调用，还提供了异步调用的方式，在接口中实现异步调用，消费端可以利用调用接口的时间去做其他接口调用，利用Future模式来异步导入和获取到结果
2. 获取到结果使用RpcContext.getContext().getFuture获取到异步的结果

### dubbo线程池
1. 默认大小的线程池是200大小，fix代表的是创建固定大小的线程池，cache非固定大小的线程池，在线程不足的模式下，如果单一时间很多任务过来，使用cache线程池的话会出现系统卡顿的问题
2. 线程池实现主要是对FixThreadPool做扩展，对线程池进行监控，遍历线程池，如果超出指定的部分就进行警告，接入短信平台进行操作。
3. 导入dubbo-common，继承fixedThreadPool实现Runnable，构建日志管理器，定义线程管理的阈值90%，创建currenthashMap存储线程对象。
4. 每隔3s打印出线程池的情况，在服务端使用导入线程生成器

### dubbo的路由规则
1. 两个服务提供，一台是提供者，一台是其他的机器，每个提供者可以返回不同的信息
2. 消费者创建死循环，等待用户输入，在进行调用模拟请求演示路由规则，通过ipconfig
3. 服务的路由配置，支持条件路由和脚本路由，route：//    0.0.0.0： 所有的路由


### 路由和上线系统
1. 使用zookeeper的路径感知能力，在服务准备重启之前将ip地址和服务名称写到zookeeper
2. 服务消费监听目录，读取其中需要关闭的机器
3. 请求过来，判断是否请求该应用，如果是请求重启应用，将服务提供者从服务列表剔除。操作zookeepe的工具类使用curator框架
4. 创建预发布的路径管理器，监听zookeeper路径实现PathChildrenCacheListener路径监听器，路径节点变化的时候自动调用
5. 判断节点信息在不在

### 服务动态降级
1. 服务降级，在服务器压力剧增的情况下，dubbo管理控制台控制
2. dubbo admin的屏蔽和容错，屏蔽是直接返回null，容错是当报错的时候返回null
3. 指定简单值或者null
4. 整合hystrix，熔断器
