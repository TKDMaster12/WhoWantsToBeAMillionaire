package com.example.whowantstobeamillionaire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.whowantstobeamillionaire.utils.ScoreAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreList extends AppCompatActivity {

    @BindView(R.id.ScoreListView) ListView scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide the toolbar , make full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_score_list);
        Objects.requireNonNull(getSupportActionBar()).hide();

        ButterKnife.bind(this);

        initialize();
    }

    //create sqlite class and set up adapter
    private void initialize() {
        SQLiteScore sqLiteScore = new SQLiteScore(this);
        ScoreAdapter adapter = new ScoreAdapter(this, sqLiteScore.getAllScoresHighest());
        scoreList.setAdapter(adapter);
    }

    //click to sort scores from highest to lowest
    public void scoresBy(View view) {
        SQLiteScore sqLiteScore = new SQLiteScore(this);
        ScoreAdapter adapter = new ScoreAdapter(this, sqLiteScore.getAllScoresHighest());
        scoreList.setAdapter(adapter);
    }

    //click to sort scores from oldest to newest
    public void historyBy(View view) {
        SQLiteScore sqLiteScore = new SQLiteScore(this);
        ArrayList<Success> list = sqLiteScore.getAllScores();
        Collections.reverse(list);
        ScoreAdapter adapter = new ScoreAdapter(this, list);
        scoreList.setAdapter(adapter);
    }

    //click to delete all scores in database
    public void deleteScore(View view) {
        SQLiteScore sqLiteScore = new SQLiteScore(this);
        sqLiteScore.deleteAllScores();
        ScoreAdapter adapter = new ScoreAdapter(this, sqLiteScore.getAllScoresHighest());
        scoreList.setAdapter(adapter);
    }
}