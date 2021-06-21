package com.dam05.geopromenade.Repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.dam05.geopromenade.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.dam05.geopromenade.Common.NodesNames.USERS;

public class AuthRepository {
    private static final String TAG = "AuthRepository";
    private Application application;

    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> firebaseUserLiveData;
    private MutableLiveData<User> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference usersCollectionReference = firebaseFirestore.collection(USERS);
    private UserRepository userRepository;

    public AuthRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseUserLiveData = new MutableLiveData<>();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();
        userRepository = new UserRepository(application);

        if (firebaseAuth.getCurrentUser() != null) {
            firebaseUserLiveData.postValue(firebaseAuth.getCurrentUser());

            // postvalue userLiveData
            postValueUserLiveData();

            loggedOutLiveData.postValue(false);
        }
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserLiveData() {
        return firebaseUserLiveData;
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

    //    public void register(String email, String password){
    public void register(User user) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(application.getMainExecutor(), task -> {
                    if (task.isSuccessful()) {
                        user.setId(firebaseAuth.getCurrentUser().getUid());
                        firebaseUserLiveData.postValue(firebaseAuth.getCurrentUser());

                        //Création d'un nouveau utilisateur dans Firestore
                        DocumentReference userDocumentReference = usersCollectionReference.document(user.getId()); // Le chemin vers le noeud du nouveau utilisateur est sont ID
                        userDocumentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> userCreationTask) {
                                if (userCreationTask.isSuccessful()) {
                                    userLiveData.postValue(user);
                                }
                                {
                                    Toast.makeText(application.getApplicationContext(), "Registration Failure: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(application.getApplicationContext(), "Registration Failure: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void login(User user) {
        firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firebaseUserLiveData.postValue(firebaseAuth.getCurrentUser());

                        // R2cupère l'utilisateur depuis Firestore
                        DocumentReference userDocumentReference = usersCollectionReference.document(firebaseAuth.getCurrentUser().getUid());
                        userDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> userLoginTask) {
                                if (userLoginTask.isSuccessful()) {
                                    User user = userLoginTask.getResult().toObject(User.class);
                                    userLiveData.postValue(user);
                                } else {
                                    Toast.makeText(application.getApplicationContext(), "Login failure: " + userLoginTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(application.getApplicationContext(), "Login failure: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void logOut(){
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
    }

    public void postValueUserLiveData(){
        DocumentReference userDocumentReference = usersCollectionReference.document(firebaseAuth.getCurrentUser().getUid());
        userDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    userLiveData.postValue(task.getResult().toObject(User.class));
                }else{
                    Toast.makeText(application.getApplicationContext(), "Error to get user - Message: " + task.getException(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onComplete: Error to get user - Message: " + task.getException());
                }
            }
        });
    }
}
