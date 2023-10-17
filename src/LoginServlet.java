import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }


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

        


        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try (Connection conn = dataSource.getConnection()) {

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
                out.write(responseJsonObject.toString());
            }

        } catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Log error to localhost log
            request.getServletContext().log("Error:", e);
            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
    }
}
