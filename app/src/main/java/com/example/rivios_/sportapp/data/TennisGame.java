package com.example.rivios_.sportapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class TennisGame implements Parcelable {


    private long id;
    private long player1Id;
    private long player2Id;
    private String player1;
    private String player2;
    private int result1;
    private int result2;
    private Date datum;
    private String winner;
    private String set1;
    private String set2;
    private String set3;
    private String set4;
    private String set5;

    public TennisGame() {
        this.id = 0;
        this.player1 = null;
        this.player2 = null;
        this.result1 = 0;
        this.result2 = 0;
        this.datum = null;
        this.winner = null;
        this.set1 = null;
        this.set2 = null;
        this.set3 = null;
        this.set4 = null;
        this.set5 = null;
    }

    public TennisGame(long id, long player1Id, long player2Id, String player1, String player2, int result1, int result2, Date datum, String winner, String set1, String set2, String set3, String set4, String set5) {
        this.id = id;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.player1 = player1;
        this.player2 = player2;
        this.result1 = result1;
        this.result2 = result2;
        this.datum = datum;
        this.winner = winner;
        this.set1 = set1;
        this.set2 = set2;
        this.set3 = set3;
        this.set4 = set4;
        this.set5 = set5;
    }

    protected TennisGame(Parcel in) {
        id = in.readLong();
        player1Id = in.readLong();
        player2Id = in.readLong();
        player1 = in.readString();
        player2 = in.readString();
        result1 = in.readInt();
        result2 = in.readInt();
        datum = new Date(in.readLong());
        winner = in.readString();
        set1 = in.readString();
        set2 = in.readString();
        set3 = in.readString();
        set4 = in.readString();
        set5 = in.readString();
    }

    public static final Creator<TennisGame> CREATOR = new Creator<TennisGame>() {
        @Override
        public TennisGame createFromParcel(Parcel in) {
            return new TennisGame(in);
        }

        @Override
        public TennisGame[] newArray(int size) {
            return new TennisGame[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public int getResult1() {
        return result1;
    }

    public void setResult1(int result1) {
        this.result1 = result1;
    }

    public int getResult2() {
        return result2;
    }

    public void setResult2(int result2) {
        this.result2 = result2;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getSet1() {
        return set1;
    }

    public void setSet1(String set1) {
        this.set1 = set1;
    }

    public String getSet2() {
        return set2;
    }

    public void setSet2(String set2) {
        this.set2 = set2;
    }

    public String getSet3() {
        return set3;
    }

    public void setSet3(String set3) {
        this.set3 = set3;
    }

    public String getSet4() {
        return set4;
    }

    public void setSet4(String set4) {
        this.set4 = set4;
    }

    public String getSet5() {
        return set5;
    }

    public void setSet5(String set5) {
        this.set5 = set5;
    }

    public long getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(long player1Id) {
        this.player1Id = player1Id;
    }

    public long getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(long player2Id) {
        this.player2Id = player2Id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(player1Id);
        parcel.writeLong(player2Id);
        parcel.writeString(player1);
        parcel.writeString(player2);
        parcel.writeInt(result1);
        parcel.writeInt(result2);
        parcel.writeLong(datum.getTime());
        parcel.writeString(winner);
        parcel.writeString(set1);
        parcel.writeString(set2);
        parcel.writeString(set3);
        parcel.writeString(set4);
        parcel.writeString(set5);
    }
}
