package com.cs399.tbgs.anagramz;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssetManager am = this.getAssets();
        try {
            InputStream is = am.open("wordlist.txt");
            WordList wl = WordList.getInstance();
            wl.parseWordFile(is);
        }
        catch(IOException e){
            System.out.println("Goddamnit Kenny.");
        }
    }

    public void gotoChallengeSelection(View view) {
        Intent intent = new Intent(this, ChallengeSelectionActivity.class);
        finish();
        startActivity(intent);
    }
}
