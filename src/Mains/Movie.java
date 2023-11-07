package Mains;

import Mains.DirectorFilms;

import java.util.List;
public class Movie {

    private List<DirectorFilms> directorFilmsList;

    public void setDirectorFilmsList(List<DirectorFilms> directorFilmsList) {
        this.directorFilmsList = directorFilmsList;
    }

    public List<DirectorFilms> getDirectorFilmsList(){
        return this.directorFilmsList;
    }
    public void addDirectorFilm(DirectorFilms directorFilms){
        this.directorFilmsList.add(directorFilms);
    }

}
