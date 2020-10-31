package com.example.grupee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_TIMER = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView splashScreen = findViewById(R.id.splashScreen);
        splashScreen.animate().alpha(4000F).setDuration(0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(MainActivity.this,LoginOrRegister.class);
                startActivity(mainIntent);
                finish();
            }
        },SPLASH_DISPLAY_TIMER);
    }
}