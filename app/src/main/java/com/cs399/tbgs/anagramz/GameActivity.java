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
import android.widget.ProgressBar;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private Random random;
    private List<String> letterRack;
    private List<String> guessRack;
    private FlowLayout letterLayout;
    private FlowLayout guessLayout;
    private List<String> correctWords;
    private List<String> incorrectWords;
    private int timeLimit = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        letterRack = new ArrayList<String>();
        letterLayout = (FlowLayout)findViewById(R.id.inputLettersLayout);
        guessRack = new ArrayList<String>();
        guessLayout = (FlowLayout)findViewById(R.id.guessLettersLayout);
        correctWords = new ArrayList<String>();
        incorrectWords = new ArrayList<String>();

        String challengeSeed = getIntent().getExtras().getString("challengeSeed");
        System.out.println(challengeSeed.hashCode());

        // Create and setup our random number generator
        this.random = new Random(challengeSeed.hashCode());

        GameActivity game = this;

        String input = WordList.getInstance().getWordAt(this.random.nextInt());
        input = scramble(this.random, input);

        for (char c: input.toCharArray()) {
            letterRack.add(String.valueOf(c));
            addLetterToRack(letterRack, c);
        }

        final Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                if(timeLimit<=0)
                    gotoResultScreen();
                else {
                    timeLimit -= 100;
                    ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar4);
                    pb.setProgress(timeLimit);
                    timerHandler.postDelayed(this, 100);
                }
            }
        };
        timerHandler.postDelayed(timerRunnable, 100);
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

    public void addLetterToRack(final List<String> rackTo, final char letter) {
        final FlowLayout layoutTo;
        final List<String> rackFrom;
        if (rackTo == this.letterRack){
            layoutTo = this.letterLayout;
            rackFrom = this.guessRack;
        }
        else if (rackTo == this.guessRack){
            layoutTo = this.guessLayout;
            rackFrom = this.letterRack;
        }
        else {
            throw new Error("Unknown rack provided");
        }

        String s = String.valueOf(letter);
        Button letterButton = new Button(this);
        letterButton.setText(s);
        letterButton.setLayoutParams(new ViewGroup.LayoutParams(150, 150));
        letterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FlowLayout parentLayout = (FlowLayout) v.getParent();
                Button button = (Button) v;
                parentLayout.removeView(button);

                rackTo.remove(String.valueOf(letter));
                addLetterToRack(rackFrom, letter);
            }
        });
        layoutTo.addView(letterButton);

        rackTo.add(String.valueOf(letter));
    }

    public void gotoResultScreen() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("foundWords", ""+correctWords.size());
        intent.putExtra("incorrectWords", ""+incorrectWords.size());
        finish();
        startActivity(intent);
    }

    public void checkAnagram(View view) {
        String guess = "";
        for (String c: guessRack) {
            guess = guess.concat(c);
        }
        if (WordList.getInstance().isValidWord(guess) && !correctWords.contains(guess)) {
            timeLimit+=5000;
            System.out.println("FOUND WORD");
            correctWords.add(guess);
        }
        else {
            System.out.println("NOT A WORD");
            incorrectWords.add(guess);
        }
    }
}
