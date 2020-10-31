package com.example.grupee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginOrRegister extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        loginButton = findViewById(R.id.login_btn);
        registerButton = findViewById(R.id.register_btn);

        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });
    }

    public void openLoginActivity(){
        Intent loginIntent = new Intent(LoginOrRegister.this, LoginScreen.class);
        startActivity(loginIntent);
        finish();
    }

    public void openRegisterActivity(){
        Intent registerIntent = new Intent(LoginOrRegister.this, RegisterActivity.class);
        startActivity(registerIntent);
        finish();
    }
}