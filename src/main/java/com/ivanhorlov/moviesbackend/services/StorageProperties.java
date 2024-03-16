package com.ivanhorlov.moviesbackend.services;

import org.springframework.stereotype.Component;

@Component("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "users_avatars";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}