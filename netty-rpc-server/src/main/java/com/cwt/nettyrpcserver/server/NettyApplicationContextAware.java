package com.cwt.nettyrpcserver.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import com.cwt.common.annotation.PRCServer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class NettyApplicationContextAware implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(NettyApplicationContextAware.class);

    @Value("${zk.address}")
    private String zookeeperAddress;

    @Value("${zk.port}")
    private int zookeeperPort;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> rpcBeanMap = new HashMap<>();
        for (Object object : applicationContext.getBeansWithAnnotation(PRCServer.class).values()) {
            System.out.println(object.getClass());
            System.out.println("111"+object.getClass().getAnnotation(PRCServer.class).cls().getName());
            System.out.println("222"+object);
            rpcBeanMap.put("/" + object.getClass().getAnnotation(PRCServer.class).cls().getName(), object);
        }
        for(Object object:rpcBeanMap.values()){
            System.out.println(object);
        }

        try {
            NettyServer.start(zookeeperAddress, zookeeperPort, rpcBeanMap);
        } catch (Exception e) {
            logger.error("register error !", e);
        }
    }
}
