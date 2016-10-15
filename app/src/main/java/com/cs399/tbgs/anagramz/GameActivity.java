package com.cs399.tbgs.anagramz;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private Random random;
    private ArrayList<String> letterRack;
    private ArrayList<String> guessRack;
    private FlowLayout letterLayout;
    private FlowLayout guessLayout;
    private ArrayList<String> correctWords;
    private ArrayList<String> incorrectWords;
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

        // Create and setup our random number generator
        this.random = new Random(challengeSeed.hashCode());

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
                    ProgressBar pb = (ProgressBar) findViewById(R.id.timeProgressBar);
                    TextView tv = (TextView) findViewById(R.id.timeString);
                    pb.setProgress(timeLimit);
                    tv.setText(timeToString(timeLimit) + " remaining");
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
        final ArrayList<String> rackFrom;
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
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(width, height);
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
        layoutParams.setMargins(margin, margin, margin, margin);
        letterButton.setLayoutParams(layoutParams);
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
        intent.putStringArrayListExtra("correctWords", correctWords);
        intent.putStringArrayListExtra("incorrectWords", incorrectWords);
        startActivity(intent);
    }

    public String timeToString(int timeMS) {
        String timeString = "";
        int seconds = (timeMS/1000) % 60;
        int minutes = ((timeMS/1000)/60) % 60;
        int hours = (((timeMS/1000)/60)/24) % 60;

        if (hours != 0) {
            timeString += hours + " hours";
        }
        if (minutes != 0) {
            timeString += minutes + " minutes";
        }

        timeString += seconds + " seconds";

        return timeString;
    }

    public void checkAnagram(View view) {
        final TextView mTextHistory = (TextView) findViewById(R.id.historyText);
        final ScrollView mScrollView = (ScrollView) findViewById(R.id.historyScroller);

        String newLine = "\n";
        String guess = "";

        if (guessRack.size() == 0)
            return;

        for (String c: guessRack) {
            guess = guess.concat(c);
        }

        if (correctWords.size() + incorrectWords.size() == 0) {
            newLine = "";
        }

        if (correctWords.contains(guess) || incorrectWords.contains(guess)) {
            incorrectWords.add(guess);
            mTextHistory.append(newLine + "You already guessed " + guess + "!");
        }
        else if (WordList.getInstance().isValidWord(guess)) {
            timeLimit += 1000 * guessRack.size();
            correctWords.add(guess);
            mTextHistory.append(newLine + "Correct word " + guess + " +" + guessRack.size() + " seconds");
        }
        else {
            incorrectWords.add(guess);
            mTextHistory.append(newLine + "Incorrect word " + guess + "");
        }

        mScrollView.post(new Runnable()
        {
            public void run()
            {
                mScrollView.smoothScrollTo(0, mTextHistory.getBottom());
            }
        });
    }
}
