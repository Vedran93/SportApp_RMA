package com.example.rivios_.sportapp.activities.jogging;

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
import com.example.rivios_.sportapp.R;
import com.example.rivios_.sportapp.adapters.JoggingRaceAdapter;
import com.example.rivios_.sportapp.data.JoggingRace;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class JoggingRaceList extends AppCompatActivity implements AdapterView.OnItemLongClickListener, DeleteDialog.DeleteDialogListener{
    private ArrayList<JoggingRace> races;

    ListView lvRaces;
    JoggingRaceAdapter adapter;
    EditText editText;
    GameDBHelper dbHelper = GameDBHelper.getInstance(this);

    int deletePos;
    long deleteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lvRaces = (ListView) findViewById(R.id.lvStats);
        lvRaces.setBackground(getResources().getDrawable(R.color.jogging));

        editText = (EditText) findViewById(R.id.txtsearch);
        editText.setBackground(getResources().getDrawable(R.color.jogging));

        races = dbHelper.getJoggingRaces();
        Log.d("PERO", "Num races " + races.size());
        adapter = new JoggingRaceAdapter(races);
        lvRaces.setAdapter(adapter);

        lvRaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(), JoggingRaceDetails.class);
                i.putExtra("RACE", races.get(position));
                startActivity(i);
            }
        });
        lvRaces.setOnItemLongClickListener(this);

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
                ArrayList<JoggingRace> games = dbHelper.getJoggingRaces();
                String filter = editText.getText().toString().toLowerCase();

                races.clear();
                if (filter.equals(""))
                {
                    races.addAll(games);
                }
                else
                {
                    for (JoggingRace g : games)
                    {
                        if (g.getStart().toLowerCase().contains(filter)
                                || g.getFinish().toLowerCase().contains(filter)
                                || sdf.format(g.getDate()).contains(filter)) {
                            races.add(g);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        DeleteDialog delDialog =  new DeleteDialog();
        delDialog.setListener(this);
        deletePos = position;
        deleteId = id;
        delDialog.show(getFragmentManager(), Constants.DELETE_DIALOG_TAG);
        return true;
    }

    @Override
    public void onDialogClick(boolean yes) {
        if (yes) {
            if (dbHelper.deleteBJoggingRace(deleteId)) {
                races.remove(deletePos);
                adapter.notifyDataSetChanged();
            }
        }
        else
        {
            Intent i = new Intent();
            i.setClass(this, JoggingRaceStats.class);
            i.putExtra(Constants.GAME, races.get(deletePos));
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        editText.setText("");
    }
}
