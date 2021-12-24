package ru.bmstu.rapirapr.azmetov.akka;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreActor extends AbstractActor {

    private final Map<String, List<TestResult>> testResultsMap = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(Message.class, id -> sender().tell(getProgramResults(id), self()))
                .build();
    }

//    private void saveResults(String id, TestResult result) {
//        if (!testResultsMap.containsKey(id)) {
//            testResultsMap.put(id, new ArrayList<>());
//        }
//        testResultsMap.get(id).add(result);
//    }
//
//    private List<TestResult> getProgramResults(String id) {
//        if (!testResultsMap.containsKey(id)) {
//            return new ArrayList<>();
//        }
//        return testResultsMap.get(id);
//    }
}