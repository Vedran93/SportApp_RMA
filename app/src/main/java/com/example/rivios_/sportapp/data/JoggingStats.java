package com.example.rivios_.sportapp.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class JoggingStats implements Parcelable{
    private long raceId;
    private long runnerId;
    private long time;
    private int place;

    public JoggingStats() {
        this.raceId = 0;
        this.runnerId = 0;
        this.time = 0;
        this.place = 0;
    }

    public JoggingStats(long raceId, long runnerId, long time, int place) {
        this.raceId = raceId;
        this.runnerId = runnerId;
        this.time = time;
        this.place = place;
    }

    protected JoggingStats(Parcel in) {
        raceId = in.readLong();
        runnerId = in.readLong();
        time = in.readLong();
        place = in.readInt();
    }

    public static final Creator<JoggingStats> CREATOR = new Creator<JoggingStats>() {
        @Override
        public JoggingStats createFromParcel(Parcel in) {
            return new JoggingStats(in);
        }

        @Override
        public JoggingStats[] newArray(int size) {
            return new JoggingStats[size];
        }
    };

    public long getRaceId() {
        return raceId;
    }

    public void setRaceId(long raceId) {
        this.raceId = raceId;
    }

    public long getRunnerId() {
        return runnerId;
    }

    public void setRunnerId(long runnerId) {
        this.runnerId = runnerId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(raceId);
        parcel.writeLong(runnerId);
        parcel.writeLong(time);
        parcel.writeInt(place);
    }
}
