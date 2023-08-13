package com.example.androidcomicreader.Model;

public class MangaCategory {
    int MangaID;
    int CategoryID;
    int ID;

    public MangaCategory() {
    }

    public MangaCategory(int mangaID, int categoryID,int ID) {
        MangaID = mangaID;
        CategoryID = categoryID;
        this.ID = ID;
    }

    public int getMangaID() {
        return MangaID;
    }

    public void setMangaID(int mangaID) {
        MangaID = mangaID;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        String test = String.valueOf(this.ID);
        return test;
    }
}
