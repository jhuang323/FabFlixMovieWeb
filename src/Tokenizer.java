import java.util.StringTokenizer;

public class Tokenizer {
    public static void main(String[] args) {
        System.out.println("This is a tokenizer test");
        String testtoken = "This is a test token";

        StringTokenizer st1 = new StringTokenizer(testtoken," ");

        while(st1.hasMoreTokens()){
            System.out.println("TOken: " + st1.nextToken());
        }


    }
}
