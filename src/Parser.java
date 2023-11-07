import Actors.InsertActors;
import Actors.SAXParserServletActors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Parser {
    public static void main(String[] args) {
        SAXParserServletActors spe = new SAXParserServletActors();
        spe.runExample();
        InsertActors ia = new InsertActors();
        ia.run(spe);
    }

}