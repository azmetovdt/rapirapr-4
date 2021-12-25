package ru.bmstu.rapirapr.azmetov.akka;

import akka.actor.ActorRef;
import org.apache.zookeeper.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZookeeperController {
    private ZooKeeper zoo;
    private ActorRef actor;

    public static final String NODE_ROOT_PATH = "/s";
    public static final String NODE_SERVER_PATH = "/server";
    public static final String UPDATE_LOG_MESSAGE = "Список нод изменилсяо: ";

    public ZookeeperController(String host, ActorRef actor) {
        this.actor = actor;
    }

    public void close() throws InterruptedException {
        zoo.close();
    }

    public void addNode(String host, String path, CreateMode mode) throws InterruptedException, KeeperException {
        zoo.create(path, host.getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
    }

    public void addServerNode(String host) throws InterruptedException, KeeperException {
        addNode(host, NODE_ROOT_PATH + NODE_SERVER_PATH, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public void watchNodes() {
        try {
            List<String> nodes = zoo.getChildren(NODE_ROOT_PATH, watchedEvent -> {
                if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    watchNodes();
                }
            });
            System.out.println(UPDATE_LOG_MESSAGE);
            List<String> hosts = new ArrayList<>();
            for (String node : nodes) {
                System.out.println(node);
                hosts.add(
                        Arrays.toString(zoo.getData(NODE_ROOT_PATH + "/" + node, false, null))
                );
            }
            actor.tell(new SaveHostsMessage(hosts), ActorRef.noSender());
        } catch (KeeperException | InterruptedException e) {
            System.out.println(e);
        }
    }
}
