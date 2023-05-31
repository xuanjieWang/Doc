# 搭建RabbitMQ集群


1. 修改三台节点的host文件，/etc/hostname
2. hostname在每一个机器中都存在
3. 使用远程复制命令，在第一个cookie命令复制到第二台和第三台    scp /var/lib/rabbitmq/.erlang.cookie root@node2/var/rabbitmq/.erlang.cookoe
4. 三台节点上面都执行重启命令 rabbitmmq-server -detached 
5. 在其他子节点执行： rabbitmqctl stop_app 
                    rabbitmqctl reset
                    rabbitmqctl join_cluster rabbit@node1
                    rabbitmqctl start_app
6. 查询集群状态 rabbitmqctl cluster——status
7. 创建集群账号和超级管理员账户
8. 使用Haproxy负载均衡到所有的节点
9. 节点脱离命令：rabbitmqctl forget_cluster_node rabbit@node2(node1机器上面执行)

