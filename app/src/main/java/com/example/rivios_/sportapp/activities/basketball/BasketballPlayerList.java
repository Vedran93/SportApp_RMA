package com.example.rivios_.sportapp.activities.basketball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.example.rivios_.sportapp.Constants;
import com.example.rivios_.sportapp.GameDBHelper;
import com.example.rivios_.sportapp.adapters.BasketballPlayerStatsAdapter;
import com.example.rivios_.sportapp.R;
import com.example.rivios_.sportapp.data.Athlete;
import com.example.rivios_.sportapp.data.BasketballPlayerStats;
import com.example.rivios_.sportapp.data.BasketballStats;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class BasketballPlayerList extends AppCompatActivity{
    GameDBHelper dbHelper = GameDBHelper.getInstance(this);
    ArrayList<BasketballPlayerStats> basketballPlayerStats = new ArrayList<>();

    ListView lvBasketballPlayers;
    ArrayList<BasketballPlayerStats> adaptedStats = new ArrayList<>();
    BasketballPlayerStatsAdapter adapter = new BasketballPlayerStatsAdapter(adaptedStats);
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lvBasketballPlayers = (ListView) findViewById(R.id.lvStats);
        lvBasketballPlayers.setBackground(getResources().getDrawable(R.color.basketball));

        editText = (EditText) findViewById(R.id.txtsearch);
        editText.setBackground(getResources().getDrawable(R.color.basketball));

        ArrayList<Athlete> athletes = dbHelper.getAthletes(Constants.DISCIPLINE_BASKETBALL);
        for (Athlete pl : athletes)
        {
            ArrayList<BasketballStats> stats = dbHelper.getBasketballPlayerStats(pl.getId(), true);
            BasketballPlayerStats ps = new BasketballPlayerStats();
            ps.setAthlete(pl);
            ps.setGameCount(stats.size());
            BasketballStats sum = new BasketballStats(pl.getId(), (long) 0, 0, 0, 0, "");

            for (BasketballStats st : stats)
            {
                sum.setPoints(sum.getPoints() + st.getPoints());
                sum.setAssists(sum.getAssists() + st.getAssists());
                sum.setJumps(sum.getJumps() + st.getJumps());
            }
            ps.setStats(sum);
            basketballPlayerStats.add(ps);
        }
        Collections.sort(basketballPlayerStats);

        adaptedStats.addAll(basketballPlayerStats);
        lvBasketballPlayers.setAdapter(adapter);

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

                    adaptedStats.addAll(basketballPlayerStats);
                }
                else
                {
                    for (BasketballPlayerStats ps : basketballPlayerStats)
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
