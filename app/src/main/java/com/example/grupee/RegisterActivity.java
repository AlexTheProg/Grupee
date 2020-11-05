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

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailField, usernameField, passwordField, confirmPasswordField;
    private ImageButton eyeToggle, confirmPassEyeToggle;
    private Button login;
    boolean passwordShow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailField = findViewById(R.id.email_edit_text);
        usernameField = findViewById(R.id.username_edit_text);
        passwordField = findViewById(R.id.password_edit_text);
        confirmPasswordField = findViewById(R.id.confirm_password_edit_text);
        eyeToggle = findViewById(R.id.password_toggle);
        confirmPassEyeToggle = findViewById(R.id.confirm_password_toggle);
        login = findViewById(R.id.login_btn);

        eyeToggle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(passwordShow){
                    passwordShow = false;
                    eyeToggle.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    passwordField.setTransformationMethod(null);
                }else{
                    passwordShow = true;
                    eyeToggle.setImageResource(R.drawable.ic_baseline_visibility_24);
                    passwordField.setTransformationMethod(new PasswordTransformationMethod());
                }
                passwordField.setSelection(passwordField.getText().length());
            }
        });

        confirmPassEyeToggle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(passwordShow){
                    passwordShow = false;
                    confirmPassEyeToggle.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    confirmPasswordField.setTransformationMethod(null);
                }else{
                    passwordShow = true;
                    confirmPassEyeToggle.setImageResource(R.drawable.ic_baseline_visibility_24);
                    confirmPasswordField.setTransformationMethod(new PasswordTransformationMethod());
                }
                confirmPasswordField.setSelection(confirmPasswordField.getText().length());
            }
        });

        emailField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    emailField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_focus_bg));
                    usernameField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
                    passwordField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
                    confirmPasswordField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));

                }
            }
        });

        usernameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    emailField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
                    usernameField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_focus_bg));
                    passwordField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
                    confirmPasswordField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));

                }
            }
        });

        passwordField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    emailField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
                    usernameField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
                    passwordField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_focus_bg));
                    confirmPasswordField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));

                }
            }
        });

        confirmPasswordField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    emailField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
                    usernameField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
                    passwordField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
                    confirmPasswordField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_focus_bg));

                }
            }
        });

        emailField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    emailField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_focus_bg));
                    usernameField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
                    passwordField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
                    confirmPasswordField.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));

                }
            }
        });


        passwordField.addTextChangedListener(new TextWatcher() {  //culoare buton login in functie de completarea campurilor
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(confirmPasswordField.getText()) && !TextUtils.isEmpty(s) && TextUtils.equals(passwordField.getText().toString(), confirmPasswordField.getText().toString())){
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

        confirmPasswordField.addTextChangedListener(new TextWatcher() {  //culoare buton login in functie de completarea campurilor
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(passwordField.getText()) && !TextUtils.isEmpty(s) && TextUtils.equals(passwordField.getText().toString(), confirmPasswordField.getText().toString())){
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
        Intent loginOrRegister = new Intent(RegisterActivity.this, LoginOrRegisterActivity.class);
        startActivity(loginOrRegister);
        finish();
    }
}