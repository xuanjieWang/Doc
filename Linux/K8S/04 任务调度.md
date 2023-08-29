## pod 择优而居
1. k8s集群中，任务被定义为pod这个概念，pod是集群可以承载任务的原子单元，pod是容器组
2. 一个pod实际上封装了多个容i强化的应用，被封装在一个pod里面的容器是相当程度的耦合关系

## 择优策略
1. 第一步，从所有节点中排除不满足条件的节点
2. 给剩余的节点打分，最后得分高胜出
3. pod配置文件是json

#### POd配置文件三个关键的地方：镜像地址，命令，容器端口
```yml
{
  "apiVersion": "v1",
  "kind": "Pod",
  "metadata": {
    "name": "app"
  },
  "spec": {
    "containers": [
      {
        "name": "app",
        "image": "registry.cn-hangzhou.aliyuncs.com/kube-easy/ app:latest",
        "command": [ "app"],
        "ports": [
        {
          "containerPort": 2580
        }
      ]
    }
]
}
```

## 日志级别
1. 集群调度算法被实现为运行在master 节点上的系统组件，这一点和 api server 类似。
2. 其对应的进程名是 kube-scheduler。kube-scheduler 支持多个级别的日志输出。
3. 打分的过程，我们需要把日志级别提高到10，即加入参数 --v=10。
```yml
kube-scheduler --address=127.0.0.1 --kubeconfig=/etc/kubernetes/scheduler. conf --leader-elect=true
--v=10
```

## 创建Pod
1. 使用 curl，以证书和 pod 配置文件等作为参数，通过 POST 请求访问 api server 的接口，我们可以在集群里创建对应的pod。
```yml
# curl -X POST -H 'Content-Type: application/json;charset=utf-8' --cert ./ client.crt --cacert ./ca.crt --key
./client.key https://47.110.197.238:6443/api/v1/namespaces/default/pods -d@
app.json
```
## 预选规则
1. 根据预选的规则将不符合的节点过滤掉
2. 常见的预选规则：PodFitsResourcesPred（判断节点上面的剩余规则）和PodFitsHost-PortsPred（节点上面的端口是否被占用）

## 优选kube-scheduler根据节点可用资源给剩余节点打分
1. 亮中计算方式：节点剩余cpu和内存占用总cpu和内存的比例（剩余的占比，使用率）
2. cpu和内存使用比例之差的绝对值，绝对值越大，得分越少。（使用的差值，资源使用比例接近）
3. 亲和性，或者一个服务有多个相同的pod组成的情况下，多个pod在节点上面的分散程度

