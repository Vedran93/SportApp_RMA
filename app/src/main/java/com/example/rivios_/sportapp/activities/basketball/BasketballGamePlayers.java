package com.example.rivios_.sportapp.activities.basketball;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.rivios_.sportapp.Constants;
import com.example.rivios_.sportapp.DeleteDialog;
import com.example.rivios_.sportapp.GameDBHelper;
import com.example.rivios_.sportapp.adapters.BasketballPlayerStatsAdapter;
import com.example.rivios_.sportapp.R;
import com.example.rivios_.sportapp.data.BasketballPlayerStats;
import com.example.rivios_.sportapp.data.BasketballStats;

import java.util.ArrayList;

public class BasketballGamePlayers extends AppCompatActivity implements AdapterView.OnItemLongClickListener, DeleteDialog.DeleteDialogListener {
    long gameId;
    ArrayList<BasketballPlayerStats> playerStats = new ArrayList<>();
    BasketballPlayerStatsAdapter adapter;
    ListView lvplayerStats;
    GameDBHelper dbHelper = GameDBHelper.getInstance(this);
    private int deletePos = -1;
    private long deleteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basketball_archive);

        //fillDBPlayers();

        Intent i = getIntent();
        gameId = i.getLongExtra("GAME_ID", 0);


        lvplayerStats = (ListView) findViewById(R.id.lvPlayerStats);

        ArrayList<BasketballStats> stats = dbHelper.getBasketballPlayerStats(gameId, false);

        for (BasketballStats st : stats)
        {
            playerStats.add(new BasketballPlayerStats(dbHelper.getAthlete(st.getPlayerId()), st, 0));
        }

        //basketballPlayerStats = dbHelper.getBasketballPlayerStats(gameId, false);

        adapter = new BasketballPlayerStatsAdapter(playerStats);

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
            if (dbHelper.deleteBasketballGamePlayerStats(playerStats.get(deletePos).getStats().getGameId(), playerStats.get(deletePos).getStats().getPlayerId())) {
                playerStats.remove(deletePos);
                adapter.notifyDataSetChanged();
            }
        }
        else
        {
            Intent i = new Intent();
            i.setClass(this, BasketballAddPlayers.class);
            i.putExtra(Constants.ATHLETE_TAG, playerStats.get(deletePos).getAthlete());
            i.putExtra(Constants.STATS_TAG, playerStats.get(deletePos).getStats());
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        playerStats.clear();
        ArrayList<BasketballStats> stats = dbHelper.getBasketballPlayerStats(gameId, false);

        for (BasketballStats st : stats)
        {
            playerStats.add(new BasketballPlayerStats(dbHelper.getAthlete(st.getPlayerId()), st, 0));
        }
        adapter.notifyDataSetChanged();
    }
}
