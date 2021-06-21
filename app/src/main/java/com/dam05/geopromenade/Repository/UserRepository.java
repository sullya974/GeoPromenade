/**
 * Le Repository : A l'intérieur de chaque ViewModel, nous allons retrouver une ou plusieurs classes de type Repository.
 * Il nous servira de médiateur entre le ViewModel et les différentes sources de données.
 * Le but du repository est d’isoler la source de données (DAO) du ViewModel, afin que ce dernier ne manipule pas directement la source de données.
 * Cela peut faciliter le changement de la source de données sans impacter l’ensemble des couches de l’appli.
 */

package com.dam05.geopromenade.Repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.dam05.geopromenade.LiveData.UserListLiveData;
import com.dam05.geopromenade.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicReference;

public class UserRepository {
    private static final String TAG = "UserRepository";

    private Application application;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference usersCollectionReference = firebaseFirestore.collection("Users");

    public UserRepository(Application application) {
        this.application = application;
    }

    public User createUserInFirestore(User user) {
        //Set l 'id du user
//        String id = java.util.UUID.randomUUID().toString();
//        user.setId(id);
        // Nouveau utilisateur Firesotre - Instancie le document représentant le nouveau USER
        /*IMPORTANT: Si l'ID est existant l'utilisateur existant sera mis à jour*/
        DocumentReference newUserRef = usersCollectionReference.document(user.getId());

        newUserRef.set(user).addOnCompleteListener(userCreationTask -> {
            if (userCreationTask.isSuccessful()) {
                user.setCreated(true);
                Log.d(TAG, "createUserInFirestore: Success");
            } else {
                Log.d(TAG, "createUserInFirestore: Error");
            }
        });
        return user;
    }

    public UserListLiveData getUsersLiveData(){
        CollectionReference collectionReference = firebaseFirestore.collection("Users");

        return new UserListLiveData(collectionReference);
    }

    public MutableLiveData<User> getUserById(String userId) {
        MutableLiveData<User> authenticatedUserMutableLiveData = new MutableLiveData<>();
        DocumentReference userDocumentReference = usersCollectionReference.document(userId);
        userDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: Successfully get the user from firestore.");
                    User user = task.getResult().toObject(User.class);
                    authenticatedUserMutableLiveData.setValue(user);
                }else{
                    Toast.makeText(application.getApplicationContext(), "Error to get user - Message: " + task.getException(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onComplete: Error to get user - Message: " + task.getException());
                }
            }
        });

        return authenticatedUserMutableLiveData;
    }
}
