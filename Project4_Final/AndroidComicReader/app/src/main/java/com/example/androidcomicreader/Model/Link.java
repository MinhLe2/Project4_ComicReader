package com.example.androidcomicreader.Model;

public class Link {
    private int ID ;
    private String linkName;
    private String Link;
    private int ChapterID;
    private int mFlagImage;

    public Link(){
    }

    public Link(int ID, String link, int chapterID) {
        this.ID = ID;
        Link = link;
        ChapterID = chapterID;
    }
    public Link(  String linkName,String link,int mFlagImagee) {
        this.linkName = linkName;
        Link = link;
        mFlagImage = mFlagImagee;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public int getChapterID() {
        return ChapterID;
    }

    public void setChapterID(int chapterID) {
        ChapterID = chapterID;
    }

    public int getmFlagImage() {
        return mFlagImage;
    }

    public void setmFlagImage(int mFlagImage) {
        this.mFlagImage = mFlagImage;
    }

    @Override
    public String toString() {
        return this.Link;
    }
}
