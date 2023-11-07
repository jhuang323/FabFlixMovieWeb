package Actors;

public class Actor {
    private String starName;
    private String birthYear;

    public String getBirthYear() {
        return birthYear;
    }
    public String getStarName() {
        return starName;
    }
    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }
    public void setStarName(String starName) {
        this.starName = starName;
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Actor Name: " + starName + ", Birth Year: " + birthYear + "\n");
        return sb.toString();
    }
}