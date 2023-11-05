import java.util.List;

public class Director {

    private String name;
    private String id;
    public String getDirectorName(){
        return this.name;
    }
    public String getDirectorID(){
        return this.id;
    }
    public void setDirectorName(String name){
        this.name = name;
    }
    public void setDirectorID(String id){
        this.name = id;
    }
//    public String toString() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("Director Details - ");
//        sb.append("Name:" + name);
//        sb.append(",");
//        sb.append("ID:" + id);
//        sb.append(".");
//
//        return sb.toString();
//    }
}
