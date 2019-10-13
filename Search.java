import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Exception;
import java.io.*;
import java.util.Set;

public class Search {
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
        HashMap<Integer, ArrayList<ArrayList<Integer>>> matchingWordId = new HashMap<Integer, ArrayList<ArrayList<Integer>>>();

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
                    if (currentState != null) {
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
                            resutls.add(currentChar);

                            elem = new ArrayList<>();
                            elem.add(i + 1);
                            elem.add(i + currentChar.length() + 1 - BEG - END); // fin
                            resutlsF.add(elem);

                            matchingWordId.computeIfAbsent(idLine, k -> new ArrayList<>()).add((elem));

                            change = false;
                            if (maxMatching.length() < currentChar.length())
                                maxMatching = currentChar;
                            currentChar = "";
                        } else if (nextState != null && dfa.Transitions.containsKey(currentState) && i < N - 1) {
                            // System.out.println(" ---------------------------------------------------" + txt.charAt(i));
                            if (!dfa.f.contains(currentState)) {

                                currentChar = currentChar + txt.charAt(i + 1);
                                // System.out.println(" 0 currentChar : " + currentChar);
                                change = false;
                                // currentState = nextState;
                                nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));

                                if (dfa.Transitions.get(currentState).keySet().contains(DOT) && nextState == null) {
                                    // System.out.println(" 2 >>>>>>>>>>> !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! DOT" );
                                    nextState = dfa.Transitions.get(currentState).get(DOT);
                                } else {
                                    nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));
                                }

                                // System.out.println(" 0 nextState : " + nextState);
                                currentState = nextState;
                                i += 1;
                                int oldI = i;


                                while (nextState != null && i < N - 1) {
                                    // System.out.println("........WHILE..............");
                                    // System.out.println("text : " + txt);
                                    // System.out.println(" * txt: " + txt.charAt(i));
                                    // System.out.println(" * je suis la " );

                                    change = true;
                                    // System.out.println(" * current txt: " + txt.charAt(i));
                                    // System.out.println(" * next txt: " + txt.charAt(i + 1));

                                    // System.out.println(" AVANT currentState : " + currentState);
                                    // System.out.println(" 0 je suis la!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" +
                                    //  dfa.Transitions.get(currentState));




                                    // System.out.println(" * je suis la 1 " );

                                    // System.out.println(" dfa.Transitions.get(currentState)  : " + dfa.Transitions.get(currentState));

                                    if (dfa.Transitions.get(currentState) != null) {
                                        nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));
                                        // System.out.println(" 1 je suis la!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! nextState : " + nextState);

                                        if (dfa.Transitions.get(currentState).keySet().contains(DOT) && nextState == null) {
                                            // System.out.println(" c'est un . 1 je suis la!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                            nextState = dfa.Transitions.get(currentState).get(DOT);
                                        } else {
                                            nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));
                                        }
                                        // System.out.println("        $$ nextState: " + nextState);
                                        if (nextState == null) {
                                            // System.out.println("0  WHILE JE suis la POINTINTERROGATION >>>>>>>>>>> nextState : " + nextState);
                                            // System.out.println("			1 curr txt: " + txt.charAt(i));
                                            // System.out.println("     curr keySet() : " + dfa.Transitions.get(currentState).keySet());
                                            // System.out.println("     curr : " + dfa.Transitions.get(currentState));

                                            Set<Integer> IdTransition = dfa.Transitions.get(currentState).keySet();
                                            ArrayList<Integer> IdTransitionList = new ArrayList<Integer>(IdTransition);
                                            ArrayList<Integer> nextStatetmp = dfa.Transitions.get(currentState).get(IdTransitionList.get(0));
                                            // System.out.println("     avant if POINTINTERROGATION " + dfa.Transitions.get(nextStatetmp));
                                            if(dfa.Transitions.get(nextStatetmp)!= null) {
                                                if (dfa.Transitions.get(nextStatetmp).keySet().contains(POINTINTERROGATION)) {
                                                    nextState = nextStatetmp;
                                                    currentState = nextState;
                                                    // System.out.println("        ** POINTINTERROGATION");
                                                    // System.out.println("    ** 2 AVANT $$ nextState: " + nextState);
                                                    nextState = dfa.Transitions.get(nextState).get(POINTINTERROGATION);
                                                    currentState = nextState;


                                                    // System.out.println("    ** 3 AVANT $$ nextState: " + dfa.Transitions.get(currentState));
                                                    nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));
                                                    // System.out.println("    ** 2 APRES $$ nextState: " + nextState);
                                                    POINTINTERROGATIONB = false;
                                                    System.out.println("	kkkkkkk  curr txt: " + txt.charAt(i));
                                                } else {
                                                    nextState = null;
                                                }
                                            }
                                            // System.out.println("    --- APRES nextState: " + nextState);
                                        } else {
                                            // System.out.println(" 1 ESLE WHILE JE suis la POINTINTERROGATION >>>>>>>>>>> nextState : " + nextState);
                                            // il reste encore des transitiona effectué ....


                                            if (!dfa.f.contains(nextState)) {
                                                // System.out.println("        1 !dfa.f.contains(nextState))");
                                                ArrayList<Integer> oldnextState = nextState;
                                                ArrayList<Integer> oldcurrentState = currentState;
                                                boolean changeState = false;
                                                if (dfa.Transitions.get(nextState).keySet().contains(POINTINTERROGATION)) {
                                                    // si c'est pas se char alors c'est le suivant
                                                    // System.out.println(" OUI PPINT INTEROGATION : " + nextState);
                                                    currentState = nextState;
                                                    // System.out.println("        ** POINTINTERROGATION");
                                                    // System.out.println("    ** 2 AVANT $$ nextState: " + nextState);
                                                    nextState = dfa.Transitions.get(currentState).get(POINTINTERROGATION);
                                                    // System.out.println("    ** 2 APRES $$ nextState: " + nextState);
                                                    POINTINTERROGATIONB = false;
                                                    changeState = true;
                                                    // System.out.println("	kkkkkkk  curr txt: " + txt.charAt(i));
                                                } else {

                                                    // System.out.println("2 IF DFA F ESLE WHILE JE suis la POINTINTERROGATION >>>>>>>>>>> nextState : " + nextState);
                                                    // System.out.println("	 1 curr txt: " + txt.charAt(i));
                                                    // System.out.println("     curr keySet() : " + dfa.Transitions.get(nextState).keySet());
                                                    // System.out.println("     curr : " + dfa.Transitions.get(nextState));

                                                    Set<Integer> IdTransition = dfa.Transitions.get(nextState).keySet();

                                                    // System.out.println(" >>>>>>>>>>> ");

                                                    ArrayList<Integer> IdTransitionList = new ArrayList<Integer>(IdTransition);

                                                    // System.out.println(" >>>>>>>>>>> ");

                                                    ArrayList<Integer> nextStatetmp = dfa.Transitions.get(nextState).get(IdTransitionList.get(0));
                                                    // System.out.println(" >>>>>>>>>>> " + nextStatetmp);
                                                    // System.out.println(" setkey " + dfa.Transitions.get(nextStatetmp));

                                                    changeState = true;
                                                    if (dfa.Transitions.get(nextStatetmp) != null) {
                                                        if (dfa.Transitions.get(nextStatetmp).keySet().contains(POINTINTERROGATION)) {
                                                            nextState = nextStatetmp;
                                                            currentState = nextState;

                                                            // System.out.println("        ** POINTINTERROGATION");
                                                            currentState = nextState;
                                                            // System.out.println("    ** 2 AVANT $$ nextState: " + nextState);
                                                            nextState = dfa.Transitions.get(currentState).get(POINTINTERROGATION);
                                                            // System.out.println("    ** 2 APRES $$ nextState: " + nextState);
                                                            POINTINTERROGATIONB = false;
                                                            // System.out.println("	kkkkkkk  curr txt: " + txt.charAt(i));
                                                        }
                                                    }
                                                }
                                                // System.out.println("	changeState : " + POINTINTERROGATIONB);
                                                // System.out.println("	changeState : " + POINTINTERROGATIONB);
                                                if (changeState && POINTINTERROGATIONB) {
                                                    // System.out.println("3 je suis la!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                                    currentState = oldcurrentState;
                                                    nextState = oldnextState;
                                                }
                                            }
                                        }
                                        // System.out.println(" 		New currentChar : " + currentState);
                                        // System.out.println(" 		NextStat : " + nextState);
                                    } else {
                                        // System.out.println("		nextState = null");
                                        nextState = null;
                                    }
                                    // System.out.println("3 je suis la!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                                    if (nextState != null) {
                                        // System.out.println("	if (nextState != null)  ");
                                        currentChar = currentChar + txt.charAt(i + 1);
                                        // System.out.println("			1 curr txt: " + txt.charAt(i));
                                        // System.out.println("			1 next txt: " + txt.charAt(i + 1));
                                        // System.out.println(" 			1 currentChar: " + currentChar);
                                        precState = currentState;
                                        currentState = nextState;
                                        // System.out.println(" 			1 currentState: " + currentState);
                                    } else {
                                        // System.out.println(" je break !!!");
                                        break;
                                    }

                                    i += 1;
                                    // System.out.println(" * txt: " + txt.charAt(i));
                                    // System.out.println("...........Fin WHILE..............");
                                }

                                if (nextState != null && i == N - 1) {
                                    // System.out.println(" Apres le while : nextState != null && i == N - 1");
                                    currentState = nextState;
                                    nextState = null;
                                }
                                i = oldI;
                                // System.out.println(" Apres le while " + i);
                                // System.out.println(" >>>> nextState : " + nextState);

                                if (nextState == null && change) {
                                    // System.out.println(" suite du break !!!");
                                    // System.out.println(" APRES le break currentChar: " + currentChar);
                                    i -= 1;
                                    // System.out.println(" >>>> currentState : " + currentState);
                                    // System.out.println(" >>>> nextState : " + nextState);
                                    // System.out.println(" >>>> dfa.f. : " + dfa.f);
                                    if (dfa.f.contains(currentState)) {
                                        // System.out.println(">>>>>> je suis accepté : " + currentChar);
                                        // System.out.println("txt.charAt(i); : " + txt.charAt(i));
                                        // System.out.println("i : " + i);
                                        ArrayList<Integer> elem = new ArrayList<>();
                                        elem.add(idLine);
                                        elem.add(i + 1);
                                        elem.add(i + currentChar.length() + 1 - BEG - END);

                                        resutls.add(currentChar);

                                        elem = new ArrayList<>();
                                        elem.add(i + 1);
                                        elem.add(i + currentChar.length() + 1 - BEG - END); // fin
                                        resutlsF.add(elem);

                                        matchingWordId.computeIfAbsent(idLine, k -> new ArrayList<>()).add((elem));

                                        currentChar = "";
                                        change = false;
                                        if (maxMatching.length() < currentChar.length())
                                            maxMatching = currentChar;
                                    } else {
                                        // System.out.println(" je ne suis pas accepté : " + currentChar + " | " +
                                           //      currentState);
                                        // System.out.println("     curr : " + dfa.Transitions.get(currentState));
                                    }
                                    currentChar = "";
                                }
                            } else {
                                // System.out.println(" 2 >>>> currentChar : " + currentChar);
                                ArrayList<Integer> elem = new ArrayList<>();
                                elem.add(idLine);
                                // 1 : currentChar.length() = wordToSearch.length()-1
                                // pour currentChar.length()- 1
                                // 1 pour commencer de 1 et non de 0
                                elem.add(i + 1);
                                elem.add(i + currentChar.length() + 1 - BEG - END);
                                resutls.add(currentChar);

                                elem = new ArrayList<>();
                                elem.add(i + 1);
                                elem.add(i + currentChar.length() + 1 - BEG - END);
                                resutlsF.add(elem);

                                matchingWordId.computeIfAbsent(idLine, k -> new ArrayList<>()).add((elem));

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