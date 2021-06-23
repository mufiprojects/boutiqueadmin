package com.example.acer.boutiqueadmin;

public class user {
    private String userName;
    private Boolean isUserActive;

    public user(){

    }
    public user(String userName, Boolean isUserActive) {
        this.userName = userName;
        this.isUserActive = isUserActive;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getUserActive() {
        return isUserActive;
    }

    public void setUserActive(Boolean userActive) {
        isUserActive = userActive;
    }
}
