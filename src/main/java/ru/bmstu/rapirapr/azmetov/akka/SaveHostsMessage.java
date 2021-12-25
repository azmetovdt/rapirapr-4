package ru.bmstu.rapirapr.azmetov.akka;


public class SaveHostsMessage {
    private static String[] hosts;

    public SaveHostsMessage(String[] hosts) {
        this.hosts = hosts;
    }

    public String[] getHosts() {
        return hosts;
    }
}
