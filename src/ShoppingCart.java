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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * This IndexServlet is declared in the web annotation below,
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "ShoppingCart", urlPatterns = "/api/shopping-cart")
public class ShoppingCart extends HttpServlet {
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

    //helper function to build json
    private JsonArray buildShoppingCartArr(HashMap<String, MoviePrice> ahashMap){
        JsonArray retShoppingCartArr = new JsonArray();

        //iter over hashmap
        for(Map.Entry<String,MoviePrice> ahashmapEntry: ahashMap.entrySet()){
            //build json obj
            JsonObject aMovieJsonobj = new JsonObject();
            aMovieJsonobj.addProperty("MovieTitle", ahashmapEntry.getValue().getMovieTitle());
            aMovieJsonobj.addProperty("MoviePrice",ahashmapEntry.getValue().getMoviePrice());
            aMovieJsonobj.addProperty("MovieQuantity",ahashmapEntry.getValue().getMovieCount());

            //add to array
            retShoppingCartArr.add(aMovieJsonobj);

        }
        return retShoppingCartArr;


    }

    /**
     * handles GET requests to store session information
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        //HashMap<String, MoviePrice> cart = new HashMap<String, MoviePrice>();
        HashMap<String, MoviePrice> cart = (HashMap<String, MoviePrice>) session.getAttribute("Cart");
        //Grab action type and movieId from parameters
        String action = request.getParameter("action");
        String movieId = request.getParameter("movieid");

        //Check if movieId is in Cart
        if(action.equals("add") && cart.get(movieId) == null){
            //run sql to get title and price

            // Output stream to STDOUT
            PrintWriter out = response.getWriter();
            try (Connection conn = dataSource.getConnection()) {
                String queryNamePrice = "SELECT mv.title,mv.price\n" +
                        "FROM movies as mv\n" +
                        "WHERE mv.id=?";

                //prepare statement
                PreparedStatement namePriceStatement = conn.prepareStatement(queryNamePrice);
                //set prepare statement params
                namePriceStatement.setString(1,movieId);

                ResultSet namePriceResultSet = namePriceStatement.executeQuery();

                //advance to next
                namePriceResultSet.next();

                Float afloatprice = Float.valueOf(namePriceResultSet.getString("price"));

                //in cart put in data
                cart.put(movieId,new MoviePrice(movieId,namePriceResultSet.getString("title"),afloatprice));

                //close pricetitle table
                namePriceResultSet.close();
                namePriceStatement.close();

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

        //Perform corresponding action
        switch (action) {
            case "add":
                cart.get(movieId).setMovieCount(1);
                break;
            case "subtract":
                cart.get(movieId).setMovieCount(-1);
                break;
            case "delete":
                cart.remove(movieId);
                break;
            case "view":
                //
                response.setContentType("application/json");
                JsonArray ThefinalJsonArr = buildShoppingCartArr(cart);
                response.getWriter().write(ThefinalJsonArr.toString());

            default:
                //Perform view
                
                break;
        }
//        //long lastAccessTime = session.getLastAccessedTime();
//
//        JsonObject responseJsonObject = new JsonObject();
//       // responseJsonObject.addProperty("sessionID", sessionId);
//        //responseJsonObject.addProperty("lastAccessTime", new Date(lastAccessTime).toString());
//
//        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
//        if (previousItems == null) {
//            previousItems = new ArrayList<String>();
//        }
//        // Log to localhost log
//        request.getServletContext().log("getting " + previousItems.size() + " items");
//        JsonArray previousItemsJsonArray = new JsonArray();
//        previousItems.forEach(previousItemsJsonArray::add);
//        responseJsonObject.add("previousItems", previousItemsJsonArray);
//
//        // write all the data into the jsonObject
//        response.getWriter().write(responseJsonObject.toString());
    }

}
