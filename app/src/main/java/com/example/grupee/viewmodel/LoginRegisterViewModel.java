package com.example.grupee.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.grupee.model.AuthenticationAndDatabaseRepo;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterViewModel extends AndroidViewModel {
    private final AuthenticationAndDatabaseRepo authenticationAndDatabaseRepo;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;

    public LoginRegisterViewModel(@NonNull Application application) {
        super(application);
        authenticationAndDatabaseRepo = new AuthenticationAndDatabaseRepo(application);
        userMutableLiveData = authenticationAndDatabaseRepo.getUserLiveData();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void login(String email, String password){
        authenticationAndDatabaseRepo.login(email, password);
    }

    public void register(String email, String password){
        authenticationAndDatabaseRepo.databaseAndAuthRegister(email, password);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData(){
        return userMutableLiveData;
    }
}
