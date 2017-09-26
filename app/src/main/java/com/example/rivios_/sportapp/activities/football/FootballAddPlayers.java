package com.example.rivios_.sportapp.activities.football;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rivios_.sportapp.Constants;
import com.example.rivios_.sportapp.GameDBHelper;
import com.example.rivios_.sportapp.R;
import com.example.rivios_.sportapp.data.Athlete;
import com.example.rivios_.sportapp.data.FootballPlayersStats;
import com.example.rivios_.sportapp.data.FootballStats;

import java.util.ArrayList;

public class FootballAddPlayers extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList<FootballPlayersStats> newPlayers = new ArrayList<FootballPlayersStats>();

    ArrayList<Athlete> existingPlayers;
    ArrayList<String> nicknames;

    EditText etIme;
    EditText etNadimak;
    EditText etGolovi;
    EditText etAsistencije;
    Spinner spEkipe;
    Spinner spPlayers;
    ListView lvPlayers;

    ArrayAdapter<FootballPlayersStats> plAdapter;
    ArrayAdapter<String> spTeamsAdapter;
    ArrayAdapter<String> spPlayersAdapter;

    GameDBHelper dbHelper;

    boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_players);
        Intent i = getIntent();
        dbHelper = GameDBHelper.getInstance(this);

        if (i.hasExtra(Constants.ATHLETE_TAG) && i.hasExtra(Constants.STATS_TAG))
        {
            update = true;
        }
        else
        {
            update = false;
        }

        etIme = (EditText) findViewById(R.id.playername);
        etNadimak = (EditText) findViewById(R.id.playernickname);
        etGolovi = (EditText) findViewById(R.id.goals);
        etAsistencije = (EditText) findViewById(R.id.assists);
        spEkipe = (Spinner) findViewById(R.id.spinner);
        spPlayers = (Spinner) findViewById(R.id.spinner_players);
        lvPlayers = (ListView) findViewById(R.id.listaIgraca);

        if (!update) {
            plAdapter = new ArrayAdapter<FootballPlayersStats>(this, android.R.layout.simple_list_item_1, newPlayers);
            lvPlayers.setAdapter(plAdapter);
            lvPlayers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    newPlayers.remove(i);
                    plAdapter.notifyDataSetChanged();
                    return true;
                }
            });

            ArrayList<String> teams = new ArrayList<String>();
            teams.add("Odaberite ekipu");
            teams.add(i.getStringExtra(Constants.TEAM1_TAG));
            teams.add(i.getStringExtra(Constants.TEAM2_TAG));
            spTeamsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teams);
            spEkipe.setAdapter(spTeamsAdapter);
            spEkipe.setSelection(0);
            spEkipe.setOnItemSelectedListener(this);

            existingPlayers = dbHelper.getAthletes(Constants.DISCIPLINE_FOOTBALL);
            nicknames = new ArrayList<String>();
            nicknames.add("Novi sportaš");
            for (Athlete a : existingPlayers) {
                nicknames.add(a.getNickname());
            }
            spPlayersAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nicknames);
            spPlayers.setAdapter(spPlayersAdapter);
            spPlayers.setSelection(0);
            spPlayers.setOnItemSelectedListener(this);
        }
        else
        {
            newPlayers.add(new FootballPlayersStats((Athlete) i.getParcelableExtra(Constants.ATHLETE_TAG),
                    (FootballStats) i.getParcelableExtra(Constants.STATS_TAG), 0));

            etIme.setText(newPlayers.get(0).getAthlete().getName());
            etNadimak.setText(newPlayers.get(0).getAthlete().getNickname());
            etGolovi.setText(Integer.toString(newPlayers.get(0).getStats().getGoals()));
            etAsistencije.setText(Integer.toString(newPlayers.get(0).getStats().getAssists()));

            spEkipe.setVisibility(View.INVISIBLE);
            spPlayers.setVisibility(View.INVISIBLE);
            lvPlayers.setVisibility(View.INVISIBLE);
            findViewById(R.id.dodajigraca).setVisibility(View.INVISIBLE);
        }
    }

    public void addFootballPlayer (View v)
    {
        String ime = etIme.getText().toString();
        String nadimak = etNadimak.getText().toString();

        int golovi;
        int asistencije;
        String ekipa;

        try {
            golovi = Integer.parseInt(etGolovi.getText().toString());
            asistencije = Integer.parseInt(etAsistencije.getText().toString());
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, "Nepravilni podaci.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ime.equals("") || nadimak.equals(""))
        {
            Toast.makeText(this, "Nije upisano ime ili nadimak.", Toast.LENGTH_SHORT).show();
            return;
        }
        for (FootballPlayersStats s : newPlayers)
        {
            if (s.getAthlete().getNickname().equals(nadimak))
            {
                Toast.makeText(this, "Igrač već dodan.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        ekipa = spEkipe.getSelectedItem().toString();
        if (ekipa.equals("Odaberite ekipu"))
        {
            Toast.makeText(this, "Ekipa nije odabrana.", Toast.LENGTH_SHORT).show();
            return;
        }

        newPlayers.add(new FootballPlayersStats(new Athlete(0, ime, nadimak, Constants.DISCIPLINE_FOOTBALL), new FootballStats(0, 0, golovi, asistencije, ekipa), 1));
        plAdapter.notifyDataSetChanged();

        etIme.setText("");
        etNadimak.setText("");
        etGolovi.setText("");
        etAsistencije.setText("");
        spEkipe.setSelection(0);
    }

    public void saveFootballPlayer (View v)
    {
        if (!update) {
            ArrayList<Athlete> igraci = new ArrayList<Athlete>();
            ArrayList<FootballStats> statistike = new ArrayList<FootballStats>();

            for (FootballPlayersStats p : newPlayers) {
                igraci.add(p.getAthlete());
                statistike.add(p.getStats());
                Log.d("PERO", "Dodan igrac " + p.getAthlete().getNickname());
            }

            Intent i = new Intent();
            i.putParcelableArrayListExtra(Constants.PLAYERS, igraci);
            i.putParcelableArrayListExtra(Constants.STATS, statistike);
            setResult(RESULT_OK, i);
        }
        else
        {
            newPlayers.get(0).getAthlete().setName(etIme.getText().toString());
            newPlayers.get(0).getAthlete().setNickname(etNadimak.getText().toString());
            try {
                newPlayers.get(0).getStats().setGoals(Integer.parseInt(etGolovi.getText().toString()));
                newPlayers.get(0).getStats().setAssists(Integer.parseInt(etAsistencije.getText().toString()));
            }
            catch (NumberFormatException e)
            {
                Toast.makeText(this, "Neispravan upis.", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.updateAthlete(newPlayers.get(0).getAthlete());
            dbHelper.updateFootballStats(newPlayers.get(0).getStats());
        }

        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId())
        {
            case R.id.spinner:
                teamSelected();
                break;
            case R.id.spinner_players:
                playerSelected();
                break;
            default:
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void teamSelected()
    {
        nicknames.clear();
        nicknames.add("Odaberi igrača");
        if (!spEkipe.getSelectedItem().toString().equals("Odaberite ekipu")) {
            for (Athlete a : existingPlayers) {
                ArrayList<FootballStats> stats = dbHelper.getFoottballPlayerStats(a.getId(), true);
                for (FootballStats s : stats) {
                    if (s.getTeam().equals(spEkipe.getSelectedItem().toString())) {
                        nicknames.add(a.getNickname());
                        break;
                    }
                }
            }
        }
        else
        {
            for (Athlete a : existingPlayers)
            {
                nicknames.add(a.getNickname());
            }
        }
        spPlayersAdapter.notifyDataSetChanged();
        spPlayers.setSelection(0);
    }

    public void playerSelected()
    {
        String nick = spPlayers.getSelectedItem().toString();
        if (!nick.equals("Odaberi igrača"))
        {
            Athlete a = dbHelper.getAthlete(dbHelper.getAthleteID(nick));
            etIme.setText(a.getName());
            etNadimak.setText(a.getNickname());
        }
        else
        {
            etIme.setText("");
            etNadimak.setText("");
        }
    }
}
