package ru.bmstu.rapirapr.azmetov.akka;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreActor extends AbstractActor {

    private final List<String> hosts = new ArrayList<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(RandomHostMessage.class -> )
                .match(TestResult.class, this::saveResults)
                .build();
    }

    private void saveResults(TestResult result) {
        System.out.println("saving: " + result.toString());
        testResultsMap.put(result.getUrl(), result.getTime());
    }

    private void getRandomHost() {
        return hosts
    }

    private Number getProgramResults(String id) {
        return testResultsMap.getOrDefault(id, -1);
    }
}