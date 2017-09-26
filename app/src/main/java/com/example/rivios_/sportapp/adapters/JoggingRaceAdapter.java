package com.example.rivios_.sportapp.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rivios_.sportapp.R;
import com.example.rivios_.sportapp.data.JoggingRace;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class JoggingRaceAdapter extends BaseAdapter {
    ArrayList<JoggingRace> races;

    public JoggingRaceAdapter(ArrayList<JoggingRace> races) {
        this.races = races;
    }

    @Override
    public int getCount() {
        return races.size();
    }

    @Override
    public Object getItem(int position) {
        return races.get(position);
    }

    @Override
    public long getItemId(int position) {
        return races.get(position).getRaceId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RaceViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.jogging_race_list_element, null);

            holder = new RaceViewHolder();
            holder.tvStart = (TextView) convertView.findViewById(R.id.tvStart);
            holder.tvFinish = (TextView) convertView.findViewById(R.id.tvFinish);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);

            convertView.setTag(holder);
        }
        else {
            holder = (RaceViewHolder) convertView.getTag();
        }

        JoggingRace current = races.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");

        holder.tvStart.setText(current.getStart());
        holder.tvFinish.setText(current.getFinish());
        holder.tvDistance.setText(Double.toString(current.getDistance() / 1000.0) + " km");
        holder.tvDate.setText(sdf.format(current.getDate()));

        return convertView;
    }

    static class RaceViewHolder {
        private TextView tvStart;
        private TextView tvFinish;
        private TextView tvDistance;
        private TextView tvDate;
    }
}
