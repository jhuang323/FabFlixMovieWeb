import Actors.Actor;
import Actors.SAXParserServletActors;
import Mains.*;
import jakarta.servlet.ServletConfig;
import test.Movie_Casts;
import test.SAXParserServletCastsJustin;

import java.sql.*;
import java.util.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class InsertMainsAndCasts {
    private DataSource dataSource;
    private HashMap<String, MappedMovie> movieIDMap;
    private Set<String> movieFields = new HashSet<String>();
    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc" +
                    "/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void insert(SAXParserServletMain mainParser, SAXParserServletCastsJustin castParser, SAXParserServletActors actorParser) throws Exception{
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        Class.forName("com.mysql.jdbc.Driver").newInstance();

        HashMap<String,Integer> insertedGenreMap = new HashMap<>();
        HashMap<String,String> insertedStarMap = new HashMap<>();
        HashMap<String, Actor> actorbirthyearMap = actorParser.getActorsMap();

        String qmaxMovID = "select SUBSTRING(max(id),3) as mmid from movies";
        String qmaxStarID = "select substring(max(id),3) as msid from stars;";
        String qmaxgenreID = "select max(id) as mgid from genres";

        String insertmovieSQL = "INSERT INTO movies (id,title,year,director)\n" +
                "VALUES\n" +
                "(?, ?, ?, ?)";

        String insertgenreSQL = "INSERT INTO genres (id,name)\n" +
                "VALUES\n" +
                "(?, ?)";
        String insertgimSQL = "INSERT INTO genres_in_movies (genreId,movieId)\n" +
                "VALUES\n" +
                "(?, ?)";

        String insertstarSQL = "INSERT INTO stars (id,name,birthYear)\n" +
                "VALUES\n" +
                "(?, ?, ?)";
        String insertsimSQL = "INSERT INTO stars_in_movies (starId,movieId)\n" +
                "VALUES\n" +
                "(?, ?)";

        try (Connection conn = DriverManager.getConnection(loginUrl, loginUser, loginPasswd)) {
            PreparedStatement prepmoviestatement = conn.prepareStatement(insertmovieSQL);
            PreparedStatement prepgenrestatement = conn.prepareStatement(insertgenreSQL);
            PreparedStatement prepgimstatement = conn.prepareStatement(insertgimSQL);
            PreparedStatement prepstarstatement = conn.prepareStatement(insertstarSQL);
            PreparedStatement prepsimstatement = conn.prepareStatement(insertsimSQL);

            int curMovieID = 0;
            int curGenreID = 0;
            int curStarID = 0;

            //create statement
            Statement maxmidstatement = conn.createStatement();
            //create statement
            Statement maxgidstatement = conn.createStatement();
            //create statement
            Statement maxsidstatement = conn.createStatement();

            ResultSet rsmmid = maxmidstatement.executeQuery(qmaxMovID);
            rsmmid.next();

            curMovieID = Integer.parseInt(rsmmid.getString("mmid"));

            rsmmid.close();
            maxmidstatement.close();

            ResultSet rsmgid = maxgidstatement.executeQuery(qmaxgenreID);

            rsmgid.next();

            curGenreID = Integer.parseInt(rsmgid.getString("mgid"));

            rsmgid.close();
            maxgidstatement.close();

            ResultSet rsmsid = maxsidstatement.executeQuery(qmaxStarID);
            rsmsid.next();

            curStarID = Integer.parseInt(rsmsid.getString("msid"));

            rsmsid.close();
            maxgidstatement.close();

            HashMap<String, Movie_Casts> theCastMvMap = castParser.getCast().getMovieMap();
            movieIDMap = new HashMap<String, MappedMovie>();
            Iterator it =
                    mainParser.getMovieMap().entrySet().iterator();
            int count = 0;
            while (it.hasNext()) {
                Map.Entry MoviemapElement = (Map.Entry)it.next();

                String MmelemKey = (String) MoviemapElement.getKey();
                Movie MmelemVal = (Movie) MoviemapElement.getValue();
                String allMovieFields =
                        (MmelemVal.getMovieTitle() + MmelemVal.getDirectorName() + MmelemVal.getDirectorName()).toLowerCase();
                if(movieFields.contains(allMovieFields)){
                    continue;
                }
                else{
                    movieFields.add(allMovieFields);
                }
                MappedMovie newMovie = new MappedMovie();
                newMovie.setDirectorName(MmelemVal.getDirectorName());
                newMovie.setMovieID(MmelemVal.getMovieID());
                newMovie.setMovieTitle(MmelemVal.getMovieTitle());
                newMovie.setMovieYear(MmelemVal.getMovieYear());

                if(!(MmelemVal.getCat() == null)){
                    newMovie.setGenreList(MmelemVal.getCat().getGenreNames());
                }
                else{
                    newMovie.setGenreList(new ArrayList<String>());
                }
                System.out.println("fid" + MmelemVal.getMovieID());
                System.out.println("star" + theCastMvMap.get(MmelemVal.getMovieID()));
                if(theCastMvMap.get(MmelemVal.getMovieID()) == null){
                    newMovie.setstarsList(new ArrayList<String>());
                }
                else{
                    newMovie.setstarsList(theCastMvMap.get(MmelemVal.getMovieID()).getstarlist());
                }
                movieIDMap.put(newMovie.getMovieID(), newMovie);
            }
            System.out.println("test");

            for(MappedMovie amapmov: movieIDMap.values()){
                try{
                    Integer.parseInt(amapmov.getMovieYear());
                }
                catch (NumberFormatException e){
                    continue;
                }
                System.out.println(amapmov.getMovieID()+"title" +amapmov.getMovieTitle()+"year"+amapmov.getMovieYear()
                +"dirname"+amapmov.getDirectorName());
                curMovieID++;
                String tarMovieID = "zz"+curMovieID;
                prepmoviestatement.setString(1,tarMovieID);
                prepmoviestatement.setString(2,amapmov.getMovieTitle());
                prepmoviestatement.setInt(3, Integer.parseInt(amapmov.getMovieYear()));
                prepmoviestatement.setString(4,amapmov.getDirectorName());
                prepmoviestatement.executeUpdate();
                //insert the genre list
                int targenreid = 0;
                if(amapmov.getGenreList() != null){
                    for(String agenreStr: amapmov.getGenreList()){
                        System.out.println("genre"+ agenreStr);
                        if(!insertedGenreMap.containsKey(agenreStr)){
                            //insert into genre
                            targenreid = curGenreID + 1;
                            curGenreID++;
                            prepgenrestatement.setInt(1,targenreid);
                            prepgenrestatement.setString(2,agenreStr);
                            prepgenrestatement.executeUpdate();
                            //put into genre map
                            insertedGenreMap.put(agenreStr,targenreid);
                        }
                        else{
                            //get the id and set
                            targenreid = insertedGenreMap.get(agenreStr);
                        }
                        //for genres in movies
                        prepgimstatement.setInt(1,targenreid);
                        prepgimstatement.setString(2,tarMovieID);
                        prepgimstatement.executeUpdate();
                    }
                }
                String targetstarID;
                //the stars portion
                for(String astarname:amapmov.getstarsList()){
                    System.out.println("star name" + astarname);
                    if(!insertedStarMap.containsKey(astarname)){
                        targetstarID ="zm" + curStarID + 1;
                        curStarID++;
                        prepstarstatement.setString(1,targetstarID);
                        prepstarstatement.setString(2,astarname);
                        try{
                            System.out.println("the birth year" + Integer.parseInt(actorbirthyearMap.get(astarname).getBirthYear()));
                            prepstarstatement.setInt(3,Integer.parseInt(actorbirthyearMap.get(astarname).getBirthYear()));
                        }
                        catch (Exception e){
                            prepstarstatement.setNull(3,Types.VARCHAR);
                        }
                        prepstarstatement.executeUpdate();
                        //add to insert starmap
                        insertedStarMap.put(astarname,targetstarID);
                    }
                    else{
                        targetstarID = insertedStarMap.get(astarname);
                    }
                    //insert into the sim table
                    prepsimstatement.setString(1,targetstarID);
                    prepsimstatement.setString(2,tarMovieID);
                    prepsimstatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.out.println("an exception accoured");
            e.printStackTrace();
        }
    }
}