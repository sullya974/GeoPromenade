package com.dam05.geopromenade.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dam05.geopromenade.Model.User;
import com.dam05.geopromenade.Repository.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<FirebaseUser> firebaseUserLiveData;
    private MutableLiveData<User> userLiveData;

    public LoginRegisterViewModel(Application application) {
        super(application);
        this.authRepository = new AuthRepository(application);
        firebaseUserLiveData = authRepository.getFirebaseUserLiveData();
        userLiveData = authRepository.getUserLiveData();
    }

    public void login(User user){
        authRepository.login(user);
    }
    public void register(User user){
        authRepository.register(user);
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserLiveData() {
        return firebaseUserLiveData;
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }
}
