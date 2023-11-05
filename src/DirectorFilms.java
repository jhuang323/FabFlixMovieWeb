import java.util.List;
import java.util.ArrayList;
public class DirectorFilms {
    private Director director;
    private List<Film> filmList;
    public List<Film> getFilmList(){
        return this.filmList;
    }
    public void addFilm(Film film){
        this.filmList.add(film);
    }

    public Director getDirector() {
        return director;
    }
    public void setDirector(Director director) {
        this.director = director;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Director Details - ");
        sb.append("Name:" + director.getDirectorName());
        sb.append(",");
        sb.append("ID:" + director.getDirectorID());
        sb.append(".");

        return sb.toString();
    }
}
