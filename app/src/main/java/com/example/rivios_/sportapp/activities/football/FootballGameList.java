package com.example.rivios_.sportapp.activities.football;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rivios_.sportapp.Constants;
import com.example.rivios_.sportapp.DeleteDialog;
import com.example.rivios_.sportapp.GameDBHelper;
import com.example.rivios_.sportapp.adapters.FootballGameStatsAdapter;
import com.example.rivios_.sportapp.R;
import com.example.rivios_.sportapp.data.FootballGame;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FootballGameList extends AppCompatActivity implements DeleteDialog.DeleteDialogListener, AdapterView.OnItemLongClickListener{
    ArrayList<FootballGame> footballGames;
    FootballGameStatsAdapter adapter;
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
        editText.setBackground(getResources().getDrawable(R.color.football));

        lvGameStats = (ListView) findViewById(R.id.lvStats);
        lvGameStats.setBackground(getResources().getDrawable(R.color.football));

        dbHelper = GameDBHelper.getInstance(this);

        footballGames = dbHelper.getFootballGames();
        adapter = new FootballGameStatsAdapter(footballGames);
        lvGameStats.setAdapter(adapter);
        lvGameStats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(), FootballGamePlayers.class);
                i.putExtra("GAME_ID", id);
                startActivity(i);
            }
        });
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
                ArrayList<FootballGame> games = dbHelper.getFootballGames();
                String filter = editText.getText().toString().toLowerCase();

                footballGames.clear();
                if (filter.equals(""))
                {
                    footballGames.addAll(games);
                }
                else
                {
                    for (FootballGame g : games)
                    {
                        if (g.getTeam1().toLowerCase().contains(filter)
                                || g.getTeam2().toLowerCase().contains(filter)
                                || sdf.format(g.getDatum()).contains(filter)
                                || (g.getTeam1() + " VS " + g.getTeam2()).toLowerCase().contains(filter)) {
                            footballGames.add(g);
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
            if (dbHelper.deleteFoottballGame(deleteId)) {
                footballGames.remove(deletePos);
                adapter.notifyDataSetChanged();
            }
        }
        else
        {
            Intent i = new Intent();
            i.setClass(this, FootballGameStatsActivity.class);
            i.putExtra(Constants.GAME, footballGames.get(deletePos));
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        editText.setText("");
    }
}
