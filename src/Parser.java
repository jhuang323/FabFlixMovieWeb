import Actors.InsertActors;
import Actors.SAXParserServletActors;
import Casts.SAXParserServletCasts;
import Mains.SAXParserServletMain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


import Actors.InsertActors;
import Actors.SAXParserServletActors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class Parser {
    public static void main(String[] args) throws Exception{
//        final long startTime = System.currentTimeMillis();
//        SAXParserServletActors spe = new SAXParserServletActors();
//        spe.runExample();
//        InsertActors ia = new InsertActors();
//        ia.run(spe);
//        final long endTime = System.currentTimeMillis();
//        System.out.println("Total execution time: " + (endTime - startTime));

        final long startTimeSecond = System.currentTimeMillis();
        SAXParserServletCasts spc = new SAXParserServletCasts();
        spc.runExample();
        SAXParserServletMain spm = new SAXParserServletMain();
        spm.runExample();
        InsertMainsAndCasts imc = new InsertMainsAndCasts();
        imc.insert(spm, spc);
        final long endTimeSecond = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTimeSecond - startTimeSecond));
    }

}