package com.example.andoridproject;


public class ListViewItem  {
    private String name;
    private String date;
    private boolean isSelected = false;

    public ListViewItem()
    {//default constructor
    }
    public ListViewItem(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
