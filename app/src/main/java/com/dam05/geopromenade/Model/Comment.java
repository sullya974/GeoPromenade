package com.dam05.geopromenade.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Comment {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private User author;
    private String content;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWayPointId() {
        return wayPointId;
    }

    public void setWayPointId(String wayPointId) {
        this.wayPointId = wayPointId;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    private String wayPointId;
    private @ServerTimestamp Date dateCreation;


    public Comment() {
    }

    public Comment(User author, String content, String wayPointId, Date dateCreation) {
        this.author = author;
        this.content = content;
        this.wayPointId = wayPointId;
        this.dateCreation = dateCreation;
    }
}
