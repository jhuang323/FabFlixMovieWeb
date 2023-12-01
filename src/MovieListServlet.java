import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysql.cj.Session;
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

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

// Declaring a WebServlet called StarsServlet, which maps to url "/api/MovieList"
@WebServlet(name = "MovieListServlet", urlPatterns = "/api/movie-list")
public class MovieListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedbmaster");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

//    my sorting string constructing helper funct
    private String createSortingString(String afirstparam,String atypeparamone,String atypeparamtwo){
        String retSortsqlQuery = "";
        if(afirstparam != null){
            //replace the atypeparam with ASC OR DESC
            String OrderSqlString1 = "";
            String OrderSqlString2 = "";

            if(atypeparamone.equals("a")){
                OrderSqlString1 = "ASC";
            }
            else{
                OrderSqlString1 = "DESC";
            }

            //for the second type param
            if(atypeparamtwo.equals("a")){
                OrderSqlString2 = "ASC";
            }
            else{
                OrderSqlString2 = "DESC";
            }

            //check if first is title
            if(afirstparam.equals("title")){
                //by title first
                retSortsqlQuery = afirstparam + " " + OrderSqlString1;
                retSortsqlQuery += ", rating " + OrderSqlString2;
            }
            else{
                //by rating first
                retSortsqlQuery = afirstparam + " " + OrderSqlString1;
                retSortsqlQuery += ", title " + OrderSqlString2;
            }
        }

        return retSortsqlQuery;
    }
    private int calcPageOffset(int aPageNum,int aNumLimit){
        return (aPageNum-1) * aNumLimit;
    }

//    the tokenizer function for full text
    private static String booleanStrtokenizer(String astr){
        StringTokenizer st1 = new StringTokenizer(astr," ");
        String outStr = "";


        while(st1.hasMoreTokens()){
            String atoken = st1.nextToken();
            System.out.println("TOken: " + atoken);
            outStr += "+" + atoken + "*";
        }

        return outStr;

    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        //get session and store url
        HttpSession currUserSess = (HttpSession) request.getSession();

        //set the curr session Movie List store URL
        String theFullUrlString = "MovieList.html" + "?" + request.getQueryString();
        currUserSess.setAttribute("MovieStoreUrl",theFullUrlString);


        //searching

        // Retrieve parameter title from url request.
        String title = request.getParameter("title");
        // The log message can be found in localhost log
        request.getServletContext().log("getting title: " + title);
        System.out.println("the movie title search:" + title);

        // Retrieve parameter year from url request.
        String year = request.getParameter("year");
        // The log message can be found in localhost log
        request.getServletContext().log("getting year: " + year);
        System.out.println("the movie year search:" + year);

        // Retrieve parameter director from url request.
        String director = request.getParameter("director");
        // The log message can be found in localhost log
        request.getServletContext().log("getting director: " + director);
        System.out.println("the movie director search:" + director);

        // Retrieve parameter star_name from url request.
        String star_name = request.getParameter("star_name");
        // The log message can be found in localhost log
        request.getServletContext().log("getting star's name: " + star_name);
        System.out.println("the movie star's name search:" + star_name);

        //browsing section

        // Retrieve parameter genre from url request.
        String genreNameParam = request.getParameter("genre");
        // The log message can be found in localhost log
        request.getServletContext().log("getting genre: " + genreNameParam);
        //retrieve the single char title
        String chr = request.getParameter("char");
        // The log message can be found in localhost log
        request.getServletContext().log("getting singleCharTitle: " + chr);

        //sorting section
        //get the sortfirst param
        String sortFirstParam = request.getParameter("sortfirst");

        //get the sorttype param 1 and 2
        String sortTypeParamFirst = request.getParameter("sorttype1");
        String sortTypeParamSecond = request.getParameter("sorttype2");

        //Pagination section
        //get the page param
        int pageParam = Integer.parseInt(request.getParameter("page"));

        //get the numlimit param
        int numlimitParam = Integer.parseInt(request.getParameter("numlimit"));
        int CalcnumlimitParam = numlimitParam;

        //Full text section
        boolean fulltextParam = Boolean.valueOf(request.getParameter("fulltext"));



        //checking
        String isChecking = request.getParameter("checking");

        if(isChecking == null){
            isChecking="false";
        }
        else{
            //is checking is true
            CalcnumlimitParam = 1;
        }

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {



            //Declare the main query
            String Mainquery = "SELECT m.id,m.title, m.year, m.director, rtng.rating\n" +
                    "FROM movies as m\n" +
                    "LEFT JOIN ratings rtng ON m.id=rtng.movieId\n";
            //Declare the prepare statement
            PreparedStatement MainPrepStatement = null;

            boolean top20Choice = false;

            if(genreNameParam == null && chr == null && star_name == null && title == null && director == null && year == null
            && fulltextParam == false){
                //do something that shows you didnt input anything
                //should not be possible??? maybe since we control the api calls from front end

                top20Choice = true;

                Mainquery = "SELECT m.id,m.title, m.year, m.director, rtng.rating\n" +
                        "FROM movies as m \n" +
                        "JOIN ratings rtng ON m.id=rtng.movieId\n" +
                        "ORDER BY rtng.rating DESC\n" +
                        "LIMIT 20";



                MainPrepStatement = conn.prepareStatement(Mainquery);
            }
            else if(fulltextParam == true){

                //build Search query
                Mainquery = "SELECT m.id,m.title, m.year, m.director, rtng.rating,DAMLEVP(m.title, ?) AS EditDist\n" +
                        "FROM movies as m\n" +
                        "LEFT JOIN ratings rtng ON m.id=rtng.movieId\n" +
                        "WHERE DAMLEVP(m.title, ?)\n" +
                        "ORDER BY EditDist ASC";


                //sorting
//                System.out.println(createSortingString(sortFirstParam,sortTypeParam));
                Mainquery += "," + createSortingString(sortFirstParam,sortTypeParamFirst,sortTypeParamSecond);

                //add the limits
                Mainquery += "\nLIMIT " + CalcnumlimitParam;
                Mainquery += "\nOFFSET " + calcPageOffset(pageParam,numlimitParam);




                //prep statement
                MainPrepStatement = conn.prepareStatement(Mainquery);

                //tokenize
                String tokenboolStr = booleanStrtokenizer(title);

                MainPrepStatement.setString(1,title);
                MainPrepStatement.setString(2,title);
            }
            else if(genreNameParam == null && chr == null){
                //Build Search query
                //see note below but need to add %title% aka % in the setString
                if (star_name != null){
                    star_name = '%' + star_name + '%';
                }
                if (title != null){
                    title = '%' + title + '%';
                }
                if (director != null){
                    director = '%' + director + '%';
                }




                String[] paramsArray = {star_name, title, year, director};

                HashMap<String, Integer> params = new HashMap<String, Integer>();
                int counter = 1;
                for(int i=0;i<paramsArray.length;i++){
                    if(paramsArray[i] != null){
                        params.put(paramsArray[i], counter);
                        counter ++;
                    }
                }
//                String searchQuery = "SELECT m.id,m.title, m.year, m.director,ratings.rating\n" +
//                        "FROM movies as m \n" +
//                        "JOIN ratings ON m.id=ratings.movieId \n";
                System.out.println("the search query is now being built.");
                if(params.get(star_name) != null) {
                    Mainquery += "JOIN stars_in_movies ON m.id=stars_in_movies.movieId \n" +
                            "JOIN stars ON stars_in_movies.starId=stars.id \n" +
                            "WHERE lower(stars.name) LIKE lower(?) ";
                    if(params.get(star_name) != counter-1){
                        Mainquery += "AND ";
                    }
                }
                if(params.get(title) != null){
                    if(params.get(title) == 1){
                        Mainquery += "WHERE lower(m.title) LIKE lower(?) ";
                    }
                    else{
                        Mainquery += "lower(m.title) LIKE lower(?) ";
                    }
                    if(params.get(title) != counter-1){
                        Mainquery += "AND ";
                    }
                }
                if(params.get(year) != null){
                    if(params.get(year) == 1){
                        Mainquery += "WHERE lower(m.year) = lower(?) ";
                    }
                    else{
                        Mainquery += "lower(m.year) = lower(?) ";
                    }
                    if(params.get(year) != counter-1){
                        Mainquery += "AND ";
                    }
                }
                if(params.get(director) != null){
                    if(params.get(director) == 1){
                        Mainquery += "WHERE lower(m.director) LIKE lower(?) ";
                    }
                    else{
                        Mainquery += "lower(m.director) LIKE lower(?) ";
                    }
                    if(params.get(director) != counter-1) {
                        Mainquery += "AND ";
                    }



                }

                //sorting
//                System.out.println(createSortingString(sortFirstParam,sortTypeParam));
                Mainquery += "\nORDER BY " + createSortingString(sortFirstParam,sortTypeParamFirst,sortTypeParamSecond);

                //add the limits
                Mainquery += "\nLIMIT " + CalcnumlimitParam;
                Mainquery += "\nOFFSET " + calcPageOffset(pageParam,numlimitParam);

                Mainquery += ";";
                // Declare our statement
                MainPrepStatement = conn.prepareStatement(Mainquery);
                // Set the parameter represented by "?" in the query to the id we get from url,
                // num 1 indicates the first "?" in the query
                for (String key : params.keySet()) {
                    MainPrepStatement.setString(params.get(key), key);
                }
            }
            else{
                //Building the Browsing query
                if(genreNameParam != null){
                    //looking for genre
                    Mainquery += "JOIN genres_in_movies as gim ON m.id=gim.movieId\n" +
                            "JOIN genres as grne ON gim.genreId=grne.id\n" +
                            "WHERE lower(grne.name) = lower(?)";

                    //sorting
//                System.out.println(createSortingString(sortFirstParam,sortTypeParam));
                    Mainquery += "\nORDER BY " + createSortingString(sortFirstParam,sortTypeParamFirst,sortTypeParamSecond);

                    //add the limits
                    Mainquery += "\nLIMIT " + CalcnumlimitParam;
                    Mainquery += "\nOFFSET " + calcPageOffset(pageParam,numlimitParam);


                    MainPrepStatement = conn.prepareStatement(Mainquery);
                    MainPrepStatement.setString(1, genreNameParam);

                }
                else{
                    //browse by movies starting by a character
                    if(chr.equals("*")){
                        Mainquery += "WHERE lower(m.title) REGEXP lower(?) ";

                        //sorting
//                System.out.println(createSortingString(sortFirstParam,sortTypeParam));
                        Mainquery += "\nORDER BY " + createSortingString(sortFirstParam,sortTypeParamFirst,sortTypeParamSecond);

                        //add the limits
                        Mainquery += "\nLIMIT " + CalcnumlimitParam;
                        Mainquery += "\nOFFSET " + calcPageOffset(pageParam,numlimitParam);

                        MainPrepStatement = conn.prepareStatement(Mainquery);
                        MainPrepStatement.setString(1,  "^[^A-Za-z0-9]");
                    }
                    else{
                        Mainquery += "WHERE lower(m.title) LIKE lower(?) ";


                        //sorting
//                System.out.println(createSortingString(sortFirstParam,sortTypeParam));
                        Mainquery += "\nORDER BY " + createSortingString(sortFirstParam,sortTypeParamFirst,sortTypeParamSecond);

                        //add the limits
                        Mainquery += "\nLIMIT " + CalcnumlimitParam;
                        Mainquery += "\nOFFSET " + calcPageOffset(pageParam,numlimitParam);


                        MainPrepStatement = conn.prepareStatement(Mainquery);
                        MainPrepStatement.setString(1, chr + '%');
                    }


                }

            }

            //Executed in a while loop of the top 20 movies
            String queryFirstThreeGenres = "SELECT grne.name\n" +
                    "FROM genres_in_movies as gim\n" +
                    "JOIN genres as grne ON gim.genreId=grne.id\n" +
                    "WHERE gim.movieId = ?\n" +
                    "ORDER BY grne.name ASC\n" +
                    "LIMIT 3";

            String queryFirstThreeStars = "SELECT simo.starId,COUNT(simo.starId),str.name\n" +
                    "FROM stars_in_movies as simo\n" +
                    "JOIN stars as str ON simo.starId=str.id\n" +
                    "WHERE simo.starId IN (\n" +
                    "SELECT DISTINCT sim.starId\n" +
                    "FROM stars_in_movies as sim\n" +
                    "WHERE sim.movieId=?)\n" +
                    "GROUP BY simo.starId\n" +
                    "ORDER BY COUNT(simo.starId) DESC,str.name ASC\n" +
                    "LIMIT 3";


            // Perform the query
            ResultSet resultSet = MainPrepStatement.executeQuery();
            //Create the final json array to be returned
            JsonArray jsonArrayMovieList = new JsonArray();
            JsonObject retJsonObjChecking = new JsonObject();

            if(isChecking.equals("true")){
                if(top20Choice || resultSet.next() == false){
                    //is empty
                    retJsonObjChecking.addProperty("empty",true);
                }
                else {
                    retJsonObjChecking.addProperty("empty",false);
                }
            }
            else{



                //            //queryFirstThreeGenres and queryFirstThreeStars
                // Declare our FirstThreeGenres statement
                PreparedStatement statementFirstThreeGenres = conn.prepareStatement(queryFirstThreeGenres);

                // Declare our FirstThreeGenres statement
                PreparedStatement statementFirstThreeStars = conn.prepareStatement(queryFirstThreeStars);


                while (resultSet.next()){
                    //get movie id
                    String currentMovId = resultSet.getString("id");
                    //create a json object
                    JsonObject SingleMovieJsonObj = new JsonObject();

                    //add id title year and director
                    SingleMovieJsonObj.addProperty("id",currentMovId);
                    SingleMovieJsonObj.addProperty("title",resultSet.getString("title"));
                    SingleMovieJsonObj.addProperty("year",resultSet.getString("year"));
                    SingleMovieJsonObj.addProperty("director",resultSet.getString("director"));




//                System.out.println("the movie id: " + currentMovId + " title: " + resultSet.getString("title"));
                    //call the get movie The three genres portion

//
//                // Set the parameter represented by "?" in the query to the movie id from top20,
//                // num 1 indicates the first "?" in the query
                    statementFirstThreeGenres.setString(1, currentMovId);

//                //Execute statement
                    ResultSet resultSetFirstThreeGenres = statementFirstThreeGenres.executeQuery();

                    JsonArray InnerGenreArry = new JsonArray();
//
                    while (resultSetFirstThreeGenres.next()){
//                    System.out.println("Genres:" + resultSetFirstThreeGenres.getString("name"));
                        InnerGenreArry.add(resultSetFirstThreeGenres.getString("name"));

                    }




                    //closing genres
                    resultSetFirstThreeGenres.close();


                    //append inner array
                    SingleMovieJsonObj.add("genre",InnerGenreArry);

                    //Getting the three stars portion

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
//                    System.out.println("Star:" + resultSetFirstThreeStars.getString("name"));
                        InnerStarObj.addProperty("id",resultSetFirstThreeStars.getString("starId"));
                        InnerStarObj.addProperty("name",resultSetFirstThreeStars.getString("name"));

                        //append to the innerstar list
                        InnerStarArry.add(InnerStarObj);

                    }

                    //closing stars
                    resultSetFirstThreeStars.close();

                    //append inner array
                    SingleMovieJsonObj.add("star",InnerStarArry);

                    //rating portion
                    String ratingStr = resultSet.getString("rating");
                    if(resultSet.wasNull()){
                        SingleMovieJsonObj.addProperty("rating","N/A");
                    }
                    else {
                        SingleMovieJsonObj.addProperty("rating",ratingStr);
                    }



                    //append json obj to array
                    jsonArrayMovieList.add(SingleMovieJsonObj);

//
                }
                //close prepare statements
                //first 3 genres
                statementFirstThreeGenres.close();
                //stars prepare statement
                statementFirstThreeStars.close();

            }



            //close top20
            resultSet.close();
            MainPrepStatement.close();



            // Log to localhost log
            request.getServletContext().log("getting " + jsonArrayMovieList.size() + " results");

            // Write JSON string to output
            if(isChecking.equals("true")){
                out.write(retJsonObjChecking.toString());
            }
            else {
                out.write(jsonArrayMovieList.toString());
            }

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
