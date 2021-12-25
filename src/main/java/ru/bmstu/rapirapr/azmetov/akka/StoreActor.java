package ru.bmstu.rapirapr.azmetov.akka;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.*;

public class StoreActor extends AbstractActor {

    private final List<String> hosts = new ArrayList<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(RandomHostMessage.class, m -> { sender().tell(new HostMessage(getRandomHost()), getSelf()); })
                .match(SaveHostMessage.class, this::saveResults)
                .build();
    }

    private void saveResults(SaveHostMessage m) {
        hosts.add(m.getHost());
    }

    private String getRandomHost() {
        return hosts.get(new Random().nextInt(hosts.toArray().length));
    }

}