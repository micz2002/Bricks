import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Bricks {
    public static void main(String[] args) {
        File file = new File("plik.txt");
        if(file.exists()) {
            System.out.println("Wczytywanie danych z pliku");
            Scanner dataFromFile = null;
            try {
                dataFromFile = new Scanner(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

             while(dataFromFile.hasNextLine()){
                String line = dataFromFile.nextLine();

                String[] parts = line.split(":");
                int number = Integer.parseInt(parts[0]);
                String code = parts[1];
                System.out.println("tu numer: " + number + ", a tu kod: " + code);
            }
        } else{
            //Wprowadz dane recznie, reszta programu taka sama
        }

    }
}


