package Mains;

import Mains.Film;

import java.util.Iterator;
import java.util.List;

public class DirectorFilms {
    private Director director;
    private List<Film> filmList;
    public List<Film> getFilmList(){
        return this.filmList;
    }
    public void setFilmList(List<Film> filmList){
        this.filmList = filmList;
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
        sb.append("Number of Films: " + filmList.size() + "\n");
        sb.append("Director: " + director.getDirectorName() + ", ID: " + director.getDirectorID() + "\n");
        Iterator<Film> it = filmList.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
        }
        sb.append("\n");
        return sb.toString();
    }
}
