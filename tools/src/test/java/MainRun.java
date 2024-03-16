import java.io.*;

public class MainRun {

    public static void main(String[] args) {

        System.out.println("Hello there");

        File a = new File("my.txt");
        try {
           printStrings();
        } catch (IOException e) {
            e.printStackTrace();
        }
;    }


    public static void printStrings() throws IOException {
        new Example().runThis();
    }
}
