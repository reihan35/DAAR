
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Exception;
import java.io.*;
import java.util.Set;

public class Search {
    /**
     * Class qui contient
     *      la méthode qui permet de chercher une expréssion régulière avec un automate
     *      la méthode kmp (le calcule de la retenu & celle qui match le mots dans le text)
     */
    public final int DOT = 0xD07;
    public final int POINTINTERROGATION = 0xF07;

    // etape 5
    public ArrayList<ArrayList<Integer>> searchWithDFA(DFA dfa, String txt, String regEx, int idLine, int idCol) {
        int N = txt.length();
        ArrayList<String> resutls = new ArrayList<>();
        ArrayList<ArrayList<Integer>> resutlsF = new ArrayList<ArrayList<Integer>>();
        boolean POINTINTERROGATIONB = true;
        ArrayList<Integer> currentState = null;
        ArrayList<Integer> precState = null;
        ArrayList<Integer> nextState = null;
        String currentChar = "";
        String maxMatching = "";
        HashMap<String, ArrayList<ArrayList<Integer>>> matchingWordId = new HashMap<String, ArrayList<ArrayList<Integer>>>();

        boolean change = false;
        int idxCharFirstRep = -1;

        int BEG = 0;
        if (regEx.substring(0, 1).contains("^"))
            BEG = 1;

        int END = 0;
        if (regEx.substring(regEx.length() - 1, regEx.length()).contains("$"))
            END = 1;


        for (ArrayList<Integer> state : dfa.q0) {
            currentChar = "";

            for (int i = 0; i < N; i++) {
                if (dfa.Transitions.containsKey(state)) {

                    if (dfa.Transitions.get(state).keySet().contains(DOT)) {
                        currentState = dfa.Transitions.get(state).get(DOT);
                    } else {
                        currentState = dfa.Transitions.get(state).get((int) txt.charAt(i));
                    }

                    if (currentState == null) {
                        Set<Integer> IdTransition = dfa.Transitions.get(state).keySet();
                        ArrayList<Integer> IdTransitionList = new ArrayList<Integer>(IdTransition);
                        currentState = dfa.Transitions.get(state).get(IdTransitionList.get(0));
                        if (dfa.Transitions.get(currentState).keySet().contains(POINTINTERROGATION)) {
                            currentState = dfa.Transitions.get(currentState).get(POINTINTERROGATION);
                            POINTINTERROGATIONB = false;
                            currentState = dfa.Transitions.get(currentState).get((int) txt.charAt(i));
                        } else {
                            currentState = null;
                        }
                    }
                    if (currentState != null && i<N-1) {
                        if(i>N-1)
                            break;
                        nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));

                        if (dfa.Transitions.get(currentState).keySet().contains(DOT) && nextState == null) {
                            nextState = dfa.Transitions.get(currentState).get(DOT);
                        } else if (i < N - 1) {
                            if (dfa.Transitions.get(currentState).keySet().contains(POINTINTERROGATION)) {
                                currentState = dfa.Transitions.get(currentState).get(POINTINTERROGATION);
                            }
                            nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));
                        }

                        currentChar = currentChar + txt.charAt(i);

                        if (dfa.f.contains(currentState) && nextState == null) {

                            currentChar = currentChar + txt.charAt(i);

                            ArrayList<Integer> elem = new ArrayList<>();
                            elem.add(idLine);
                            elem.add(i + 1);
                            elem.add(i + currentChar.length() + 1 - BEG - END); // fin
                            resutlsF.add(elem);
                            resutls.add(currentChar);

                            matchingWordId.computeIfAbsent(currentChar, k -> new ArrayList<>()).add((elem));

                            change = false;
                            if (maxMatching.length() < currentChar.length())
                                maxMatching = currentChar;
                            currentChar = "";
                        } else if (nextState != null && dfa.Transitions.containsKey(currentState) && i < N - 1) {
                            if (!dfa.f.contains(currentState)) {

                                currentChar = currentChar + txt.charAt(i + 1);
                                change = false;
                                nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));

                                if (dfa.Transitions.get(currentState).keySet().contains(DOT) && nextState == null) {
                                    nextState = dfa.Transitions.get(currentState).get(DOT);
                                } else {
                                    nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));
                                }

                                currentState = nextState;
                                i += 1;
                                int oldI = i;


                                while (nextState != null && i < N - 1) {
                                    change = true;

                                    if (dfa.Transitions.get(currentState) != null) {
                                        nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));

                                        if (dfa.Transitions.get(currentState).keySet().contains(DOT) && nextState == null) {
                                            nextState = dfa.Transitions.get(currentState).get(DOT);
                                        } else {
                                            nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));
                                        }
                                        if (nextState == null) {

                                            Set<Integer> IdTransition = dfa.Transitions.get(currentState).keySet();
                                            ArrayList<Integer> IdTransitionList = new ArrayList<Integer>(IdTransition);
                                            ArrayList<Integer> nextStatetmp = dfa.Transitions.get(currentState).get(IdTransitionList.get(0));
                                            if(dfa.Transitions.get(nextStatetmp)!= null) {
                                                if (dfa.Transitions.get(nextStatetmp).keySet().contains(POINTINTERROGATION)) {
                                                    nextState = nextStatetmp;
                                                    currentState = nextState;
                                                    nextState = dfa.Transitions.get(nextState).get(POINTINTERROGATION);
                                                    currentState = nextState;

                                                    nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));
                                                    POINTINTERROGATIONB = false;
                                                } else {
                                                    nextState = null;
                                                }
                                            }
                                        } else {

                                            if (!dfa.f.contains(nextState)) {
                                                ArrayList<Integer> oldnextState = nextState;
                                                ArrayList<Integer> oldcurrentState = currentState;
                                                boolean changeState = false;
                                                if (dfa.Transitions.get(nextState).keySet().contains(POINTINTERROGATION)) {
                                                    currentState = nextState;
                                                    nextState = dfa.Transitions.get(currentState).get(POINTINTERROGATION);
                                                    POINTINTERROGATIONB = false;
                                                    changeState = true;
                                                } else {

                                                    Set<Integer> IdTransition = dfa.Transitions.get(nextState).keySet();

                                                    ArrayList<Integer> IdTransitionList = new ArrayList<Integer>(IdTransition);

                                                    ArrayList<Integer> nextStatetmp = dfa.Transitions.get(nextState).get(IdTransitionList.get(0));

                                                    changeState = true;
                                                    if (dfa.Transitions.get(nextStatetmp) != null) {
                                                        if (dfa.Transitions.get(nextStatetmp).keySet().contains(POINTINTERROGATION)) {
                                                            nextState = nextStatetmp;
                                                            currentState = nextState;

                                                            currentState = nextState;
                                                            nextState = dfa.Transitions.get(currentState).get(POINTINTERROGATION);
                                                            POINTINTERROGATIONB = false;
                                                        }
                                                    }
                                                }
                                                if (changeState && POINTINTERROGATIONB) {
                                                    currentState = oldcurrentState;
                                                    nextState = oldnextState;
                                                }
                                            }
                                        }
                                    } else {
                                        nextState = null;
                                    }

                                    if (nextState != null) {
                                        currentChar = currentChar + txt.charAt(i + 1);
                                        precState = currentState;
                                        currentState = nextState;
                                    } else {
                                        break;
                                    }

                                    i += 1;
                                }

                                if (nextState != null && i == N - 1) {
                                    currentState = nextState;
                                    nextState = null;
                                }
                                i = oldI;

                                if (nextState == null && change) {
                                    i -= 1;
                                    if (dfa.f.contains(currentState)) {
                                        ArrayList<Integer> elem = new ArrayList<>();
                                        elem.add(idLine);
                                        elem.add(i + 1);
                                        elem.add(i + currentChar.length() + 1 - BEG - END);

                                        resutlsF.add(elem);
                                        resutls.add(currentChar);
                                        matchingWordId.computeIfAbsent(currentChar, k -> new ArrayList<>()).add((elem));

                                        currentChar = "";
                                        change = false;
                                        if (maxMatching.length() < currentChar.length())
                                            maxMatching = currentChar;
                                    } else {
                                    }
                                    currentChar = "";
                                }
                            } else {
                                ArrayList<Integer> elem = new ArrayList<>();
                                elem.add(idLine);
                                elem.add(i + 1);
                                elem.add(i + currentChar.length() + 1 - BEG - END);
                                resutlsF.add(elem);
                                resutls.add(currentChar);
                                matchingWordId.computeIfAbsent(currentChar, k -> new ArrayList<>()).add((elem));

                                change = false;
                                if (maxMatching.length() < currentChar.length())
                                    maxMatching = currentChar;
                                currentChar = "";
                            }
                        } else {
                            currentChar = "";
                        }
                    }
                    currentChar = "";
                }
            }
        }
        return resutlsF;
    }

    public int[] retenue(String str) {
        int[] retenue = new int[str.length()];
        char[] facteur = str.toCharArray();

        retenue[0] = 0;
        retenue[1] = 0;

        for (int i = 2; i < facteur.length; i++) {
            if (facteur[retenue[i - 1]] == facteur[i - 1])
                retenue[i] = retenue[i - 1] + 1;
            else
                retenue[i] = 0;
        }

        for (int i = 0; i < retenue.length; i++)
            if (facteur[i] == facteur[0] && retenue[i] == 0)
                retenue[i] = -1;

        return retenue;

    }

    public ArrayList<ArrayList<Integer>> matchingWords(char[] facteur, int[] retenue, char[] line, int idline) {
        int i = 0;
        int j = 0;
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        while (i < line.length) {

            if (j == facteur.length) {
                ArrayList<Integer> elem = new ArrayList<Integer>();
                elem.add(idline + 1);
                elem.add(i - facteur.length + 1);
                result.add(elem);
                j = 0;
            }

            if (line[i] == facteur[j]) {
                i++;
                j++;
            } else {
                if (retenue[j] == -1) {
                    i++;
                    j = 0;
                } else {
                    j = retenue[j];
                }
            }

        }

        if (j == facteur.length) {
            ArrayList<Integer> elem = new ArrayList<Integer>();
            elem.add(idline + 1);
            elem.add(i - facteur.length + 1);
            result.add(elem);
        }
        return result;
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