import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class UpdateSecurePassword {

    /*
     * 
     * This program updates your existing moviedb customers table to change the
     * plain text passwords to encrypted passwords.
     * 
     * You should only run this program **once**, because this program uses the
     * existing passwords as real passwords, then replace them. If you run it more
     * than once, it will treat the encrypted passwords as real passwords and
     * generate wrong values.
     * 
     */
    public static void main(String[] args) throws Exception {

        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        Statement statement = connection.createStatement();

        Statement statementEmp = connection.createStatement();

        // change the customers table password column from VARCHAR(20) to VARCHAR(128)
        String alterQuery = "ALTER TABLE customers MODIFY COLUMN password VARCHAR(128)";
        int alterResult = statement.executeUpdate(alterQuery);
        //do same for employee table
        String alterQueryEmployee = "ALTER TABLE employees MODIFY COLUMN password VARCHAR(128)";
        int alterResultEmp = statementEmp.executeUpdate(alterQueryEmployee);


        System.out.println("altering customers table schema completed, " + alterResult + " rows affected " + alterResultEmp);

        // get the ID and password for each customer
        String query = "SELECT id, password from customers";
        String queryEmp = "SELECT email, password from employees";

        ResultSet rs = statement.executeQuery(query);
        ResultSet rsEmp = statementEmp.executeQuery(queryEmp);

        // we use the StrongPasswordEncryptor from jasypt library (Java Simplified Encryption) 
        //  it internally use SHA-256 algorithm and 10,000 iterations to calculate the encrypted password
        PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

        ArrayList<String> updateQueryList = new ArrayList<>();

        System.out.println("encrypting password (this might take a while)");
        while (rs.next()) {
            // get the ID and plain text password from current table
            String id = rs.getString("id");
            String password = rs.getString("password");
            
            // encrypt the password using StrongPasswordEncryptor
            String encryptedPassword = passwordEncryptor.encryptPassword(password);

            // generate the update query
            String updateQuery = String.format("UPDATE customers SET password='%s' WHERE id=%s;", encryptedPassword,
                    id);
            updateQueryList.add(updateQuery);
        }
        rs.close();

        ArrayList<String> updateQueryListEmp = new ArrayList<>();

        while (rsEmp.next()) {
            // get the ID and plain text password from current table
            String email = rsEmp.getString("email");
            String password = rsEmp.getString("password");

            System.out.println("em " + email + "pass " + password);

            // encrypt the password using StrongPasswordEncryptor
            String encryptedPassword = passwordEncryptor.encryptPassword(password);

            // generate the update query
            String updateQuery = String.format("UPDATE employees SET password='%s' WHERE email='%s';", encryptedPassword,
                    email);
            updateQueryListEmp.add(updateQuery);
        }
        rsEmp.close();

        // execute the update queries to update the password
        System.out.println("updating password");
        int count = 0;
        for (String updateQuery : updateQueryList) {
            int updateResult = statement.executeUpdate(updateQuery);
            count += updateResult;
        }


        int countEmp = 0;
        for (String updateQuery : updateQueryListEmp) {
            System.out.println(updateQuery);
            int updateResult = statementEmp.executeUpdate(updateQuery);
            countEmp += updateResult;
        }

        System.out.println("updating password completed, " + count + " rows affected " + countEmp);

        statement.close();
        statementEmp.close();
        connection.close();

        System.out.println("finished");

    }

}
