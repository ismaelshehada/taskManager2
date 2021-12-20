package com.example.taskmanager.Modle;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Tasks implements Serializable {
    @DocumentId
    private String id;
    private String title;
    private String summary;
    private String description;
    private int favorite;
    private String date;
    private String time;
    private String image;
    private String longitude;
    private String latitude;

    public Tasks() {
    }

    public Tasks(String id, String title, String summary, String description, int favorite, String date, String time, String image, String longitude, String latitude) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.description = description;
        this.favorite = favorite;
        this.date = date;
        this.time = time;
        this.image = image;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFavorite() {
        return favorite;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
