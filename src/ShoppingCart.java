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

    //helper function to build json obj for total
    private JsonObject buildTotalObj(HashMap<String, MoviePrice> ahashMap){
        JsonObject retTotalObj = new JsonObject();

        float runningTotal = 0;
        int totalItems = 0;

        //iter overhash map
        for(Map.Entry<String,MoviePrice> ahashmapEntry: ahashMap.entrySet()) {
            //get quantity and price
            int thecount = ahashmapEntry.getValue().getMovieCount();
            float theprice = ahashmapEntry.getValue().getMoviePrice();

            totalItems += thecount;

            runningTotal += thecount * theprice;
        }



        retTotalObj.addProperty("total",Math.round(runningTotal * 100.0) / 100.0);
        retTotalObj.addProperty("numofitems",totalItems);

        //return
        return retTotalObj;

    }

    //helper function to build json
    private JsonArray buildShoppingCartArr(HashMap<String, MoviePrice> ahashMap){
        JsonArray retShoppingCartArr = new JsonArray();

        //iter over hashmap
        for(Map.Entry<String,MoviePrice> ahashmapEntry: ahashMap.entrySet()){
            //build json obj
            JsonObject aMovieJsonobj = new JsonObject();
            aMovieJsonobj.addProperty("MovieID", ahashmapEntry.getValue().getMovieId());
            aMovieJsonobj.addProperty("SalesID", ahashmapEntry.getValue().getSalesId());
            aMovieJsonobj.addProperty("MovieTitle", ahashmapEntry.getValue().getMovieTitle());
            aMovieJsonobj.addProperty("MoviePrice",ahashmapEntry.getValue().getMoviePrice());
            aMovieJsonobj.addProperty("MovieQuantity",ahashmapEntry.getValue().getMovieCount());

            //add to array
            retShoppingCartArr.add(aMovieJsonobj);

        }
        return retShoppingCartArr;


    }

    /**
       The post request portion

     **/
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //get session
        HttpSession session = request.getSession();
        //HashMap<String, MoviePrice> cart = new HashMap<String, MoviePrice>();
        HashMap<String, MoviePrice> cart = (HashMap<String, MoviePrice>) session.getAttribute("Cart");
        //get the params from in post data
        //Possible actions add or subtract
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
                synchronized (cart) {
                    cart.get(movieId).setMovieCount(1);
                }
                break;
            case "subtract":
                if((cart.get(movieId).getMovieCount() - 1) == 0){
                    synchronized (cart) {
                        cart.remove(movieId);
                    }
                }
                else {
                    synchronized (cart) {
                        cart.get(movieId).setMovieCount(-1);
                    }
                }
                break;
            case "delete":
                synchronized (cart) {
                    cart.remove(movieId);
                }
                break;
            case "clearcart":
//                clear the Usercart
                synchronized (cart) {
                    cart.clear();
                }
            default:
                //Perform view

                break;
        }


    }

    /**
     * handles GET requests to store session information
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //get action parameter
        String actionparam = request.getParameter("action");

        //load the session hashmap
        HttpSession session = request.getSession();
        HashMap<String, MoviePrice> cart = (HashMap<String, MoviePrice>) session.getAttribute("Cart");

        //set response as json
        response.setContentType("application/json");

        if(actionparam.equals("total")){
            //return the total
            JsonObject ThefinalJsonObjTotal = buildTotalObj(cart);
            response.getWriter().write(ThefinalJsonObjTotal.toString());

        }
        else{
            //return a json array of the objects
            JsonArray ThefinalJsonArr = buildShoppingCartArr(cart);
            response.getWriter().write(ThefinalJsonArr.toString());
        }






    }

}
