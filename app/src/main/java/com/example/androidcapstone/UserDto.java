package com.example.androidcapstone;

import com.google.gson.annotations.Expose;




public class UserDto {

    private String username;

    private String password;

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}