/***
 *
 * Le contrôleur : activité ou fragment, dédié à la manipulation interface graphique
 *
 *
 */

package com.dam05.geopromenade.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dam05.geopromenade.Model.User;
import com.dam05.geopromenade.UserClient;
import com.dam05.geopromenade.R;
import com.dam05.geopromenade.View.Login.LoginActivity;
import com.dam05.geopromenade.ViewModel.LoggedInViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;

import static com.dam05.geopromenade.Common.Constants.ERROR_DIALOG_REQUEST;
import static com.dam05.geopromenade.Common.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.dam05.geopromenade.Common.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navView;
    LoggedInViewModel loggedInViewModel;
    TextView tvUserInfo;

    //Gestion des fragments
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean locationPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initDrawerMenu();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        loggedInViewModel = new ViewModelProvider(this).get(LoggedInViewModel.class);
//        loggedInViewModel.getFirebaseUserLiveData().observe(this, new Observer<FirebaseUser>() {
//            @Override
//            public void onChanged(FirebaseUser firebaseUser) {
//                if(firebaseUser != null){
//                    tvUserInfo.setText(firebaseUser.getEmail());
//                }
//            }
//        });
//        loggedInViewModel.getUserLiveData().observe(this, new Observer<User>() {
//            @Override
//            public void onChanged(User user) {
//                if(user != null){
//                    tvUserInfo.setText(user.getFirstname() + " " + user.getLastname() + "\n" + user.getEmail());
//                }
//            }
//        });
        User currentUser = ((UserClient)(getApplicationContext())).getUser();
        if(currentUser != null){
            tvUserInfo.setText(currentUser.getFirstname() + " " + currentUser.getLastname() + "\n" + currentUser.getEmail());
        }

        loggedInViewModel.getLoggedOutLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loggedOut) {
                if(loggedOut){
                    Toast.makeText(MainActivity.this, getString(R.string.user_logged_out), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void initUI() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        View navHeaderView = navView.getHeaderView(0);
        tvUserInfo = navHeaderView.findViewById(R.id.tvUserInfo);
    }

    void initDrawerMenu(){
        setSupportActionBar(toolbar);
        //This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Options d'accessibilité
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        //Synchro le bouton hamburger et le menu
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
    }

    private void addFragment() {
        fragmentManager = getSupportFragmentManager();
        // Commencer la discution avec le fragment
        fragmentTransaction = fragmentManager.beginTransaction();
        //Appel du nouveau fragment
        HomeFragment homeFragment = new HomeFragment();
//        Fragment_01 fragment_01 = new Fragment_01();
        // Ajouter au container de fragment
        fragmentTransaction.add(R.id.fragment_container, homeFragment);

        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:// Id des items de note menu_principal
                setFragment(new HomeFragment());
                break;
            case R.id.nav_createUser:
                setFragment(new CreateUserFragment());
                break;
            case R.id.nav_usersList:
                setFragment(new UsersListFragment());
                break;
            case R.id.nav_newItinerary:
                setFragment(new NewItineraryFragment());
                break;
            case R.id.nav_myProfile:
//                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case R.id.nav_createComment:
                setFragment(new CommentFragment());
            case R.id.nav_logout:
//                authViewModel.logOut();
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                loggedInViewModel.loggedOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            default:
                setFragment(new UsersListFragment());
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void getLastKnownLocation() {
//        Log.d(TAG, "getLastKnownLocation: called.");
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull @NotNull Task<Location> task) {
//                if (task.isSuccessful()) {
//                    Location location = task.getResult();
//                    GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
//                    Log.d(TAG, "onComplete: latitude: " + geoPoint.getLatitude());
//                    Log.d(TAG, "onComplete: longitude: " + geoPoint.getLongitude());
//
//                    userViewModel.getUser(FirebaseAuth.getInstance().getUid());
//                    userViewModel.userLiveData.observe(MainActivity.this, userResult -> {
//                        if (userResult != null) {
////                            startActivity(new Intent(MainActivity.this, MainActivity.class));
//                            UserLocation userLocation = new UserLocation();
//                            userLocation.setGeoPoint(geoPoint);
//                            userLocation.setTimestamp(null);
//                            userLocation.setUser(userResult);
//                            userViewModel.saveUSerLocation(userLocation);
//                        } else {
//                            Toast.makeText(MainActivity.this, "Erreur récupération user", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//            }
//        });
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            getLastKnownLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (locationPermissionGranted) {
                    getLastKnownLocation();
                } else {
                    getLocationPermission();
                }
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if (checkMapServices()) {
            if (locationPermissionGranted) {
                getLastKnownLocation();
            } else {
                getLocationPermission();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkMapServices() {
        if (isServicesOK()) {
            if (isMapsEnabled()) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}