import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

public class Bricks {

    static int blocksMissing = 0;

    public static void main(String[] args) {
        List<String> freeBlocks = new ArrayList<>();
        Map<Integer, List<String>> instructions = new HashMap<>();
        List<String> blocksUsed1 = new ArrayList<>();
        List<String> blocksUsed2 = new ArrayList<>();

        int freeBlocksSizeAtStart = freeBlocks.size();
        int blocksRemaining = 0;
        int successfulBuilds = 0;
        int unsuccessfulBuilds = 0;


        double start = System.currentTimeMillis();

        File file = new File("plik.txt");

        if (file.exists()) {
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

                    int number = 0;
                    String code = null;
                    char[] codeCArray = new char[0];
                    try {
                        String[] parts = line.split(":");
                        number = Integer.parseInt(parts[0]);
                        code = parts[1];
                        codeCArray = code.toCharArray();
                    } catch (NumberFormatException e) {
                        System.out.println("klops");
                        return;
                    }

                    String allowedLetters = "ABCDEFGHIJKLMNO";
                    String allowedLettersInInstruction = "ABCDEFGHIJKLMN";
                    char[] allowedLettersCArray = allowedLetters.toCharArray();
                    char[] allowedLettersInInstructionCArray = allowedLettersInInstruction.toCharArray();

                    if(codeCArray.length != 4){
                        System.out.println("klops");
                        return;
                    } else if (number != 0 && hasInstrUnpermittedLetter(codeCArray, allowedLettersInInstructionCArray)) {
                        System.out.println("klops");
                        return;
                    } else if (hasInstrUnpermittedLetter(codeCArray, allowedLettersCArray)) {
                        System.out.println("klops");
                        return;
                    }

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
                }
                dataFromFile.close();

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

                    boolean nextCycleOfLoop = false;
                    boolean containing = false;

                    for (String code : codes) {

                        if ((number) % 3 == 0) {
                            if (isContaining(codes, freeBlocks, nextCycleOfLoop, containing)) {
                                containing = true;
                                System.out.println("zawiera sie");
                                freeBlocks.remove(code);
                                blocksUsed1.add(code);
                                if (nextCycleOfLoop == false) {
                                    successfulBuilds++;
                                }
                            } else if (nextCycleOfLoop == false) {
                                System.out.println("nie zawiera sie");
                                unsuccessfulBuilds++;
                            }
                            //tu trzeba dodac dla pozostalych isntrukcji
                        }
                        nextCycleOfLoop = true;
                    }

                }
                //////to samo ale dla instrukcji pozostalych nie dzielacych sie bez reszty prez 3
                for (Map.Entry<Integer, List<String>> entry : instructions.entrySet()) {
                    Integer number = entry.getKey();
                    List<String> codes = entry.getValue();

                    //do wywalenia
                    System.out.println("Klucz: " + number);

                    boolean nextCycleOfLoop = false;
                    boolean containing = false;

                    for (String code : codes) {

                        if ((number) % 3 != 0) {
                            if (isContaining(codes, freeBlocks, nextCycleOfLoop, containing)) {
                                containing = true;
                                System.out.println("zawiera sie");
                                freeBlocks.remove(code);
                                blocksUsed2.add(code);
                                if (nextCycleOfLoop == false) {
                                    successfulBuilds++;
                                }
                            } else if (nextCycleOfLoop == false) {
                                System.out.println("nie zawiera sie");
                                unsuccessfulBuilds++;
                            }
                        }
                        nextCycleOfLoop = true;
                    }

                }
            }// else{
//            //Wprowadz dane recznie, reszta programu taka sama
//        }
        }

        System.out.println("///////////////////////////////////////////////");

        blocksRemaining = freeBlocks.size();

        System.out.println(blocksUsed1.size());
        System.out.println(blocksUsed2.size());
        System.out.println(blocksRemaining);
        System.out.println(blocksMissing);
        System.out.println(successfulBuilds);
        System.out.println(unsuccessfulBuilds);

        System.out.println("CZAS WYKONANIA PROGRAMU: " + ((System.currentTimeMillis() - start) / 1000));
    }

    static public boolean isContaining(List<String> firstList, List<String> secondList, boolean nextCycleOfLoop, boolean containing) {
        //firstArray powinna byc codesArray a secondArray to freeBlocksArray

        int counter = 0; //counter is then are the same values
        int counterPreviousValue = counter;
        List<String> copyOfSecondList = new ArrayList<>(secondList);

        if (!containing) {
            for (int i = 0; i < firstList.size(); i++) {

                for (int j = 0; j < copyOfSecondList.size(); j++) {

                    if (firstList.get(i).equals(copyOfSecondList.get(j))) {
                        copyOfSecondList.remove(j);
                        counter++;
                        break;
                    }
                }
                if ((counter == counterPreviousValue) && nextCycleOfLoop == false) {
                    blocksMissing++;
                    System.out.println("BRAKUJACE KLOCKI: " + blocksMissing);
                }
                if (counter == firstList.size())
                    return true;

                counterPreviousValue++;
            }
            return false;
        }
        return true;
    }

    static public boolean hasInstrUnpermittedLetter(char[] codeCArray, char[] allowedLCArray) {

        boolean isUnpermittedLetter = false;

        for (int i = 0; i < codeCArray.length; i++) {

            for (int j = 0; j < allowedLCArray.length; j++) {

                if (codeCArray[i] == allowedLCArray[j]) {
                    isUnpermittedLetter = false;
                    break;
                }
                isUnpermittedLetter = true;
            }
            if (isUnpermittedLetter)
                return true;
        }
        return false;
    }
}


