package com.cs399.tbgs.anagramz;

/**
 * Created by Tanner Stevens on 10/9/2016.
 */

public class WordList {
    private static WordList instance = null;
    protected WordList(){
        //Do WordList.txt parsing here?
        System.out.println("Test.");
    }
    public static WordList getInstance() {
        if(instance == null){
            instance = new WordList();
        }
        return instance;
    }
}
