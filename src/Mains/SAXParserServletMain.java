package Mains;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class SAXParserServletMain extends DefaultHandler {
    private static final String FILMS = "films";
    private static final String FILM = "film";
    private static final String CATS = "cats";
    private static final String CAT = "cat";
    private static final String DIRNAME = "dirname";
    private static final String FID = "fid";
    private static final String TITLE = "t";
    private static final String YEAR = "year";
    private String element;
    private HashMap<String, String> genres;
    private HashMap<String, Movie> movieMap;
    private List<String> errorMessages;

    public SAXParserServletMain() {

        movieMap = new HashMap<String, Movie>();
        genres = new HashMap<String, String>();
        errorMessages = new ArrayList<String>();
        genres.put("dram", "Drama");
        genres.put("draam", "Drama");
        genres.put("susp", "Suspense");
        genres.put("romt", "Romance");
        genres.put("myst", "Mystery");
        genres.put("comd", "Comedy");
        genres.put("docu", "Documentary");
        genres.put("bio", "Biography");
        genres.put("musc", "Musical");
        genres.put("epic", "Epic");
        genres.put("west", "Western");
        genres.put("hist", "History");
        genres.put("actn", "Action");
        genres.put("fant", "Fantasy");
        genres.put("stage musical", "Stage Musical");
        genres.put("cart", "Cartoon");
        genres.put("comd west", "Comedy Western");
        genres.put("scfi", "Science Fiction");
        genres.put("kinky", "Kinky");
        genres.put("advt", "Adventure");
        genres.put("horr", "Horror");
        genres.put("surr", "Surreal");
        genres.put("road", "Road");
        genres.put("noir", "Noir");
        genres.put("porn", "Porn");
        genres.put("porb", "Porn");
        genres.put("cult", "Cult");

        genres.put("ctxx", "Uncategorized");
        genres.put("actn", "Violence");
        genres.put("advt", "Adventure");
        genres.put("avga", "Avantgarde");
        genres.put("camp", "Now-camp");
        genres.put("cart", "Cartoon");
        genres.put("cnr", "Copsandrobbers");
        genres.put("comd", "Comedy");
        genres.put("disa", "Disaster");
        genres.put("docu", "Documentary");
        genres.put("dram", "Drama");
        genres.put("epic", "Epic");
        genres.put("faml", "Family");
        genres.put("hist", "History");
        genres.put("horr", "Horror");
        genres.put("musc", "Musical");
        genres.put("myst", "Mystery");
        genres.put("noir", "Black");
        genres.put("porn", "Pornography");
        genres.put("romt", "Romantic");
        genres.put("scfi", "Sciencefiction");
        genres.put("surl", "Sureal");
        genres.put("susp", "Thriller");
        genres.put("west", "Western");


        genres.put("susp", "Thriller");
        genres.put("cnr", "Cops and robbers");
        genres.put("sram", "Drama");
        genres.put("west", "Western");
        genres.put("myst", "Mystery");
        genres.put("s.f.", "Science Fiction");
        genres.put("advt", "Adventure");
        genres.put("horr", "Horror");
        genres.put("romt", "Romantic");
        genres.put("comd", "Comedy");
        genres.put("musc", "Musical");
        genres.put("docu", "Documentary");
        genres.put("biop", "Biographical picture");
        genres.put("tv", "TV Show");
        genres.put("tvs", "TV Series");
        genres.put("tvm", "TV Miniseries");

    }
    public void parseDocument() {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser sp = spf.newSAXParser();
            sp.parse("mains243.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        element = new String(ch, start, length);
    }
    String tempDirectorName;
    Movie tempMovie;
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        element = "";
        if (qName.equalsIgnoreCase(FILM)) {
            tempMovie = new Movie();
        } else if (qName.equalsIgnoreCase(CATS)) {
            tempMovie.setCat(new Cat());
            tempMovie.getCat().setGenreNames(new ArrayList<String>());
        }
    }
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase(DIRNAME)) {
            tempDirectorName = (element.toString().trim());
        } else if (qName.equalsIgnoreCase(FID)) {
            tempMovie.setMovieID(element.toString().trim());
            tempMovie.setDirectorName(tempDirectorName);
            movieMap.put(element.toString().trim(), tempMovie);
        }else if (qName.equalsIgnoreCase(TITLE)) {
            tempMovie.setMovieTitle(element.toString().trim());
        }else if (qName.equalsIgnoreCase(YEAR)) {
            tempMovie.setMovieYear(element.toString().trim());
        } else if (qName.equalsIgnoreCase(CAT)) {
            if(tempMovie.getCat() != null){
                if(genres.get(element.toString().trim().toLowerCase()) == null){
                    tempMovie.getCat().addGenre(element.toString().trim());
                }
                else{
                    tempMovie.getCat().addGenre(genres.get(element.toString().trim().toLowerCase()));
                }
            }

        }else if(qName.equalsIgnoreCase(FILMS)){
            if(tempMovie.getCat() == null){
                errorMessages.add("Following movie does not have <Cats> tag: " + tempMovie
                        .getMovieID());
            }
            if(movieMap.get(tempMovie.getMovieID()) != null){
                if(tempMovie.getMovieTitle() == null){
                    movieMap.remove(tempMovie.getMovieID());
                    errorMessages.add("Following movie does not have movie title: " + tempMovie.getMovieID());
                }
                else if(tempMovie.getMovieYear() == null){
                    movieMap.remove(tempMovie.getMovieID());
                    errorMessages.add("Following movie does not have movie year: " + tempMovie.getMovieID());
                }
            }
        }

    }

    public HashMap<String, Movie> getMovieMap(){
        return movieMap;
    }
    public List<String> getErrorMessages() {
        return errorMessages;
    }
    public static void main(String[] args) {
        SAXParserServletMain spe = new SAXParserServletMain();
        spe.parseDocument();
    }

}