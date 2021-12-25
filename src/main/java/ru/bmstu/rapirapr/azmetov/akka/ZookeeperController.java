package ru.bmstu.rapirapr.azmetov.akka;

import akka.actor.ActorRef;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperController {
    private ZooKeeper zoo;
    private ActorRef actor;

    public ZookeeperController(String host, ActorRef actor) {
        
    }
}
