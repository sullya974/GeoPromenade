package com.dam05.geopromenade.Repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dam05.geopromenade.Model.Itinerary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.dam05.geopromenade.Common.NodesNames.ITINERARIES;
import static com.dam05.geopromenade.Common.NodesNames.USERS;

public class ItineraryRepository {
    private static final String TAG = "ItineraryRepository";
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference itinerariesCollectionReference = firebaseFirestore.collection(ITINERARIES);
    private Application application;

    public ItineraryRepository(Application application) {
        this.application = application;
    }

    public void saveItinerary(Itinerary itinerary){
        DocumentReference newItineraryRef = itinerariesCollectionReference.document();
        itinerary.setId(newItineraryRef.getId());
        newItineraryRef.set(itinerary).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Manage success
                }else{
                    Toast.makeText(application.getApplicationContext(), "Itinerary creation failed - Message:" + task.getException(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Itinerary creation failed - Message:" + task.getException());
                }
            }
        });
    }
}
