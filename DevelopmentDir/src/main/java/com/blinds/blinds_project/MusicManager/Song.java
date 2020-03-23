package com.blinds.blinds_project.MusicManager;


public class Song {

    private String name, artist, link, userWhoAdded;

    public Song(String name, String artist, String link, String userWhoAdded){
        this.name = name;
        this.artist = artist;
        this.link = link;
        this.userWhoAdded = userWhoAdded;
    }

    public Song(Song s){
        this.name = s.name;
        this.artist = s.artist;
        this.link = s.link;
        this.userWhoAdded = s.userWhoAdded;
    }

    public String getName(){
        return this.name;
    }

    public String getArtist(){
        return this.artist;
    }

    public String getLink(){
        return this.link;
    }

    public String getUserWhoAdded(){
        return this.userWhoAdded;
    }

    public String toString(){
        return this.name + " - " + this.artist;
    }

    protected  void setName(String s){
        this.name = s;
    }

}
