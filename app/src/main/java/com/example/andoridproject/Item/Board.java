package com.example.andoridproject.Item;

import java.io.Serializable;

public class Board implements Serializable {
    private String title;
    private String content;
    private String userID;
    private String userName;
    private String date;
    private String key = null;
    private int comments;
    private int stars;



    Board(){}

    public Board(String title, String content, String userID, String userName, String date,int comments,int stars)
    {
        this.title = title;
        this.content=content;
        this.userID = userID;
        this.userName = userName;
        this.date = date;
        this.comments = comments;
        this.stars = stars;
    }
    public void setComments(int comments){ this.comments = comments; }
    public void setStars(int stars){ this.stars = stars; }
    public int getComments(){ return comments; }
    public int getStars() { return stars; }
    public String getTitle(){ return title; }
    public String getContent(){ return content; }
    public String getUserID(){ return userID; }
    public String getUserName(){ return userName; }
    public String getKey(){ return key; }
    public String getDate(){ return date; }
    public void setKey(String key){ this.key = key; }
}
