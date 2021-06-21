package com.dam05.geopromenade.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dam05.geopromenade.Model.Comment;
import com.dam05.geopromenade.R;
import com.dam05.geopromenade.ViewModel.CommentViewModel;

public class CommentFragment extends Fragment {
    private TextView etContent, etWayPointId;
    Button btnCreate;
    CommentViewModel commentViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        initUI(view);
        commentViewModel = new ViewModelProvider(getActivity()).get(CommentViewModel.class);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wayPointId = etWayPointId.getText().toString();
                String content = etContent.getText().toString();

                Comment comment = new Comment();
                comment.setContent(content);
                comment.setWayPointId(wayPointId);

                commentViewModel.createComment(comment);
            }
        });

        return view;
    }

    private void initUI(View view){
        etContent = view.findViewById(R.id.etContent);
        etWayPointId = view.findViewById(R.id.etWayPointId);
        btnCreate  = view.findViewById(R.id.btnCreate);
    }

//    public void btnCreateClick(View view){
//        String wayPointId = etWayPointId.getText().toString();
//        String content = etContent.getText().toString();
//
//        Comment comment = new Comment();
//        comment.setContent(content);
//        comment.setWayPointId(wayPointId);
//
//        commentViewModel.createComment(comment);
//    }
}