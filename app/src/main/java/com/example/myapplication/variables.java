package com.example.myapplication;

public class variables {

    public String names,imageLink,username;

    public variables(String names, String imageLink) {
        this.names = names;
        this.imageLink = imageLink;
        this.username=username;
    }

    public variables() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
