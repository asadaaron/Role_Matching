package com.example.rolematching.model;

public class TableDataOfRoleMatching {
    private String timeStamp;
    private String value;

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
        return "TableDataOfRoleMatching{" +
                "timeStamp='" + timeStamp + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
