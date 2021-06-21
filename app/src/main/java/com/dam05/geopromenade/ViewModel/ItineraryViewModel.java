package com.dam05.geopromenade.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.dam05.geopromenade.Model.Itinerary;
import com.dam05.geopromenade.Repository.ItineraryRepository;

public class ItineraryViewModel extends AndroidViewModel {
    private ItineraryRepository itineraryRepository;

    public ItineraryViewModel(@NonNull Application application) {
        super(application);

        itineraryRepository = new ItineraryRepository(application);
    }

    public void saveItinerary(Itinerary itinerary){
        itineraryRepository.saveItinerary(itinerary);
    }
}
