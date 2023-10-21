import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet {
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

//        HttpSession currUserSess = (HttpSession) request.getSession();
//        String theUrl = (String) currUserSess.getAttribute("MovieStoreUrl");

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");

        // The log message can be found in localhost log
        request.getServletContext().log("getting id: " + id);

        System.out.println("the movie id:" + id);

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource

            //create new json object
            JsonObject jsonObject = new JsonObject();

            // Construct a query with parameter represented by "?"
//            String query = "SELECT * from stars as s, stars_in_movies as sim, movies as m " +
//                    "where m.id = sim.movieId and sim.starId = s.id and s.id = ?";

            //create the strings for the mysql query
            String querymovie = "SELECT m.title, m.year, m.director FROM movies as m WHERE m.id = ?";
            String querygenre = "SELECT grne.name FROM genres_in_movies as gim JOIN genres as grne ON gim.genreId=grne.id WHERE gim.movieId = ?";
            String querystar = "SELECT str.id,str.name FROM stars_in_movies as sim JOIN stars as str ON sim.starId=str.id WHERE sim.movieId=?";
            String queryrating = "SELECT * FROM ratings as rtng WHERE rtng.movieId=?";

            // Declare our statement
//            PreparedStatement statement = conn.prepareStatement(query);

            //movie table section

            //prepare statement
            PreparedStatement movieStatement = conn.prepareStatement(querymovie);


            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
//            statement.setString(1, id);

            movieStatement.setString(1,id);

            System.out.println("the mvstatement" + movieStatement);

            // Perform the query
//            ResultSet rs = statement.executeQuery();

            ResultSet movieResultSet = movieStatement.executeQuery();
            movieResultSet.next();


            System.out.println("the title:" + movieResultSet.getString("title"));
            System.out.println("the Year:" + movieResultSet.getString("year"));
            System.out.println("the Director:" + movieResultSet.getString("director"));



            String movieTitle = movieResultSet.getString("title");
            String movieYear = movieResultSet.getString("year");
            String movieDirector = movieResultSet.getString("director");
            

            jsonObject.addProperty("title", movieTitle);
            jsonObject.addProperty("year", movieYear);
            jsonObject.addProperty("director", movieDirector);



            //close movie table
            movieResultSet.close();
            movieStatement.close();

            //Query genre section
            PreparedStatement genreStatement = conn.prepareStatement(querygenre);
            genreStatement.setString(1,id);

            ResultSet genreResultSet = genreStatement.executeQuery();

            JsonArray GenrejsonArray = new JsonArray();

            while(genreResultSet.next()){
                System.out.println("genre: " + genreResultSet.getString("name"));

                String tempGenreName = genreResultSet.getString("name");
                //add to json array
                GenrejsonArray.add(tempGenreName);

            }

            //add the genrejsonarray to main json object
            jsonObject.add("genre",GenrejsonArray);
            System.out.println(GenrejsonArray);


            genreResultSet.close();
            genreStatement.close();



            //Query Star section
            //prepare statement
            PreparedStatement starStatement = conn.prepareStatement(querystar);
            starStatement.setString(1,id);

            ResultSet starResultSet = starStatement.executeQuery();

            JsonArray StarjsonArray = new JsonArray();

            while(starResultSet.next()){
                System.out.println("stars: " + starResultSet.getString("name"));
                System.out.println("stars id: " + starResultSet.getString("id"));

                String tempStarId = starResultSet.getString("id");
                String tempStarName = starResultSet.getString("name");

                //create json object
                JsonObject InnerStarjsonObj = new JsonObject();

                InnerStarjsonObj.addProperty("id",tempStarId);
                InnerStarjsonObj.addProperty("name",tempStarName);


                //add to json array
                StarjsonArray.add(InnerStarjsonObj);

            }


            //append json array to main jsonobj
            jsonObject.add("star",StarjsonArray);





            starResultSet.close();
            starStatement.close();


            //Rating portion
            //prepare statement
            PreparedStatement ratingStatement = conn.prepareStatement(queryrating);
            ratingStatement.setString(1,id);

            System.out.println(ratingStatement);

            ResultSet ratingResultSet = ratingStatement.executeQuery();

            ratingResultSet.next();

            System.out.println("rating:" + ratingResultSet.getString("rating"));

            String TheRating = ratingResultSet.getString("rating");

            //add to main json obj
            jsonObject.addProperty("rating",TheRating);


            ratingStatement.close();
            ratingResultSet.close();






            

//            System.out.println("the array: " + movieTitle + movieYear + movieDirector);

//            // Iterate through each row of rs
//            while (rs.next()) {
//
//                String starId = rs.getString("starId");
//                String starName = rs.getString("name");
//                String starDob = rs.getString("birthYear");
//
//                String movieId = rs.getString("movieId");
//                String movieTitle = rs.getString("title");
//                String movieYear = rs.getString("year");
//                String movieDirector = rs.getString("director");
//
//                // Create a JsonObject based on the data we retrieve from rs
//
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("star_id", starId);
//                jsonObject.addProperty("star_name", starName);
//                jsonObject.addProperty("star_dob", starDob);
//                jsonObject.addProperty("movie_id", movieId);
//                jsonObject.addProperty("movie_title", movieTitle);
//                jsonObject.addProperty("movie_year", movieYear);
//                jsonObject.addProperty("movie_director", movieDirector);
//
//                jsonArray.add(jsonObject);
//            }
//            rs.close();
//            statement.close();



            // Write JSON string to output
            out.write(jsonObject.toString());
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
