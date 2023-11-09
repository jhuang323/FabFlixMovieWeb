package Casts;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class SAXParserServletCasts extends DefaultHandler {
    private static final String DIRNAME = "is";
    private static final String MOVIE = "m";
    private static final String FILMID = "f";
    private static final String STARNAME = "a";
    private Films_Casts cast;
    private String element;
    public SAXParserServletCasts() {
        cast = new Films_Casts();
    }
    public void parseDocument() {
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
        }
    }
    public Films_Casts getCast(){
        return cast;
    }
    public static void main(String[] args) {
        SAXParserServletCasts spe = new SAXParserServletCasts();
        spe.parseDocument();
    }

}