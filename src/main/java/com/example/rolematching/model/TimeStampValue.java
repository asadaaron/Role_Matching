package com.example.rolematching.model;

import java.time.Instant;

public class TimeStampValue {
    private String timeStamp;
    private String value;
    public Instant timeInstant;

    public Instant getTimeInstant() {
        return timeInstant;
    }

    public void setTimeInstant(Instant timeInstant) {
        this.timeInstant = timeInstant;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TimeStampValue{" +
                "timeStamp='" + timeStamp + '\'' +
                ", value='" + value + '\'' +
                ", timeInstant=" + timeInstant +
                '}';
    }
}
