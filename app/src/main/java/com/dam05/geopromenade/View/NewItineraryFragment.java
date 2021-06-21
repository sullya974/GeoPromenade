package com.dam05.geopromenade.View;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dam05.geopromenade.Model.Itinerary;
import com.dam05.geopromenade.Model.User;
import com.dam05.geopromenade.R;
import com.dam05.geopromenade.ViewModel.ItineraryViewModel;
import com.dam05.geopromenade.ViewModel.LoggedInViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;

import static com.dam05.geopromenade.Common.Constants.MAPVIEW_BUNDLE_KEY;

public class NewItineraryFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "NewItineraryFragment";
    private MapView mapView;
    Button btnStart;
    Button btnFinish;

    private ItineraryViewModel itineraryViewModel;
    LoggedInViewModel loggedInViewModel;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap googleMap;
//    private UserLoca

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_itinerary, container, false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        itineraryViewModel = new ViewModelProvider(getActivity()).get(ItineraryViewModel.class);

//        mapView = view.findViewById(R.id.mapViewItinerary);
        initUI(view);
        initGoogleMap(savedInstanceState);

        return view;
    }

    public void btnStartItineraryClick(View view) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                Log.d(TAG, "onComplete: latitude: " + geoPoint.getLatitude());
                Log.d(TAG, "onComplete: longitude: " + geoPoint.getLongitude());

                loggedInViewModel = new ViewModelProvider(getActivity()).get(LoggedInViewModel.class);
                loggedInViewModel.getUserLiveData().observe(getActivity(), new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        if(user != null){
                            Itinerary itinerary = new Itinerary();
                            itinerary.setStartGeoPoint(geoPoint);
                            itinerary.setStartTimestamp(null);
                            itinerary.setUser(user);
                            itineraryViewModel.saveItinerary(itinerary);

                            Toast.makeText(getActivity(), "Starting itinerary info has been succesfully registered", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(), "Erreur récupération user", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void initUI(View view){
        mapView = view.findViewById(R.id.mapViewItinerary);
        btnStart = view.findViewById(R.id.btnStart);
        btnFinish = view.findViewById(R.id.btnFinish);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        Log.d(TAG, "onComplete: latitude: " + geoPoint.getLatitude());
                        Log.d(TAG, "onComplete: longitude: " + geoPoint.getLongitude());

                        loggedInViewModel = new ViewModelProvider(getActivity()).get(LoggedInViewModel.class);
                        loggedInViewModel.getUserLiveData().observe(getActivity(), new Observer<User>() {
                            @Override
                            public void onChanged(User user) {
                                if(user != null){
                                    Itinerary itinerary = new Itinerary();
                                    itinerary.setStartGeoPoint(geoPoint);
                                    itinerary.setStartTimestamp(null);
                                    itinerary.setUser(user);
                                    itineraryViewModel.saveItinerary(itinerary);

                                    Toast.makeText(getActivity(), "Starting itinerary info has been succesfully registered", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getActivity(), "Erreur récupération user", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}