package com.example.grupee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginScreen extends AppCompatActivity {

    public EditText usernameField, passwordField;
    private ImageButton eyeToggle;
    private Button login, loginWithFacebookBtn;

    boolean passShow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        usernameField = findViewById(R.id.username_edit_text);
        passwordField = findViewById(R.id.password_edit_text);
        eyeToggle = findViewById(R.id.password_toggle);
        login = findViewById(R.id.login_btn);
        loginWithFacebookBtn = findViewById(R.id.facebook_login_btn);

        eyeToggle.setOnClickListener(new View.OnClickListener(){ //buton eyeToggle TODO: verificat functionalitate cand apesi pe el, nu merge ok la prima apasare
            @Override
            public void onClick(View v) {
                if(passShow){
                    passShow = false;
                    eyeToggle.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    passwordField.setTransformationMethod(new PasswordTransformationMethod());
                }else
                {
                    passShow = true;
                    eyeToggle.setImageResource(R.drawable.ic_baseline_visibility_24);
                    passwordField.setTransformationMethod(null);
                }
            }
        });

        usernameField.setOnFocusChangeListener(new View.OnFocusChangeListener() { //gri deschis onFocus pe username off pe parola
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    usernameField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_focus_bg));
                    passwordField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
                }
            }
        });

        passwordField.setOnFocusChangeListener(new View.OnFocusChangeListener() { //gri deschis onFocus pe parola off pe user
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    passwordField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_focus_bg));
                    usernameField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
                }
            }
        });

        usernameField.addTextChangedListener(new TextWatcher() {  //culoare buton login in functie de completarea campurilor
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(passwordField.getText()) && !TextUtils.isEmpty(s)){
                    login.setEnabled(true);
                    login.setClickable(true);
                    login.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    login.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_bg_active));
                }else{
                    login.setEnabled(false);
                    login.setClickable(false);
                    login.setTextColor(getResources().getColor(R.color.grey));
                    login.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        passwordField.addTextChangedListener(new TextWatcher() {  //culoare buton login in functie de completarea campurilor
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(usernameField.getText()) && !TextUtils.isEmpty(s)){
                    login.setEnabled(true);
                    login.setClickable(true);
                    login.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    login.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_bg_active));
                }else{
                    login.setEnabled(false);
                    login.setClickable(false);
                    login.setTextColor(getResources().getColor(R.color.grey));
                    login.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onBackPressed() {
        Intent loginOrRegister = new Intent(LoginScreen.this, LoginOrRegister.class);
        startActivity(loginOrRegister);
        finish();
        //super.onBackPressed();
    }
}