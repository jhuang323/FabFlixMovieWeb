import java.util.List;
public class Casts {

    private List<DirectorFilms_Casts> directorFilmsList;

    public void setDirectorFilmsList(List<DirectorFilms_Casts> directorFilmsList) {
        this.directorFilmsList = directorFilmsList;
    }

    public List<DirectorFilms_Casts> getDirectorFilmsList(){
        return this.directorFilmsList;
    }
    public void addDirectorFilm(DirectorFilms_Casts directorFilms){
        this.directorFilmsList.add(directorFilms);
    }

}