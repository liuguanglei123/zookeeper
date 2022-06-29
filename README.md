# Zookeeper简述
Zookeeper是一个开源的分布式的，为分布式英勇敢提供协调服务的Apache项目。

Zookeeper从设计模式角度来理解，是一个基于观察者模式设计的分布式服务管理框架，它负责存储和管理大家都关心的数据，
然后接受观察者的注册，一旦这些数据的状态发生了变更，Zookeeper就负责通知已经在Zookeeper上注册的那些观察者做出响应的反应。

总结：Zookeeper = 文件系统 + 通知机制

#[]("https://github.com/liuguanglei123/zookeeper/blob/main/images/zk_work_theory.png")