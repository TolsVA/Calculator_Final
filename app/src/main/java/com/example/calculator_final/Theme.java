package com.example.calculator_final;

import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

public enum Theme {
    ONE(R.style.CalKey1_AppThemeLight, R.string.material_light, "0"),
    TWO(R.style.CalKey, R.string.cal_key_style, "1"),
    THREE(R.style.MyCoolStyle, R.string.my_cool_style, "2"),
    FOUR(R.style.AppThemeDark, R.string.material_dark, "3");

    @StyleRes
    private final int theme;

    @StringRes
    private final int name;

    private final String key;

    Theme(int theme, int name, String key) {
        this.theme = theme;
        this.name = name;
        this.key = key;
    }

    public int getTheme() {
        return theme;
    }

    public int getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
