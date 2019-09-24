import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Exception;
import java.util.Arrays;
import java.util.Random;
import java.util.HashSet;
import java.util.*;
import java.io.*;

public class Search{

    // etape 5
    public ArrayList<String> searchWithDFA(DFA dfa, String txt) {
        int N = txt.length();
        ArrayList<String> resutls = new ArrayList<String>();
        ArrayList<Integer> currentState = null;
        ArrayList<Integer> nextState = null;
        String currentChar = "";
        String maxMatching = "";

        boolean change = false;

        for (ArrayList<Integer> state : dfa.q0) {
            for (int i = 0; i < N; i++) {

                if (dfa.Transitions.containsKey(state)) {
                    currentState = dfa.Transitions.get(state).get((int) txt.charAt(i));
                    if (currentState != null) {

                        if (dfa.f.contains(currentState)) {
                            currentChar = currentChar + txt.charAt(i);
                            // System.out.println(" >>>> currentChar : " + currentChar);

                            resutls.add(currentChar);
                            if (maxMatching.length() < currentChar.length())
                                maxMatching = currentChar;
                            currentChar = "";
                        }

                        if (dfa.Transitions.containsKey(currentState) && i < N - 1) {
                            // System.out.println(" 1 *** currentChar : " + currentChar);

                            if (!dfa.f.contains(currentState)) {
                                // System.out.println(" 2 *** currentChar : " + currentChar);
                                currentChar = currentChar + txt.charAt(i);
                                nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));
                                // System.out.println(" 3 *** currentChar : " + currentChar);
                                if (nextState != null)
                                    currentChar = currentChar + txt.charAt(i + 1);
                                else
                                    currentChar = "";

                                // System.out.println("    0 next txt: " + txt.charAt(i + 1));

                                change = false;

                                while (nextState != null && i < N - 1) {
                                    change = true;
                                    i += 1;
                                    // System.out.println("    1 current txt: " + txt.charAt(i));
                                    // System.out.println("    0 next txt: " + txt.charAt(i + 1));
                                    nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i));
                                    if (nextState != null) {
                                        currentChar = currentChar + txt.charAt(i + 1);
                                        // System.out.println("    1 next txt: " + txt.charAt(i + 1));
                                    }
                                    // System.out.println("    1 currentChar: " + currentChar);
                                }
                                if (nextState == null && change){
                                    i -= 1;
                                    currentState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));
                                    System.out.println(" >>>> nextState : " + nextState);
                                    if(dfa.f.contains(nextState)){
                                        resutls.add(currentChar);
                                        if (maxMatching.length() < currentChar.length())
                                            maxMatching = currentChar;
                                    }
                                    currentChar = "";
                                }
                            }else{
                                // System.out.println(" 2 >>>> currentChar : " + currentChar);
                                resutls.add(currentChar);
                                if (maxMatching.length() < currentChar.length())
                                    maxMatching = currentChar;
                                currentChar = "";
                            }
                        }
                    }
                }
            }
        }
        if (resutls.size() != 0) {
            System.out.println("results: " + resutls);
            System.out.println("maxMatching in this line (texte): " + maxMatching);
        }

        return resutls;
    }

    public int [] retenue(String str){
        int [] retenue = new int [str.length()];
        char[]facteur = str.toCharArray();

        int currentIdx = 0;
        int value = 0;
        int j = 1;

        for (int i=0; i<facteur.length; i++) {
            value = 0;
            if (facteur[i] == facteur[0]) {
                retenue[currentIdx] = -1;
                currentIdx += 1;
            }else if(currentIdx == 1) {
                currentIdx ++;
                retenue[currentIdx] = 0;
            }else{
                j = 1;
                while (j <= currentIdx-j+1) {
                    if(str.substring(0,j).equals(str.substring(currentIdx-j,currentIdx))) {
                        value = j;
                    }
                    j++;
                }

                retenue[currentIdx] = value;
                currentIdx += 1;
            }
        }
        return retenue;
    }

    public int matchingWords(char[] facteur, int[] retenue, char[] texte) {
        int i = 0;
        int j = 0;

        while(i < texte.length) {
            if (j==facteur.length)
                return (i-facteur.length);


            if(texte[i] == facteur[j]) {
                i++;
                j++;
            }

            else {
                if(retenue[j] == -1) {
                    i++;
                    j=0;
                }
                else {
                    j = retenue[j];
                }
            }

        }

        if(j==facteur.length)
            return i-facteur.length;

        else
            return -1;

    }

    public static void readFileLine(String file, DFA d) throws FileNotFoundException {
        ArrayList<String> resutls = new ArrayList<String>();
        try {
            Scanner scanner = new Scanner(new File(file));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // d.search(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFileChar(String file) throws FileNotFoundException {
        String result = "";
        try {
            FileReader fr = new FileReader(file);
            int i;
            while ((i = fr.read()) != -1)
                result += (char) i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

