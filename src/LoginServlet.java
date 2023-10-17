import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("Username");
        String password = request.getParameter("Password");

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Construct a query with parameter represented by "?" where it returns the users info if it exists in the customer table
        String queryUserInput = "SELECT *" +
                " FROM customers" +
                " WHERE customers.email = ?" +
                "AND customers.password = ?;";

        // Declare our statement
        PreparedStatement statementUserInput = conn.prepareStatement(queryUserInput);

        // Set the parameter represented by "?" in the query to the username and password we get from the form,
        // num 1 indicates the first "?" in the query
        statementUserInput.setString(1, username);
        statementUserInput.setString(2, password);

        // Perform the query
        ResultSet resultSetStarInfo = statementUserInput.executeQuery();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        JsonObject responseJsonObject = new JsonObject();
        if (username.equals("anteater") && password.equals("123456")) {
            // Login success:

            // set this user into the session
            request.getSession().setAttribute("user", new User(username));

            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");

        } else {
            // Login fail
            responseJsonObject.addProperty("status", "fail");
            // Log to localhost log
            request.getServletContext().log("Login failed");
            // sample error messages. in practice, it is not a good idea to tell user which one is incorrect/not exist.
            if (!username.equals("anteater")) {
                responseJsonObject.addProperty("message", "User " + username + " doesn't exist");
            } else {
                responseJsonObject.addProperty("message", "Incorrect password");
            }
        }
        response.getWriter().write(responseJsonObject.toString());
    }
}
