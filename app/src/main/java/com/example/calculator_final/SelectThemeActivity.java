package com.example.calculator_final;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectThemeActivity extends AppCompatActivity {

    public static final String EXTRA_THEME = "EXTRA_THEME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent launchIntent = getIntent();
        Theme selectedTheme = (Theme) launchIntent.getSerializableExtra(EXTRA_THEME);
        setTheme(selectedTheme.getTheme());

        setContentView(R.layout.activity_select_theme);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        LinearLayout themeContainer = findViewById(R.id.themes);

        for (Theme theme: Theme.values()) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_thema, themeContainer, false);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent data = new Intent();
                    data.putExtra(EXTRA_THEME, theme);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
            });

            TextView themeItemTitle = view.findViewById(R.id.theme_item);
            themeItemTitle.setText(theme.getName());

            ImageView check = view.findViewById(R.id.check);

            if (theme.equals(selectedTheme)) {
                switch (theme.getKey()) {
                    case "3":
                        check.setColorFilter(getResources().getColor(R.color.white));
                        break;
                    case "0":
                        check.setColorFilter(getResources().getColor(R.color.red));
                        break;
                    case "2":
                        check.setColorFilter(getResources().getColor(R.color.purple_700));
                        break;
                }
                check.setVisibility(View.VISIBLE);
            } else {
                check.setVisibility(View.INVISIBLE);
            }
            themeContainer.addView(view);
        }
    }
}