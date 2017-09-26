package com.example.rivios_.sportapp.activities.football;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.example.rivios_.sportapp.Constants;
import com.example.rivios_.sportapp.adapters.FootballPlayerStatsAdapter;
import com.example.rivios_.sportapp.GameDBHelper;
import com.example.rivios_.sportapp.R;
import com.example.rivios_.sportapp.data.Athlete;
import com.example.rivios_.sportapp.data.FootballPlayersStats;
import com.example.rivios_.sportapp.data.FootballStats;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class FootballPlayerList extends AppCompatActivity {
    GameDBHelper dbHelper = GameDBHelper.getInstance(this);
    ArrayList<FootballPlayersStats> footballPlayersStats = new ArrayList<>();

    ListView lvFootballPlayers;
    ArrayList<FootballPlayersStats> adaptedStats = new ArrayList<>();
    FootballPlayerStatsAdapter adapter = new FootballPlayerStatsAdapter(adaptedStats);
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lvFootballPlayers = (ListView) findViewById(R.id.lvStats);
        lvFootballPlayers.setBackground(getResources().getDrawable(R.color.football));

        editText = (EditText) findViewById(R.id.txtsearch);
        editText.setBackground(getResources().getDrawable(R.color.football));

        ArrayList<Athlete> athletes = dbHelper.getAthletes(Constants.DISCIPLINE_FOOTBALL);
        for (Athlete pl : athletes)
        {
            ArrayList<FootballStats> stats = dbHelper.getFoottballPlayerStats(pl.getId(), true);
            FootballPlayersStats ps = new FootballPlayersStats();
            ps.setAthlete(pl);
            ps.setGameCount(stats.size());
            FootballStats sum = new FootballStats(pl.getId(), 0, 0, 0, "");

            for (FootballStats st : stats)
            {
                sum.setGoals(sum.getGoals() + st.getGoals());
                sum.setAssists(sum.getAssists() + st.getAssists());
            }
            ps.setStats(sum);
            footballPlayersStats.add(ps);
        }
        Collections.sort(footballPlayersStats);
        adaptedStats.addAll(footballPlayersStats);
        lvFootballPlayers.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                String filter = charSequence.toString().toLowerCase();

                adaptedStats.clear();
                if (filter.equals(""))
                {
                    adaptedStats.addAll(footballPlayersStats);
                }
                else
                {
                    for (FootballPlayersStats ps : footballPlayersStats)
                    {
                        if (ps.getAthlete().getName().toLowerCase().contains(filter)
                                || ps.getAthlete().getNickname().toLowerCase().contains(filter))
                        {
                            adaptedStats.add(ps);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}