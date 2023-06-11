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

        int blocksRemaining = 0;
        int successfulBuilds = 0;
        int unsuccessfulBuilds = 0;

        String allowedLetters = "ABCDEFGHIJKLMNO";
        String allowedLettersInInstruction = "ABCDEFGHIJKLMN";
        char[] allowedLettersCArray = allowedLetters.toCharArray();
        char[] allowedLettersInInstructionCArray = allowedLettersInInstruction.toCharArray();

        File file = new File("plik.txt");
        Scanner scanner = null;

        if (file.exists()) {
            if (file.canRead()) {
                try {
                    scanner = new Scanner(file);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();

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

                    boolean lineOK = isLineOK(codeCArray, allowedLettersInInstructionCArray, allowedLettersCArray, number);
                    if (!lineOK)
                        return;

                    addingElements(instructions, freeBlocks, number, code);
                }

            }
        } else {
            scanner = new Scanner(System.in);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.isBlank())
                    break;

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

                boolean lineOK = isLineOK(codeCArray, allowedLettersInInstructionCArray, allowedLettersCArray, number);
                if (!lineOK)
                    return;

                addingElements(instructions, freeBlocks, number, code);

            }
        }
        scanner.close();

        for (Map.Entry<Integer, List<String>> entry : instructions.entrySet()) {
            Integer number = entry.getKey();
            List<String> codes = entry.getValue();

            boolean nextCycleOfLoop = false;
            boolean containing = false;

            for (String code : codes) {

                if ((number) % 3 == 0) {
                    if (isContaining(codes, freeBlocks, nextCycleOfLoop, containing)) {
                        containing = true;
                        freeBlocks.remove(code);
                        blocksUsed1.add(code);
                        if (nextCycleOfLoop == false) {
                            successfulBuilds++;
                        }
                    } else if (nextCycleOfLoop == false) {
                        unsuccessfulBuilds++;
                    }
                }
                nextCycleOfLoop = true;
            }
        }
        //the same loop for number of instructions which are not divisible  by 3 without remainder
        for (Map.Entry<Integer, List<String>> entry : instructions.entrySet()) {
            Integer number = entry.getKey();
            List<String> codes = entry.getValue();

            boolean nextCycleOfLoop = false;
            boolean containing = false;

            for (String code : codes) {

                if ((number) % 3 != 0) {
                    if (isContaining(codes, freeBlocks, nextCycleOfLoop, containing)) {
                        containing = true;
                        freeBlocks.remove(code);
                        blocksUsed2.add(code);
                        if (nextCycleOfLoop == false) {
                            successfulBuilds++;
                        }
                    } else if (nextCycleOfLoop == false) {
                        unsuccessfulBuilds++;
                    }
                }
                nextCycleOfLoop = true;
            }
        }

        blocksRemaining = freeBlocks.size();

        System.out.println(blocksUsed1.size());
        System.out.println(blocksUsed2.size());
        System.out.println(blocksRemaining);
        System.out.println(blocksMissing);
        System.out.println(successfulBuilds);
        System.out.println(unsuccessfulBuilds);
    }

    static public boolean isContaining(List<String> firstList, List<String> secondList, boolean nextCycleOfLoop, boolean containing) {

        int counter = 0;
        List<String> copyOfSecondList = new ArrayList<>(secondList);

        boolean notContaining = true;

        if (!containing) {
            for (int i = 0; i < firstList.size(); i++) {

                for (int j = 0; j < copyOfSecondList.size(); j++) {

                    if (firstList.get(i).equals(copyOfSecondList.get(j))) {
                        copyOfSecondList.remove(j);
                        counter++;
                        notContaining = false;
                        break;
                    }
                    notContaining = true;
                }
                if (notContaining && nextCycleOfLoop == false) {
                    blocksMissing++;
                }
                if (counter == firstList.size())
                    return true;

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

    static public boolean isLineOK(char[] codeCArray, char[] allowedLettersInInstructionCArray, char[] allowedLettersCArray, int number) {
        if (codeCArray.length != 4) {
            System.out.println("klops");
            return false;
        } else if (number != 0 && hasInstrUnpermittedLetter(codeCArray, allowedLettersInInstructionCArray)) {
            System.out.println("klops");
            return false;
        } else if (hasInstrUnpermittedLetter(codeCArray, allowedLettersCArray)) {
            System.out.println("klops");
            return false;
        }
        return true;
    }

    static public void addingElements(Map<Integer, List<String>> instructions, List<String> freeBlocks, int number, String code) {
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
}


