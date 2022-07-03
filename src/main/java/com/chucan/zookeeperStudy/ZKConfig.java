package com.chucan.zookeeperStudy;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @Author: Yeman
 * @CreatedDate: 2022-07-02-18:08
 * @Description:
 */
@Configuration
public class ZKConfig {

    @Bean("zk")
    public ZooKeeper getZookeeper() throws IOException {
        String connectString = "192.168.170.128:2181";
        int sessionTimeout = 10000;
        ZooKeeper zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("根据具体的业务进行下一步操作。。。");
            }
        });

        System.out.println(zooKeeper);

        return zooKeeper;
    }
}
