# 跨地点的交换机，联合交换机

1. 开启rabbitmq联合插件,每一台机器上面都需要开启
  ```
  rabbitmq-plugins enable rabbitmq_federation
  rabbitmq-plugins enable rabbitmq_federation_management
  ```
2. 进行重启：
3. 原理：上游的数据是交换机为几点，一号节点的交换机配置二号交换机的地址，先注册出2号节点的交换机，再创建上游节点的交换机
4. 再web界面中进行配置，再添加规则police，交换机策略

数据同步：联邦交换机和联邦队列，在不同的地区的队列中，将一个数据中的队列联邦到另一个地区的队列，就可以在另外的地方进行消费。完成数据同步。

### shovel：将源端的数据拉取到目的地的数据，负责连接源和目的地，负责消息的读写和连接失问题的处理
```
rabbitmq-plugins enable rabbitmq_shovel
rabbitmq-plugins enable rabbitmq_shovel_management
systemctl restart rabbitmq-server
```

