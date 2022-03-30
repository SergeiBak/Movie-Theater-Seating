import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        Theater theater = new Theater();
        // String inputFile = args[0];
        // String outputFile = args[1];
        String inputFile = "D:/GitHub/Interview/Movie-Theater-Seating/fullInput.txt";

        Main mainClass = new Main();
        mainClass.ProcessInputFile(inputFile, theater);
        theater.PrintReservations();
        theater.PrintTheaterLayout();
    }

    public void ProcessInputFile(String filename, Theater theater) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while (line != null) {
                theater.ProcessReservation(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}