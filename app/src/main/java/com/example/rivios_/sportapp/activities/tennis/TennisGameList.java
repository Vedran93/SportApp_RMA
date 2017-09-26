package com.example.rivios_.sportapp.activities.tennis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.rivios_.sportapp.Constants;
import com.example.rivios_.sportapp.DeleteDialog;
import com.example.rivios_.sportapp.GameDBHelper;
import com.example.rivios_.sportapp.adapters.FootballGameStatsAdapter;
import com.example.rivios_.sportapp.R;
import com.example.rivios_.sportapp.adapters.TennisGameStatsAdapter;
import com.example.rivios_.sportapp.data.TennisGame;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TennisGameList extends AppCompatActivity implements DeleteDialog.DeleteDialogListener, AdapterView.OnItemLongClickListener{
    ArrayList<TennisGame> tennisGames;
    TennisGameStatsAdapter adapter;
    ListView lvGameStats;
    EditText editText;
    GameDBHelper dbHelper;

    private int deletePos = -1;
    private long deleteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        editText = (EditText) findViewById(R.id.txtsearch);
        lvGameStats = (ListView) findViewById(R.id.lvStats);
        editText.setBackground(getResources().getDrawable(R.color.tennis));
        lvGameStats.setBackground(getResources().getDrawable(R.color.tennis));

        dbHelper = GameDBHelper.getInstance(this);
        tennisGames = dbHelper.getTennisGames();

        adapter = new TennisGameStatsAdapter(tennisGames);
        lvGameStats.setAdapter(adapter);
        lvGameStats.setOnItemLongClickListener(this);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                ArrayList<TennisGame> games = dbHelper.getTennisGames();
                String filter = editText.getText().toString().toLowerCase();

                tennisGames.clear();
                if (filter.equals(""))
                {
                    tennisGames.addAll(games);
                }
                else
                {
                    for (TennisGame g : games)
                    {
                        if (g.getPlayer1().toLowerCase().contains(filter)
                                || g.getPlayer2().toLowerCase().contains(filter)
                                || sdf.format(g.getDatum()).contains(filter)
                                || (g.getPlayer1() + " VS " + g.getPlayer2()).toLowerCase().contains(filter)) {
                            tennisGames.add(g);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
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
            if (dbHelper.deleteTennisGame(deleteId)) {
                tennisGames.remove(deletePos);
                adapter.notifyDataSetChanged();
            }
        }
        else
        {
            Intent i = new Intent();
            i.setClass(this, TennisGameStatsActivity.class);
            i.putExtra(Constants.GAME, tennisGames.get(deletePos));
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        editText.setText("");
    }
}
