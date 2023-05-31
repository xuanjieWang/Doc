## springCloudSteam简介
服务和服务之间的通信，对外rest，对内prc，微服务中间的消息中间键是不同的。兼容kafka和rabbitmq
消息中间件：应用解耦，异步处理，流量消峰，日志处理（kafka）

## 什么是SpringCloudSteam
用于构建消息驱动微服务应用程序框架。消费者是阻塞监听的（时刻连接的），生产者是调用的（需要就进行调用）。

#### 依赖
spring-cloud-stream-binder-kafka
spring-cloud-stream-binder-rabbit

### 常用注解
1. @input：注解标识输入通道，接收（消息消费者）的消息将通过该通道进入应用程序
2. @OutPut： 注解标识输出通道，发布（消息生产者）的消息将通过该通道离开应用程序
3. @StreamLintener 监听队列，消费者的队列的消息接收
4. @EnableBinding 注解标识绑定，将信道channel和交换机exchange绑定在一起

### 工作原理
1. 生产者 -> source -> channel ->  Binder
2. 消费者 -> sink 

生产者配置文件代码yml: @EnableBinding
```
spring :
  cloud:
    stream:
      bindings:
        output:
          destination:  交换机名
```
消费者配置文件代码yam:   使用注解: @EnableBinding+@StreamListener
```
spring :
  cloud:
    stream:
      bindings:
        input:
          destination:  交换机名
```

消息分组：多个消息的消费者，多个消费者监听同一个队列，在配置文件中添加grpud。防止重复消费
消息分区：多个统一用户的数据发送，将所有的信息发到一个
```
spring :
  cloud:
    stream:
      bindings:
        outPut:
          destination：交换机名称
          producer:
            partition-key-expression: 表达式规则
            partition-count 分区数量
```
```
spring :
  cloud:
    stream:
      instance-count:2  总数
      instance-index:0 
      bindings:
        input:
          destination：交换机名称
          group：分区
          consumer：true支持分区
```
再发送消息的时候：source.output.send(MessagBuilder.withPayload(message).setHeader("xxx",0).build())

