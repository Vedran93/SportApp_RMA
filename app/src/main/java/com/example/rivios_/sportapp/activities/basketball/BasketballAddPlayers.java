package com.example.rivios_.sportapp.activities.basketball;

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
import com.example.rivios_.sportapp.data.BasketballPlayerStats;
import com.example.rivios_.sportapp.data.BasketballStats;

import java.util.ArrayList;

public class BasketballAddPlayers extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList<BasketballPlayerStats> pst = new ArrayList<BasketballPlayerStats>();

    ArrayList<Athlete> existingPlayers;
    ArrayList<String> nicknames;

    EditText etIme;
    EditText etNadimak;
    EditText etPoeni;
    EditText etAsistencije;
    EditText etSkokovi;
    Spinner spEkipe;
    Spinner spPlayers;
    ListView lvPlayers;

    ArrayAdapter<BasketballPlayerStats> plAdapter;
    ArrayAdapter<String> spAdapter;
    ArrayAdapter<String> spPlayersAdapter;

    GameDBHelper dbHelper;

    boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basketball_players);
        Intent i = getIntent();
        if (i.hasExtra(Constants.ATHLETE_TAG) && i.hasExtra(Constants.STATS_TAG))
        {
            update = true;
        }
        else
        {
            update = false;
        }

        dbHelper = GameDBHelper.getInstance(this);

        etIme = (EditText) findViewById(R.id.imekosarkasa);
        etNadimak = (EditText) findViewById(R.id.nadimakkosarkasa);
        etPoeni = (EditText) findViewById(R.id.poeni);
        etAsistencije = (EditText) findViewById(R.id.asistencije);
        etSkokovi = (EditText) findViewById(R.id.skokovi);
        spEkipe = (Spinner) findViewById(R.id.spinner);
        spPlayers = (Spinner) findViewById(R.id.spinner2);
        lvPlayers = (ListView) findViewById(R.id.listaIgraca);

        if (!update) {
            plAdapter = new ArrayAdapter<BasketballPlayerStats>(this, android.R.layout.simple_list_item_1, pst);
            lvPlayers.setAdapter(plAdapter);
            lvPlayers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    pst.remove(i);
                    plAdapter.notifyDataSetChanged();
                    return true;
                }
            });

            ArrayList<String> teams = new ArrayList<String>();
            teams.add("Odaberite ekipu");
            teams.add(i.getStringExtra(Constants.TEAM1_TAG));
            teams.add(i.getStringExtra(Constants.TEAM2_TAG));
            spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teams);
            spEkipe.setAdapter(spAdapter);
            spEkipe.setSelection(0);
            spEkipe.setOnItemSelectedListener(this);

            existingPlayers = dbHelper.getAthletes(Constants.DISCIPLINE_BASKETBALL);
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
            pst.add(new BasketballPlayerStats((Athlete) i.getParcelableExtra(Constants.ATHLETE_TAG),
                    (BasketballStats) i.getParcelableExtra(Constants.STATS_TAG), 0));

            etIme.setText(pst.get(0).getAthlete().getName());
            etNadimak.setText(pst.get(0).getAthlete().getNickname());
            etPoeni.setText(Integer.toString(pst.get(0).getStats().getPoints()));
            etAsistencije.setText(Integer.toString(pst.get(0).getStats().getAssists()));
            etSkokovi.setText(Integer.toString(pst.get(0).getStats().getJumps()));

            spEkipe.setVisibility(View.INVISIBLE);
            spPlayers.setVisibility(View.INVISIBLE);
            lvPlayers.setVisibility(View.INVISIBLE);
            findViewById(R.id.dodajigraca).setVisibility(View.INVISIBLE);
        }
    }

    public void dodajIgraca (View v)
    {
        String ime = etIme.getText().toString();
        String nadimak = etNadimak.getText().toString();

        int poeni;
        int asistencije;
        int skokovi;
        String ekipa;

        try {
            poeni = Integer.parseInt(etPoeni.getText().toString());
            asistencije = Integer.parseInt(etAsistencije.getText().toString());
            skokovi = Integer.parseInt(etSkokovi.getText().toString());
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
        for (BasketballPlayerStats s : pst)
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

        pst.add(new BasketballPlayerStats(new Athlete(0, ime, nadimak, Constants.DISCIPLINE_BASKETBALL), new BasketballStats(0, 0, poeni, asistencije, skokovi, ekipa), 1));
        plAdapter.notifyDataSetChanged();

        etIme.setText("");
        etNadimak.setText("");
        etPoeni.setText("");
        etAsistencije.setText("");
        etSkokovi.setText("");
        spEkipe.setSelection(0);
    }

    public void spremi (View v)
    {
        if (!update) {
            ArrayList<Athlete> igraci = new ArrayList<Athlete>();
            ArrayList<BasketballStats> statistike = new ArrayList<BasketballStats>();

            for (BasketballPlayerStats p : pst) {
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
            pst.get(0).getAthlete().setName(etIme.getText().toString());
            pst.get(0).getAthlete().setNickname(etNadimak.getText().toString());
            try {
                pst.get(0).getStats().setPoints(Integer.parseInt(etPoeni.getText().toString()));
                pst.get(0).getStats().setAssists(Integer.parseInt(etAsistencije.getText().toString()));
                pst.get(0).getStats().setJumps(Integer.parseInt(etSkokovi.getText().toString()));
            }
            catch (NumberFormatException e)
            {
                Toast.makeText(this, "Neispravan upis.", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.updateAthlete(pst.get(0).getAthlete());
            dbHelper.updateBasketballStats(pst.get(0).getStats());
        }

        finish();
    }

    public void teamSelected()
    {
        nicknames.clear();
        nicknames.add("Odaberi igrača");
        if (!spEkipe.getSelectedItem().toString().equals("Odaberite ekipu")) {
            for (Athlete a : existingPlayers) {
                ArrayList<BasketballStats> stats = dbHelper.getBasketballPlayerStats(a.getId(), true);
                for (BasketballStats s : stats) {
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId())
        {
            case R.id.spinner:
                teamSelected();
                break;
            case R.id.spinner2:
                playerSelected();
                break;
            default:
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
