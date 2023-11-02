import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "AddStar", urlPatterns = "/_dashboard/api/AddStar")
public class AddStarServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String star_name = request.getParameter("fullName");
        String star_year = request.getParameter("birthYear");

        PrintWriter out = response.getWriter();
        try (Connection conn = dataSource.getConnection()) {

            JsonObject responseJsonObject = new JsonObject();
//            PreparedStatement statementUserEmailInput = conn.prepareStatement(queryUserEmail);
//            statementUserEmailInput.setString(1, username);
//            ResultSet resultUserEmailInput = statementUserEmailInput.executeQuery();

//            if(resultUserEmailInput.next()){
//                String queryEmail = resultUserEmailInput.getString("email");
//                String queryPassword = resultUserEmailInput.getString("password");
//                request.getSession().setAttribute("employee", new Employee(username));
//                responseJsonObject.addProperty("status", "success");
//                responseJsonObject.addProperty("message", "success");

//                resultUserEmailInput.close();
//                statementUserEmailInput.close();
//            }
//            else{
//                responseJsonObject.addProperty("status", "fail");
//                request.getServletContext().log("Login failed");
//                responseJsonObject.addProperty("message", "Error occured while adding Star");
//            }

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
