package ru.bmstu.rapirapr.azmetov.akka;

public class TestResult {
    private final MessageTest messageTest;
    private final String status;
    private final String output;

    public TestResult(MessageTest messageTest, String status, String output) {
        this.messageTest = messageTest;
        this.status = status;
        this.output = output;
    }

    public MessageTest getMessageTest() {
        return messageTest;
    }

    public String getStatus() {
        return this.status;
    }

    public String getOutput() {
        return this.output;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "messageTest=" + this.messageTest.toString() + '\n' +
                ", status='" + this.status + '\'' +
                ", output='" + this.output + '\'' +
                '}';
    }
}
