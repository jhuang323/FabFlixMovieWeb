/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
public class MoviePrice {

    private int count;
    private float price;

    public MoviePrice(int count, float price) {

        this.count = 0;
        this.price = 0;
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

}
