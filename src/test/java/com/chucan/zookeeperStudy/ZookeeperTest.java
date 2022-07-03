package com.chucan.zookeeperStudy;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * 备注一下这里踩到的坑
 */

/**
 * 1.获取zk客户端链接对象
 * 2.调用相关API完成对应的个功能
 * 3.关闭资源
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ZookeeperTest {

    @Autowired
    @Qualifier("zk")
    private ZooKeeper client;

    /**
     * connectString:连接zk服务的地址，形式为 ip:host,ip:host...可以填写多个
     * sessionTimeout：超时时长
     */
    /* @Test
    public void testCreateZookeeperClient() throws InterruptedException, IOException {
        String connectString = "192.168.170.128:2181";
        int sessionTimeout = 10000;
        ZooKeeper zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("根据具体的业务进行下一步操作。。。");
            }
        });

        System.out.println(zooKeeper);

        zooKeeper.close();
    }*/

    @Test
    public void checkClient(){
        System.out.println(client);
    }

    /**
     * 创建节点
     * 1.path 指定创建节点的路径
     * 2.data 指定要创建的节点的数据
     * 3.acl：对操作用户的权限控制
     * 4.createMode: 指定当前节点的类型（持久化/临时）
     */
    @Test
    public void createPath() throws KeeperException, InterruptedException {
        client.create("/sanguo","shuhan".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(client);
    }

    @Test
    public void createPath2() throws KeeperException, InterruptedException {
        client.create("/hongloumeng","sunwukong".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(client);
    }

    /**
     * 获取某个路径下的子节点（不监控）
     */
    @Test
    public void getChildNoWatch() throws KeeperException, InterruptedException {
        List<String> childrens = client.getChildren("/", false);
        for(String each: childrens){
            System.out.println(each);
        }
    }

    /**
     * 获取某个路径下的子节点（监控）
     */
    @Test
    public void getChildWatch() throws KeeperException, InterruptedException {
        List<String> childrens = client.getChildren("/", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("发现目录下节点有变化！");
            }
        });

        Thread.sleep(30000);
    }

    /**
     * 判断节点是否存在(不监控）
     * exist方法也可以增加监控，和查询子节点一样
     * @throws InterruptedException
     */
    @Test
    public void existNode() throws KeeperException, InterruptedException {
        Stat exists = client.exists("/sanguo1", false);
        if(exists == null) {
            System.out.println("no Exist");
        }else{
            System.out.println("Exist");
        }
    }

    /**
     * 获取节点的数据(不监控）
     * getData方法也可以增加监控，和查询子节点一样
     * @throws InterruptedException
     */
    @Test
    public void getNodeData() throws KeeperException, InterruptedException {
        Stat exists = client.exists("/sanguo", false);
        if(exists == null) {
            System.out.println("节点不存在！");
        }else{
            byte[] data = client.getData("/sanguo", false, exists);
            System.out.println(new String(data));
        }
    }

    /**
     * 设置节点的数据
     * @throws InterruptedException
     */
    @Test
    public void setNodeData() throws KeeperException, InterruptedException {
        Stat stat = client.exists("/sanguo", false);
        if(stat == null) {
            System.out.println("节点不存在！");
        }else{
            Stat data = client.setData("/sanguo", "shu,han,wu".getBytes(), stat.getVersion());
        }
    }

    /**
     * 删除空节点(是指没有子节点，不是指当前节点没有内容）
     * @throws InterruptedException
     */
    @Test
    public void deleteNode() throws KeeperException, InterruptedException {
        Stat stat = client.exists("/t", false);
        if(stat == null) {
            System.out.println("节点不存在！");
        }else{
            //删除节点
            client.delete("/t", stat.getVersion());
        }
    }

    /**
     * 递归删除节点(可以删除带有子节点的父节点）
     * @throws InterruptedException
     */
    @Test
    public void deleteAllNode() throws KeeperException, InterruptedException {
        Stat stat = client.exists("/s", false);
        if(stat == null) {
            System.out.println("节点不存在！");
        }else{
            //删除节点
            deleteAllNode("/s");
        }
    }

    public void deleteAllNode(String path) throws KeeperException, InterruptedException {
        Stat stat = client.exists(path, false);
        if(stat == null) {
            System.out.println("节点不存在！");
        }else{
            //删除节点
            List<String> children = client.getChildren(path, false);
            if(children.isEmpty()){
                client.delete(path, stat.getVersion());
            }else{
                //递归调用，先把所有的子节点删除，再删除自己
                for(String child: children){
                    deleteAllNode(path+"/"+child);
                }
                client.delete(path, stat.getVersion());
            }
        }
    }








    @After
    public void close() throws InterruptedException {
        client.close();
    }
















}
