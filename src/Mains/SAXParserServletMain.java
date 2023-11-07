package Mains;

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

public class SAXParserServletMain extends DefaultHandler {
    private static final String MOVIES = "movies";
    private static final String DIRECTOR_FILMS = "directorfilms";
    private static final String DIRECTOR = "director";
    private static final String FILMS = "films";
    private static final String FILM = "film";
    private static final String CATS = "cats";
    private static final String CAT = "cat";
    private static final String DIRID = "dirid";
    private static final String DIRNAME = "dirname";
    private static final String FID = "fid";
    private static final String TITLE = "t";
    private static final String YEAR = "year";
    private Movie movie;
    private String element;
    private HashMap<String, String> genres;
    private int directorFilmListSize;
    private int filmListSize;

    public SAXParserServletMain() {

        movie = new Movie();
        directorFilmListSize = 0;
        genres = new HashMap<String, String>();
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

    }

    public void runExample() {
        parseDocument();
        printData();
    }

    private void parseDocument() {
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
    private void printData() {

        System.out.println("No of Directors Films '" + movie.getDirectorFilmsList().size() + "'.");

        Iterator<DirectorFilms> it = movie.getDirectorFilmsList().iterator();
        int c = 1;
        while (it.hasNext()) {
            System.out.println(it.next().toString());
            System.out.println(c);
            c+=1;
        }
        System.out.println(genres.toString());
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
            movie.addDirectorFilm(new DirectorFilms());
            directorFilmListSize++;
        } else if (qName.equalsIgnoreCase(DIRECTOR)) {
            latestDirectorFilm().setDirector(new Director());
        } else if (qName.equalsIgnoreCase(FILMS)) {
            latestDirectorFilm().setFilmList(new ArrayList<Film>());
            filmListSize = 0;
        } else if (qName.equalsIgnoreCase(FILM)) {
            latestDirectorFilm().addFilm(new Film());
            filmListSize++;
        } else if (qName.equalsIgnoreCase(CATS)) {
            latestFilm().setCatList(new ArrayList<Cat>());
        }else if (qName.equalsIgnoreCase(CAT)) {
            if(latestFilm().getCatList() != null){
                latestFilm().addCatList(new Cat());
            }
        }
    }
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase(DIRID)) {
            latestDirectorFilm().getDirector().setDirectorID(element.toString().trim());
        } else if (qName.equalsIgnoreCase(DIRNAME)) {
            latestDirectorFilm().getDirector().setDirectorName(element.toString().trim());
        } else if (qName.equalsIgnoreCase(FID)) {
            latestFilm().setFilmID(element.toString().trim());
        }else if (qName.equalsIgnoreCase(TITLE)) {
            latestFilm().setTitle(element.toString().trim());
        }else if (qName.equalsIgnoreCase(YEAR)) {
            latestFilm().setYear(element.toString().trim());
        }else if (qName.equalsIgnoreCase(CAT)) {
            String lowered = (element.toString()).trim().toLowerCase();
            if(genres.containsKey(lowered)){
                latestFilm().latestCategory().setGenreName(genres.get(lowered));
            }
            else{
                genres.put(lowered, lowered);
                latestFilm().latestCategory().setGenreName(lowered);
            }
        }

    }

    private DirectorFilms latestDirectorFilm() {
        List<DirectorFilms> directorFilmsList = movie.getDirectorFilmsList();
        int latestDirectorFilmIndex = directorFilmListSize - 1;
        return directorFilmsList.get(latestDirectorFilmIndex);
    }
    private Film latestFilm() {
        List<Film> filmList = latestDirectorFilm().getFilmList();
        int latestFilmIndex = filmListSize - 1;
        return filmList.get(latestFilmIndex);
    }

    public static void main(String[] args) {
        SAXParserServletMain spe = new SAXParserServletMain();
        spe.runExample();
    }

}