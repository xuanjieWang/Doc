###  dubbo
1. 高性能轻量级开源的RPC框架，面向接口远程调用，容错和负载均衡，自动注册和发现
2. dubbo面向接口远程调用，使用动态代理调用转发Dubbo远程框架，将调用信息序列化为网络字节流，通过网络传输到服务提供
3. 容错和负载均衡，使用hytrix实现，使用fallback方法执行，
4. 自动注册和发现使用zookeeper

### SPI机制
1. mate-info/service下面的配置文件实现，命名方式是接口全类名
2. 扩展点接口：是一种插件共享机制，
3. dubbo中的过滤器，filter机制是专门为提供。在每次的远程方法执行完成中后，实现ip黑白名单，监控功能，日志记录
4. 实现org.apache.dubbo.rpc.Filter接口，指定生产端注册，计算方法运行时间的代码实现，在META-INF.dubbo中创建org.apache.dubbo.rpc.filter文件，将之前的类写到文件中去。
