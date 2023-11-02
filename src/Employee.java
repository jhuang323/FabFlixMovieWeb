/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
public class Employee {

    private final String email;

    public Employee(String aemail) {
        this.email = aemail;

    }
    public String getUserEmail(){
        return this.email;
    }

}
