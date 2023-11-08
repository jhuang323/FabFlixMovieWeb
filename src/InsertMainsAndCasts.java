import Casts.DirectorFilms_Casts;
import Casts.Movie_Casts;
import Casts.SAXParserServletCasts;
import Mains.*;
import jakarta.servlet.ServletConfig;

import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;

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

    public void insert(SAXParserServletMain mainParser, SAXParserServletCasts castParser) throws Exception{
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        Class.forName("com.mysql.jdbc.Driver").newInstance();

        try (Connection conn = DriverManager.getConnection(loginUrl, loginUser, loginPasswd)) {
            CallableStatement insertMoviesCS = conn.prepareCall("{call add_movie(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            movieIDMap = new HashMap<String, MappedMovie>();
            Iterator<DirectorFilms> it =
                    mainParser.getMovie().getDirectorFilmsList().iterator();
            while (it.hasNext()) {
                DirectorFilms dirFilm = it.next();
                Director currentDirector = dirFilm.getDirector();

                Iterator<Film> filmIterator = dirFilm.getFilmList().iterator();
                while(filmIterator.hasNext()){
                    Film currentFilm = filmIterator.next();

                    MappedMovie newMovie = new MappedMovie();
                    newMovie.setDirectorName(currentDirector.getDirectorName());
                    newMovie.setMovieID(currentFilm.getFilmID());
                    newMovie.setMovieTitle(currentFilm.getFilmTitle());
                    newMovie.setMovieYear(currentFilm.getFilmYear());
                    newMovie.setGenreList(currentFilm.getCatList());
                    movieIDMap.put(newMovie.getMovieID(), newMovie);
                }
            }
            //System.out.println(movieIDMap.toString());

            Iterator<DirectorFilms_Casts> dirFilmsCastsIterator =
                    castParser.getCast().getDirectorFilmsList().iterator();
            while (dirFilmsCastsIterator.hasNext()) {
                DirectorFilms_Casts dirFilm = dirFilmsCastsIterator.next();
                //Director_Casts currentDirector = dirFilm.getDirector();

                Iterator<Movie_Casts> movieCastsIteratorIterator = dirFilm.getMovieList().iterator();
                while(movieCastsIteratorIterator.hasNext()){
                    Movie_Casts currentMovie = movieCastsIteratorIterator.next();
                    MappedMovie currentMapMovie = movieIDMap.get(currentMovie.getMovieID());
                    if(currentMapMovie != null){
                        insertMoviesCS.setString(1,currentMapMovie.getMovieTitle());
                        try {
                            insertMoviesCS.setInt(2, Integer.parseInt(currentMapMovie.getMovieYear()));
                        }
                        catch(NumberFormatException e){
                            insertMoviesCS.setNull(2, Types.INTEGER);
                        }
                        insertMoviesCS.setString(3,currentMapMovie.getDirectorName());
                        insertMoviesCS.setString(4,currentMovie.getStarName());
                        insertMoviesCS.setNull(5,Types.INTEGER);
//                        System.out.println(currentMapMovie.getMovieTitle());
//                        System.out.println(currentMapMovie.getMovieID());
                        if(currentMapMovie.getGenreList() == null){
                            insertMoviesCS.registerOutParameter(7, Types.INTEGER);
                            insertMoviesCS.registerOutParameter(8, Types.VARCHAR);
                            insertMoviesCS.registerOutParameter(9, Types.VARCHAR);
                            insertMoviesCS.registerOutParameter(10, Types.INTEGER);
                            insertMoviesCS.executeUpdate();
                            int rsuccess = insertMoviesCS.getInt(7);
                            String rmovieID = insertMoviesCS.getString(8);
                            String rstarID = insertMoviesCS.getString(9);
                            int rgenreID = insertMoviesCS.getInt(10);

                            if (rsuccess == 1) {
                                System.out.println("Successfully added" +
                                        " movie ID: " + rmovieID + " star ID: " + rstarID + "" +
                                        " genre ID: " + rgenreID
                                );
                            } else {
                                System.out.println("Failed to " +
                                        "add New Movie"
                                );
                            }
                        }
                        else {
                            Iterator<Cat> genreIterator = currentMapMovie.getGenreList().iterator();
                            while (genreIterator.hasNext()) {
                                Cat genre = genreIterator.next();
                                insertMoviesCS.setString(6, genre.getGenreName());

                                insertMoviesCS.registerOutParameter(7, Types.INTEGER);
                                insertMoviesCS.registerOutParameter(8, Types.VARCHAR);
                                insertMoviesCS.registerOutParameter(9, Types.VARCHAR);
                                insertMoviesCS.registerOutParameter(10, Types.INTEGER);
                                insertMoviesCS.executeUpdate();
                                int rsuccess = insertMoviesCS.getInt(7);
                                String rmovieID = insertMoviesCS.getString(8);
                                String rstarID = insertMoviesCS.getString(9);
                                int rgenreID = insertMoviesCS.getInt(10);

                                if (rsuccess == 1) {
                                    System.out.println("Successfully added" +
                                            " movie ID: " + rmovieID + " star ID: " + rstarID + "" +
                                            " genre ID: " + rgenreID
                                    );
                                } else {
                                    System.out.println("Failed to " +
                                            "add New Movie"
                                    );
                                }
                            }
                        }

                    }
                    //ignore any movie here that
                }
            }




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