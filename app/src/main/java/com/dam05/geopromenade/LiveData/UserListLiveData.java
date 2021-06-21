package com.dam05.geopromenade.LiveData;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dam05.geopromenade.Model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

//public class UserListLiveData extends LiveData<List<User>> implements EventListener<DocumentSnapshot> {
public class UserListLiveData extends LiveData<List<User>> implements EventListener<QuerySnapshot> {
    private static final String TAG = "UserListLiveData";
    private List<User> userListTemp = new ArrayList<>();
    public MutableLiveData<List<User>> userList = new MutableLiveData<>();
//    private DocumentReference documentReference;
    private CollectionReference collectionReference;

    private ListenerRegistration listenerRegistration = () -> {};

//    public UserListLiveData(DocumentReference documentReference) {
//        this.documentReference = documentReference;
//    }

    public UserListLiveData(CollectionReference collectionReference) {
        this.collectionReference = collectionReference;
    }

    @Override
    protected void onActive() {
        listenerRegistration = collectionReference.addSnapshotListener(this);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        listenerRegistration.remove();
        super.onInactive();
    }

//    @Override
//    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//        if(documentSnapshot != null && documentSnapshot.exists()){
//
//            Map<String, Object> listItems = documentSnapshot.getData();
//            userListTemp.clear();
//
//            for (Map.Entry<String, Object> entry : listItems.entrySet()) {
//                Log.d(TAG, "onEvent: " + entry.getValue().toString());
//            }
//
//            User user = documentSnapshot.toObject(User.class);
//            userListTemp.add(user);
//
//            userList.setValue(userListTemp);
//        }else{
//            Log.d(TAG, "onEvent: ERROR");
//        }
//    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
        if(error != null){
            Log.e(TAG, "onEvent: Listen failed", error);
        }

        if(queryDocumentSnapshots != null){
            //Clear the list and add all the users again
            userListTemp.clear();

            for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                User user = doc.toObject(User.class);
                userListTemp.add(user);
            }
            Log.d(TAG, "onEvent: User list size: " + userListTemp.size());

            userList.setValue(userListTemp);
        }
    }
}
