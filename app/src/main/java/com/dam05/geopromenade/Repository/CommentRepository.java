package com.dam05.geopromenade.Repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dam05.geopromenade.Model.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.dam05.geopromenade.Common.NodesNames.ITINERARIES;

public class CommentRepository {
    private static final String TAG = "CommentRepository";
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference itinerariesCollectionReference = firebaseFirestore.collection(ITINERARIES);
    private Application application;

    public CommentRepository(Application application) {
        this.application = application;
    }

    public void createComment(Comment comment){
        DocumentReference newCommentRef = itinerariesCollectionReference.document();
        comment.setId(newCommentRef.getId());// Init id
        newCommentRef.set(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(application, "Comment created successfully: " + task.getException(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(application, "Error comment creation: " + task.getException(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Error comment creation: " + task.getException());
                }
            }
        });
    }
}
