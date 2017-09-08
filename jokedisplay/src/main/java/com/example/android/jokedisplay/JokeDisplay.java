package com.example.android.jokedisplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokeDisplay extends AppCompatActivity {

    private static final String KEY_JOKE = "key-joke";
    private TextView mJokeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_display);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mJokeText = (TextView) findViewById(R.id.tv_joke);

        if (getIntent().getExtras() == null) finish();

        String joke = getIntent().getExtras().getString(KEY_JOKE);

        mJokeText.setText(joke);
    }
}
