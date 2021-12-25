package ru.bmstu.rapirapr.azmetov.akka;

import akka.actor.ActorRef;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZookeeperController {
    private ZooKeeper zoo;
    private ActorRef actor;

    public ZookeeperController(String host, ActorRef actor) {
        this.actor = actor;
    }
    public void close() throws InterruptedException {
        zoo.close();
    }

    public void addNode(String host, String path, CreateMode mode)
    public void watchNodes() {
        try {
            List<String> nodes = zoo.getChildren("", watchedEvent -> {
                if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    watchNodes();
                }
            });

            List<String> hosts = new ArrayList<>();
            for (String node : nodes) {
                hosts.add(
                        Arrays.toString(zoo.getData("" + "/" + node, false, null))
                );
            }
            actor.tell(new SaveHostsMessage(hosts), ActorRef.noSender());
        } catch (KeeperException | InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}
