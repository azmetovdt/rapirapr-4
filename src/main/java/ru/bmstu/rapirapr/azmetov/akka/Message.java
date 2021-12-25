package ru.bmstu.rapirapr.azmetov.akka;


public class RandomHostMessage {
    private final String url;

    public Message(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
