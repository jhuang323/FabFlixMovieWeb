import com.google.gson.JsonArray;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


// Declaring a WebServlet called StarsServlet, which maps to url "/api/stars"
@WebServlet(name = "MovieListServlet", urlPatterns = "/api/movie-list")
public class MovieListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            // Declare our statement
            Statement statementTop20 = conn.createStatement();
            Statement statementMovieInfo = conn.createStatement();
            Statement statementRatings = conn.createStatement();

            //Execute once to grab necessary information
            String queryTop20 = "CREATE TEMPORARY TABLE top20 AS\n" +
                    "SELECT movies.id\n" +
                    "FROM movies\n" +
                    "JOIN ratings ON movies.id=ratings.movieId\n" +
                    "ORDER BY ratings.rating DESC\n" +
                    "LIMIT 20;";

            String queryMovieInfo = "SELECT m.id,m.title, m.year, m.director" +
                    "FROM movies as m " +
                    "WHERE m.id in (SELECT id FROM top20);";

            String queryRatings = "SELECT * from stars";

            //Executed in a while loop of the top 20 movies
            String queryFirstThreeGenres = "SELECT grne.name\n" +
                    "FROM genres_in_movies as gim\n" +
                    "JOIN genres as grne ON gim.genreId=grne.id\n" +
                    "WHERE gim.movieId = ?\n" +
                    "LIMIT 3;";

            String queryFirstThreeStars = "SELECT str.id,str.name\n" +
                    "FROM stars_in_movies as sim\n" +
                    "JOIN stars as str ON sim.starId=str.id\n" +
                    "WHERE sim.movieId=?\n" +
                    "LIMIT 3;";

            // Perform the query
            ResultSet resultSetTop20 = statementTop20.executeQuery(queryTop20);
            ResultSet resultSetMovieInfo = statementMovieInfo.executeQuery(queryMovieInfo);
            ResultSet resultSetRatings = statementRatings.executeQuery(queryRatings);
            ResultSet resultSetFirstThreeGenres;
            ResultSet resultSetFirstThreeStars;

            JsonArray jsonArrayMovieList = new JsonArray();

            //While loop to fill jsonArrayMovieList with MovieInfo
            while(resultSetMovieInfo.next()){

            }
            //While loop to fill jsonArrayMovieList with Ratings
            while(resultSetRatings.next()){

            }
            PreparedStatement statementFirstThreeGenres;
            PreparedStatement statementFirstThreeStars;

            //Loop through top20 resultset and call 2 queries:
            //queryFirstThreeGenres and queryFirstThreeStars
            while (resultSetTop20.next()){
                //call the get movie
                // Declare our FirstThreeGenres statement
                statementFirstThreeGenres = conn.prepareStatement(queryFirstThreeGenres);

                // Set the parameter represented by "?" in the query to the movie id from top20,
                // num 1 indicates the first "?" in the query
                statementFirstThreeGenres.setString(1, resultSetTop20.getString("id"));
                //Execute statement
                resultSetFirstThreeGenres = statementFirstThreeGenres.executeQuery(queryFirstThreeGenres);

                // Declare our FirstThreeStars statement
                statementFirstThreeStars = conn.prepareStatement(queryFirstThreeStars);

                // Set the parameter represented by "?" in the query to the movie id from top20,
                // num 1 indicates the first "?" in the query
                statementFirstThreeStars.setString(1, resultSetTop20.getString("id"));
                resultSetFirstThreeStars = statementFirstThreeStars.executeQuery(queryFirstThreeStars);

                resultSetFirstThreeGenres.close();
                resultSetFirstThreeStars.close();
                statementFirstThreeGenres.close();
                statementFirstThreeStars.close();

            }
            resultSetTop20.close();
            resultSetMovieInfo.close();
            resultSetRatings.close();
            statementTop20.close();
            statementMovieInfo.close();
            statementRatings.close();


            // Iterate through each row of rs
//            System.out.println("inssservlet");
//            while (rs.next()) {
//                System.out.println("test" + rs.getString("id"));
//                String star_id = rs.getString("id");
//                String star_name = rs.getString("name");
//                String star_dob = rs.getString("birthYear");
//
//                // Create a JsonObject based on the data we retrieve from rs
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("star_id", star_id);
//                jsonObject.addProperty("star_name", star_name);
//                jsonObject.addProperty("star_dob", star_dob);
//
//                jsonArray.add(jsonObject);
//            }
//            rs.close();
//            statement.close();

            //Drop top20 temp table
            Statement statementDropTable = conn.createStatement();
            String queryDropTop20 = "DROP TEMPORARY TABLE IF EXISTS top20;\n";
            ResultSet resultSetDropTop20 = statementTop20.executeQuery(queryTop20);
            resultSetDropTop20.close();
            statementDropTable.close();
//
//            // Log to localhost log
//            request.getServletContext().log("getting " + jsonArray.size() + " results");
//
//            // Write JSON string to output
//            out.write(jsonArray.toString());
            // Set response status to 200 (OK)
            response.setStatus(200);

        } catch (Exception e) {

            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }

        // Always remember to close db connection after usage. Here it's done by try-with-resources

    }
}
