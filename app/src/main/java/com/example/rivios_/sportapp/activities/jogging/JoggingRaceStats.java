package com.example.rivios_.sportapp.activities.jogging;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rivios_.sportapp.Constants;
import com.example.rivios_.sportapp.DatePickerFragment;
import com.example.rivios_.sportapp.GameDBHelper;
import com.example.rivios_.sportapp.R;
import com.example.rivios_.sportapp.activities.MapsActivity;
import com.example.rivios_.sportapp.data.Athlete;
import com.example.rivios_.sportapp.data.JoggingRace;
import com.example.rivios_.sportapp.data.JoggingStats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class JoggingRaceStats extends AppCompatActivity {
    private JoggingRace newRace = null;
    private ArrayList<Athlete> runners = new ArrayList<>();
    private ArrayList<JoggingStats> stats = new ArrayList<>();
    private boolean update;

    EditText etSart;
    EditText etFinish;
    EditText etRaceDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogging);

        etSart = (EditText) findViewById(R.id.start);
        etFinish = (EditText) findViewById(R.id.finish);
        etRaceDate = (EditText) findViewById(R.id.datum);

        Intent i = getIntent();
        if (i.hasExtra(Constants.GAME))
        {
            update = true;
            newRace = i.getParcelableExtra(Constants.GAME);

            etSart.setText(newRace.getStart());
            etFinish.setText(newRace.getFinish());
            etRaceDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(newRace.getDate()));

            findViewById(R.id.addRunner).setVisibility(View.INVISIBLE);
            findViewById(R.id.archive).setVisibility(View.INVISIBLE);
        }
        else
        {
            update = false;
        }
    }

    public void runnersActivity(View v) {
        if (newRace == null)
        {
            Toast.makeText(this, "Prvo odredite rutu.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent();
        i.setClass(this, JoggingRunners.class);
        startActivityForResult(i, Constants.PLAYER_RESULT);
    }

    public void addRoute(View v) {
        Intent i = new Intent();
        i.setClass(this, MapsActivity.class);
        startActivityForResult(i, Constants.ROUTE_RESULT);
    }

    public void saveToDB(View v) {
        if (newRace == null)
        {
            Toast.makeText(this, "Nije postavljena utrka", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try
        {
            newRace.setDate(sdf.parse(etRaceDate.getText().toString()));
        }
        catch(ParseException e)
        {
            Toast.makeText(this, "Nije upisan datum.", Toast.LENGTH_SHORT).show();
            return;
        }

        GameDBHelper dbHelper = GameDBHelper.getInstance(this);
        Log.d("PERO", "Spremljena statistika: datum " + sdf.format(newRace.getDate()));
        Log.d("PERO", "Spremljena statistika: duljina " + Integer.toString(newRace.getDistance()));

        if (!update) {
            dbHelper.addJoggingRace(newRace);
        }
        else {
            dbHelper.updateJoggingRace(newRace);
            finish();
        }

        long rid = dbHelper.getJoggingRaceID(newRace.getStart(), newRace.getFinish(), newRace.getDate());
        newRace.setRaceId(rid);



        if (runners.size() > 0)
        {
            for (Athlete runner : runners)
            {
                if (dbHelper.getAthleteID(runner.getNickname()) == -1) {
                    dbHelper.addAthlete(runner);
                }
                long pid = dbHelper.getAthleteID(runner.getNickname());
                runner.setId(pid);
                Log.d("PERO", "Spremljen igrač: " + pid);
            }
        }

        if (stats.size() > 0) {
            for (int i = 0; i < stats.size(); i++) {
                stats.get(i).setRaceId(newRace.getRaceId());
                stats.get(i).setRunnerId(runners.get(i).getId());

                dbHelper.addJoggingStats(stats.get(i));

                Log.d("PERO", "Spremljena statistika: utakmica "
                        + stats.get(i).getRaceId()
                        + ", igrač " + stats.get(i).getRunnerId());
            }
        }

        etSart.setText("");
        etFinish.setText("");
        etRaceDate.setText("");
        Toast.makeText(this, "Utrka uspješno spremljena.", Toast.LENGTH_SHORT).show();

        newRace = null;
        runners.clear();
        stats.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.ROUTE_RESULT)
        {
            if (resultCode == RESULT_OK)
            {
                Log.d("PERO", "RESULT_OK");
                newRace = new JoggingRace();

                newRace.setStart(data.getStringExtra(Constants.START_TAG));
                newRace.setFinish(data.getStringExtra(Constants.FINISH_TAG));
                newRace.setDistance((int)data.getLongExtra(Constants.DIST_TAG, 0));
                newRace.setEncodedRoute(data.getStringExtra(Constants.ROUTE_TAG));

                Log.d("PERO", "Dist: " + newRace.getDistance());

                etSart.setText(newRace.getStart());
                etFinish.setText(newRace.getFinish());
            }
        }
        else if (requestCode == Constants.PLAYER_RESULT)
        {
            if (resultCode == RESULT_OK)
            {
                Log.d("PERO", "RESULT_OK");
                runners = data.getParcelableArrayListExtra(Constants.ATHLETE_TAG);
                stats = data.getParcelableArrayListExtra(Constants.STATS_TAG);

                newRace.setWinner(runners.get(0).getNickname());
            }
        }
    }

    public void setDate (View v)
    {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), Constants.DATE_PICKER_TAG);
    }

    public void openRaces(View v)
    {
        Intent i = new Intent();
        i.setClass(this, JoggingRaceList.class);
        startActivity(i);
    }
}
