package com.cwt.common.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class ZooKeeperOp {

    private static final String zkAddress = "192.168.180.129:2181";
    private static final ZkClient zkClient = new ZkClient(zkAddress);

    public static void register(String serviceName, String serviceAddress) {
        if (!zkClient.exists(serviceName)) {
            zkClient.createPersistent(serviceName);
        }
        zkClient.createEphemeral(serviceName + "/" + serviceAddress);
        System.out.printf("create node %s \n", serviceName + "/" + serviceAddress);
    }

    public static String discover(String serviceName) {
        List<String> children = zkClient.getChildren(serviceName);
        if (CollectionUtils.isEmpty(children)) {
            return "";
        }
        return children.get(ThreadLocalRandom.current().nextInt(children.size()));
    }
}
