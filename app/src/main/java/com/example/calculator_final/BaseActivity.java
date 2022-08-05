package com.example.calculator_final;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    // Имя параметра в настройках
    public static final String SAVED_STYLE = "SAVED_STYLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Устанавливать тему надо только до установки макета активити
        setTheme(getSavedTheme().getTheme());

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public void saveTheme(Theme theme) {
        SharedPreferences sharedPreferences = getSharedPreferences("Themes", MODE_PRIVATE);

        sharedPreferences.edit()
                .putString(SAVED_STYLE, theme.getKey())
                .apply();
    }

    public Theme getSavedTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("Themes", Context.MODE_PRIVATE);

        String key = String.valueOf(sharedPreferences.getString(SAVED_STYLE, Theme.ONE.getKey()));

        for (Theme theme : Theme.values()) {
            if (key.equals(theme.getKey())) {
                return theme;
            }
        }
        return Theme.ONE;
    }
}
