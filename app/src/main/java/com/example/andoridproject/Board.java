package com.example.andoridproject;

import java.io.Serializable;

public class Board implements Serializable {
    private String title;
    private String content;
    private String userID;
    private String userName;
    private String date;
    private String key = null;

    Board(){}

    Board(String title, String content, String userID, String userName,String date)
    {
        this.title = title;
        this.content=content;
        this.userID = userID;
        this.userName = userName;
        this.date = date;
    }
    public String getTitle(){ return title; }
    public String getContent(){ return content; }
    public String getUserID(){ return userID; }
    public String getUserName(){ return userName; }
    public String getKey(){ return key; }
    public String getDate(){ return date; }
    public void setKey(String key){ this.key = key; }
}
