import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * This IndexServlet is declared in the web annotation below,
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "ShoppingCart", urlPatterns = "/api/shopping-cart")
public class ShoppingCart extends HttpServlet {

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
        if(cart.get(movieId) == null){
            cart.put(movieId, new MoviePrice(0));
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
            default:
                //Perform view
                
                break;
        }
        //long lastAccessTime = session.getLastAccessedTime();

        JsonObject responseJsonObject = new JsonObject();
       // responseJsonObject.addProperty("sessionID", sessionId);
        //responseJsonObject.addProperty("lastAccessTime", new Date(lastAccessTime).toString());

        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
        if (previousItems == null) {
            previousItems = new ArrayList<String>();
        }
        // Log to localhost log
        request.getServletContext().log("getting " + previousItems.size() + " items");
        JsonArray previousItemsJsonArray = new JsonArray();
        previousItems.forEach(previousItemsJsonArray::add);
        responseJsonObject.add("previousItems", previousItemsJsonArray);

        // write all the data into the jsonObject
        response.getWriter().write(responseJsonObject.toString());
    }

}
