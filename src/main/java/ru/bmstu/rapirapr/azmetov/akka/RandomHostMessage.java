package ru.bmstu.rapirapr.azmetov.akka;


public class RandomHostMessage {
    private static String host;

    public RandomHostMessage(String host) {
        this.host = host;
    }

    public static String getHost() {
        return host;
    }
}
