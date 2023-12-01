package _dashboard;

import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;


@WebServlet(name = "AddMovie", urlPatterns = "/_dashboard/api/AddMovie")
public class AddMovieServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedbmaster");
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
        //get movie info
        String movie_title = request.getParameter("title");
        int movie_year = 0;

        if(!request.getParameter("year").isEmpty())
        {
            movie_year = Integer.parseInt(request.getParameter("year"));
        }

        String movie_director = request.getParameter("director");
        String movie_genre = request.getParameter("genre");



        PrintWriter out = response.getWriter();
        try (Connection conn = dataSource.getConnection()) {

            JsonObject responseJsonObject = new JsonObject();

            CallableStatement insertMoviesCS = conn.prepareCall("{call add_movie(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            //set in param
            insertMoviesCS.setString(1,movie_title);


            if(movie_year == 0){
                insertMoviesCS.setNull(2,Types.INTEGER);
            }
            else{
                insertMoviesCS.setInt(2,movie_year);
            }

            insertMoviesCS.setString(3,movie_director);
            insertMoviesCS.setString(4,star_name);

            if(star_year == 0){
                insertMoviesCS.setNull(5,Types.INTEGER);
            }
            else{
                insertMoviesCS.setInt(5,star_year);
            }

            insertMoviesCS.setString(6,movie_genre);

            insertMoviesCS.registerOutParameter(7,Types.INTEGER);
            insertMoviesCS.registerOutParameter(8,Types.VARCHAR);
            insertMoviesCS.registerOutParameter(9,Types.VARCHAR);
            insertMoviesCS.registerOutParameter(10,Types.INTEGER);

            //exec statement
            insertMoviesCS.executeUpdate();

            //retrieve return
            int rsuccess = insertMoviesCS.getInt(7);
            String rmovieID = insertMoviesCS.getString(8);
            String rstarID = insertMoviesCS.getString(9);
            int rgenreID = insertMoviesCS.getInt(10);



            responseJsonObject.addProperty("status",rsuccess);

            if (rsuccess == 1){
                responseJsonObject.addProperty("message","Successfully added" +
                        " movie ID: " + rmovieID + " star ID: " + rstarID + "" +
                        " genre ID: " + rgenreID
                );
            }
            else {
                responseJsonObject.addProperty("message","Failed to " +
                        "add New Movie"
                );
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
