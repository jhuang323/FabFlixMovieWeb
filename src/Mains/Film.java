package Mains;

import java.util.Iterator;
import java.util.List;

public class Film {
    private String filmID;
    private String title;
    private String year;
    private List<Cat> catList;
    public List<Cat> getCatList(){
        return this.catList;
    }
    public void setCatList(List<Cat> catList){
        this.catList = catList;
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
    public Cat latestCategory(){
        int latestCatIndex = catList.size() - 1;
        return catList.get(latestCatIndex);
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if(catList != null) {
            sb.append("Number of Cats for Film: " + catList.size() + "\n");
            sb.append("Film Title: " + title + ", ID: " + filmID + ", Year: " + year + "\n");

            Iterator<Cat> it = catList.iterator();
            while (it.hasNext()) {
                sb.append(it.next().toString());
            }
            sb.append("\n");
        }
        else{
            sb.append("No categories found \n");
        }
        return sb.toString();
    }
}
