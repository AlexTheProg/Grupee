package com.example.grupee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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


    }

    @Override
    public void onBackPressed() {
        Intent loginOrRegister = new Intent(RegisterActivity.this, LoginOrRegisterActivity.class);
        startActivity(loginOrRegister);
        finish();
    }
}