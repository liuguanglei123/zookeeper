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
在ZK中记录每台服务器的访问数，让访问数最少的服务器去处理最新的客户端请求
![zk数据结构](https://github.com/liuguanglei123/zookeeper/blob/main/images/zk_load_balancing.png)

# zk安装
下载zookeeper 最新版镜像
```
docker search zookeeper    
docker pull zookeeper 
docker images              //查看下载的本地镜像
docker inspect zookeeper   //查看zookeeper详细信息
新建一个文件夹
mkdir zookeeper
挂载本地文件夹并启动服务
docker run -d -e TZ="Asia/Shanghai" -p 2181:2181 -v /root/docker/zookeeper:/data --name zookeeper --restart always 3721c1c97fbd

参数解释
-e TZ="Asia/Shanghai" # 指定上海时区 
-d # 表示在一直在后台运行容器
-p 2181:2181 # 对端口进行映射，将本地2181端口映射到容器内部的2181端口
--name # 设置创建的容器名称
-v # 将本地目录(文件)挂载到容器指定目录；
--restart always #始终重新启动zookeeper
进入容器(zookeeper)
docker exec -it zookeeper bash 

如果启动时需要加一些参数，比如 设置ZOO_INIT_LIMIT=10，可以用下面的命令
docker run -e "ZOO_INIT_LIMIT=10" --name some-zookeeper --restart always -d zookeeper

```

Zookeeper中的配置文件zoo.cfg中参数含义解读如下：
1) tickTime =2000：通信心跳数，Zookeeper服务器与客户端心跳时间，单位毫秒

Zookeeper使用的基本时间，服务器之间或客户端与服务器之间维持心跳的时间间隔，也就是每个tickTime时间就会发送一个心跳，时间单位为毫秒。
它用于心跳机制，并且设置最小的session超时时间为两倍心跳时间。(session的最小超时时间是2*tickTime)

2) initLimit =10：LF初始通信时限

集群中的Follower跟随者服务器与Leader领导者服务器之间初始连接时能容忍的最多心跳数（tickTime的数量），用它来限定集群中的Zookeeper服务器连接到Leader的时限。

3) syncLimit =5：LF同步通信时限

集群中Leader与Follower之间的最大响应时间单位，假如响应超过syncLimit * tickTime，Leader认为Follwer死掉，从服务器列表中删除Follwer。

4) dataDir：数据文件目录+数据持久化路径

主要用于保存Zookeeper中的数据。

5) clientPort =2181：客户端连接端口

监听客户端连接的端口。

zk客户端启动

bin/zkCli.sh


## 分布式安装部署
1）集群规划

在hadoop102、hadoop103和hadoop104三个节点上部署Zookeeper。

2）解压安装

（1）解压Zookeeper安装包到/opt/module/目录下

[atguigu@hadoop102 software]$ tar -zxvf zookeeper-3.5.7.tar.gz -C /opt/module/

（2）同步/opt/module/zookeeper-3.5.7目录内容到hadoop103、hadoop104

[atguigu@hadoop102 module]$ xsync zookeeper-3.5.7/

3）配置服务器编号

（1）在/opt/module/zookeeper-3.5.7/这个目录下创建zkData

[atguigu@hadoop102 zookeeper-3.5.7]$ mkdir -p zkData

（2）在/opt/module/zookeeper-3.5.7/zkData目录下创建一个myid的文件

[atguigu@hadoop102 zkData]$ touch myid

添加myid文件，注意一定要在linux里面创建，在notepad++里面很可能乱码

（3）编辑myid文件

[atguigu@hadoop102 zkData]$ vi myid

在文件中添加与server对应的编号：2

（4）拷贝配置好的zookeeper到其他机器上

[atguigu@hadoop102 zkData]$ xsync myid

并分别在hadoop103、hadoop104上修改myid文件中内容为3、4

4）配置zoo.cfg文件

（1）重命名/opt/module/zookeeper-3.5.7/conf这个目录下的zoo_sample.cfg为zoo.cfg

[atguigu@hadoop102 conf]$ mv zoo_sample.cfg zoo.cfg

（2）打开zoo.cfg文件

[atguigu@hadoop102 conf]$ vim zoo.cfg

修改数据存储路径配置

dataDir=/opt/module/zookeeper-3.5.7/zkData

增加如下配置

#######################cluster##########################

server.2=hadoop102:2888:3888

server.3=hadoop103:2888:3888

server.4=hadoop104:2888:3888

（3）同步zoo.cfg配置文件

[atguigu@hadoop102 conf]$ xsync zoo.cfg

（4）配置参数解读

server.A=B:C:D。

A是一个数字，表示这个是第几号服务器；

集群模式下配置一个文件myid，这个文件在dataDir目录下，这个文件里面有一个数据就是A的值，Zookeeper启动时读取此文件，拿到里面的数据与zoo.cfg里面的配置信息比较从而判断到底是哪个server。

B是这个服务器的地址；

C是这个服务器Follower与集群中的Leader服务器交换信息的端口；

D是万一集群中的Leader服务器挂了，需要一个端口来重新进行选举，选出一个新的Leader，而这个端口就是用来执行选举时服务器相互通信的端口。

5）集群操作

（1）分别启动Zookeeper

[atguigu@hadoop102 zookeeper-3.5.7]$ bin/zkServer.sh start

[atguigu@hadoop103 zookeeper-3.5.7]$ bin/zkServer.sh start

[atguigu@hadoop104 zookeeper-3.5.7]$ bin/zkServer.sh start

（2）查看状态

[atguigu@hadoop102 zookeeper-3.5.7]# bin/zkServer.sh status

JMX enabled by default

Using config: /opt/module/zookeeper-3.5.7/bin/../conf/zoo.cfg

Mode: follower

[atguigu@hadoop103 zookeeper-3.5.7]# bin/zkServer.sh status

JMX enabled by default

Using config: /opt/module/zookeeper-3.5.7/bin/../conf/zoo.cfg

Mode: leader

[atguigu@hadoop104 zookeeper-3.5.7]# bin/zkServer.sh status

JMX enabled by default

Using config: /opt/module/zookeeper-3.5.7/bin/../conf/zoo.cfg

Mode: follower

## 客户端命令行操作
| 命令基本语法  |   功能描述   |
| ------------- | ------------- |
| help  | 显示所有操作命令  |
| ls path  | 使用 ls 命令来查看当前znode的子节点  |
| ls -w path  | 监听子节点变化  |
| ls -s path  | 附加次级信息（包含创建时间等信息）  |
| create nodename | 普通创建节点  |
| create -e nodename  | 创建临时节点（重启或者超时消失）  |
| create -s nodename  | 创建一个带序号的节点  |
| get path  | 获得节点的值  |
| get -w path  | 获得节点的值  |
| get path  | 获得节点的值  |
| set path pathContent | 设置节点的具体值,pathContent是一个字符串（设置多次会覆盖，类似map的put方法）   |
| stat  | 查看节点状态  |
| delete  | 删除节点  |
| deleteall  | 递归删除节点  |

# ZK的API操作
请查看 ZookeeperTest.java 文件内容

# Zookeeper内部原理
## 节点类型
![zk节点类型](https://github.com/liuguanglei123/zookeeper/blob/main/images/zk_node_type.png)

## Stat结构体
![zk节点类型](https://github.com/liuguanglei123/zookeeper/blob/main/images/zk_stat_1.png)











