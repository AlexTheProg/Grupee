package com.example.grupee.oldPack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.grupee.R;
import com.example.grupee.view.LoginRegisterFragment;

public class LoginOrRegisterActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        loginButton = findViewById(R.id.register_user);
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
        Intent loginIntent = new Intent(LoginOrRegisterActivity.this, LoginRegisterFragment.class);
        startActivity(loginIntent);
        finish();
    }

    public void openRegisterActivity(){
        Intent registerIntent = new Intent(LoginOrRegisterActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
        finish();
    }
}