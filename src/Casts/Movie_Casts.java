package Casts;

import java.util.Iterator;
import java.util.List;

public class Movie_Casts {
    private String movieID;
    private String movieTitle;
    private String starName;
    public String getMovieID() {
        return movieID;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getStarName() {
        return starName;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Movie Title: " + movieTitle + ", ID: " + movieID + ", Star Name: " + starName +
                "\n");
        return sb.toString();
    }
}