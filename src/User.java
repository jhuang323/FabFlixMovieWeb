/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
public class User {

    private final int userid;
    private final String username;

    public User(String username,int auserId) {
        this.username = username;
        this.userid = auserId;
    }
    public int getUserid(){
        return this.userid;
    }

}
