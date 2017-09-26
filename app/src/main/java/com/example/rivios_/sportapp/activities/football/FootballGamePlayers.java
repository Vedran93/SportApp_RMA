package com.example.rivios_.sportapp.activities.football;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.rivios_.sportapp.Constants;
import com.example.rivios_.sportapp.DeleteDialog;
import com.example.rivios_.sportapp.adapters.FootballPlayerStatsAdapter;
import com.example.rivios_.sportapp.GameDBHelper;
import com.example.rivios_.sportapp.R;
import com.example.rivios_.sportapp.data.FootballPlayersStats;
import com.example.rivios_.sportapp.data.FootballStats;

import java.util.ArrayList;

public class FootballGamePlayers extends AppCompatActivity implements AdapterView.OnItemLongClickListener, DeleteDialog.DeleteDialogListener {
    long gameId;
    ArrayList<FootballPlayersStats> playerStats = new ArrayList<>();
    FootballPlayerStatsAdapter adapter;
    ListView lvplayerStats;
    EditText editText;
    GameDBHelper dbHelper = GameDBHelper.getInstance(this);
    private int deletePos = -1;
    private long deleteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_game_players);

        //fillDBPlayers();

        Intent i = getIntent();
        gameId = i.getLongExtra("GAME_ID", 0);

        Log.d("PERO", "Arhiva igraca, utakmica: " + gameId);

        lvplayerStats = (ListView) findViewById(R.id.lvStats);
        lvplayerStats.setBackground(getResources().getDrawable(R.color.football));

        ArrayList<FootballStats> stats = dbHelper.getFoottballPlayerStats(gameId, false);

        for (FootballStats st : stats)
        {
            playerStats.add(new FootballPlayersStats(dbHelper.getAthlete(st.getPlayerId()), st, 0));
        }

        //basketballPlayerStats = dbHelper.getBasketballPlayerStats(gameId, false);

        adapter = new FootballPlayerStatsAdapter(playerStats);

        lvplayerStats.setAdapter(adapter);
        lvplayerStats.setOnItemLongClickListener(this);
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
        DeleteDialog delDialog =  new DeleteDialog();
        delDialog.setListener(this);
        deletePos = pos;
        deleteId = id;
        delDialog.show(getFragmentManager(), Constants.DELETE_DIALOG_TAG);
        return true;
    }

    @Override
    public void onDialogClick(boolean yes) {
        if (yes) {
            if (dbHelper.deleteFootballGamePlayerStats(playerStats.get(deletePos).getStats().getGameId(), playerStats.get(deletePos).getStats().getPlayerId())) {
                playerStats.remove(deletePos);
                adapter.notifyDataSetChanged();
            }
        }
        else
        {
            Intent i = new Intent();
            i.setClass(this, FootballAddPlayers.class);
            i.putExtra(Constants.ATHLETE_TAG, playerStats.get(deletePos).getAthlete());
            i.putExtra(Constants.STATS_TAG, playerStats.get(deletePos).getStats());
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        playerStats.clear();
        ArrayList<FootballStats> stats = dbHelper.getFoottballPlayerStats(gameId, false);

        for (FootballStats st : stats)
        {
            playerStats.add(new FootballPlayersStats(dbHelper.getAthlete(st.getPlayerId()), st, 0));
        }
        adapter.notifyDataSetChanged();
    }
}
