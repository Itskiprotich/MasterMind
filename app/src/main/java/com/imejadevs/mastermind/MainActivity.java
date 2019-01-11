package com.imejadevs.mastermind;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.imejadevs.mastermind.About.About_Us;
import com.imejadevs.mastermind.Leaders.LeaderBoard;
import com.imejadevs.mastermind.NewGame.PlayActivity;
import com.imejadevs.mastermind.Settings.User_Settings;

public class MainActivity extends AppCompatActivity {

    Button button_newgame, button_instructions, button_exit;
    Vibrator vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void onclicknewgame(View view) {
        vib.vibrate(50);
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
        finish();
    }

    public void onclickexit(View view) {
        vib.vibrate(50);
        this.finish();
    }

    public void onclickinstructions(View view) {
        vib.vibrate(50);
        Intent instructionsintent = new Intent(this, About_Us.class);
        startActivity(instructionsintent);
    }

    public void onclickLeader(View view) {
        vib.vibrate(50);
        Intent instructionsintent = new Intent(this, LeaderBoard.class);
        startActivity(instructionsintent);
    }

    public void onclickSettings(View view) {
        vib.vibrate(50);
        Intent instructionsintent = new Intent(this, User_Settings.class);
        startActivity(instructionsintent);
    }
}
