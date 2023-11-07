package Mains;

import java.util.Iterator;

public class Cat {

    private String name;
    public String getGenreName(){
        return this.name;
    }
    public void setGenreName(String name){
        this.name = name;
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Cat Name: " + name + "\n");
        return sb.toString();
    }
}
