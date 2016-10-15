package com.cs399.tbgs.anagramz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView correctTV = (TextView)findViewById(R.id.CorrectTextView);
        correctTV.setText(correctTV.getText()+getIntent().getExtras().getString("foundWords"));
        TextView incorrectTV = (TextView)findViewById(R.id.IncorrectTextView);
        incorrectTV.setText(incorrectTV.getText()+getIntent().getExtras().getString("incorrectWords"));
        /*TextView missedTV = (TextView)findViewById(R.id.RemainingTextView);
        missedTV.setText(missedTV.getText()+getIntent().getExtras().getString("nMissedWords"));

        TextView missedWordsTV = (TextView)findViewById(R.id.MissedTextView);
        missedWordsTV.setText(getIntent().getExtras().getString("missedWords"));*/
    }

    public void gotoChallenge(View view){
        Intent intent = new Intent(this, ChallengeSelectionActivity.class);
        finish();
        startActivity(intent);
    }
}
