package Actors;
import java.io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SAXParserServletActors extends DefaultHandler {
    private static final String ACTOR = "actor";
    private static final String NAME = "stagename";
    private static final String BIRTHYEAR = "dob";
    private List<Actor> actorList;
    private String element;
    private HashMap<String, String> actorsMap;
    private int actorListSize;

    public SAXParserServletActors() {
        actorList = new ArrayList<Actor>();
        actorsMap = new HashMap<String, String>();
        actorListSize = 0;
    }

    public void runExample() {
        parseDocument();
        printData();
//        System.out.println("Here");
//        try (Connection conn = dataSource.getConnection()) {
//            System.out.println("Starting procedure");
//            CallableStatement insertStarsCS = conn.prepareCall("{call add_star(?, ?, ?, ?)}");
//
//            Iterator<Actor> it = actorList.iterator();
//            while (it.hasNext()) {
//                Actor actor = it.next();
//                insertStarsCS.setString(1, actor.getStarName());
//                String year = actor.getBirthYear();
//                if(year.equals("")){
//                    insertStarsCS.setNull(2, Types.INTEGER);
//                }
//                else{
//                    insertStarsCS.setInt(2,Integer.parseInt(year));
//                }
//                insertStarsCS.registerOutParameter(3,Types.INTEGER);
//                insertStarsCS.registerOutParameter(4,Types.VARCHAR);
//                insertStarsCS.executeUpdate();
//                int rsuccess = insertStarsCS.getInt(3);
//                String rstarID = insertStarsCS.getString(4);
//                if (rsuccess == 1){
//                    System.out.println("Successfully added" + " star ID: " + rstarID);
//                }
//                else {
//                    System.out.println("Failed to " + "add New Star");
//                }
//            }
//        } catch (Exception e) {
//
//        }
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
            actorListSize++;
        }
    }
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase(NAME)) {
            latestActor().setStarName(element.toString().trim());
        } else if (qName.equalsIgnoreCase(BIRTHYEAR)) {
            String year = element.toString().trim();
            Actor latestActorObject = latestActor();
            String nameAndYear = latestActorObject.getStarName() + year;
            if(actorsMap.get(nameAndYear) == null){
                latestActorObject.setBirthYear(element.toString().trim());
                actorsMap.put(nameAndYear, latestActorObject.getStarName());
            }
            else{
                actorList.remove(actorListSize-1);
                actorListSize--;
            }
        }
    }
    private Actor latestActor() {
        int latestActorIndex = actorListSize - 1;
        return actorList.get(latestActorIndex);
    }
    public List<Actor> getActorList(){
        return actorList;
    }
    public static void main(String[] args) {
        SAXParserServletActors spe = new SAXParserServletActors();
        spe.runExample();

        System.out.println("");
    }

}