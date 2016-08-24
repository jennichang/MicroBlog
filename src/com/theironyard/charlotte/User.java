package com.theironyard.charlotte;

import java.util.ArrayList;

/**
 * Created by jenniferchang on 8/24/16.
 */
public class User {
    String name;
    //private String password;
    ArrayList<Message> messagesList = new ArrayList<>();

    public User(String name) {
        this.name = name;
        //this.password = password;
    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
}
