package com.cs399.tbgs.anagramz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ChallengeSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_selection);
    }

    public void gotoAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void gotoGame(View view) {
        EditText et=(EditText)findViewById(R.id.seedText);
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("challengeSeed", et.getText().toString());
        startActivity(intent);
    }
}