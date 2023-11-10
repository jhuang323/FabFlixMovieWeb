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
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This IndexServlet is declared in the web annotation below,
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = "/api/checkout")
public class CheckoutServlet extends HttpServlet {
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
     * handles GET requests to store session information
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        long lastAccessTime = session.getLastAccessedTime();

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("sessionID", sessionId);
        responseJsonObject.addProperty("lastAccessTime", new Date(lastAccessTime).toString());

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

    private void recordTransactions(HashMap<String, MoviePrice> ahashMap,User auserObj){

        int curUserId = auserObj.getUserid();
        Date curdate = new Date();
        SimpleDateFormat curDateMysqlFormat = new SimpleDateFormat("yyyy-MM-dd");
        String curdateStr = curDateMysqlFormat.format(curdate);

        String insertSalesUpdate = "INSERT INTO sales(id,customerId,movieId,saleDate,quantity)\n" +
                "VALUES\n";
        String queryMSalesId = "select MAX(id) as mid from sales";

        int MaxId = 0;

        //get the max sales id first
        try (Connection conn = dataSource.getConnection()) {

            PreparedStatement qMaxSalesStatement = conn.prepareStatement(queryMSalesId);

            ResultSet MaxidRS =  qMaxSalesStatement.executeQuery();

            MaxidRS.next();

            //get the max id
            MaxId = MaxidRS.getInt("mid");


            //close statements and rs
            qMaxSalesStatement.close();
            MaxidRS.close();


        } catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());


        }

        //test execute update



        //insert format: (961,'tt0399582','2023-10-26',10)

        //iterate through the hashmap
        //iter over hashmap
        int count = 0;
        for(Map.Entry<String,MoviePrice> ahashmapEntry: ahashMap.entrySet()){
            MaxId++;
            String aSingleInsertData = "("+ MaxId + "," +curUserId + "," + "'" + ahashmapEntry.getValue().getMovieId() + "','" + curdateStr + "'," + ahashmapEntry.getValue().getMovieCount() + ")";

            //update the hashmap sid
            ahashmapEntry.getValue().setSalesId(MaxId);


            count++;
            if(count == ahashMap.size()){
                insertSalesUpdate += aSingleInsertData;
            }
            else{
                insertSalesUpdate += aSingleInsertData + ",\n";
            }

        }


        //test execute update
        try (Connection conn = dataSource.getConnection()) {

            PreparedStatement insertSalesStatement = conn.prepareStatement(insertSalesUpdate);

            insertSalesStatement.executeUpdate();

            //close statements
            insertSalesStatement.close();

        } catch (Exception e) {
        // Write error message JSON object to output
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("errorMessage", e.getMessage());


        }




    }

    /**
     * handles POST requests to add and show the item list information
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstName = request.getParameter("First_name");
        String lastName = request.getParameter("Last_name");
        String creditCardNumber = request.getParameter("CreditCard_number");
        String expirationDate = request.getParameter("Expiration_date");




        HttpSession session = request.getSession();
        HashMap<String, MoviePrice> Usercart = (HashMap<String, MoviePrice>) session.getAttribute("Cart");
        User UserObj = (User) session.getAttribute("user");

        //verify the payment info
        String queryCreditCard = "SELECT * \n" +
                "FROM creditcards as cc\n" +
                "WHERE cc.id = ? AND cc.firstName = ? AND cc.lastName = ? AND cc.Expiration = ?";


        // Output stream to STDOUT
        PrintWriter out = response.getWriter();




        try (Connection conn = dataSource.getConnection()) {
            JsonObject responseJsonObject = new JsonObject();
            // Declare our cc statement
            PreparedStatement statementCreditCard = conn.prepareStatement(queryCreditCard);

            // Set the parameter represented by "?" in the query to the username and password we get from the form,
            // num 1 indicates the first "?" in the query
            statementCreditCard.setString(1, creditCardNumber);
            statementCreditCard.setString(2, firstName);
            statementCreditCard.setString(3, lastName);
            statementCreditCard.setString(4, expirationDate);

            // Perform the query
            ResultSet resultsetCreditCard = statementCreditCard.executeQuery();


            //check if cart is empty first
            if(Usercart.isEmpty()) {
                responseJsonObject.addProperty("success",0);
                responseJsonObject.addProperty("message","Payment Failed: Cart is Empty");

            }else if(resultsetCreditCard.next() == false){
                //fail to find the cc info
                responseJsonObject.addProperty("success",0);
                responseJsonObject.addProperty("message","Payment Failed: Please recheck the information entered");
            }
            else {
                //record the items in session
                recordTransactions(Usercart,UserObj);


                //store hashmap back in session
                session.setAttribute("Cart",Usercart);


                //payment success
                responseJsonObject.addProperty("success",1);
                responseJsonObject.addProperty("message","Payment Success");
            }




            //close
            resultsetCreditCard.close();
            statementCreditCard.close();
            //write to out
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

//        // get the previous items in a ArrayList
//        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
//        if (previousItems == null) {
//            previousItems = new ArrayList<String>();
//            previousItems.add(item);
//            session.setAttribute("previousItems", previousItems);
//        } else {
//            // prevent corrupted states through sharing under multi-threads
//            // will only be executed by one thread at a time
//            synchronized (previousItems) {
//                previousItems.add(item);
//            }
//        }
//
//        JsonObject responseJsonObject = new JsonObject();
//
//        JsonArray previousItemsJsonArray = new JsonArray();
//        previousItems.forEach(previousItemsJsonArray::add);
//        responseJsonObject.add("previousItems", previousItemsJsonArray);
//
//        response.getWriter().write(responseJsonObject.toString());
    }
}
