package com.example.rivios_.sportapp.activities.tennis;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rivios_.sportapp.Constants;
import com.example.rivios_.sportapp.DatePickerFragment;
import com.example.rivios_.sportapp.GameDBHelper;
import com.example.rivios_.sportapp.R;
import com.example.rivios_.sportapp.data.TennisGame;
import com.example.rivios_.sportapp.data.Athlete;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TennisGameStatsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ArrayList<Athlete> players;
    private ArrayList<String> playerNames = new ArrayList<>();
    private ArrayList<String> playerNicknames = new ArrayList<>();
    private TennisGame currentTennisgame = new TennisGame();
    private ArrayList<Athlete> currentTennisPlayers = new ArrayList<Athlete>();
    private boolean update;

    Spinner spinnerPlayer1;
    Spinner spinnerPlayer2;
    EditText etpl1Name;
    EditText etpl1Nickname;
    EditText etpl2Name;
    EditText etpl2Nickname;
    EditText etResult;
    EditText etfirstSet;
    EditText etsecondSet;
    EditText etthirdSet;
    EditText etfourthSet;
    EditText etfifthSet;
    EditText etDatum;
    GameDBHelper dbHelper;
    ArrayAdapter<String> spPlayersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = GameDBHelper.getInstance(this);
        setContentView(R.layout.activity_tennis_game);

        spinnerPlayer1 = (Spinner) findViewById(R.id.spPlayer1);
        spinnerPlayer2 = (Spinner) findViewById(R.id.spPlayer2);
        etpl1Name = (EditText) findViewById(R.id.player1Name);
        etpl1Nickname = (EditText) findViewById(R.id.player1Nickname);
        etpl2Name = (EditText) findViewById(R.id.player2Name);
        etpl2Nickname = (EditText) findViewById(R.id.player2Nickname);
        etResult = (EditText) findViewById(R.id.tennisresult);
        etfirstSet = (EditText) findViewById(R.id.firstSet);
        etsecondSet = (EditText) findViewById(R.id.secondSet);
        etthirdSet = (EditText) findViewById(R.id.thirdSet);
        etfourthSet = (EditText) findViewById(R.id.fourthSet);
        etfifthSet = (EditText) findViewById(R.id.fifthSet);
        etDatum = (EditText) findViewById(R.id.datum);

        Intent i = getIntent();
        if (i.hasExtra(Constants.GAME))
        {
            update = true;
            currentTennisgame = i.getParcelableExtra(Constants.GAME);
            Log.d("PERO", Long.toString(currentTennisgame.getPlayer1Id()));
            Log.d("PERO", Long.toString(currentTennisgame.getPlayer2Id()));
            Athlete player1 = dbHelper.getAthlete(currentTennisgame.getPlayer1Id());
            Athlete player2 = dbHelper.getAthlete(currentTennisgame.getPlayer2Id());

            etpl1Name.setText(player1.getName());
            etpl1Nickname.setText(player1.getNickname());
            etpl2Name.setText(player2.getName());
            etpl2Nickname.setText(player2.getNickname());
            etResult.setText(Integer.toString(currentTennisgame.getResult1())
                    + ":" + Integer.toString(currentTennisgame.getResult2()));
            etfirstSet.setText(currentTennisgame.getSet1());
            etsecondSet.setText(currentTennisgame.getSet2());
            etthirdSet.setText(currentTennisgame.getSet3());
            etfourthSet.setText(currentTennisgame.getSet4());
            etfifthSet.setText(currentTennisgame.getSet5());
            etDatum.setText(new SimpleDateFormat("dd.MM.yyyy").format(currentTennisgame.getDatum()));

            findViewById(R.id.button2).setVisibility(View.INVISIBLE);
        }
        else
        {
            update = false;
        }

        players = dbHelper.getAthletes(Constants.DISCIPLINE_TENNIS);
        playerNames.add("Odaberi igrača");
        playerNicknames.add("Odaberi igrača");
        for(Athlete a : players){
            playerNames.add(a.getName());
            playerNicknames.add(a.getNickname());
        }
        spPlayersAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playerNames);
        spinnerPlayer1.setAdapter(spPlayersAdapter);
        spinnerPlayer1.setSelection(0);
        spinnerPlayer1.setOnItemSelectedListener(this);
        spinnerPlayer2.setAdapter(spPlayersAdapter);
        spinnerPlayer2.setSelection(0);
        spinnerPlayer2.setOnItemSelectedListener(this);
    }

    public void saveTennisGame (View v) {
        String player1 = etpl1Name.getText().toString();
        String nickname1 = etpl1Nickname.getText().toString();
        String player2 = etpl2Name.getText().toString();
        String nickname2 = etpl2Nickname.getText().toString();
        String result = etResult.getText().toString();
        String datum = etDatum.getText().toString();
        String set1 = etfirstSet.getText().toString();
        String set2 = etsecondSet.getText().toString();
        String set3 = etthirdSet.getText().toString();
        String set4 = etfourthSet.getText().toString();
        String set5 = etfifthSet.getText().toString();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd.MM.yyyy");

        if (player1.equals("")) {
            Toast.makeText(this, "Nije unešeno ime prvog igrača.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nickname1.equals("")) {
            Toast.makeText(this, "Nije unešen nadimak prvog igrača.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (player2.equals("")) {
            Toast.makeText(this, "Nije unešeno ime drugog igrača.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nickname2.equals("")) {
            Toast.makeText(this, "Nije unešen nadimak drugog igrača.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (result.equals("")) {
            Toast.makeText(this, "Nije upisan rezultat.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (datum.equals("")) {
            Toast.makeText(this, "Nije upisan datum.", Toast.LENGTH_SHORT).show();
            return;
        }


        currentTennisgame.setPlayer1(player1);
        currentTennisgame.setPlayer2(player2);
        currentTennisPlayers.add(new Athlete(0, player1, nickname1, Constants.DISCIPLINE_TENNIS));
        currentTennisPlayers.add(new Athlete(0, player2, nickname2, Constants.DISCIPLINE_TENNIS));

        if (((result.indexOf(":") <= 0) || (result.indexOf(":") == result.length() - 1)) ||
                (!set1.equals("")&&((set1.indexOf(":") <= 0) || (set1.indexOf(":") == set1.length() - 1))) ||
                (!set2.equals("")&&((set2.indexOf(":") <= 0) || (set2.indexOf(":") == set2.length() - 1))) ||
                (!set3.equals("")&&((set3.indexOf(":") <= 0) || (set3.indexOf(":") == set3.length() - 1))) ||
                (!set4.equals("")&&((set4.indexOf(":") <= 0) || (set4.indexOf(":") == set4.length() - 1))) ||
                (!set5.equals("")&&((set5.indexOf(":") <= 0) || (set5.indexOf(":") == set5.length() - 1)))) {
            Toast.makeText(this, "Neispravno upisan rezultat.", Toast.LENGTH_SHORT).show();
            return;
        }
        int brDvotocke = 0;
        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) == ':')
                brDvotocke++;
            if (brDvotocke > 1) {
                Toast.makeText(this, "Neispravno upisan rezultat.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        int indikator = result.indexOf(":");;
        int result1;
        int result2;

        try {
            result1 = Integer.parseInt(result.substring(0, indikator));
            result2 = Integer.parseInt(result.substring(indikator + 1));
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, "Neispravno upisan rezultat.", Toast.LENGTH_SHORT).show();
            return;
        }

        currentTennisgame.setResult1(result1);
        currentTennisgame.setResult2(result2);
        currentTennisgame.setSet1(set1);
        currentTennisgame.setSet2(set2);
        currentTennisgame.setSet3(set3);
        currentTennisgame.setSet4(set4);
        currentTennisgame.setSet5(set5);

        if (currentTennisgame.getResult1() < currentTennisgame.getResult2()) {
            currentTennisgame.setWinner(player2);
        } else if (currentTennisgame.getResult1() > currentTennisgame.getResult2()) {
            currentTennisgame.setWinner(player1);
        } else {
            currentTennisgame.setWinner("");
        }
        try {
            currentTennisgame.setDatum(dateFormat.parse(datum));
        } catch (ParseException e) {
            Toast.makeText(this, "Neispravno upisan datum.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Athlete player : currentTennisPlayers)
        {
            if (dbHelper.getAthleteID(player.getNickname()) == -1) {
                dbHelper.addAthlete(player);
            }
            long pid = dbHelper.getAthleteID(player.getNickname());
            player.setId(pid);
        }
        currentTennisgame.setPlayer1Id(currentTennisPlayers.get(0).getId());
        currentTennisgame.setPlayer2Id(currentTennisPlayers.get(1).getId());

        if (!update) {
            dbHelper.addTennisGame(currentTennisgame);
        }
        else {
            dbHelper.updateTennisGame(currentTennisgame);
            finish();
        }

        long gid = dbHelper.getTennisGameID(currentTennisgame.getPlayer1(), currentTennisgame.getPlayer2(), currentTennisgame.getDatum());
        currentTennisgame.setId(gid);

        etpl1Name.setText("");
        etpl1Nickname.setText("");
        etpl2Name.setText("");
        etpl2Nickname.setText("");
        etResult.setText("");
        etfirstSet.setText("");
        etsecondSet.setText("");
        etthirdSet.setText("");
        etfourthSet.setText("");
        etfifthSet.setText("");
        etDatum.setText("");
        currentTennisgame = new TennisGame();
    }

    public void birajDatum (View v)
    {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), Constants.DATE_PICKER_TAG);
    }


    public void tennisArchive (View v)
    {
        Intent i = new Intent();
        i.setClass(this, TennisGameList.class);
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String Player1check = spinnerPlayer1.getSelectedItem().toString();
        String Team2check = spinnerPlayer2.getSelectedItem().toString();

        if (!Player1check.equals("Odaberi igrača"))
        {
            String myStr = spinnerPlayer1.getSelectedItem().toString();
            etpl1Name.setText(myStr);
            etpl1Nickname.setText(playerNicknames.get(i));
        }

        if (!Team2check.equals("Odaberi igrača")){

            String myStr = spinnerPlayer2.getSelectedItem().toString();
            etpl2Name.setText(myStr);
            etpl2Nickname.setText(playerNicknames.get(i));
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
