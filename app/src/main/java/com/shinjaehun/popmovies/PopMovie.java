package com.shinjaehun.popmovies;

import java.io.Serializable;

public class PopMovie implements Serializable {

    String title;
    String poster_Location;
    String backdrop_path;
    String overview;
    String release_Date;
    String rating;
    String popularity;

    public PopMovie(String title, String poster_Location, String backdrop_path, String overview, String release_Date, String rating, String popularity) {
        this.title = title;
        this.poster_Location = poster_Location;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.release_Date = release_Date;
        this.rating = rating;
        this.popularity = popularity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPoster_Location(String poster_Location) {
        this.poster_Location = poster_Location;
    }

    public void setRelease_Date(String release_Date) {
        this.release_Date = release_Date;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }


}
