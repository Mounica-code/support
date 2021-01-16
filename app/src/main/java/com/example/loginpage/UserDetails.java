package com.example.loginpage;

import java.util.Date;

public class UserDetails {
    static String username = "";
    static String password = "";
    static String chatWith = "";
    static long Date ;

    public UserDetails(String date) {
        Date = new Date().getTime();
    }

    public static long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }
}
