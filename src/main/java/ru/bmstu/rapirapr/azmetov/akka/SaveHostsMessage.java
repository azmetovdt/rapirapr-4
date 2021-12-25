package ru.bmstu.rapirapr.azmetov.akka;


public class SaveHostMessage {
    private static String host;

    public SaveHostMessage(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }
}
