package com.example.whowantstobeamillionaire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameScreen extends AppCompatActivity {

    @BindView(R.id.answerA) Button answerA;
    @BindView(R.id.answerB) Button answerB;
    @BindView(R.id.answerC) Button answerC;
    @BindView(R.id.answerD) Button answerD;
    @BindView(R.id.withdrawal) Button withdraw;
    @BindView(R.id.FiftyFiftyLifeLine) ImageButton fiftyFifty;
    @BindView(R.id.audienceLifeLine) ImageButton audience;
    @BindView(R.id.phoneLifeLine) ImageButton phone;
    @BindView(R.id.questionContainer) TextView questionContainer;
    @BindView(R.id.timer) TextView timer;

    private final Button[] moneyButtonArray = new Button[15];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide the toolbar , make full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        ButterKnife.bind(this);

        //setup moneyArray for different rounds
        moneyButtonArray[0] = findViewById(R.id.round1);
        moneyButtonArray[1] = findViewById(R.id.round2);
        moneyButtonArray[2] = findViewById(R.id.round3);
        moneyButtonArray[3] = findViewById(R.id.round4);
        moneyButtonArray[4] = findViewById(R.id.round5);
        moneyButtonArray[5] = findViewById(R.id.round6);
        moneyButtonArray[6] = findViewById(R.id.round7);
        moneyButtonArray[7] = findViewById(R.id.round8);
        moneyButtonArray[8] = findViewById(R.id.round9);
        moneyButtonArray[9] = findViewById(R.id.round10);
        moneyButtonArray[10] = findViewById(R.id.round11);
        moneyButtonArray[11] = findViewById(R.id.round12);
        moneyButtonArray[12] = findViewById(R.id.round13);
        moneyButtonArray[13] = findViewById(R.id.round14);
        moneyButtonArray[14] = findViewById(R.id.round15);

        try {
            //setup game
            //create constructor, connect functions to ui displays
            Game game = new Game(this, answerA, answerB, answerC, answerD, questionContainer);
            game.setMoneyArray(moneyButtonArray);
            game.setTimeCounter(timer);
            game.setFiftyFifty(fiftyFifty);
            game.setPhone(phone);
            game.setAudience(audience);
            game.setExitButton(withdraw);
            game.setQuestionPieces(15);
            game.playGame();
        } catch (Exception e) {
            Log.e("ERROR RECEIVED", e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //press back button stop music , stop timer
        if (Game.mediaPlayer.isPlaying())
            Game.mediaPlayer.stop();

        try {
            Game.timer.cancel();
            Game.countDownTimer.cancel();
        } catch (Exception e) {
            Log.e("Timer", e.toString());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //pause activity stop music
        if (Game.mediaPlayer.isPlaying())
            Game.mediaPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //stop activity stop music
        if (Game.mediaPlayer.isPlaying())
            Game.mediaPlayer.stop();

        //stop activity cancel timer
        try {
            Game.timer.cancel();
        } catch (Exception e) {
            Log.e("Timer", e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //destroy activity stop music
        if (Game.mediaPlayer.isPlaying())
            Game.mediaPlayer.stop();

        //destroy activity cancel timer
        try {
            Game.timer.cancel();
        } catch (Exception e) {
            Log.e("Timer", e.toString());
        }
    }
}
