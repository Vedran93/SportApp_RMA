package com.example.rivios_.sportapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class FootballStats implements Parcelable{

    private long playerId;
    private long gameId;
    private int goals;
    private int assists;
    private String team;

    public FootballStats() {
        this.playerId = 0;
        this.gameId = 0;
        this.goals = 0;
        this.assists = 0;
        this.team = null;
    }

    public FootballStats(long playerId, long gameId, int goals, int assists, String team) {
        this.playerId = playerId;
        this.gameId = gameId;
        this.goals = goals;
        this.assists = assists;
        this.team = team;
    }

    protected FootballStats(Parcel in) {
        playerId = in.readLong();
        gameId = in.readLong();
        goals = in.readInt();
        assists = in.readInt();
        team = in.readString();
    }

    public static final Creator<FootballStats> CREATOR = new Creator<FootballStats>() {
        @Override
        public FootballStats createFromParcel(Parcel in) {
            return new FootballStats(in);
        }

        @Override
        public FootballStats[] newArray(int size) {
            return new FootballStats[size];
        }
    };

    public long getGameId() {
        return gameId;
    }
    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getPlayerId() {
        return playerId;
    }
    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public int getGoals() {
        return goals;
    }
    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }
    public void setAssists(int assists) {
        this.assists = assists;
    }


    public String getTeam() {
        return team;
    }
    public void setTeam(String team) {
        this.team = team;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(playerId);
        parcel.writeLong(gameId);
        parcel.writeInt(goals);
        parcel.writeInt(assists);
        parcel.writeString(team);
    }
}
