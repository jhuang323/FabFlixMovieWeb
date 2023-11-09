import Actors.SAXParserServletActors;
import Casts.SAXParserServletCasts;
import Mains.SAXParserServletMain;
import test.SAXParserServletCastsJustin;

public class Parser {
    public static void main(String[] args) throws Exception{
        final long startTimeSecond = System.currentTimeMillis();
        SAXParserServletCastsJustin spc = new SAXParserServletCastsJustin();
        spc.runExample();
        SAXParserServletMain spm = new SAXParserServletMain();
        spm.parseDocument();
        SAXParserServletActors spa = new SAXParserServletActors();
        spa.runExample();
        InsertMainsAndCasts imc = new InsertMainsAndCasts();
        imc.insert(spm, spc, spa);
        final long endTimeSecond = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTimeSecond - startTimeSecond));
    }

}