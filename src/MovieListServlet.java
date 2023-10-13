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


            //Execute once to grab necessary information
            String queryTop20 = "SELECT m.id,m.title, m.year, m.director,rtng.rating\n" +
                    "FROM movies as m \n" +
                    "JOIN ratings rtng ON m.id=rtng.movieId\n" +
                    "ORDER BY rtng.rating DESC\n" +
                    "LIMIT 20;\n";



            //Executed in a while loop of the top 20 movies
            String queryFirstThreeGenres = "SELECT grne.name\n" +
                    "FROM genres_in_movies as gim\n" +
                    "JOIN genres as grne ON gim.genreId=grne.id\n" +
                    "WHERE gim.movieId = ?\n" +
                    "LIMIT 3";

            String queryFirstThreeStars = "SELECT str.id,str.name\n" +
                    "FROM stars_in_movies as sim\n" +
                    "JOIN stars as str ON sim.starId=str.id\n" +
                    "WHERE sim.movieId=?\n" +
                    "LIMIT 3;";


            // Perform the query
            ResultSet resultSetTop20 = statementTop20.executeQuery(queryTop20);
            //Create the final json array to be returned
            JsonArray jsonArrayMovieList = new JsonArray();

//            //queryFirstThreeGenres and queryFirstThreeStars
            while (resultSetTop20.next()){
                //get movie id
                String currentMovId = resultSetTop20.getString("id");
                //create a json object
                JsonObject SingleMovieJsonObj = new JsonObject();

                //add id title year and director
                SingleMovieJsonObj.addProperty("id",currentMovId);
                SingleMovieJsonObj.addProperty("title",resultSetTop20.getString("title"));
                SingleMovieJsonObj.addProperty("year",resultSetTop20.getString("year"));
                SingleMovieJsonObj.addProperty("director",resultSetTop20.getString("director"));




                System.out.println("the movie id: " + currentMovId + " title: " + resultSetTop20.getString("title"));
                //call the get movie The three genres portion
                // Declare our FirstThreeGenres statement
                PreparedStatement statementFirstThreeGenres = conn.prepareStatement(queryFirstThreeGenres);
//
//                // Set the parameter represented by "?" in the query to the movie id from top20,
//                // num 1 indicates the first "?" in the query
                statementFirstThreeGenres.setString(1, currentMovId);

//                //Execute statement
                ResultSet resultSetFirstThreeGenres = statementFirstThreeGenres.executeQuery();

                JsonArray InnerGenreArry = new JsonArray();
//
                while (resultSetFirstThreeGenres.next()){
                    System.out.println("Genres:" + resultSetFirstThreeGenres.getString("name"));
                    InnerGenreArry.add(resultSetFirstThreeGenres.getString("name"));

                }

                //closing genres
                resultSetFirstThreeGenres.close();
                statementFirstThreeGenres.close();

                //append inner array
                SingleMovieJsonObj.add("genre",InnerGenreArry);

                //Getting the three stars portion
                // Declare our FirstThreeGenres statement
                PreparedStatement statementFirstThreeStars = conn.prepareStatement(queryFirstThreeStars);
//
//                // Set the parameter represented by "?" in the query to the movie id from top20,
//                // num 1 indicates the first "?" in the query
                statementFirstThreeStars.setString(1, currentMovId);

//                //Execute statement
                ResultSet resultSetFirstThreeStars = statementFirstThreeStars.executeQuery();

                JsonArray InnerStarArry = new JsonArray();
//
                while (resultSetFirstThreeStars.next()){
                    JsonObject InnerStarObj = new JsonObject();
                    System.out.println("Star:" + resultSetFirstThreeStars.getString("name"));
                    InnerStarObj.addProperty("id",resultSetFirstThreeStars.getString("id"));
                    InnerStarObj.addProperty("name",resultSetFirstThreeStars.getString("name"));

                    //append to the innerstar list
                    InnerStarArry.add(InnerStarObj);

                }

                //closing stars
                resultSetFirstThreeStars.close();
                statementFirstThreeStars.close();

                //append inner array
                SingleMovieJsonObj.add("star",InnerStarArry);

                //rating portion
                SingleMovieJsonObj.addProperty("rating",resultSetTop20.getString("rating"));


                //append json obj to array
                jsonArrayMovieList.add(SingleMovieJsonObj);

//
            }
            //close top20
            resultSetTop20.close();
            statementTop20.close();



            // Log to localhost log
            request.getServletContext().log("getting " + jsonArrayMovieList.size() + " results");

            // Write JSON string to output
            out.write(jsonArrayMovieList.toString());
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
