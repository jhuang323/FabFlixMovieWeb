import java.util.List;

public class Film {
    private String filmID;
    private String title;
    private String year;
    private List<Cat> catList;
    public List<Cat> getCatList(){
        return this.catList;
    }
    public void addCatList(Cat category){
        this.catList.add(category);
    }
    public String getFilmID() {
        return filmID;
    }

    public String getFilmTitle() {
        return title;
    }

    public String getFilmYear() {
        return year;
    }

    public void setFilmID(String filmID) {
        this.filmID = filmID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
