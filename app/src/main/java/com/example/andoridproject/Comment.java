package com.example.andoridproject;

public class Comment {
    private String comment;
    private String key;
    private String userID;
    private String userName;

    public Comment(String comment, String Uid, String UserName) {
        this.comment = comment;
        this.key = key;
        this.userID = Uid;
        this.userName = UserName;
    }
    public Comment() {}

    public String getUserName() {
        return userName;
    }

    public String getComment() {
        return comment;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key)
    {
        this.key = key;
    }
    public String getUserID() {
        return userID;
    }
}
