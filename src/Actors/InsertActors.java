package Actors;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;

public class InsertActors extends HttpServlet {
    private DataSource dataSource;
    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void run(SAXParserServletActors spe) {
        System.out.println("Here");
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("Starting procedure");
            CallableStatement insertStarsCS = conn.prepareCall("{call add_star(?, ?, ?, ?)}");

            Iterator<Actor> it = spe.getActorList().iterator();
            while (it.hasNext()) {
                Actor actor = it.next();
                insertStarsCS.setString(1, actor.getStarName());
                String year = actor.getBirthYear();
                if(year.equals("")){
                    insertStarsCS.setNull(2, Types.INTEGER);
                }
                else{
                    insertStarsCS.setInt(2,Integer.parseInt(year));
                }
                insertStarsCS.registerOutParameter(3,Types.INTEGER);
                insertStarsCS.registerOutParameter(4,Types.VARCHAR);
                insertStarsCS.executeUpdate();
                int rsuccess = insertStarsCS.getInt(3);
                String rstarID = insertStarsCS.getString(4);
                if (rsuccess == 1){
                    System.out.println("Successfully added" + " star ID: " + rstarID);
                }
                else {
                    System.out.println("Failed to " + "add New Star");
                }
            }
        } catch (Exception e) {

        }
    }
}