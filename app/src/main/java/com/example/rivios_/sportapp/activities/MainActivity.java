package com.example.rivios_.sportapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.rivios_.sportapp.R;
import com.example.rivios_.sportapp.activities.basketball.BasketballGameStatsActivity;
import com.example.rivios_.sportapp.activities.football.FootballGameStatsActivity;
import com.example.rivios_.sportapp.activities.jogging.JoggingRaceStats;
import com.example.rivios_.sportapp.activities.tennis.TennisGameStatsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void basketballgamestats(View v) {
        Intent i = new Intent();
        i.setClass(this, BasketballGameStatsActivity.class);
        startActivity(i);
    }
    public void footballgamestats(View v) {
        Intent i = new Intent();
        i.setClass(this, FootballGameStatsActivity.class);
        startActivity(i);
    }


    public void tennisActivity(View v) {
        Intent i = new Intent();
        i.setClass(this, TennisGameStatsActivity.class);
        startActivity(i);
    }

    public void joggingRace(View v) {
        Intent i = new Intent();
        i.setClass(this, JoggingRaceStats.class);
        startActivity(i);
    }
}
