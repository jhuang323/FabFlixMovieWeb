import java.util.List;
import java.util.ArrayList;
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
//        sb.append("Director Details - ");
//        sb.append("ID:" + director.getDirectorID());
//        sb.append(",\n");
//        sb.append("Name:" + director.getDirectorName());
//        sb.append(",\n");
//        sb.append("Film Details - ");
//        sb.append(",\n");
//        sb.append("Name1: " + filmList.get(0).getFilmTitle());
//        sb.append(",\n");
//        sb.append("ID1: " + filmList.get(0).getFilmID());
//        sb.append(",\n");
////        sb.append("Name2: " + filmList.get(1).getFilmTitle());
////        sb.append(",\n");
////        sb.append("ID2: " + filmList.get(1).getFilmID());
////        sb.append(",\n");
////        sb.append("Name3: " + filmList.get(2).getFilmTitle());
////        sb.append(",\n");
////        sb.append("ID3: " + filmList.get(2).getFilmID());
//        sb.append(",\n");
////        sb.append(",");
////        sb.append("ID:" + director.getDirectorID());
//        sb.append(".");

        return sb.toString();
    }
}
