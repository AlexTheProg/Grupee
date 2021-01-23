package com.example.grupee.view;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.grupee.R;
import com.example.grupee.viewmodel.LoginRegisterViewModel;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterFragment extends Fragment {
    private EditText emailField, passwordField;
    private ImageButton eyeToggle;
    private Button login, loginWithFacebookBtn;

    private LoginRegisterViewModel loginRegisterViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginRegisterViewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);
        loginRegisterViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    Navigation.findNavController(getView()).navigate(R.id.fragment_container_view_tag);
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_register_layout, container, false);

        emailField = view.findViewById(R.id.email_edit_text);
        passwordField = view.findViewById(R.id.password_edit_text);
        login = view.findViewById(R.id.register_user);

        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

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
                loginRegisterViewModel.login(email, password);
            }
        });

        return view;
    }


}