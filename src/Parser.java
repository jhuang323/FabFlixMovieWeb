import Actors.InsertActors;
import Actors.SAXParserServletActors;
import Casts.SAXParserServletCasts;
import Mains.SAXParserServletMain;
public class Parser {
    public static void main(String[] args) throws Exception{
//        final long startTime = System.currentTimeMillis();
//        SAXParserServletActors spe = new SAXParserServletActors();
//        spe.runExample();
//        InsertActors ia = new InsertActors();
//        ia.run(spe);
//        final long endTime = System.currentTimeMillis();
//        System.out.println("Total execution time: " + (endTime - startTime));

        final long startTimeSecond = System.currentTimeMillis();
        SAXParserServletCasts spc = new SAXParserServletCasts();
        spc.runExample();
        SAXParserServletMain spm = new SAXParserServletMain();
        spm.runExample();
        InsertMainsAndCasts imc = new InsertMainsAndCasts();
        imc.insert(spm, spc);
        final long endTimeSecond = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTimeSecond - startTimeSecond));
    }

}