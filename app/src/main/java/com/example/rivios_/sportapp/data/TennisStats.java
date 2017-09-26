package com.example.rivios_.sportapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class TennisStats implements Parcelable {

    private long gameId;
    private int playerId;
    private int setsWon;

    public TennisStats() {
        this.gameId = 0;
        this.playerId = 0;
        this.setsWon = 0;
    }

    public TennisStats(long gameId, int result1, int result2) {
        this.gameId = gameId;
        this.playerId = result1;
        this.setsWon = result2;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int result1) {
        this.playerId = result1;
    }

    public int getSetsWon() {
        return setsWon;
    }

    public void setSetsWon(int result2) {
        this.setsWon = result2;
    }


    protected TennisStats(Parcel in) {

        gameId = in.readLong();
        playerId = in.readInt();
        setsWon = in.readInt();

    }

    public static final Creator<TennisStats> CREATOR = new Creator<TennisStats>() {
        @Override
        public TennisStats createFromParcel(Parcel in) {
            return new TennisStats(in);
        }

        @Override
        public TennisStats[] newArray(int size) {
            return new TennisStats[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
