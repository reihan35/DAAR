import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Exception;
import java.util.Random;
import java.io.*;
import java.time.*;

// java RegEx '`S(a|r|g)*on`' text1

public class RegEx {

    // MACROS
    public static final int CONCAT = 0xC04CA7;
    public static final int ETOILE = 0xE7011E;
    public static final int ALTERN = 0xA17E54;
    public static final int PROTECTION = 0xBADDAD;

    static final int PARENTHESEOUVRANT = 0x16641664;
    static final int PARENTHESEFERMANT = 0x51515151;
    static final int DOT = 0xD07;
    static final int POINTINTERROGATION = 0xF07;

    static final String RED = "\033[0;31m"; // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String RESET = "\033[0m";  // Text Reset
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE

    // REGEX
    public static String regEx;
    public static String fileName;
    public static String option = null;
    public static boolean i = false;
    public static boolean w = false;
    public static boolean x = false;
    public static boolean c = false;
    public static boolean m = false;
    public static int mNB = 0;
    public static boolean o = false;

    // CONSTRUCTOR
    public RegEx() {
    }

    public static void main(String arg[]) throws Exception {

        String line = "";

        boolean allChar = false;

        if (arg.length == 0) {
            return;
        } else if (arg.length == 2) {
            regEx = arg[0];
            fileName = arg[1];
            System.out.println(" >> file name : " + fileName);
            System.out.println("regEx : " + regEx);

            if (regEx.charAt(0) == '`' && regEx.charAt(regEx.length() - 1) == '`') {
                System.out.println(" >> je suis dans le truc : " + regEx.substring(1, regEx.length() - 1));
                regEx = regEx.substring(1, regEx.length() - 1);
                allChar = true;
            }
            System.out.println("1 regEx : " + regEx);

        } else if (arg.length == 3) {
            // daar [OPTIONS] PATTERN [FILE]
            System.out.println("nb arg 3 ");
            option = arg[0];
            regEx = arg[1];
            fileName = arg[2];
            System.out.println(" >> regEx : " + regEx);
            System.out.println(" >> option : " + option);
            System.out.println(" >> file name : " + fileName);

            // ok 1.option l :
            if (option.contains("l")) {
                System.out.println("File name : " + PURPLE_BOLD + fileName);
                return;
            }

            // OK option i; Ignorer les distinctions de casse dans les fichiers PATTERN et le fichier d'entrée
            if (option.contains("i")) {
                regEx = regEx.toLowerCase();
                i = true;
            }

            // OK option c: Supprimer la sortie normale; à la place, imprimez le nombre de ligne de correspondantes
            if (option.contains("c"))
                c = true;

            if (option.contains("m")) {
                m = true;
                String[] options = option.split("m");
                mNB = Integer.parseInt(options[1]);
            }

            // (KO) option o : print que les matching trouver
            if (option.contains("o")) {
                o = true;
            }
            if (option.contains("w")) {
                w = true;
            }

        }
        if (regEx.length() < 1) {
            System.err.println("  >> ERROR: empty regEx.");
        }
        boolean BEG = false;
        boolean END = false;

        if (regEx.charAt(0) == '`' && regEx.charAt(regEx.length() - 1) == '`') {
            System.out.println(" >> je suis dans le truc : " + regEx.substring(1, regEx.length() - 1));
            regEx = regEx.substring(1, regEx.length() - 1);
            allChar = true;
        }

        if (regEx.length() < 1) {
            System.err.println("  >> ERROR: empty regEx.");
        } else {

            try {
                Search search = new Search();
                ArrayList wordMatch = new ArrayList<>();
                String[] regExs = regEx.split("\\n");

                if (regEx.substring(0, 1).contains("^"))
                    BEG = true;

                if (regEx.substring(regEx.length() - 1, regEx.length()).contains("$"))
                    END = true;

                ArrayList<String> linesOrigin = getLinesFiles(fileName);
                ArrayList<String> lines = getLinesFiles(fileName, i, BEG, END);

                File file = new File(fileName);
                if (isRegex(regEx)) {
                    RegExTree ret = parse(allChar);
                    NFA n = ret.toAutomaton();
                    DFA d = n.to_DFA();
                    d.print();
                    // d.minDFA();

                    if (isRegex(regEx)) {
                        ArrayList<ArrayList<Integer>> result = mainM1(lines, d, regEx);
                        if(c)
                            return;
                        printWordsInColorM1(lines, result, BEG, END);
                    }
                } else {
                    // method 3
                    if (w) {
                        File cache = new File(fileName + "_cache" );
                        if (Indexing.toHashInt(Indexing.FileToStrings(file)).get(regEx) != null) {
                            System.out.println(" 2 ....Method3.....");
                            if (!cache.exists()) {
                                try {
                                    Indexing.makeCash(Indexing.FileToStrings(file), fileName);
                                } catch (Exception e) {
                                    System.out.println("ERREUR" + e);
                                }
                            }

                            Trie t = Indexing.trieFromFile(cache);
                            System.out.println(t.search(regEx));
                            printWordsInColor(regEx, FileToStrings(file), t.search(regEx));
                        } else {
                            // KMP (method 2)
                            System.out.println("....Method2.....");
                            ArrayList<ArrayList<Integer>> result = mainKMP(lines, c);
                            printWordsInColorKMP(regEx, lines, result);
                        }
                    } else {
                        ArrayList<ArrayList<Integer>> result = mainKMP(lines, c);
                        printWordsInColorKMP(regEx, lines, result);
                    }
                }

            } catch (Exception e) {
                System.err.println("  >> ERROR: syntax error for regEx \"" + regEx + "\". " + e);
            }
        }

    }

    public static ArrayList<ArrayList<Integer>> mainM1(ArrayList<String> lines, DFA d, String regEx) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        Search search = new Search();

        int i = 0;
        int nbWordsMatch = 0;

        for (String line : lines) {
            i++;

            ArrayList<ArrayList<Integer>> matching = search.searchWithDFA(d, line, regEx, i, 0);
            if (matching.size() > 0)
                result.addAll(matching);

            nbWordsMatch += matching.size();
        }
        if(c)
            System.out.println("Nombre de motif qui match: " + RED + nbWordsMatch + RESET);
        return result;
    }

    public static ArrayList<ArrayList<Integer>> mainKMP(ArrayList<String> lines, boolean c) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        Search search = new Search();
        int[] retenue = search.retenue(regEx);
        int nbLineMatch = 0;

        int i = 0;
        for (String line : lines) {
            i++;
            ArrayList<ArrayList<Integer>> matching = search.matchingWords(regEx.toCharArray(), retenue,
                    line.toCharArray(), i - 1);

            if (matching.size() > 0)
                result.addAll(matching);
            nbLineMatch++;
        }
        return result;

    }

    //Cette fonction renvoie la liste des lignes d'un fichier
    public static ArrayList<String> FileToStrings(File fileName) {
        try {
            String line = null;
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Trie t = new Trie();
            ArrayList<String> strings = new ArrayList<String>();

            while ((line = bufferedReader.readLine()) != null) {
                strings.add(line);
            }
            return strings;

        } catch (Exception e) {
            return null;
        }
    }
    //Cette fonction affiche les résultats de Radix Tree en coloriant les motifs retrouvés
    public static void printWordsInColor(String reg, ArrayList<String> lines, ArrayList<ArrayList<Integer>> ti) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[42m";
        for (int i = 0; i < ti.size(); i++) {
            Object lineNumt = ti.get(i).get(0);
            String s = (String) lineNumt;
            int lineNum = Integer.parseInt(s);
            Object indext = ti.get(i).get(1);
            String s2 = (String) indext;
            s2 = s2.substring(1, s2.length());
            int index = Integer.parseInt(s2);
            String line = lines.get(lineNum - 1);
            System.out.println(line);

            System.out.println(
                    ANSI_RESET +
                            line.substring(0, index - 1) +
                            ANSI_RED +
                            (index > 0 ? line.substring(index - 1, index + reg.length() - 1) : "")
                            + ANSI_RESET +
                            ((index + reg.length() - 1) > line.length() ? line.substring(index + reg.length() - 1, line.length()) : ""));

        }
    }

    //Cette fonction affiche les résultats de KMP en coloriant les motifs retrouvés
    public static void printWordsInColorKMP(String reg, ArrayList<String> lines, ArrayList<ArrayList<Integer>> ti) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[42m";
        for (int i = 0; i < ti.size(); i++) {

            String line = lines.get(ti.get(i).get(0) - 1);

            System.out.println(ANSI_RESET +
                    (ti.get(i).get(1) > 1 ? line.substring(0, ti.get(i).get(1) - 1) : "") +
                    RED +
                    line.substring(ti.get(i).get(1) - 1, ti.get(i).get(1) + reg.length() - 1) +
                    ANSI_RESET +
                    (ti.get(i).get(1) + reg.length() < line.length() ? line.substring(ti.get(i).get(1) + reg.length() - 1, line.length() - 1) : ""));

        }
    }

    //Cette fonction affiche les résultats de la méthode des automates en coloriant les motifs retrouvés
    public static void printWordsInColorM1(ArrayList<String> lines, ArrayList<ArrayList<Integer>> ti, boolean BEG, boolean END) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[42m";
        for (int i = 0; i < ti.size(); i++) {
            if(m){
                if(i==mNB)
                    break;
            }

            String line = lines.get(ti.get(i).get(0) - 1);

            if (BEG)
                line = line.replace("^", "");

            if (END)
                line = line.replace("$", "");

            System.out.println(
                    (((ti.get(i).get(1) > 1) && !o) ? line.substring(0, ti.get(i).get(1) - 1) : "") +
                            RED +
                            line.substring(ti.get(i).get(1) - 1, ti.get(i).get(2) - 1) +
                            ANSI_RESET +
                            ((line.length() > (ti.get(i).get(2)-1) && !o) ? line.substring(ti.get(i).get(2)-1, line.length()) : ""));
        }
    }

    public static ArrayList<String> getLinesFiles(String file) throws
            FileNotFoundException {
        ArrayList<String> lines = new ArrayList<String>();
        String line;
        try {
            int i = 0;
            Scanner scanner = new Scanner(new File(file));
            while (scanner.hasNextLine()) {

                line = scanner.nextLine();

                lines.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static ArrayList<String> getLinesFiles(String file, boolean IgnoretoLowerCase, boolean BEG, boolean END) throws
            FileNotFoundException {
        ArrayList<String> lines = new ArrayList<String>();
        String line;
        try {
            int i = 0;
            Scanner scanner = new Scanner(new File(file));
            while (scanner.hasNextLine()) {

                line = scanner.nextLine();

                if (IgnoretoLowerCase)
                    line = line.toLowerCase();

                if (line.length() != 0) {
                    if (BEG)
                        line = "^" + line;
                    if (END)
                        line = line + "$";

                    lines.add(line);
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void readFileLine(String file, DFA d, Search search) throws FileNotFoundException {
        ArrayList<String> resutls = new ArrayList<String>();
        try {
            Scanner scanner = new Scanner(new File(file));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // System.out.println("line: " + line);
                search.searchWithDFA(d, line, regEx, 0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Cette fonction verifie si une chaine de caracter est un regex
    private static boolean isRegex(String str) {
        return ((!str.equals("")) && (str != null) && (str.contains(".") || str.contains("*") || str.contains("|")
                || str.contains("(") || str.contains(")") || str.contains("^") || str.contains("?") || str.contains("$") || str.contains("\\n")));
    }

    // FROM REGEX TO SYNTAX TREE
    private static RegExTree parse(boolean allChar) throws Exception {
        // BEGIN DEBUG: set conditionnal to true for debug example
        if (false)
            throw new Exception();
        RegExTree example = exampleAhoUllman();
        if (false)
            return example;
        // END DEBUG

        ArrayList<RegExTree> result = new ArrayList<RegExTree>();
        for (int i = 0; i < regEx.length(); i++) {
            boolean ignore = false;

            if (regEx.charAt(i) == '\\') {
                System.out.println(">>>>>>>>>>> !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! .......................");
                ignore = true;
                i++;
            }

            if (allChar)
                result.add(new RegExTree(charToRoot(regEx.charAt(i), true), new ArrayList<RegExTree>()));
            else
                result.add(new RegExTree(charToRoot(regEx.charAt(i), ignore), new ArrayList<RegExTree>()));
        }
        return parse(result);
    }

    private static int charToRoot(char c, boolean ignore) {
        if (ignore)
            return (int) c;
        if (c == '.')
            return DOT;
        if (c == '?')
            return POINTINTERROGATION;
        if (c == '*')
            return ETOILE;
        if (c == '|')
            return ALTERN;
        if (c == '(')
            return PARENTHESEOUVRANT;
        if (c == ')')
            return PARENTHESEFERMANT;
        return (int) c;
    }

    private static RegExTree parse(ArrayList<RegExTree> result) throws Exception {
        while (containParenthese(result))
            result = processParenthese(result);
        while (containEtoile(result))
            result = processEtoile(result);
        while (containConcat(result))
            result = processConcat(result);
        while (containAltern(result))
            result = processAltern(result);

        if (result.size() > 1)
            throw new Exception();

        return removeProtection(result.get(0));
    }

    private static boolean containParenthese(ArrayList<RegExTree> trees) {
        for (RegExTree t : trees)
            if (t.root == PARENTHESEFERMANT || t.root == PARENTHESEOUVRANT)
                return true;
        return false;
    }

    private static ArrayList<RegExTree> processParenthese(ArrayList<RegExTree> trees) throws
            Exception {
        ArrayList<RegExTree> result = new ArrayList<RegExTree>();
        boolean found = false;
        for (RegExTree t : trees) {
            if (!found && t.root == PARENTHESEFERMANT) {
                boolean done = false;
                ArrayList<RegExTree> content = new ArrayList<RegExTree>();
                while (!done && !result.isEmpty())
                    if (result.get(result.size() - 1).root == PARENTHESEOUVRANT) {
                        done = true;
                        result.remove(result.size() - 1);
                    } else
                        content.add(0, result.remove(result.size() - 1));
                if (!done)
                    throw new Exception();
                found = true;
                ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
                subTrees.add(parse(content));
                result.add(new RegExTree(PROTECTION, subTrees));
            } else {
                result.add(t);
            }
        }
        if (!found)
            throw new Exception();
        return result;
    }

    private static boolean containEtoile(ArrayList<RegExTree> trees) {
        for (RegExTree t : trees)
            if (t.root == ETOILE && t.subTrees.isEmpty())
                return true;
        return false;
    }

    private static ArrayList<RegExTree> processEtoile(ArrayList<RegExTree> trees) throws Exception {
        ArrayList<RegExTree> result = new ArrayList<RegExTree>();
        boolean found = false;
        for (RegExTree t : trees) {
            if (!found && t.root == ETOILE && t.subTrees.isEmpty()) {
                if (result.isEmpty())
                    throw new Exception();
                found = true;
                RegExTree last = result.remove(result.size() - 1);
                ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
                subTrees.add(last);
                result.add(new RegExTree(ETOILE, subTrees));
            } else {
                result.add(t);
            }
        }
        return result;
    }

    private static boolean containConcat(ArrayList<RegExTree> trees) {
        boolean firstFound = false;
        for (RegExTree t : trees) {
            if (!firstFound && t.root != ALTERN) {
                firstFound = true;
                continue;
            }
            if (firstFound)
                if (t.root != ALTERN)
                    return true;
                else
                    firstFound = false;
        }
        return false;
    }

    private static ArrayList<RegExTree> processConcat(ArrayList<RegExTree> trees) throws Exception {
        ArrayList<RegExTree> result = new ArrayList<RegExTree>();
        boolean found = false;
        boolean firstFound = false;
        for (RegExTree t : trees) {
            if (!found && !firstFound && t.root != ALTERN) {
                firstFound = true;
                result.add(t);
                continue;
            }
            if (!found && firstFound && t.root == ALTERN) {
                firstFound = false;
                result.add(t);
                continue;
            }
            if (!found && firstFound && t.root != ALTERN) {
                found = true;
                RegExTree last = result.remove(result.size() - 1);
                ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
                subTrees.add(last);
                subTrees.add(t);
                result.add(new RegExTree(CONCAT, subTrees));
            } else {
                result.add(t);
            }
        }
        return result;
    }

    private static boolean containAltern(ArrayList<RegExTree> trees) {
        for (RegExTree t : trees)
            if (t.root == ALTERN && t.subTrees.isEmpty())
                return true;
        return false;
    }

    private static ArrayList<RegExTree> processAltern(ArrayList<RegExTree> trees) throws Exception {
        ArrayList<RegExTree> result = new ArrayList<RegExTree>();
        boolean found = false;
        RegExTree gauche = null;
        boolean done = false;
        for (RegExTree t : trees) {
            if (!found && t.root == ALTERN && t.subTrees.isEmpty()) {
                if (result.isEmpty())
                    throw new Exception();
                found = true;
                gauche = result.remove(result.size() - 1);
                continue;
            }
            if (found && !done) {
                if (gauche == null)
                    throw new Exception();
                done = true;
                ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
                subTrees.add(gauche);
                subTrees.add(t);
                result.add(new RegExTree(ALTERN, subTrees));
            } else {
                result.add(t);
            }
        }
        return result;
    }

    private static RegExTree removeProtection(RegExTree tree) throws Exception {
        if (tree.root == PROTECTION && tree.subTrees.size() != 1)
            throw new Exception();
        if (tree.subTrees.isEmpty())
            return tree;
        if (tree.root == PROTECTION)
            return removeProtection(tree.subTrees.get(0));

        ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
        for (RegExTree t : tree.subTrees)
            subTrees.add(removeProtection(t));
        return new RegExTree(tree.root, subTrees);
    }

    // EXAMPLE
// --> RegEx from Aho-Ullman book Chap.10 Example 10.25
    private static RegExTree exampleAhoUllman() {
        RegExTree a = new RegExTree((int) 'a', new ArrayList<RegExTree>());
        RegExTree b = new RegExTree((int) 'b', new ArrayList<RegExTree>());
        RegExTree c = new RegExTree((int) 'c', new ArrayList<RegExTree>());
        ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
        subTrees.add(c);
        RegExTree cEtoile = new RegExTree(ETOILE, subTrees);
        subTrees = new ArrayList<RegExTree>();
        subTrees.add(b);
        subTrees.add(cEtoile);
        RegExTree dotBCEtoile = new RegExTree(CONCAT, subTrees);
        subTrees = new ArrayList<RegExTree>();
        subTrees.add(a);
        subTrees.add(dotBCEtoile);
        return new RegExTree(ALTERN, subTrees);
    }
}



// UTILITARY CLASS
class RegExTree {
    protected int root;
    protected ArrayList<RegExTree> subTrees;

    public RegExTree(int root, ArrayList<RegExTree> subTrees) {
        this.root = root;
        this.subTrees = subTrees;
    }
    // FROM TREE TO PARENTHESIS

    public NFA toAutomaton() {

        if (root == RegEx.CONCAT) {
            return subTrees.get(0).toAutomaton().concaten(subTrees.get(1).toAutomaton());
        }

        if (root == RegEx.ETOILE) {
            return subTrees.get(0).toAutomaton().etoil();
        }

        if (root == RegEx.ALTERN) {
            return subTrees.get(0).toAutomaton().altern(subTrees.get(1).toAutomaton());
        }

        // simple_char();

        ArrayList<Integer> Q = new ArrayList<Integer>();
        int r1 = 0;
        int r2 = 0;
        while (r1 == r2 || r1 == 0 || r2 == 0) {
            Random rand = new Random();
            r1 = rand.nextInt(1000);
            r2 = rand.nextInt(1000);
        }

        // Si on est là c'est qu'on tombe les feuilles de l'arbre (des lettre)

        Q.add(r1);
        Q.add(r2);
        ArrayList<Integer> Sigma = new ArrayList<Integer>();
        Sigma.add(root);
        Tuple ti = new Tuple(r2, root);
        ArrayList<Tuple> t = new ArrayList<Tuple>();
        t.add(ti);
        HashMap<Integer, ArrayList<Tuple>> Transitions = new HashMap<Integer, ArrayList<Tuple>>();
        Transitions.put(r1, t);
        NFA n = new NFA(Q, Sigma, r1, r2, Transitions);
        return n;
    }

    public String toString() {
        if (subTrees.isEmpty())
            return rootToString();
        String result = rootToString() + "(" + subTrees.get(0).toString();
        for (int i = 1; i < subTrees.size(); i++)
            result += "," + subTrees.get(i).toString();
        return result + ")";
    }

    private String rootToString() {
        if (root == RegEx.CONCAT)
            return ".";
        if (root == RegEx.ETOILE)
            return "*";
        if (root == RegEx.ALTERN)
            return "|";
        if (root == RegEx.DOT)
            return ".";
        if (root == RegEx.POINTINTERROGATION)
            return "?";
        return Character.toString((char) root);
    }

}
