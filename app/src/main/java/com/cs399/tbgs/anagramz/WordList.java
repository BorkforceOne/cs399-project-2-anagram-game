package com.cs399.tbgs.anagramz;

/**
 * Created by Tanner Stevens on 10/9/2016.
 */

import java.io.*;
import android.content.Context;
import android.content.res.AssetManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WordList {
    private static WordList instance = null;
    private List<String> words;
    private HashMap<String, List<String>> anagrams;
    protected WordList(){
        words = new ArrayList<String>();
        anagrams = new HashMap<String, List<String>>();
    }
    public static WordList getInstance() {
        if(instance == null){
            instance = new WordList();
        }
        return instance;
    }

    public void parseWordFile(InputStream is){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.toUpperCase();
                words.add(line);
                char[] chars = line.toCharArray();
                Arrays.sort(chars);
                String sortedLine = new String(chars);
                if(anagrams.containsKey(sortedLine))
                    anagrams.get(sortedLine).add(line);
                else{
                    List<String> temp = new ArrayList<String>();
                    temp.add(line);
                    anagrams.put(sortedLine, temp);
                }
            }
        }
        catch(IOException e){
            System.out.println("Shits fucked up.");
        }
        System.out.println(words.size());
        System.out.println(anagrams.size());
    }

    public List<String> getAnagrams(String word){
        String key = word.toUpperCase();
        char[] chars = key.toCharArray();
        Arrays.sort(chars);
        String sortedKey = new String(chars);
        return anagrams.get(sortedKey);
    }

    public String getWordAt(int index){
        return words.get(index);
    }
}
