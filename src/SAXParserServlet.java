
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

public class SAXParserServlet extends DefaultHandler {

    List<String> myEmpls;

    private String tempVal;

    //to maintain context
    private String tempEmp;

    public SAXParserServlet() {
        myEmpls = new ArrayList<String>();
    }

    public void runExample() {
        parseDocument();
        //printData();
    }

    private void parseDocument() {

        //Will parse files: main243.xml, casts124.xml, and actors63.xml
        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("employees.xml", this);

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
//    private void printData() {
//
//        System.out.println("No of Employees '" + myEmpls.size() + "'.");
//
//        Iterator<Employee> it = myEmpls.iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next().toString());
//        }
//    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("Employee")) {
            //Use pogo here maybe -J
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("Employee")) {
            //add it to the list
            myEmpls.add(tempEmp);

        } else if (qName.equalsIgnoreCase("Name")) {
            //Use pogo here maybe -J
        } else if (qName.equalsIgnoreCase("Id")) {
            //Use pogo here maybe -J
        } else if (qName.equalsIgnoreCase("Age")) {
            //Use pogo here maybe -J
        }

    }

    public static void main(String[] args) {
        SAXParserServlet spe = new SAXParserServlet();
        spe.runExample();
    }

}