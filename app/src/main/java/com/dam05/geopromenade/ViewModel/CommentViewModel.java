package com.dam05.geopromenade.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.dam05.geopromenade.Model.Comment;
import com.dam05.geopromenade.Repository.CommentRepository;

public class CommentViewModel extends AndroidViewModel {
    private CommentRepository commentRepository;

    public CommentViewModel(@NonNull Application application) {
        super(application);

        commentRepository = new CommentRepository(application);
    }

    public void createComment(Comment comment){
        commentRepository.createComment(comment);
    }
}
