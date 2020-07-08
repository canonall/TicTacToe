package com.canonal.tictactoe.utility.operator;

import android.content.Context;
import android.content.SharedPreferences;

import com.canonal.tictactoe.utility.Constants;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceOperator {
    public static void saveData(String playerName, Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Constants.PLAYER_NAME, playerName);
        editor.apply();
    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PLAYER_NAME, Constants.NO_SHARED_PREF_PLAYER_NAME);
    }
}
