package Actors;
import java.io.IOException;
import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SAXParserServletActors extends DefaultHandler {
    private static final String ACTOR = "actor";
    private static final String NAME = "stagename";
    private static final String BIRTHYEAR = "dob";
    private String element;
    private HashMap<String, Actor> actorsMap;
    private List<String> errorMessages;

    public SAXParserServletActors() {
        actorsMap = new HashMap<String, Actor>();
        errorMessages = new ArrayList<String>();
    }
    public void parseDocument() {
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

        System.out.println("No of Actors" + actorsMap.size() + "'.");

        Iterator actorsIterator = actorsMap.entrySet().iterator();
        int c = 1;
        while (actorsIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)actorsIterator.next();
            System.out.println(mapElement.getValue().toString());
            System.out.println(c);
            c+=1;
        }
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        element = new String(ch, start, length);
    }
    Actor tempActor;

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        element = "";
        if (qName.equalsIgnoreCase(ACTOR)) {
            tempActor = new Actor();
        }
    }
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase(NAME)) {
            tempActor.setStarName(element.toString().trim());
        } else if (qName.equalsIgnoreCase(BIRTHYEAR)) {
            tempActor.setBirthYear(element.toString().trim());
            String nameAndYear = tempActor.getStarName();
            if(actorsMap.get(nameAndYear) == null){
                actorsMap.put(nameAndYear, tempActor);
            }
            else{
                errorMessages.add("Following star is a duplicate: Name: " + tempActor.getStarName()
                        + " BirthYear: " + tempActor.getBirthYear());
            }
        }
    }
    public HashMap<String, Actor> getActorsMap(){
        return actorsMap;
    }
    public List<String> getErrorMessages() {
        return errorMessages;
    }
    public static void main(String[] args) {
        SAXParserServletActors spa = new SAXParserServletActors();
        spa.parseDocument();
    }

}