package com.example.trivia.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private  SharedPreferences sharedPreferences;

    public Prefs(Activity activity) {
        this.sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }
    public void savedHighScore(int score){

        int lastScore = sharedPreferences.getInt("high_score", 0);

        if (score>lastScore){
            sharedPreferences.edit().putInt("high_score", score).apply();
        }
    }
    public int getHighScore(){
        return sharedPreferences.getInt("high_score", 0);

    }
    public void setCurrentScore(int score){
        sharedPreferences.edit().putInt("score", score).apply();
    }

    public int getCurrentScore(){
        return sharedPreferences.getInt("score", 0);
    }

    public void setState(int index){
        sharedPreferences.edit().putInt("index", index).apply();
    }
    public int getState(){
        return sharedPreferences.getInt("index", 0);
    }
}
