package com.example.rivios_.sportapp.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rivios_.sportapp.R;
import com.example.rivios_.sportapp.data.FootballGame;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FootballGameStatsAdapter  extends BaseAdapter{

    ArrayList<FootballGame> games;

    public FootballGameStatsAdapter(ArrayList<FootballGame> games) {
        this.games = games;
    }
    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int i) {
        return games.get(i);
    }

    @Override
    public long getItemId(int i) {
        return games.get(i).getId();
    }

    @Override
     public View getView(int position, View convertView, ViewGroup parent) {
        GameViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.football_list_element, null);

            holder = new GameViewHolder();
            holder.footballteam1 = (TextView) convertView.findViewById(R.id.footballteam1);
            holder.footballteam2 = (TextView) convertView.findViewById(R.id.footballteam2);
            holder.footballresult = (TextView) convertView.findViewById(R.id.footballresult);
            holder.tvDatum = (TextView) convertView.findViewById(R.id.tvDatum);

            convertView.setTag(holder);
        }
        else {
            holder = (GameViewHolder) convertView.getTag();
        }

        FootballGame current = games.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");

        holder.footballteam1.setText(current.getTeam1());
        holder.footballteam2.setText(current.getTeam2());
        holder.footballresult.setText(current.getResult1() + " : " + current.getResult2());
        holder.tvDatum.setText(sdf.format(current.getDatum()));

        return convertView;
    }

    static class GameViewHolder {
        private TextView footballteam1;
        private TextView footballteam2;
        private TextView footballresult;
        private TextView tvDatum;
    }
}
