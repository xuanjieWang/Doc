#### 生产者发送消息的流程 
1. 生产者连接RabbitMQ，建立TCP连接( Connection)，开启信道（Channel） 2. 生产者声明一个Exchange（交换器），并设置相关属性，比如交换器类型、是否持久化等 3. 生产者声明一个队列井设置相关属性，比如是否排他、是否持久化、是否自动删除等 4. 生产者通过bindingKey（绑定Key）将交换器和队列绑定（binding）起来 5. 生产者发送消息至RabbitMQ Broker，其中包含routingKey（路由键）、交换器等信息 6. 相应的交换器根据接收到的routingKey查找相匹配的队列。 7. 如果找到，则将从生产者发送过来的消息存入相应的队列中。 8. 如果没有找到，则根据生产者配置的属性选择丢弃还是回退给生产者 9. 关闭信道。
10. 关闭连接。

#### 消费者接收消息的过程 
1. 消费者连接到RabbitMQ Broker ，建立一个连接(Connection ) ，开启一个信道(Channel) 。 2. 消费者向RabbitMQ Broker 请求消费相应队列中的消息，可能会设置相应的回调函数， 以及 做一些准备工作
3. 等待RabbitMQ Broker 回应并投递相应队列中的消息， 消费者接收消息。 4. 消费者确认( ack) 接收到的消息。 5. RabbitMQ 从队列中删除相应己经被确认的消息。 6. 关闭信道。
7. 关闭连接。

### Connection 和Channel关系
1. 生产者和消费者，需要与RabbitMQ Broker 建立TCP连接，也就是Connection 。
2. 一旦TCP 连接建 立起来，客户端紧接着创建一个AMQP 信道（Channel），每个信道都会被指派一个唯一的ID。信道是建立在Connection 之上的虚拟连接， RabbitMQ 处理的每条AMQP 指令都是通过信道完成的。
3. RabbitMQ 采用类似NIO的做法，复用TCP 连接，减少性能开销，便于管理。 当每个信道的流量不是很大时，复用单一的Connection 可以在产生性能瓶颈的情况下有效地节省TCP 连接资源。
4. 当信道本身的流量很大时，一个Connection 就会产生性能瓶颈，流量被限制。需要建立多个 Connection ，分摊信道。具体的调优看业务需要。信道在AMQP 中是一个很重要的概念，大多数操作都是在信道这个层面进行的。

### RabbitMQ工作模式详解
1. 生产者发消息，启动多个消费者实例来消费消息，每个消费者仅消费部分信息，可达到负载均衡的 效果。
2. 使用fanout类型交换器，routingKey忽略。每个消费者定义生成一个队列并绑定到同一个 Exchange，每个消费者都可以消费到完整的消息。消息广播给所有订阅该消息的消费者。
3. 实现RabbitMQ的消费者有两种模式，推模式（Push）和拉模式（Pull）。 实现推模式推荐的方式 是继承 DefaultConsumer 基类，也可以使用Spring AMQP的 SimpleMessageListenerContainer 。
4. 推 模式是最常用的，但是有些情况下推模式并不适用的，
5. 比如说： 由于某些限制，消费者在某个条件成立 时才能消费消息 需要批量拉取消息进行处理 实现拉模式 RabbitMQ的Channel提供了 basicGet 方法用于拉取消息。
