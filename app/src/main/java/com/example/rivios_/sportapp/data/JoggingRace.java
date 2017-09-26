package com.example.rivios_.sportapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class JoggingRace implements Parcelable{
    private long raceId;
    private String start;
    private String finish;
    private Date date;
    private int distance;
    private String winner;
    private String encodedRoute;

    public JoggingRace() {
        this.raceId = 0;
        this.start = null;
        this.finish = null;
        this.distance = 0;
        this.winner = null;
        this.encodedRoute = null;
    }

    public JoggingRace(long raceId, String start, String finish, Date date, String winner, int distance, String route) {
        this.raceId = raceId;
        this.start = start;
        this.finish = finish;
        this.date = date;
        this.distance = distance;
        this.winner = winner;
        this.encodedRoute = route;
    }

    protected JoggingRace(Parcel in) {
        raceId = in.readLong();
        start = in.readString();
        finish = in.readString();
        date = new Date(in.readLong());
        distance = in.readInt();
        winner = in.readString();
        encodedRoute = in.readString();
    }

    public static final Creator<JoggingRace> CREATOR = new Creator<JoggingRace>() {
        @Override
        public JoggingRace createFromParcel(Parcel in) {
            return new JoggingRace(in);
        }

        @Override
        public JoggingRace[] newArray(int size) {
            return new JoggingRace[size];
        }
    };

    public long getRaceId() {
        return raceId;
    }

    public String getStart() {
        return start;
    }

    public String getFinish() {
        return finish;
    }

    public int getDistance() {
        return distance;
    }

    public void setRaceId(long raceId) {
        this.raceId = raceId;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getEncodedRoute() {
        return encodedRoute;
    }

    public void setEncodedRoute(String encodedRoute) {
        this.encodedRoute = encodedRoute;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(raceId);
        dest.writeString(start);
        dest.writeString(finish);
        dest.writeLong(date.getTime());
        dest.writeInt(distance);
        dest.writeString(winner);
        dest.writeString(encodedRoute);
    }
}
