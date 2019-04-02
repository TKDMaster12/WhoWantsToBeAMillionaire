package com.example.whowantstobeamillionaire.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.whowantstobeamillionaire.Success;
import com.example.whowantstobeamillionaire.R;

import java.util.ArrayList;

public class ScoreAdapter extends BaseAdapter {
    private final Activity activity;
    private final LayoutInflater inflater;
    private final ArrayList<Success> scores;

    public ScoreAdapter(Activity activity, ArrayList<Success> scores) {
        this.activity = activity;
        this.scores = scores;
        inflater = LayoutInflater.from(activity.getApplicationContext());
    }

    @Override
    public int getCount() {
        return scores.size();
    }

    @Override
    public Object getItem(int id) {
        return scores.get(id);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.rowscore, null);
        TextView txtMoney = v.findViewById(R.id.money);
        TextView txtPoint = v.findViewById(R.id.point);
        TextView txtTime = v.findViewById(R.id.time);

        String money  = activity.getString(R.string.currency) + " " + scores.get(i).getMoney();
        String time = scores.get(i).getTime() + " " + activity.getString(R.string.second);
        String point = scores.get(i).getPoint() + " " + activity.getString(R.string.point);

        txtMoney.setText(money);
        txtTime.setText(time);
        txtPoint.setText(point);

        return v;
    }
}