package com.example.myapplication;

public class galleryVariables {

    public String description,imageLink;

    public galleryVariables(String description, String imageLink) {
        this.description = description;
        this.imageLink = imageLink;
    }

    public galleryVariables() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
