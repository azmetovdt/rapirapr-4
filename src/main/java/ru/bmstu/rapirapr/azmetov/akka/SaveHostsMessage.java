package ru.bmstu.rapirapr.azmetov.akka;


public class SaveHostsMessage {
    private static String host;

    public SaveHostSMessage(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }
}
