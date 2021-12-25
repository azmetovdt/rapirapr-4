package ru.bmstu.rapirapr.azmetov.akka;

public class HostMessage {
    private static String host;

    public HostMessage(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }
}
