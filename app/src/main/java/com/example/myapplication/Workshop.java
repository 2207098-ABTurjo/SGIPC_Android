package com.example.myapplication;

public class Workshop {
    private String topic;
    private String time;
    private int duration;

    public Workshop() {
    }

    public Workshop(String topic, String time, int duration) {
        this.topic = topic;
        this.time = time;
        this.duration = duration;
    }

    public String getTopic() {
        return topic;
    }

    public String getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}