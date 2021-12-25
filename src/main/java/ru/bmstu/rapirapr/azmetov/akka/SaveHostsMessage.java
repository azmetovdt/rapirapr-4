package ru.bmstu.rapirapr.azmetov.akka;


import java.util.List;

public class SaveHostsMessage {
    private static List<String> hosts;

    public SaveHostsMessage(List<String> hosts) {
        this.hosts = hosts;
    }

    public List<String> getHosts() {
        return hosts;
    }
}
