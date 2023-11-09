package test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Films_Casts {
    private HashMap<String,Movie_Casts> movieList;
    public HashMap<String,Movie_Casts> getMovieMap(){
        return this.movieList;
    }

    public Films_Casts(){
        this.movieList = new HashMap<String,Movie_Casts>();
    }

    public void addMovieMap(String amid,String adirname){
        Movie_Casts tmpmc = new Movie_Casts();
        tmpmc.setDirector(adirname);
        this.movieList.put(amid,tmpmc);

    }
    public void addMovie(String akey,Movie_Casts movie){
        this.movieList.put(akey,movie);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if(movieList != null){
            sb.append("Number of Movies: " + movieList.size() + "\n");

        }
        else{
            sb.append("No movies found \n");
        }
        sb.append("\n");
        return sb.toString();
    }
}