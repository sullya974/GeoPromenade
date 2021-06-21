package com.dam05.geopromenade.Model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.time.Duration;
import java.util.Date;

public class Itinerary {
    private String id;
    private GeoPoint startGeoPoint;
    private GeoPoint finishGeoPoint;
    private @ServerTimestamp Date startTimestamp;
    private @ServerTimestamp Date finishTimestamp;
    @Exclude
    private Duration duration;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GeoPoint getStartGeoPoint() {
        return startGeoPoint;
    }

    public void setStartGeoPoint(GeoPoint startGeoPoint) {
        this.startGeoPoint = startGeoPoint;
    }

    public GeoPoint getFinishGeoPoint() {
        return finishGeoPoint;
    }

    public void setFinishGeoPoint(GeoPoint finishGeoPoint) {
        this.finishGeoPoint = finishGeoPoint;
    }

    public Date getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Date getFinishTimestamp() {
        return finishTimestamp;
    }

    public void setFinishTimestamp(Date finishTimestamp) {
        this.finishTimestamp = finishTimestamp;
    }
    @Exclude
    public Duration getDuration() {
        return duration;
    }
    @Exclude
    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
