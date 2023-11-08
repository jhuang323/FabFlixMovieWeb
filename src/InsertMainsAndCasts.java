import Casts.DirectorFilms_Casts;
import Casts.SAXParserServletCasts;
import Mains.*;
import jakarta.servlet.ServletConfig;
import test.Films_Casts;
import test.Movie_Casts;
import test.SAXParserServletCastsJustin;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

    public void insert(SAXParserServletMain mainParser, SAXParserServletCastsJustin castParser) throws Exception{
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        Class.forName("com.mysql.jdbc.Driver").newInstance();

        String insertmovieSQL = "INSERT INTO movies (id,title,year,director)\n" +
                "VALUES\n" +
                "(?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(loginUrl, loginUser, loginPasswd)) {
            CallableStatement insertMoviesCS = conn.prepareCall("{call add_moviept6(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            CallableStatement insertsingleMoviesCS = conn.prepareCall("{call add_singlemoviept6(?, ?, ?, ?, ?)}");
            PreparedStatement prepmoviestatement = conn.prepareStatement(insertmovieSQL);

            HashMap<String, Movie_Casts> theCastMvMap = castParser.getCast().getMovieMap();

            movieIDMap = new HashMap<String, MappedMovie>();
            Iterator<DirectorFilms> it =
                    mainParser.getMovie().getDirectorFilmsList().iterator();

            int count = 0;
            while (it.hasNext()) {
                DirectorFilms dirFilm = it.next();
                Director currentDirector = dirFilm.getDirector();

                Iterator<Film> filmIterator = dirFilm.getFilmList().iterator();
                while(filmIterator.hasNext()){
                    count++;
                    System.out.println(count);
                    Film currentFilm = filmIterator.next();

                    MappedMovie newMovie = new MappedMovie();
                    newMovie.setDirectorName(currentDirector.getDirectorName());
                    newMovie.setMovieID(currentFilm.getFilmID());
                    newMovie.setMovieTitle(currentFilm.getFilmTitle());
                    newMovie.setMovieYear(currentFilm.getFilmYear());
                    newMovie.setGenreList(currentFilm.getCatList());
                    System.out.println("fid" + currentFilm.getFilmID());
                    System.out.println("star" + theCastMvMap.get(currentFilm.getFilmID()));
                    if(theCastMvMap.get(currentFilm.getFilmID()) == null){
                        newMovie.setstarsList(new ArrayList<String>());
                    }
                    else{
                        newMovie.setstarsList(theCastMvMap.get(currentFilm.getFilmID()).getstarlist());
                    }
//                    newMovie.setstarsList(theCastMvMap.get(currentFilm.getFilmID()).getstarlist());

                    movieIDMap.put(newMovie.getMovieID(), newMovie);
                }
            }
            System.out.println("test");
            //System.out.println(movieIDMap.toString());


            //just add movie only
            int countid = 99;

            int btcounter = 0;

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

                prepmoviestatement.setString(1,"zid"+countid);
                countid++;


                prepmoviestatement.setString(2,amapmov.getMovieTitle());
                prepmoviestatement.setInt(3, Integer.parseInt(amapmov.getMovieYear()));
                prepmoviestatement.setString(4,amapmov.getDirectorName());

                prepmoviestatement.addBatch();
                prepmoviestatement.clearParameters();
                btcounter ++;

                if (btcounter >= 100){
//                    prepmoviestatement.executeBatch();
                    btcounter = 0;
                }

            }
            prepmoviestatement.executeBatch();




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