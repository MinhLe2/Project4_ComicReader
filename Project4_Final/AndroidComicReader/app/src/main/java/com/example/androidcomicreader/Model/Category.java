package com.example.androidcomicreader.Model;

public class Category {
    private int ID;
    private String Name;

    public Category(){
    }

    public Category(int ID, String name) {
        this.ID = ID;
        Name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return this.Name;
    }
}
