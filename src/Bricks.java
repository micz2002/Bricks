import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Bricks {
    public static void main(String[] args) {
        List<String> freeBlocks = new ArrayList<>();
        Map<Integer, List<String>> instructions = new HashMap<>();

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
                //////Zliczanie i zarzadzanie klockami
                if(number == 0) {
                    freeBlocks.add(code);
                } else {
                    if(instructions.containsKey(number)) {
                        List<String> listOfCode  = instructions.get(number);
                        listOfCode.add(code);
                    } else {
                        List<String> listOfCode = new ArrayList<>();
                        listOfCode.add(code);
                        instructions.put(number, listOfCode);
                    }
                }
                System.out.println("tu numer: " + number + ", a tu kod: " + code);
            }
            System.out.println("????????????????????????????????????????????????????");
            System.out.println("WOLNE KLOCKI: ");
            for (String freeBlock : freeBlocks) {
                System.out.println(freeBlock);
            }

            for(Map.Entry<Integer, List<String>> entry : instructions.entrySet()) {
                Integer keyInstructions = entry.getKey();
                List<String> valueInstructions = entry.getValue();
                System.out.println("Klucz: " + keyInstructions);
                System.out.println("Wartosci: " + valueInstructions);
            }
          }// else{
//            //Wprowadz dane recznie, reszta programu taka sama
//        }

    }
}


