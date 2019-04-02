package com.example.whowantstobeamillionaire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenu extends AppCompatActivity {

    @BindView(R.id.millionaire_logo) ImageView logo;
    @BindView(R.id.openVoice) Switch voice;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide the toolbar , make full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);
        Objects.requireNonNull(getSupportActionBar()).hide();

        ButterKnife.bind(this);

        //create media player
        mediaPlayer = MediaPlayer.create(this, R.raw.main_monitor);
        RotateNow();

        //get preferences if user has sound on/off
        SharedPreferences sharedPreferences = getSharedPreferences("sound", Context.MODE_PRIVATE);
        final SharedPreferences.Editor edit = sharedPreferences.edit();

        //toggle if user preferences has sound on/off

        voice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    edit.putString("sound", "ok");
                    mediaPlayer.start();
                    edit.apply();
                } else {
                    edit.putString("sound", "no");
                    mediaPlayer.stop();
                    edit.apply();
                }
            }
        });

        //if preference on play music , if off stop music
        if (Objects.requireNonNull(sharedPreferences.getString("sound", "")).equals("ok")) {
            mediaPlayer.start();
            voice.setChecked(true);
        } else
            voice.setChecked(false);
    }

    //start game
    public void newGame(View view) {
        mediaPlayer.stop();
        startActivity(new Intent(MainMenu.this, GameScreen.class));
    }

    @Override
    protected void onPause() {
        super.onPause();

        //pause activity stop music
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //stop activity stop music
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    //rotate image of logo
    private void RotateNow() {
        RotateAnimation rotate = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(15000);
        rotate.setInterpolator(new LinearInterpolator());
        logo.startAnimation(rotate);
    }

    //click highscore button start score activity
    public void highScore(View view) {
        startActivity(new Intent(MainMenu.this, ScoreList.class));
    }

    //click quit button, exit app
    public void quit(View view) {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
