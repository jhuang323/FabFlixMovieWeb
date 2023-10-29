/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
public class MoviePrice {

    private int count;
    private float price;

    private String movieTitle;
    private String movieId;

    private int SalesId;

    public MoviePrice(String aMovId, String amovTitle, float price) {

        this.movieId = aMovId;
        this.movieTitle = amovTitle;

        this.count = 0;
        this.price = price;
        this.SalesId = -1;
    }

    public String getMovieId(){
        return this.movieId;
    }
    public String getMovieTitle(){
        return this.movieTitle;
    }
    public float getMoviePrice(){
        return this.price;
    }
    public int getMovieCount(){
        return this.count;
    }
    public void setMoviePrice(float price){
        this.price = price;
    }
    public void setMovieCount(int count){
        this.count += count;
    }

    public void setSalesId(int aSid){
        this.SalesId = aSid;
    }
    public int getSalesId(){
        return this.SalesId;
    }

}
