package ru.bmstu.rapirapr.azmetov.akka;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreActor extends AbstractActor {

    private final Map<String, Number> testResultsMap = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(Message.class, message -> sender().tell(getProgramResults(message.getUrl()), self()))
                .build();
    }

//    private void saveResults(String id, TestResult result) {
//        if (!testResultsMap.containsKey(id)) {
//            testResultsMap.put(id, new ArrayList<>());
//        }
//        testResultsMap.get(id).add(result);
//    }
//
    private Number getProgramResults(String id) {
        return testResultsMap.getOrDefault(id, -1);
    }
}