package ru.bmstu.rapirapr.azmetov.akka;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoreActor extends AbstractActor {

    private final List<String> hosts = new ArrayList<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(RandomHostMessage.class, m -> {
                    sender().tell(new HostMessage(getRandomHost()), getSelf());
                })
                .match(SaveHostsMessage.class)
                .match(SaveHostMessage.class, this::saveHost)
                .build();
    }

    private void saveHost(SaveHostMessage m) {
        hosts.add(m.getHost());
    }

    private void saveHosts(SaveHostsMessage m) {
        hosts = m.getHosts();
    }

    private String getRandomHost() {
        return hosts.get(new Random().nextInt(hosts.toArray().length));
    }

}