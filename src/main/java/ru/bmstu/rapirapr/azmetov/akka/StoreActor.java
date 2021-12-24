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
                .match(TestResult.class, testResult -> saveResults(testResult.getMessageTest().getPackageId(), testResult))
                .match(String.class, id -> sender().tell(getProgramResults(id), self()))
                .build();
    }


}