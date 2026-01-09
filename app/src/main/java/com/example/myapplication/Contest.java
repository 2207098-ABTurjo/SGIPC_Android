package com.example.myapplication;

public class Contest {
    private String title;
    private String time;
    private int duration;
    private String link;

    public Contest(String title, String time, int duration, String link) {
        this.title = title;
        this.time = time;
        this.duration = duration;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    public String getLink() {
        return link;
    }
}