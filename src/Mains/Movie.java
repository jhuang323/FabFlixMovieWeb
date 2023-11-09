package Mains;
public class Movie {

    private String movieID;
    private String movieTitle;
    private String movieYear;
    private String directorName;
    private Cat cat;

    public String getMovieID() {
        return movieID;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieYear() {
        return movieYear;
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

    public String getDirectorName() {
        return directorName;
    }
    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }
}
