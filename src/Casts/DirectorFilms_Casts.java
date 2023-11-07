package Casts;

import java.util.Iterator;
import java.util.List;

public class DirectorFilms_Casts {
    private Director_Casts director;
    private List<Movie_Casts> movieList;
    public List<Movie_Casts> getMovieList(){
        return this.movieList;
    }
    public void setMovieList(List<Movie_Casts> movieList){
        this.movieList = movieList;
    }
    public void addMovie(Movie_Casts movie){
        this.movieList.add(movie);
    }

    public Director_Casts getDirector() {
        return director;
    }
    public void setDirector(Director_Casts director) {
        this.director = director;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if(movieList != null){
            sb.append("Number of Movies: " + movieList.size() + "\n");
            sb.append("Director: " + director.getDirectorName() + ", ID: " + director.getDirectorID() + "\n");
            Iterator<Movie_Casts> it = movieList.iterator();
            while (it.hasNext()) {
                sb.append(it.next().toString());
            }
        }
        else{
            sb.append("No movies found \n");
        }
        sb.append("\n");
        return sb.toString();
    }
}