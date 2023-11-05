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
import java.sql.CallableStatement;
import java.sql.Types;
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

        int star_year = 0;

        if(!request.getParameter("birthYear").isEmpty())
        {
            star_year = Integer.parseInt(request.getParameter("birthYear"));
        }



        PrintWriter out = response.getWriter();
        try (Connection conn = dataSource.getConnection()) {

            JsonObject responseJsonObject = new JsonObject();

            CallableStatement insertStarsCS = conn.prepareCall("{call add_star(?, ?, ?, ?)}");

            //set in param
            insertStarsCS.setString(1,star_name);

            if(star_year == 0){
                insertStarsCS.setNull(2,Types.INTEGER);
            }
            else{
                insertStarsCS.setInt(2,star_year);
            }


            //set out param register them
            insertStarsCS.registerOutParameter(3,Types.INTEGER);
            insertStarsCS.registerOutParameter(4,Types.VARCHAR);

            //exec statement
            insertStarsCS.executeUpdate();

            //retrieve return
            int rsuccess = insertStarsCS.getInt(3);
            String rstarID = insertStarsCS.getString(4);

            responseJsonObject.addProperty("status",rsuccess);

            if (rsuccess == 1){
                responseJsonObject.addProperty("message","Successfully added" +
                        " star ID: " + rstarID
                );
            }
            else {
                responseJsonObject.addProperty("message","Failed to " +
                        "add New Star"
                );
            }



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
