# Zookeeper简述
Zookeeper是一个开源的分布式的，为分布式英勇敢提供协调服务的Apache项目。

Zookeeper从设计模式角度来理解，是一个基于观察者模式设计的分布式服务管理框架，它负责存储和管理大家都关心的数据，
然后接受观察者的注册，一旦这些数据的状态发生了变更，Zookeeper就负责通知已经在Zookeeper上注册的那些观察者做出响应的反应。

总结：Zookeeper = 文件系统 + 通知机制

![zk工作原理](https://github.com/liuguanglei123/zookeeper/blob/main/images/zk_work_theory.png)

## zk特点

![zk特点](https://github.com/liuguanglei123/zookeeper/blob/main/images/zk_characteristic.png)

* zk集群：一个领导者（leader），多个跟随者（follower）组成
* 集群中只要有半数以上的节点存活，zk集群就能够正常服务
* 全局数据一致：每个Server保存一份想通过的数据副本，Client无论连接到哪个Server，数据都是一致的。
* 更新请求顺序进行，来自同一个client的更新请求按其发送顺序依次执行
* 数据更新原子性，一次数据更新要么成功，要么失败
* 实时性，在一定时间范围内，client能读到最新数据

## 数据结构
zk数据模型的结构与Unix文件系统类似，整体可以看做是一棵树，每个节点称作一个ZNode。

每一个ZNode默认能够存储1MB的数据，每个ZNode都可以通过其路径唯一标识。

![zk数据结构](https://github.com/liuguanglei123/zookeeper/blob/main/images/zk_data_struct.png)

## 应用场景
在分布式环境下，经常需要对应用/服务进行统一命名，便于识别。

例如：IP不容易被识别，而域名更容易识别

如下图，当client想要访问 orderService 时，可以从节点上选取其中一个子节点的值来使用，
这样我们只需要记住 orderService 即可，不需要记忆具体的ip地址

![zk数据结构](https://github.com/liuguanglei123/zookeeper/blob/main/images/zk_name_service.png)

## 统一配置管理
1) 分布式环境下，配置文件同步非常常见

(1) 一般要求一个集群中，所有节点的配置信息是一致的，比如kafka集群
(2) 对配置文件修改后，希望能够快速同步到各个节点上

2) 配置管理可以交由Zk实现

(1) 可以将配置信息写入ZK上的一个ZNode
(2) 各个客户端监听这个ZNode
(3) 一旦Znode中的数据被修改，ZK将通知各个客户端

![zk数据结构](https://github.com/liuguanglei123/zookeeper/blob/main/images/zk_config.png)

## 统一集群管理
1) 分布式环境中国，实时掌握每个节点的状态是必要的

(1) 可以根绝节点实时状态做出一些调整

2) ZK可以实现实时监控节点状态变化

(1) 可将节点信息写入ZK上的一个ZNode
(2) 监听这个Znode可获取它的实时状态变化

## 服务器动态上下线
![zk数据结构](https://github.com/liuguanglei123/zookeeper/blob/main/images/zk_dymanic_online.png)

## 软负载均衡
![zk数据结构](https://github.com/liuguanglei123/zookeeper/blob/main/images/zk_load_balancing.png)








