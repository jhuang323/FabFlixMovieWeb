import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "SingleStarServlet", urlPatterns = "/api/single-star")
public class SingleStarServlet extends HttpServlet {
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
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");

        // The log message can be found in localhost log
        request.getServletContext().log("getting id: " + id);

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource

            // Construct a query with parameter represented by "?" that grabs the star’s name and birth year
            String queryStarInfo = "SELECT stars.name, stars.birthYear" +
                    " FROM stars" +
                    " WHERE stars.id = ?;";

            // Declare our statement
            PreparedStatement statementStarInfo = conn.prepareStatement(queryStarInfo);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            statementStarInfo.setString(1, id);

            // Perform the query
            ResultSet resultSetStarInfo = statementStarInfo.executeQuery();

            JsonObject jsonObjStar = new JsonObject();

            // Move pointer to next, which is name and birthYear
            resultSetStarInfo.next();

            //The names in the .getString() call must match the name of the columns from the SELECT line
            String starName = resultSetStarInfo.getString("name");
            String starDob = resultSetStarInfo.getString("birthYear");
            if(resultSetStarInfo.wasNull()){
                starDob = "N/A";
            }

            // Create a JsonObject based on the data we retrieve from rs
            jsonObjStar.addProperty("star_name", starName);
            jsonObjStar.addProperty("star_dob", starDob);

            resultSetStarInfo.close();

            statementStarInfo.close();

            // Construct a query with parameter represented by "?" that grabs all movie ids and titles star is in
            String queryStarMovieInfo = "SELECT movies.id, movies.title" +
                    " FROM stars" +
                    " JOIN stars_in_movies" +
                    " ON stars.id = stars_in_movies.starId" +
                    " JOIN movies" +
                    " ON stars_in_movies.movieId = movies.id" +
                    " WHERE stars.id = ?;";
            // Declare our statement
            PreparedStatement statementStarMovieInfo = conn.prepareStatement(queryStarMovieInfo);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            statementStarMovieInfo.setString(1, id);

            // Perform the query
            ResultSet resultSetStarMovieInfo = statementStarMovieInfo.executeQuery();

            //Create Movie jsonArray
            JsonArray jsonArrayMovies = new JsonArray();
            // Iterate through each row of rs
            while (resultSetStarMovieInfo.next()) {

                //The names in the .getString() call must match the name of the columns from the SELECT line
                String movieId = resultSetStarMovieInfo.getString("id");
                String movieTitle = resultSetStarMovieInfo.getString("title");

                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movieId);
                jsonObject.addProperty("movie_title", movieTitle);

                jsonArrayMovies.add(jsonObject);
            }
            //Add jsonArrayMovies to jsonObjStar object
            jsonObjStar.add("movies", jsonArrayMovies);

            //Closing StarMovieInfo statement and resultSet
            resultSetStarMovieInfo.close();
            statementStarMovieInfo.close();

            // Write JSON string to output
            out.write(jsonObjStar.toString());
            // Set response status to 200 (OK)
            response.setStatus(200);

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

        // Always remember to close db connection after usage. Here it's done by try-with-resources

    }

}
