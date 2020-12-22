package com.example.grupee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    private EditText emailField, usernameField, passwordField, confirmPasswordField;
    private ImageButton eyeToggle, confirmPassEyeToggle;
    private Button register;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    boolean passwordShow = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.email_edit_text);
        usernameField = findViewById(R.id.username_edit_text);
        passwordField = findViewById(R.id.password_edit_text);
        confirmPasswordField = findViewById(R.id.confirm_password_edit_text);
        eyeToggle = findViewById(R.id.password_toggle);
        progressBar = findViewById(R.id.progressBar);
        confirmPassEyeToggle = findViewById(R.id.confirm_password_toggle);
        register =  findViewById(R.id.register_user);

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });



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
                if(!TextUtils.isEmpty(confirmPasswordField.getText()) && !TextUtils.isEmpty(s)){
                    register.setEnabled(true);
                    register.setClickable(true);
                    register.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    register.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_bg_active));
                }else{
                    register.setEnabled(false);
                    register.setClickable(false);
                    register.setTextColor(getResources().getColor(R.color.grey));
                    register.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_bg));
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
                if(!TextUtils.isEmpty(passwordField.getText()) && !TextUtils.isEmpty(s)){
                    register.setEnabled(true);
                    register.setClickable(true);
                    register.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    register.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_bg_active));
                }else{
                    register.setEnabled(false);
                    register.setClickable(false);
                    register.setTextColor(getResources().getColor(R.color.grey));
                    register.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    private void registerUser(){
        final String email = emailField.getText().toString().trim();
        final String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPass = confirmPasswordField.getText().toString().trim();

        if(username.isEmpty()){
            usernameField.setError("Username is required");
            usernameField.requestFocus();
            return;
        }

        if(email.isEmpty()){
            emailField.setError("Email is required");
            emailField.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passwordField.setError("Password is required");
            passwordField.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailField.setError("Please provide valid email");
            emailField.requestFocus();
            return;
        }

        if(password.length() < 6){
            passwordField.setError("Password length should be at least 6 characters");
            passwordField.requestFocus();
            return;
        }

        if(!password.equals(confirmPass)){
            confirmPasswordField.setError("Passwords do not match");
            confirmPasswordField.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(username, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "User has registered successfully", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Registration has failed", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
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