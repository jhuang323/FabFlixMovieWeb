import Actors.SAXParserServletActors;
import Casts.SAXParserServletCasts;
import Mains.SAXParserServletMain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Parser {
    public static void main(String[] args) throws Exception{
        final long startTimeSecond = System.currentTimeMillis();
        SAXParserServletCasts spc = new SAXParserServletCasts();
        spc.parseDocument();
        SAXParserServletMain spm = new SAXParserServletMain();
        spm.parseDocument();
        SAXParserServletActors spa = new SAXParserServletActors();
        spa.parseDocument();
        List<String> mainError = spm.getErrorMessages();
        List<String> actorError = spa.getErrorMessages();
        System.out.println(mainError.size());
        System.out.println(actorError.size());
        try {
            FileWriter myWriter = new FileWriter("BadFormat.txt");
            BufferedWriter info = new BufferedWriter(myWriter);
            for(int index=0 ; index < mainError.size() ; index++){
                info.write(mainError.get(index) + "\n");
            }
            for(int index=0; index <actorError.size();index++){
                info.write(actorError.get(index) + "\n");
            }
            info.close();
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("A file writing error occurred.");
            e.printStackTrace();
        }
        InsertMainsAndCasts imc = new InsertMainsAndCasts();
        imc.insert(spm, spc, spa);
        final long endTimeSecond = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTimeSecond - startTimeSecond));
    }

}