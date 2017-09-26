package com.example.rivios_.sportapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class BasketballStats implements Parcelable {
    private long playerId;
    private long gameId;
    private int points;
    private int assists;
    private int jumps;
    private String team;

    public BasketballStats() {
        this.playerId = 0;
        this.gameId = 0;
        this.points = 0;
        this.assists = 0;
        this.jumps = 0;
        this.team = null;
    }

    public BasketballStats(long playerId, long gameId, int points, int assists, int jumps, String team) {
        this.playerId = playerId;
        this.gameId = gameId;
        this.points = points;
        this.assists = assists;
        this.jumps = jumps;
        this.team = team;
    }

    protected BasketballStats(Parcel in) {
        playerId = in.readLong();
        gameId = in.readLong();
        points = in.readInt();
        assists = in.readInt();
        jumps = in.readInt();
        team = in.readString();
    }

    public static final Creator<BasketballStats> CREATOR = new Creator<BasketballStats>() {
        @Override
        public BasketballStats createFromParcel(Parcel in) {
            return new BasketballStats(in);
        }

        @Override
        public BasketballStats[] newArray(int size) {
            return new BasketballStats[size];
        }
    };

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getJumps() {
        return jumps;
    }

    public void setJumps(int jumps) {
        this.jumps = jumps;
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
        parcel.writeInt(points);
        parcel.writeInt(assists);
        parcel.writeInt(jumps);
        parcel.writeString(team);
    }

}
