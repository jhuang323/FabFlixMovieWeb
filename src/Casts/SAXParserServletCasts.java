package Casts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class SAXParserServletCasts extends DefaultHandler {
    private static final String CASTS = "casts";
    private static final String DIRECTOR_FILMS = "dirfilms";
    private static final String DIRID = "dirid";
    private static final String DIRNAME = "is";
    private static final String FILMC = "filmc";
    private static final String MOVIE = "m";
    private static final String FILMID = "f";
    private static final String TITLE = "t";
    private static final String STARNAME = "a";
    private Casts cast;
    private String element;
    private int directorFilmsListSize;
    private int latestMovieListSize;


    public SAXParserServletCasts() {
        cast = new Casts();
        directorFilmsListSize = 0;
    }

    public void runExample() {
        parseDocument();
        printData();
    }

    private void parseDocument() {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser sp = spf.newSAXParser();
            sp.parse("casts124.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Iterate through the list and print
     * the contents
     */
    private void printData() {

        System.out.println("No of Directors Films '" + cast.getDirectorFilmsList().size() + "'.");

        Iterator<DirectorFilms_Casts> it = cast.getDirectorFilmsList().iterator();
        int c = 1;
        while (it.hasNext()) {
            System.out.println(it.next().toString());
            System.out.println(c);
            c+=1;
        }
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        element = new String(ch, start, length);
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        element = "";
        if (qName.equalsIgnoreCase(CASTS)) {
            cast.setDirectorFilmsList(new ArrayList<DirectorFilms_Casts>());
        } else if (qName.equalsIgnoreCase(DIRECTOR_FILMS)) {
            cast.addDirectorFilm(new DirectorFilms_Casts());
            directorFilmsListSize++;
            latestDirectorFilm().setDirector(new Director_Casts());
        }else if (qName.equalsIgnoreCase(FILMC)) {
            latestDirectorFilm().setMovieList(new ArrayList<Movie_Casts>());
            latestMovieListSize = 0;
        } else if (qName.equalsIgnoreCase(MOVIE)) {
            if(latestDirectorFilm().getMovieList() != null){
                latestDirectorFilm().addMovie(new Movie_Casts());
                latestMovieListSize++;
            }
        }
    }
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase(DIRID)) {
            latestDirectorFilm().getDirector().setDirectorID(element.toString().trim());
        } else if (qName.equalsIgnoreCase(DIRNAME)) {
            latestDirectorFilm().getDirector().setDirectorName(element.toString().trim());
        } else if (qName.equalsIgnoreCase(FILMID)) {
            latestMovie().setMovieID(element.toString().trim());
        }else if (qName.equalsIgnoreCase(TITLE)) {
            latestMovie().setMovieTitle(element.toString().trim());
        }else if (qName.equalsIgnoreCase(STARNAME)) {
            latestMovie().setStarName(element.toString().trim());
        }
    }
    private DirectorFilms_Casts latestDirectorFilm() {
        List<DirectorFilms_Casts> directorFilmsList = cast.getDirectorFilmsList();
        int latestDirectorFilmIndex = directorFilmsListSize - 1;
        return directorFilmsList.get(latestDirectorFilmIndex);
    }
    private Movie_Casts latestMovie() {
        List<Movie_Casts> movieList = latestDirectorFilm().getMovieList();
        int latestFilmIndex = latestMovieListSize- 1;
        return movieList.get(latestFilmIndex);
    }
    public static void main(String[] args) {
        SAXParserServletCasts spe = new SAXParserServletCasts();
        spe.runExample();
    }

}