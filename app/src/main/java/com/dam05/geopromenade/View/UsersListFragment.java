package com.dam05.geopromenade.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dam05.geopromenade.R;
import com.dam05.geopromenade.Adapters.UserRecyclerAdapter;
import com.dam05.geopromenade.ViewModel.UserViewModel;

public class UsersListFragment extends Fragment {

    RecyclerView userRecyclerView;
    UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);

        initUI(view);
        bindUserRecyclerView();

        return view;
    }

    private void initUI(View view) {
        userRecyclerView = (RecyclerView) view.findViewById(R.id.user_recyclerView);
    }

    private void bindUserRecyclerView() {
        userViewModel.getUserListLiveData().observe(getActivity(), Observable -> {});
        userViewModel.getUserList().observe(getActivity(), userList -> {
            if(userList != null){
                UserRecyclerAdapter userRecyclerAdapter = new UserRecyclerAdapter(getActivity(), userList);
                userRecyclerView.setAdapter(userRecyclerAdapter);
                //Ajout d'un nouveau LinearLayout pour contenir les vues du RecyclerView
                //On peut alors choisir l'orintation vertical ou horizontal ou inverser la sélection
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                userRecyclerView.setLayoutManager(layoutManager);
            }
        });
    }
}