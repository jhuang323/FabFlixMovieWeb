import Actors.Actor;
import Actors.SAXParserServletActors;
import Casts.DirectorFilms_Casts;
import Casts.SAXParserServletCasts;
import Mains.*;
import jakarta.servlet.ServletConfig;
import test.Films_Casts;
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
            CallableStatement insertMoviesCS = conn.prepareCall("{call add_moviept6(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            CallableStatement insertsingleMoviesCS = conn.prepareCall("{call add_singlemoviept6(?, ?, ?, ?, ?)}");
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
//                    newMovie.setstarsList(theCastMvMap.get(currentFilm.getFilmID()).getstarlist());

                movieIDMap.put(newMovie.getMovieID(), newMovie);


            }
            System.out.println("test");
            //System.out.println(movieIDMap.toString());


            //just add movie only




            for(MappedMovie amapmov: movieIDMap.values()){
                try{
                    Integer.parseInt(amapmov.getMovieYear());
                }
                catch (NumberFormatException e){
                    continue;
                }

                System.out.println(amapmov.getMovieID()+"title" +amapmov.getMovieTitle()+"year"+amapmov.getMovieYear()
                +"dirname"+amapmov.getDirectorName());
//                if(amapmov.getMovieID() == null){
//                    prepmoviestatement.setString(1,"nid"+countid);
//                    countid++;
//                }
//                else{
//                    prepmoviestatement.setString(1,amapmov.getMovieID());
//                }
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

            // import genre and genre in movies





//            for(MappedMovie amapmov: movieIDMap.values()){
//                try{
//                    Integer.parseInt(amapmov.getMovieYear());
//                }
//                catch (NumberFormatException e){
//                    continue;
//                }
//
//
//
//                insertsingleMoviesCS.setString(1,amapmov.getMovieTitle());
//                insertsingleMoviesCS.setInt(2, Integer.parseInt(amapmov.getMovieYear()));
//                insertsingleMoviesCS.setString(3,amapmov.getDirectorName());
//
//                insertsingleMoviesCS.executeUpdate();
//
//            }






            //


            //original

            //int batchcounter = 0;

//            Iterator<DirectorFilms_Casts> dirFilmsCastsIterator =
//                    castParser.getCast().getDirectorFilmsList().iterator();
//            while (dirFilmsCastsIterator.hasNext()) {
//                DirectorFilms_Casts dirFilm = dirFilmsCastsIterator.next();
//                //Director_Casts currentDirector = dirFilm.getDirector();
//
//
//                Iterator<Movie_Casts> movieCastsIteratorIterator = dirFilm.getMovieList().iterator();
//                while(movieCastsIteratorIterator.hasNext()){
//                    Movie_Casts currentMovie = movieCastsIteratorIterator.next();
//                    MappedMovie currentMapMovie = movieIDMap.get(currentMovie.getMovieID());
////                    System.out.println("the current movie id " + currentMovie.getMovieID());
//                    if(currentMapMovie != null){
//                        insertMoviesCS.setString(1,currentMapMovie.getMovieTitle());
//                        try {
//                            insertMoviesCS.setInt(2, Integer.parseInt(currentMapMovie.getMovieYear()));
//                        }
//                        catch(NumberFormatException e){
//                            insertMoviesCS.setNull(2, Types.INTEGER);
//                        }
//                        insertMoviesCS.setString(3,currentMapMovie.getDirectorName());
//                        insertMoviesCS.setString(4,currentMovie.getStarName());
//                        insertMoviesCS.setNull(5,Types.INTEGER);
////                        System.out.println(currentMapMovie.getMovieTitle());
////                        System.out.println(currentMapMovie.getMovieID());
//                        if(currentMapMovie.getGenreList() == null){
////                            insertMoviesCS.registerOutParameter(7, Types.INTEGER);
////                            insertMoviesCS.registerOutParameter(8, Types.VARCHAR);
////                            insertMoviesCS.registerOutParameter(9, Types.VARCHAR);
////                            insertMoviesCS.registerOutParameter(10, Types.INTEGER);
//                            insertMoviesCS.addBatch();
//                            batchcounter++;
////                            int rsuccess = insertMoviesCS.getInt(7);
////                            String rmovieID = insertMoviesCS.getString(8);
////                            String rstarID = insertMoviesCS.getString(9);
////                            int rgenreID = insertMoviesCS.getInt(10);
////
////
////
////                            if (rsuccess == 1) {
////                                System.out.println("Successfully added 1" +
////                                        " movie ID: " + rmovieID + " star ID: " + rstarID + "" +
////                                        " genre ID: " + rgenreID
////                                );
////                            } else {
////                                System.out.println("Failed top 1 " +
////                                        "add New Movie"
////                                );
////                            }
//                        }
//                        else {
//                            Iterator<Cat> genreIterator = currentMapMovie.getGenreList().iterator();
//                            while (genreIterator.hasNext()) {
//                                Cat genre = genreIterator.next();
//                                insertMoviesCS.setString(6, genre.getGenreName());
//
////                                insertMoviesCS.registerOutParameter(7, Types.INTEGER);
////                                insertMoviesCS.registerOutParameter(8, Types.VARCHAR);
////                                insertMoviesCS.registerOutParameter(9, Types.VARCHAR);
////                                insertMoviesCS.registerOutParameter(10, Types.INTEGER);
//                                insertMoviesCS.addBatch();
//                                batchcounter++;
////                                int rsuccess = insertMoviesCS.getInt(7);
////                                String rmovieID = insertMoviesCS.getString(8);
////                                String rstarID = insertMoviesCS.getString(9);
////                                int rgenreID = insertMoviesCS.getInt(10);
////
////                                if (rsuccess == 1) {
////                                    System.out.println("Successfully added 2" +
////                                            " movie ID: " + rmovieID + " star ID: " + rstarID + "" +
////                                            " genre ID: " + rgenreID
////                                    );
////                                } else {
////                                    System.out.println("Failed to  2" +
////                                            "add New Movie"
////                                    );
////                                }
//                            }
//                        }
//
//
//
////                        System.out.println("Adding " + currentMapMovie.getMovieTitle() + " year " + currentMapMovie.getMovieYear() + " director "
////                                + currentMapMovie.getDirectorName() + " genre " + currentMapMovie.getGenreList());
//
//                    }
//
//                    if(batchcounter >= 100){
//                        System.out.println("Execuring batch " + batchcounter);
//                        insertMoviesCS.executeBatch();
//                        batchcounter = 0;
//                    }
//                }
//
//
//                    //ignore any movie here that
//
//            }




//            insertStarsCS.setString(1, actor.getStarName());
//            String year = actor.getBirthYear();
//            if(year.equals("")){
//                insertStarsCS.setNull(2, Types.INTEGER);
//            }
//            else{
//                insertStarsCS.setInt(2,Integer.parseInt(year));
//            }
//            insertStarsCS.registerOutParameter(3,Types.INTEGER);
//            insertStarsCS.registerOutParameter(4,Types.VARCHAR);
//            insertStarsCS.executeUpdate();
//            int rsuccess = insertStarsCS.getInt(3);
//            String rstarID = insertStarsCS.getString(4);
//            if (rsuccess == 1){
//                System.out.println("Successfully added" + " star ID: " + rstarID);
//            }
//            else {
//                System.out.println("Failed to " + "add New Star");
//            }
        } catch (Exception e) {
            System.out.println("an exception accoured");
            e.printStackTrace();
        }
    }
}