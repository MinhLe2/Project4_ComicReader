package com.example.androidcomicreader.Model;

import java.io.Serializable;

public class Comic implements Serializable {
    private int ID;
    private String Name;
    private String Image;
    private int mFlagImage;

    public Comic() {
    }

    public Comic(int ID, String name, String image) {
        this.ID = ID;
        Name = name;
        Image = image;
    }

    public Comic( String name, String image, int FlagImage) {
        Name = name;
        Image = image;
        mFlagImage = FlagImage;
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getmFlagImage() {
        return mFlagImage;
    }

    public void setmFlagImage(int mFlagImage) {
        this.mFlagImage = mFlagImage;
    }

    @Override
    public String toString() {
        return this.Name;
    }
}
