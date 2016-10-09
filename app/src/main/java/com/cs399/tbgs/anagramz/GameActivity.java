package com.cs399.tbgs.anagramz;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.apmem.tools.layouts.FlowLayout;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        String challengeSeed = getIntent().getExtras().getString("challengeSeed");

        // Create and setup our random number generator
        this.random = new Random(challengeSeed.hashCode());

        final FlowLayout inputLayout = (FlowLayout)findViewById(R.id.inputLettersLayout);
        final FlowLayout guessLayout = (FlowLayout)findViewById(R.id.guessLettersLayout);
        final GameActivity game = this;

        String input = WordList.getInstance().getWordAt(this.random.nextInt());
        input = scramble(this.random, input);


        for (char letter: input.toCharArray()) {
            final String s = String.valueOf(letter);
            final Button letterButton = new Button(this);
            letterButton.setText(s);
            letterButton.setLayoutParams(new ViewGroup.LayoutParams(150, 150));
            letterButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    inputLayout.removeView(v);
                    Button guessButton = new Button(game);
                    guessButton.setText(s);
                    guessButton.setLayoutParams(new ViewGroup.LayoutParams(150, 150));
                    guessLayout.addView(guessButton);
                }
            });
            inputLayout.addView(letterButton);
        }

        Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                gotoResultScreen();
            }
        };
        timerHandler.postDelayed(timerRunnable, 30000);
    }

    private static String scramble( Random random, String inputString )
    {
        // Convert your string into a simple char array:
        char a[] = inputString.toCharArray();

        // Scramble the letters using the standard Fisher-Yates shuffle,
        for( int i=0 ; i<a.length-1 ; i++ )
        {
            int j = random.nextInt(a.length-1);
            // Swap letters
            char temp = a[i]; a[i] = a[j];  a[j] = temp;
        }

        return new String( a );
    }

    public void gotoResultScreen() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("foundWords", ""+1);
        intent.putExtra("incorrectWords", ""+2);
        intent.putExtra("missedWords", ""+0);
        startActivity(intent);
    }
}
