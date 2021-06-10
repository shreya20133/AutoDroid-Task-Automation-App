package com.example.project.ScheduledReminder;

public class ReminderDB {
    String reminder;
    Long time;

    public ReminderDB(String reminder, Long time) {
        this.reminder = reminder;
        this.time = time;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
