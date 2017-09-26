package com.example.rivios_.sportapp.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rivios_.sportapp.R;
import com.example.rivios_.sportapp.data.FootballPlayersStats;

import java.util.ArrayList;

public class FootballPlayerStatsAdapter extends BaseAdapter {
    ArrayList<FootballPlayersStats> playerStats;

    public FootballPlayerStatsAdapter(ArrayList<FootballPlayersStats> playerStats) {
        this.playerStats = playerStats;
    }

    @Override
    public int getCount() {
        return playerStats.size();
    }

    @Override
    public Object getItem(int position) {
        return playerStats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return playerStats.get(position).getAthlete().getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StatsViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.footbal_player_list_element, null);

            holder = new StatsViewHolder();
            holder.tvNamePos = (TextView) convertView.findViewById(R.id.tvNamePos);
            holder.tvStats = (TextView) convertView.findViewById(R.id.tvStats);

            convertView.setTag(holder);
        } else {
            holder = (StatsViewHolder) convertView.getTag();
        }

        FootballPlayersStats current = playerStats.get(position);

        String str;
        if (current.getGameCount() == 0)
        {
            str = "Ekipa: " + current.getStats().getTeam();
        }
        else
        {
            str = "Odg. utakmice: " +  Integer.toString(current.getGameCount());
        }

        holder.tvNamePos.setText(current.getAthlete().getName() + "  " + current.getAthlete().getNickname() + " " + str);
        holder.tvStats.setText("GOALS:  " + current.getStats().getGoals() +
                "   AST: " + current.getStats().getAssists());

        return convertView;
    }

    static class StatsViewHolder {
        private TextView tvNamePos;
        private TextView tvStats;
    }
}
