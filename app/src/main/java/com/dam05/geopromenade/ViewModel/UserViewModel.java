/**
 * Le ViewModel : Le contrôleur implémente une classe de type ViewModel, dont le rôle est de fournir au contrôleur les données utilisées par l'interface graphique.
 * Une des spécificités de la classe ViewModel est sa capacité à "survivre" aux changements de configuration comme la rotation de l'écran par exemple,
 * sans perdre ses données... *
 */

package com.dam05.geopromenade.ViewModel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dam05.geopromenade.LiveData.UserListLiveData;
import com.dam05.geopromenade.Model.User;
import com.dam05.geopromenade.Repository.UserRepository;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class UserViewModel extends ViewModel {
    UserRepository userRepository;
    private MutableLiveData<List<User>> usersMutableLiveData;
    UserListLiveData liveData;

    public UserViewModel(Application application) {
        userRepository = new UserRepository(application);
    }

    public User createUser(User user){
        return userRepository.createUserInFirestore(user);
    }

    public LiveData<List<User>> getUserListLiveData(){
        liveData = userRepository.getUsersLiveData();
        return liveData;
    }

    public LiveData<List<User>> getUserList(){
        return liveData.userList;
    }
}
