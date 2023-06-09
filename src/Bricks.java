import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

public class Bricks {
    public static void main(String[] args) {
        List<String> freeBlocks = new ArrayList<>();
        Map<Integer, List<String>> instructions = new HashMap<>();
        List<String> blocksUsed1 = new ArrayList<>();
        List<String> blocksUsed2 = new ArrayList<>();

        int blocksRemaining = 0 ;
        int blocksMissing = 0;
        int successfulBuilds = 0;
        int unsuccessfulBuilds = 0;

        File file = new File("plik.txt");

        if(file.exists()) {
            if (file.canRead()) {
                System.out.println("Wczytywanie danych z pliku");
                Scanner dataFromFile = null;
                try {
                    dataFromFile = new Scanner(file);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                while (dataFromFile.hasNextLine()) {
                    String line = dataFromFile.nextLine();

                    String[] parts = line.split(":");
                    int number = Integer.parseInt(parts[0]);
                    String code = parts[1];
                    //////Zliczanie i dodawanie klockow
                    if (number == 0) {
                        freeBlocks.add(code);
                    } else {
                        if (instructions.containsKey(number)) {
                            List<String> listOfCode = instructions.get(number);
                            listOfCode.add(code);
                        } else {
                            List<String> listOfCode = new ArrayList<>();
                            listOfCode.add(code);
                            instructions.put(number, listOfCode);
                        }
                    }
                    System.out.println(number + ":" + code);
                }

                System.out.println("///////////////////////////////////////////////");
                System.out.println("WOLNE KLOCKI: ");
                for (String freeBlock : freeBlocks) {
                    System.out.println(freeBlock);
                }
                ////////ZARZADZANIE INSTRUKCJAMI
                for (Map.Entry<Integer, List<String>> entry : instructions.entrySet()) {
                    Integer number = entry.getKey();
                    List<String> codes = entry.getValue();
                    System.out.println("Klucz: " + number);
                 //System.out.println("Wartosci: " + codes);

                    String[] codesArray = codes.stream().toArray(String[]::new);

                    boolean nextCycleOfLoop = false;

                    for (String code : codes) {
                        String[] freeBlocksArray = freeBlocks.stream().toArray(String[]::new);
                        if ((number) % 3 == 0) {
                            if (freeBlocks.containsAll(codes)) {
                                freeBlocks.remove(code);
                                blocksUsed1.add(code);
                                if(nextCycleOfLoop == false) {
                                    successfulBuilds++; }
                            } else if(nextCycleOfLoop == false){
                                unsuccessfulBuilds++;
                            }
                            //tu trzeba dodac dla pozostalych isntrukcji
                        }
                        nextCycleOfLoop = true;
                    }

                }
            }// else{
//            //Wprowadz dane recznie, reszta programu taka sama
//        }
        }
        System.out.println("///////////////////////////////////////////////");
        System.out.println("WOLNE KLOCKI: ");
        for (String freeBlock : freeBlocks) {
            System.out.println(freeBlock);
        }
        System.out.println(blocksUsed1.size());
        System.out.println(blocksUsed2.size());
        System.out.println(blocksRemaining);
        System.out.println(blocksMissing);
        System.out.println(successfulBuilds);
        System.out.println(unsuccessfulBuilds);
    }

    static public boolean isContaining(String[] firstArray, String[] secondArray){
        //firstArray ma byc codesArray a secondArray to freeBlocksArray
        int counter = 0;
        for(int i = 0; i < firstArray.length; i ++){

            for(int j = 0; j < secondArray.length; i ++){

                if(firstArray[i] == secondArray[j]){
                 counter++;
                 break;
                }
            }
            if(counter == firstArray.length)
                return true;
        }
        return false;
    }
}


