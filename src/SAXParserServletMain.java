
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class SAXParserServletMain extends DefaultHandler {
    private static final String MOVIES = "movies";
    private static final String DIRECTOR_FILMS = "directorfilms";
    private static final String DIRECTOR = "director";
    private static final String FILM = "film";
    private static final String CAT = "cat";
    private Movie movie;
    private String element;

    public SAXParserServletMain() {
        movie = new Movie();
    }

    public void runExample() {
        parseDocument();
        printData();
    }

    private void parseDocument() {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser sp = spf.newSAXParser();
            sp.parse("main243.xml", this);

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

        System.out.println("No of Employees '" + movie.getDirectorFilmsList().size() + "'.");

        Iterator<DirectorFilms> it = movie.getDirectorFilmsList().iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        element = new String(ch, start, length);
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        element = "";
        if (qName.equalsIgnoreCase(MOVIES)) {
            //add it to the list
            movie.setDirectorFilmsList(new ArrayList<DirectorFilms>());

        } else if (qName.equalsIgnoreCase(DIRECTOR_FILMS)) {
            tempEmp.setName(tempVal);
        } else if (qName.equalsIgnoreCase(DIRECTOR)) {
            tempEmp.setName(tempVal);
        } else if (qName.equalsIgnoreCase(FILM)) {
            tempEmp.setId(Integer.parseInt(tempVal));
        } else if (qName.equalsIgnoreCase(CAT)) {
            tempEmp.setAge(Integer.parseInt(tempVal));
        }
    }
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase(MOVIES)) {
            //add it to the list
           // movie.setDirectorFilmsList(new ArrayList<DirectorFilms>());

        } else if (qName.equalsIgnoreCase(DIRECTOR_FILMS)) {
            //tempEmp.setName(tempVal);
        } else if (qName.equalsIgnoreCase(DIRECTOR)) {
            //tempEmp.setName(tempVal);
        } else if (qName.equalsIgnoreCase(FILM)) {
            //tempEmp.setId(Integer.parseInt(tempVal));
        } else if (qName.equalsIgnoreCase(CAT)) {
            //tempEmp.setAge(Integer.parseInt(tempVal));
        }

    }

    public static void main(String[] args) {
        SAXParserServletMain spe = new SAXParserServletMain();
        spe.runExample();
    }

}