import Mains.Cat;

import java.util.List;

public class MappedMovie {
    private String movieID;
    private String movieTitle;
    private String movieYear;
    private String directorName;
    private List<Cat> genreList;

    public List<Cat> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<Cat> genreList) {
        this.genreList = genreList;
    }
    public String getMovieID() {
        return movieID;
    }

    public String getMovieTitle() {
        return movieTitle;
    }
    public String getMovieYear() {
        return movieYear;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }
    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }
}