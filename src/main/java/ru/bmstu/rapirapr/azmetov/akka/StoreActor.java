package ru.bmstu.rapirapr.azmetov.akka;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreActor extends AbstractActor {

    private final List<String> hosts = new List<String>;

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(Message.class, message -> sender().tell(getProgramResults(message.getUrl()), self()))
                .match(TestResult.class, this::saveResults)
                .build();
    }

    private void saveResults(TestResult result) {
        System.out.println("saving: " + result.toString());
        testResultsMap.put(result.getUrl(), result.getTime());
    }

    private Number getProgramResults(String id) {
        return testResultsMap.getOrDefault(id, -1);
    }
}