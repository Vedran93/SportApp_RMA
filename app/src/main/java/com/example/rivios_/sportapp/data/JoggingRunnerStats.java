package com.example.rivios_.sportapp.data;

import android.support.annotation.NonNull;

import java.util.Date;

public class JoggingRunnerStats implements Comparable {
    private Athlete runner;
    private JoggingStats stats;

    public JoggingRunnerStats(Athlete runner, JoggingStats stats) {
        this.runner = runner;
        this.stats = stats;
    }

    public Athlete getRunner() {
        return runner;
    }

    public void setRunner(Athlete runner) {
        this.runner = runner;
    }

    public JoggingStats getStats() {
        return stats;
    }

    public void setStats(JoggingStats stats) {
        this.stats = stats;
    }

    public String toString()
    {
        Date d = new Date(stats.getTime());
        return stats.getPlace() + " " + runner.getNickname() + "                 " +
                " VRIJEME   " +
                Integer.toString(d.getHours()) + ":" +
                Integer.toString(d.getMinutes()) + ":" +
                Integer.toString(d.getSeconds());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        JoggingRunnerStats rs = (JoggingRunnerStats) o;
        return (int) (this.getStats().getTime() - rs.getStats().getTime());
    }
}
