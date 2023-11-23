package edu.uci.ics.fabflixmobile.data.model;

import java.util.ArrayList;

/**
 * Movie class that captures movie information for movies retrieved from MovieListActivity
 */
public class Movie {
    private final String name;
    private final String id;
    private final String director;
    private final String genres;
    private final String stars;
    private final String rating;

    public Movie(String name, String id, String director, String genres, String stars,
                 String rating) {
        this.name = name;
        this.id = id;
        this.director = director;
        this.genres = genres;
        this.stars = stars;
        this.rating = rating;

    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDirector() {
        return director;
    }

    public String getGenres() {
        return genres;
    }

    public String getStars() {
        return stars;
    }
    public String getRating(){return rating;}
}