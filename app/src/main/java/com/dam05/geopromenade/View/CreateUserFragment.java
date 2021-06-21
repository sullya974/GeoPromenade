package com.dam05.geopromenade.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dam05.geopromenade.R;
import com.dam05.geopromenade.Model.User;
import com.dam05.geopromenade.ViewModel.UserViewModel;

public class CreateUserFragment extends Fragment {

    EditText etFirstname, etLastname, etBio;
    Button btnValider;
    UserViewModel userViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_user, container, false);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        initUI(view);

        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setFirstname(etFirstname.getText().toString());
                user.setLastname(etLastname.getText().toString());
                user.setBio(etBio.getText().toString());

                userViewModel.createUser(user);
                if (user.isCreated()) {
                    Toast.makeText(getActivity(), "Creation succes", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager()
                            .beginTransaction().replace(R.id.fragment_container, new UsersListFragment()).commit();
                }
            }
        });

        return view;
    }

    void initUI(View view) {
        etFirstname = (EditText) view.findViewById(R.id.et_firstname);
        etLastname = (EditText) view.findViewById(R.id.et_lastname);
        etBio = (EditText) view.findViewById(R.id.et_bio);
        btnValider = (Button) view.findViewById(R.id.btnValider);
    }
}