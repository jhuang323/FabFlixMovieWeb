import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jasypt.util.password.StrongPasswordEncryptor;
import recaptcha.RecaptchaVerifyUtils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

@WebServlet(name = "AndroidLoginServlet", urlPatterns = "/api/android_login")
public class AndroidLoginServlet extends HttpServlet {
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
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String queryUserEmail = "SELECT email, password, id" +
                " FROM customers" +
                " WHERE customers.email = ?";

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try (Connection conn = dataSource.getConnection()) {
            JsonObject responseJsonObject = new JsonObject();
            PreparedStatement statementUserEmailInput = conn.prepareStatement(queryUserEmail);
            statementUserEmailInput.setString(1, username);
            ResultSet resultUserEmailInput = statementUserEmailInput.executeQuery();
            try {
                if(resultUserEmailInput.next() != false){
                    int inputID = resultUserEmailInput.getInt("id");
                    String queryEmail = resultUserEmailInput.getString("email");
                    String queryPassword = resultUserEmailInput.getString("password");
                    boolean success = false;
                    success = new StrongPasswordEncryptor().checkPassword(password, queryPassword);
                    if(success){
                        // set this user into the session
                        request.getSession().setAttribute("user", new User(username,inputID));
                        //set empty Cart for User
                        request.getSession().setAttribute("Cart", new HashMap<String, MoviePrice>());

                        responseJsonObject.addProperty("status", "success");
                        responseJsonObject.addProperty("message", "success");
                    }
                    else{
                        responseJsonObject.addProperty("status", "fail");
                        responseJsonObject.addProperty("message", "Incorrect password");
                    }
                    resultUserEmailInput.close();
                    statementUserEmailInput.close();
                }
                else{
                    responseJsonObject.addProperty("status", "fail");
                    request.getServletContext().log("Login failed");
                    responseJsonObject.addProperty("message", "Email " + username + " doesn't exist");

                }

            } catch (Exception e) {
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "Error");
            }

            out.write(responseJsonObject.toString());

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
