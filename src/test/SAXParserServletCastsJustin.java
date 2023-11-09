package test;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class SAXParserServletCastsJustin extends DefaultHandler {
    private static final String CASTS = "casts";
    private static final String DIRECTOR_FILMS = "dirfilms";
    private static final String DIRID = "dirid";
    private static final String DIRNAME = "is";
    private static final String FILMC = "filmc";
    private static final String MOVIE = "m";
    private static final String FILMID = "f";
    private static final String TITLE = "t";
    private static final String STARNAME = "a";
    private Films_Casts cast;
    private String element;
    private int directorFilmsListSize;
    private int latestMovieListSize;


    public SAXParserServletCastsJustin() {
        cast = new Films_Casts();
        directorFilmsListSize = 0;
    }

    public void runExample() {
        parseDocument();
//        printData();
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

//    private void printData() {
//
//        System.out.println("No of Directors Films '" + cast.getDirectorFilmsList().size() + "'.");
//
//        Iterator<DirectorFilms_Casts> it = cast.getDirectorFilmsList().iterator();
//        int c = 1;
//        while (it.hasNext()) {
//            System.out.println(it.next().toString());
//            System.out.println(c);
//            c+=1;
//        }
//    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        element = new String(ch, start, length);
    }

    Movie_Casts tempstoreMc;
    String tempfilmid;
    String tempdirname;

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        element = "";
        if (qName.equalsIgnoreCase(MOVIE)) {
            tempstoreMc = new Movie_Casts();

        }
    }
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase(DIRNAME)) {
            tempdirname = element.toString().trim();
        }
        else if (qName.equalsIgnoreCase(FILMID)) {
            tempfilmid = element.toString().trim();

            if(!cast.getMovieMap().containsKey(tempfilmid)){
                cast.addMovieMap(tempfilmid,tempdirname);
            }

        }
        else if (qName.equalsIgnoreCase(STARNAME)) {
            cast.getMovieMap().get(tempfilmid).setStarName(element.toString().trim());
//            tempstoreMc.setStarName(element.toString().trim());
//            latestMovie().setStarName(element.toString().trim());
        }
    }
//    private DirectorFilms_Casts latestDirectorFilm() {
//        List<DirectorFilms_Casts> directorFilmsList = cast.getDirectorFilmsList();
//        int latestDirectorFilmIndex = directorFilmsListSize - 1;
//        return directorFilmsList.get(latestDirectorFilmIndex);
//    }
//    private Movie_Casts latestMovie() {
//        List<Movie_Casts> movieList = latestDirectorFilm().getMovieList();
//        int latestFilmIndex = latestMovieListSize- 1;
//        return movieList.get(latestFilmIndex);
//    }
    public Films_Casts getCast(){
        return cast;
    }
    public static void main(String[] args) {
        SAXParserServletCastsJustin spe = new SAXParserServletCastsJustin();
        spe.runExample();

        System.out.println("");
    }

}