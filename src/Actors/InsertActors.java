package Actors;

import jakarta.servlet.ServletConfig;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Types;
import java.util.Iterator;
public class InsertActors {
    private DataSource dataSource;
    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void run(SAXParserServletActors spe) throws Exception{
        // Change this to your own mysql username and password
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        System.out.println("Here");

        Class.forName("com.mysql.jdbc.Driver").newInstance();

        try (Connection conn = DriverManager.getConnection(loginUrl, loginUser, loginPasswd)) {
            System.out.println("Starting procedure");
            CallableStatement insertStarsCS = conn.prepareCall("{call add_star(?, ?, ?, ?)}");

            Iterator<Actor> it = spe.getActorList().iterator();
            while (it.hasNext()) {
                Actor actor = it.next();
                insertStarsCS.setString(1, actor.getStarName());
                String year = actor.getBirthYear();

                try{
                    insertStarsCS.setInt(2,Integer.parseInt(year));
                }
                catch (NumberFormatException e){
                    insertStarsCS.setNull(2, Types.INTEGER);
                }
                insertStarsCS.registerOutParameter(3,Types.INTEGER);
                insertStarsCS.registerOutParameter(4,Types.VARCHAR);
                insertStarsCS.executeUpdate();
                int rsuccess = insertStarsCS.getInt(3);
                String rstarID = insertStarsCS.getString(4);
//                if (rsuccess == 1){
//                    System.out.println("Successfully added" + " star ID: " + rstarID);
//                }
//                else {
//                    System.out.println("Failed to " + "add New Star");
//                }
            }
        } catch (Exception e) {

            System.out.println("an exception accoured");
            e.printStackTrace();

        }
    }
}