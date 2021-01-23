package com.example.grupee.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.grupee.model.AuthenticationAndDatabaseRepo;
import com.google.firebase.auth.FirebaseUser;

public class LoggedInViewModel extends AndroidViewModel {
    private final AuthenticationAndDatabaseRepo authenticationAndDatabaseRepo;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<Boolean> loggedOutLiveData;

    public LoggedInViewModel(@NonNull Application application) {
        super(application);
        authenticationAndDatabaseRepo = new AuthenticationAndDatabaseRepo(application);
        userLiveData = authenticationAndDatabaseRepo.getUserLiveData();
        loggedOutLiveData = authenticationAndDatabaseRepo.getLoggedOutLiveData();
    }

    public void logOut(){
        authenticationAndDatabaseRepo.logOut();
    }

    public MutableLiveData<FirebaseUser> getUserLiveData(){
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData(){
        return loggedOutLiveData;
    }
}
