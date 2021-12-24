package ru.bmstu.rapirapr.azmetov.akka;


public class Message {
    private String url;

    public Message(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
