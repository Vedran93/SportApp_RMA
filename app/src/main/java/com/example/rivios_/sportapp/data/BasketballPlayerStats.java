package com.example.rivios_.sportapp.data;

import android.support.annotation.NonNull;

import com.example.rivios_.sportapp.data.Athlete;
import com.example.rivios_.sportapp.data.BasketballStats;

public class BasketballPlayerStats implements Comparable{
    private Athlete athlete;
    private BasketballStats stats;
    private int gameCount;

    public BasketballPlayerStats()
    {
        athlete = null;
        stats = null;
        gameCount = 0;
    }

    public BasketballPlayerStats(Athlete pl, BasketballStats st, int c)
    {
        this.athlete = pl;
        this.stats = st;
        this.gameCount = c;
    }

    public Athlete getAthlete() { return athlete; }
    public BasketballStats getStats() { return stats; }
    public int getGameCount() { return gameCount; }

    public void setAthlete(Athlete pl) { athlete = pl; }
    public void setStats(BasketballStats st) { stats = st; }
    public void setGameCount(int c) { gameCount = c; }

    public String toString()
    {
        return athlete.getNickname() + "       " + stats.getPoints() + " PTS  " + stats.getAssists() + " AST  " + stats.getJumps() + " RBD";
    }


    @Override
    public int compareTo(@NonNull Object o) {
        BasketballPlayerStats ps = (BasketballPlayerStats) o;

        return ps.getStats().getPoints() - stats.getPoints();
    }
}
