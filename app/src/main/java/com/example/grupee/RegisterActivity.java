package com.example.grupee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void onBackPressed() {
        Intent loginOrRegister = new Intent(RegisterActivity.this, LoginOrRegister.class);
        startActivity(loginOrRegister);
        finish();
    }
}