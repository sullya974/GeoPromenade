package com.dam05.geopromenade.View.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dam05.geopromenade.Common.NodesNames;
import com.dam05.geopromenade.Model.User;
import com.dam05.geopromenade.R;
import com.dam05.geopromenade.View.MainActivity;
import com.dam05.geopromenade.ViewModel.LoginRegisterViewModel;
import com.dam05.geopromenade.ViewModel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private TextInputEditText etFirstname;
    private TextInputEditText etLastname;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;
    private ImageView ivAvatar;

    private LoginRegisterViewModel loginRegisterViewModel;
//    private UserViewModel userViewModel;

//    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//    private CollectionReference usersCollectionReference = firebaseFirestore.collection("Users");
//    private FirebaseUser firebaseUser;
//    private StorageReference storageReference;
    //Firebase Realtime database
    private DatabaseReference databaseReference;
    // Path vers les images file
    private Uri localAvatarFileUri, serverAvatarFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

//        userViewModel = new UserViewModel();
        loginRegisterViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        loginRegisterViewModel.getFirebaseUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                }
            }
        });

        initUI();
    }

    private void initUI() {
        etFirstname = findViewById(R.id.etFirstname);
        etLastname = findViewById(R.id.etLastname);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        ivAvatar = findViewById(R.id.iv_avatar);
    }

    public void btnSignUpClick(View view) {
        try {
            String firstname = etFirstname.getText().toString().trim();
            String lastname = etLastname.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (firstname.equals("")) {
                etFirstname.setError(getString(R.string.firstname_required));
                etFirstname.hasFocus();
                return;
            } else if (lastname.equals("")) {
                etLastname.setError(getString(R.string.lastname_required));
                etLastname.hasFocus();
                return;
            } else if (email.equals("")) {
                etEmail.setError(getString(R.string.email_required));
                etEmail.hasFocus();
                return;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError(getString(R.string.email_format_invalid));
                etEmail.hasFocus();
                return;
            } else if (password.equals("")) {
                etPassword.setError(getString(R.string.enter_password));
                etPassword.hasFocus();
                return;
            } else if (confirmPassword.equals("")) {
                etConfirmPassword.setError(getString(R.string.confirm_password_required));
                etConfirmPassword.hasFocus();
                return;
            } else if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError(getString(R.string.password_confirm_password_not_equals));
                return;
            } else {
                User user = new User();
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setEmail(email);
                user.setPassword(password);
                loginRegisterViewModel.register(user);
//                // Connection à Firebase
//                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//                // Création de l'utilisateur dans Authentication sevice de Firebase
//                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        if (task.isSuccessful()) {
//                            Toast.makeText(SignUpActivity.this, "User successfully registered in firebase authentication", Toast.LENGTH_LONG).show();
//
//                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//                            User user = new User();
//                            user.setId(firebaseUser.getUid());
//                            user.setFirstname(firstname);
//                            user.setLastname(lastname);
//                            user.setCreated(false);
//
////                        if(localFileUri != null)
////                        {
////                            updateNameAndPhoto();
////                        }else{
////                            updateNameOnly();
////                        }
//
//                            userViewModel.createUser(user);
//                            if (user.isCreated()) {
//                                Toast.makeText(SignUpActivity.this, "User " + user.getFirstname() + " " + user.getLastname() + " has been successfully registered !", Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
//                            }
//                        } else {
//                            Toast.makeText(SignUpActivity.this, getString(R.string.signup_failed) + task.getException(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
            }
        } catch (Exception ex) {
            Log.d(TAG, "User sign up failed - Message:" + ex.getMessage());
        }
    }

    /**
     * Méthode MAJ nom dans Realtime DB
     */
    private void updateNameOnly() {
//        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
//                .setDisplayName(etFirstname.getText().toString().trim())
//                .build();
//
//        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    String userId = firebaseUser.getUid();
//                    /** Connexion à Realtime **/
//                    databaseReference = FirebaseDatabase // Création / récupération d'un nouveau noeud "Users"
//                            .getInstance() // Instance de connexion
//                            .getReference() // Cherche la référence désirée à partir de la racine de la Db
//                            .child(NodesNames.USERS);
//
//                    // Création HasMap pour la gestion des données
//                    HashMap<String, String> hashMap = new HashMap<>();
//                    hashMap.put(NodesNames.NAME, etFirstname.getText().toString());
//                    hashMap.put(NodesNames.EMAIL, etEmail.getText().toString());
//                    hashMap.put(NodesNames.ONLINE, "true");
//                    hashMap.put(NodesNames.AVATAR, "");
//
//                    // Envoi des datas vers Realtime
//                    databaseReference.child(userId) // Créer un noeud avec la valuer "userId" du user courant
//                            .setValue(hashMap) // Set les valeurs du user dans Realtime
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(SignUpActivity.this, R.string.user_created_successfully, Toast.LENGTH_SHORT).show();
//
//                                        //Lancement de l'activité suivante
//                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
//                                    } else {
//                                        Toast.makeText(SignUpActivity.this, getString(R.string.user_update_failed) + task.getException(), Toast.LENGTH_SHORT).show();
//                                        Log.d(TAG, "onComplete: Error user creation - Message" + task.getException());
//                                    }
//                                }
//                            });
//                } else {
//                    // S'il y a un problème
//                    Toast.makeText(SignUpActivity.this, R.string.user_update_failed, Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "onComplete: Error user updating - Message" + task.getException());
//                }
//            }
//        });
    }

    /**
     * Méthode MAJ photo et contenu dans Realtime DB
     */
    private void updateNameAndPhoto() {
//        //Renomme l'image avec "userId" et le type de fichier (ici JPG)
//        String strFileName = firebaseUser.getUid() + ".jpg";
//
//        // Créer une référence du storage avec le dossier le fichier
//        final StorageReference fileRef = storageReference.child("avatars_user/" + strFileName);
//
//        //Upload vers le storage
//        fileRef.putFile(localAvatarFileUri)
//                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                        if (task.isSuccessful()) {
//                            //Récupère l'URL de l'avatar dans le storage
//                            fileRef.getDownloadUrl()
//                                    .addOnSuccessListener(SignUpActivity.this, new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            serverAvatarFileUri = uri;
//
//                                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
//                                                    .setDisplayName(etFirstname.getText().toString().trim())
//                                                    .build();
//
//                                            firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if (task.isSuccessful()) {
//                                                        String userId = firebaseUser.getUid();
//                                                        /** Connexion à Realtime **/
//                                                        databaseReference = FirebaseDatabase // Création / récupération d'un nouveau noeud "Users"
//                                                                .getInstance() // Instance de connexion
//                                                                .getReference() // Cherche la référence désirée à partir de la racine de la Db
//                                                                .child(NodesNames.USERS);
//
//                                                        // Création HasMap pour la gestion des données
//                                                        HashMap<String, String> hashMap = new HashMap<>();
//                                                        hashMap.put(NodesNames.NAME, etFirstname.getText().toString());
//                                                        hashMap.put(NodesNames.EMAIL, etEmail.getText().toString());
//                                                        hashMap.put(NodesNames.ONLINE, "true");
//                                                        hashMap.put(NodesNames.AVATAR, serverAvatarFileUri.getPath());
//
//                                                        // Envoi des datas vers Realtime
//                                                        databaseReference.child(userId) // Créer un noeud avec la valuer "userId" du user courant
//                                                                .setValue(hashMap) // Set les valeurs du user dans Realtime
//                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                    @Override
//                                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                                        if (task.isSuccessful()) {
//                                                                            Toast.makeText(SignUpActivity.this, R.string.user_created_successfully, Toast.LENGTH_SHORT).show();
//
//                                                                            //Lancement de l'activité suivante
//                                                                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
//                                                                        } else {
//                                                                            Toast.makeText(SignUpActivity.this, getString(R.string.user_creation_failed) + task.getException(), Toast.LENGTH_SHORT).show();
//                                                                            Log.d(TAG, "onComplete: Error user creation - Message" + task.getException());
//                                                                        }
//                                                                    }
//                                                                });
//                                                    } else {
//                                                        // S'il y a un problème
//                                                        Toast.makeText(SignUpActivity.this, R.string.user_update_failed, Toast.LENGTH_SHORT).show();
//                                                        Log.d(TAG, "onComplete: Error user updating - Message" + task.getException());
//                                                    }
//                                                }
//                                            });
//
//                                        }
//                                    });
//                        }
//
//                    }
//                });
    }

    /**
     * 7.3 Ajout de la méthode pour la gestion de l'avatar de l'utilisateur
     * Ne pas oublier de la lier à l'imageView dans le XML
     **/
    public void pickImage(View v) {
        /**
         *  9 Ajout de la vérification de la permission de parcourir les dossiers du terminal
         * Avant toute chose il faut ajouter la permission dans le manifest
         **/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Ajout de l'intent implicite qui va ouvrir la galerie du terminal pour choisir un photo : Intent.ACTION_PICK
            // Il faut ensuite ajouter l'espace de stockage dans lequel recherché, ici les images stockées sur tout le terminal
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 101);
            // A noter que le request code peut-être n'importe quoi, il n'y en a un qu'un seul dans cette activité
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    102);
        }
    }

    /**
     * 10 Ajout de la méthode pour vérifier si l'on à la permission ou non
     **/
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,
                                            int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // The Old Way, la méthode startActivityForResult étant dépréciée
        if (requestCode == 102) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 101);
            } else {
                Toast.makeText(this,
                        R.string.access_permission_is_required,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 7.4 Action à effectuée en résultat de la méthode pickImage() // réponse à startActivityForResult
     **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Vérifications
        // Le resquestcode est-il le bon ?
        if (requestCode == 101) {
            // Il y'a bien une sélection d'image, sinon le resultCode = RESULT_CANCELED
            if (resultCode == RESULT_OK) {
                // Ajout des variables globales des uri cf 7.5
                // Path complet vers l'image sur le terminal
                localAvatarFileUri = data.getData();
                // Affectation de l'image sélectionnée à l'avatar (pour la variable globale cf 7.5)
                ivAvatar.setImageURI(localAvatarFileUri);
            }
        }
    }

    public void tvSignInClick(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }
}