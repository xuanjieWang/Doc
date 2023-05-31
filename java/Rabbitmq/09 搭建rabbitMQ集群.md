# 搭建RabbitMQ集群


1. 修改三台节点的host文件，/etc/hostname
2. hostname在每一个机器中都存在
3. 使用远程复制命令，在第一个cookie命令复制到第二台和第三台    scp /var/lib/rabbitmq/.erlang.cookie root@node2/var/rabbitmq/.erlang.cookoe
4. 三台节点上面都执行重启命令 rabbitmmq-server -detached 
5. 
6. 
7. 

