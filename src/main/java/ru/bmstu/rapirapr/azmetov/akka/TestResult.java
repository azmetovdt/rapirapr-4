package ru.bmstu.rapirapr.azmetov.akka;

public class TestResult {
    private final String url;
    private final Integer time;

    public TestResult(String url, Integer time) {
        this.url = url;
        this.time = time;
    }


    public String getUrl() {
        return url;
    }

    public Integer getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                ", status='" + this.getUrl() + '\'' +
                ", output='" + this.getTime() + '\'' +
                '}';
    }
}
