package Mains;

import java.util.List;

public class Cat {

    private List<String> genreNames;
    public List<String> getGenreNames(){
        return this.genreNames;
    }
    public void setGenreNames(List<String> genreNames){
        this.genreNames = genreNames;
    }
    public void addGenre(String genre){
        genreNames.add(genre);
    }

}
