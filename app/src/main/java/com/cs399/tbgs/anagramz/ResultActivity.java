package com.cs399.tbgs.anagramz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ArrayList<String> correctWords = getIntent().getExtras().getStringArrayList("correctWords");
        ArrayList<String> incorrectWords = getIntent().getExtras().getStringArrayList("incorrectWords");

        TextView correctTV = (TextView)findViewById(R.id.CorrectTextView);
        TextView incorrectTV = (TextView)findViewById(R.id.IncorrectTextView);
        TextView correctWordsTV = (TextView)findViewById(R.id.correctWordsText);
        TextView incorrectWordsTV = (TextView)findViewById(R.id.incorrectWordsText);

        correctTV.append(Integer.toString(correctWords.size()));
        incorrectTV.append(Integer.toString(incorrectWords.size()));


        String newLine = "";
        for (String word: correctWords) {
            correctWordsTV.append(newLine + word);
            newLine = "\n";
        }

        newLine = "";
        for (String word: incorrectWords) {
            incorrectWordsTV.append(newLine + word);
            newLine = "\n";
        }
    }

    public void finishGotoChallengeSelection(View view) {
        Intent intent = new Intent(this, ChallengeSelectionActivity.class);
        startActivity(intent);
        finish();
    }

    public void exitApp(View view) {
        finish();
    }

    public void gotoChallenge(View view){
        Intent intent = new Intent(this, ChallengeSelectionActivity.class);
        finish();
        startActivity(intent);
    }
}
