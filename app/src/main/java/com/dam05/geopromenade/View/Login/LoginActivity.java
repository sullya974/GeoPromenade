package com.dam05.geopromenade.View.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dam05.geopromenade.Model.User;
import com.dam05.geopromenade.UserClient;
import com.dam05.geopromenade.R;
import com.dam05.geopromenade.View.MainActivity;
import com.dam05.geopromenade.ViewModel.LoginRegisterViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private LoginRegisterViewModel loginRegisterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_login);

        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        loginRegisterViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    ((UserClient)(getApplicationContext())).setUser(user);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        });
//        loginRegisterViewModel.getFirebaseUserLiveData().observe(this, new Observer<FirebaseUser>() {
//            @Override
//            public void onChanged(FirebaseUser firebaseUser) {
//                if(firebaseUser != null){
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                }
//            }
//        });

        initUI();
    }

    private void initUI() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
    }

    public void btnLoginClick(View view) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.equals("")) {
            etEmail.setError(getString(R.string.email_required));
            etEmail.hasFocus();
            return;
        } else if (password.equals("")) {
            etPassword.setError(getString(R.string.password_required));
            etPassword.hasFocus();
            return;
        } else {
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            loginRegisterViewModel.login(user);
        }
    }

    //Méthode sign Up
    public void tvSignUpClick(View view){
        startActivity(new Intent(this, SignUpActivity.class));
    }

    // Méthode pour le bouton reset password
    public void tvResetPassword(View view){
        startActivity(new Intent(this, ResetPasswordActivity.class));
    }
}