package test;

import java.util.ArrayList;

public class Movie_Casts {
    private String movieID;
    private String movieTitle;

    private String Director;
    private ArrayList<String> starName;

    public Movie_Casts(){
        this.starName = new ArrayList<String>();
    }

    public ArrayList<String> getstarlist(){
        return this.starName;
    }

    public void setDirector(String adir) {
        this.Director = adir;
    }

    public String getMovieID() {
        return movieID;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public ArrayList<String> getStarName() {
        return starName;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setStarName(String starName) {
        this.starName.add(starName);
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Movie Title: " + movieTitle + ", ID: " + movieID + ", Star Name: " + starName +
                "\n");
        return sb.toString();
    }
}