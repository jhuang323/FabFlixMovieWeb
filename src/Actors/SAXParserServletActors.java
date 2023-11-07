package Actors;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SAXParserServletActors extends DefaultHandler {
    private static final String ACTOR = "actor";
    private static final String NAME = "stagename";
    private static final String BIRTHYEAR = "dob";
    private List<Actor> actorList;
    private String element;

    public SAXParserServletActors() {
        actorList = new ArrayList<Actor>();
    }

    public void runExample() {
        parseDocument();
        printData();
    }

    private void parseDocument() {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser sp = spf.newSAXParser();
            sp.parse("actors63.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
    private void printData() {

        System.out.println("No of Actors" + actorList.size() + "'.");

        Iterator<Actor> it = actorList.iterator();
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
        if (qName.equalsIgnoreCase(ACTOR)) {
            actorList.add(new Actor());
        }
    }
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase(NAME)) {
            latestMovie().setStarName(element.toString().trim());
        } else if (qName.equalsIgnoreCase(BIRTHYEAR)) {
            latestMovie().setBirthYear(element.toString().trim());
        }
    }
    private Actor latestMovie() {
        int latestActorIndex = actorList.size() - 1;
        return actorList.get(latestActorIndex);
    }
    public static void main(String[] args) {
        SAXParserServletActors spe = new SAXParserServletActors();
        spe.runExample();
    }

}