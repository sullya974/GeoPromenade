package com.dam05.geopromenade.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dam05.geopromenade.Model.User;
import com.dam05.geopromenade.Repository.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoggedInViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<FirebaseUser> firebaseUserLiveData;
    private MutableLiveData<User> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;


    public LoggedInViewModel(@NonNull Application application) {
        super(application);

        this.authRepository = new AuthRepository(application);
        firebaseUserLiveData = authRepository.getFirebaseUserLiveData();
        userLiveData = authRepository.getUserLiveData();
        loggedOutLiveData = authRepository.getLoggedOutLiveData();
    }

    public void loggedOut()
    {
        authRepository.logOut();
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserLiveData() {
        return firebaseUserLiveData;
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData(){
        return loggedOutLiveData;
    }
}
