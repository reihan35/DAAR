import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Exception;
import java.util.Random;
import java.io.*;
import java.time.*;

// java RegEx '`S(a|r|g)*on`' text1

public class testeReg {
    /**CLASS QUI GENERE DES FICHIER DE TEST POUR LA METHODE DES AUTOMATES ET QUI LANCE DES TESTES DESSUS
    **/
    // MACROS
    static final int CONCAT = 0xC04CA7;
    static final int ETOILE = 0xE7011E;
    static final int ALTERN = 0xA17E54;
    static final int PROTECTION = 0xBADDAD;

    static final int PARENTHESEOUVRANT = 0x16641664;
    static final int PARENTHESEFERMANT = 0x51515151;
    static final int DOT = 0xD07;
    static final int POINTINTERROGATION = 0xF07;

    // REGEX
    private static String regEx;
    private static String folderName;
    private static String option = null;
    private static ArrayList<String> words = wordsLanguage();


    static final String RED = "\033[0;31m"; // RED
    
    static final int nbFileG = 50;
    
    // CONSTRUCTOR
    public testeReg() {
    }

    public static void main(String arg[]) throws Exception {
        /*if (arg.length == 0) {
            try{
                generateFiles(10);
            }catch(Exception e){
                System.out.println(e);
            }
            return;
        } else {*/
            generateFiles(nbFileG);
            folderName = arg[0];
            boolean allChar = false;
            final File folder = new File(folderName);
            for (final File folder1 : folder.listFiles()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File(folder1.getName())));
                for (final File file : folder1.listFiles()) {
                    boolean BEG = false;
                    boolean END = false;
                 //  System.out.println(file.getName());
                    Random r = new Random();
                    String regEx = generateRegex();
                 //  System.out.println(regEx);
                    //egrep 
                    //System.out.println("egrep");
                    //System.out.println("path :" + folderName + file.getName());
                    writer.write(file.getName()+ " "); //LE NOM DU FICHIER
                    writer.write(regEx + " ");
                    String c2 = "egrep " + "-c " + regEx +" "+ folderName + folder1.getName()+ "/" + file.getName();
                 // System.out.println(c2);
                    final Process process2 = Runtime.getRuntime().exec(c2);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process2.getInputStream()));
                        String line = "";
                        try {
                            while((line = reader.readLine()) != null) {
                                //  System.out.println("je rentre");
                                writer.write(line + " "); //LE NBR MOT AVEC EGREP
                            }
                        } finally {
                            reader.close();
                        }

                    // String c = "egrep " + regEx +" "+ folderName + folder1.getName()+ "/" + file.getName();
                    Instant start = Instant.now();
                    final Process process = Runtime.getRuntime().exec(c2);
                    Instant finish = Instant.now();
                    long timeElapsed = Duration.between(start, finish).toNanos(); 
                 // System.out.println(timeElapsed);
                    
                    writer.write(timeElapsed + " "); //LE TEMPS AVEC EGREP
                    
                   if (regEx.charAt(0) == '`' && regEx.charAt(regEx.length() - 1) == '`') {
                    //     System.out.println(" >> je suis dans le truc : " + regEx.substring(1, regEx.length() - 1));
                        regEx = regEx.substring(1, regEx.length() - 1);
                        allChar = true;
                    }
                // System.out.println("1 regEx : " + regEx);


                    if (regEx.length() < 1) {
                        //      System.err.println("  >> ERROR: empty regEx.");
                    } else {
                        try {
                            Search search = new Search();
                            ArrayList wordMatch = new ArrayList<>();
                            String[] regExs = regEx.split("\\n");

                            if (regEx.substring(0, 1).contains("^"))
                                BEG = true;

                            if (regEx.substring(regEx.length() - 1, regEx.length()).contains("$"))
                                END = true;
                            //System.out.println(file.getName());
                             ArrayList<String> lines = getLinesFiles( folderName + folder1.getName()+ "/" + file.getName(), BEG, END);

                            if (isRegex(regEx)) {
                                //      System.out.println("....Method1.....");
                                // System.out.println("!!!!!!!!!!!!!!!!!!");
                                RegExTree ret = parse(regEx,allChar);
                                NFA n = ret.toAutomaton();
                                // n.print();
                                DFA d = n.to_DFA();
                                // d.print();
                             // System.out.println("................................");
                                // d.minDFA();
                             // System.out.println("................................");

                                if (isRegex(regEx)) {
                                    // la methode est plus rapide
                                    //     System.out.println("....ELSE.....");
                                    start = Instant.now();
                                    ArrayList<ArrayList<Integer>> result = mainM1(lines, d, regEx);
                                    finish = Instant.now();
                                    timeElapsed = Duration.between(start, finish).toNanos(); 
                                    writer.write(timeElapsed + " ");
                                    // printWordsInColorM1(lines, result, BEG, END);
                                    writer.write("\n");
                                }
                            }
                        }catch (Exception e) {
                            System.err.println("  >> ERROR: syntax error for regEx \"" + regEx + "\". " + e);
                        }
                    }
                }
                            writer.close();            

            }
        }
    //fonction qui gener des regex de maniere aletoi en prenant la liste des mots et rajoutant des operateurs
    public static String generateRegex(){
        Random r = new Random();
        String regEx = words.get(r.nextInt(words.size()));
        ArrayList<Character> seps = new ArrayList<Character>();
        seps.add('.');
        seps.add('*');
        seps.add('|');
        
        int r2 = r.nextInt(2);
        regEx = regEx.substring(0,2) + seps.get(r2) + regEx.substring(2,3);
        if(r2 == 2){
            regEx = regEx.substring(0,1) + seps.get(r2) + regEx.substring(1,2);
        }
        return regEx;
    }

    //Cette fonction genere des fichier de tailles 10K,100K et 1M le nbr de fichier par taille est passé en param
    public static void generateFiles(int nbr) throws Exception{
        new File("baseDeTest").mkdir();
        new File("baseDeTest//10K").mkdir();
        new File("baseDeTest//100K").mkdir();
        new File("baseDeTest//1M").mkdir();
        System.out.println("BAAH OUII");
        //File f = new File("baseDeTest");
        File f10 = new File("baseDeTest//10K//");
        File f2 = new File("baseDeTest//100K//");
        File fM = new File("baseDeTest//1M//");

        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(10000);
        a.add(100000);
        a.add(1000000);
        String s = "";
        for(int taille : a){
            if(taille == 10000){
                s = "fichier10K";
                for(int i = 0 ; i< nbr;i++){
                    //             System.out.println("ALO");
                    generateFileEnglish(taille,s + (i+1),f10);
                    //System.out.println(i);
                }
            }
            else{
                if(taille == 100000){
                    // System.out.println("ALO");

                    s = "fichier100K";
                    for(int i = 0 ; i< nbr;i++){
                        //  System.out.println("ALO");
                        generateFileEnglish(taille,s + (i+1),f2);
                        //System.out.println(i);
                    }
                }
                else {
                    if(taille == 1000000){
                        s = "fichier1M";
                        for(int i = 0 ; i< nbr;i++){
                            generateFileEnglish(taille,s + (i+1),fM);
                            //System.out.println(i);
                        }
                    }
                }
            }
            
        }
    }

    //Cette fonction cree et genere des mots aleatoir en prenant des lettres aleatoires et avec des longueur qui correspond aux meme que la distribution de longueur de lettre en anglais
    public static ArrayList<String> wordsLanguage(){
        ArrayList<Character> alphabet = new ArrayList<Character>();
        alphabet.add('a');
        alphabet.add('b');
        alphabet.add('c');
        alphabet.add('d');
        alphabet.add('e');
        alphabet.add('f');
        alphabet.add('g');
        alphabet.add('h');
        alphabet.add('i');
        alphabet.add('j');
        alphabet.add('k');
        alphabet.add('l');
        alphabet.add('m');
        alphabet.add('n');
        alphabet.add('o');
        alphabet.add('p');
        alphabet.add('q');
        alphabet.add('r');
        alphabet.add('s');
        alphabet.add('t');
        alphabet.add('u');
        alphabet.add('v');
        alphabet.add('w');
        alphabet.add('x');
        alphabet.add('y');
        alphabet.add('z');
 
        DistributedRandomNumberGenerator d = new DistributedRandomNumberGenerator();
        d.addNumber(1,0.1/100);
        d.addNumber(2,0.1/100);
        d.addNumber(3,0.6/100);
        d.addNumber(4,2.6/100);
        d.addNumber(5,5.2/100);
        d.addNumber(6,8.5/100);
        d.addNumber(7,12.2/100);
        d.addNumber(8,14/100);
        d.addNumber(9,14/100);
        d.addNumber(10,12.6/100);
        d.addNumber(11,10.1/100);
        d.addNumber(12,7.5/100);
        d.addNumber(13,5.2/100);
        d.addNumber(14,3.2/100);
        d.addNumber(15,2.0/100);
        d.addNumber(16,1.0/100);
        d.addNumber(17,0.6/100);
        d.addNumber(18,0.3/100);
        d.addNumber(19,0.2/100);
        d.addNumber(20,0.1/100);
        d.addNumber(21,0.1/100);

        ArrayList<String> words = new ArrayList<String>();
        Random r = new Random();
        String s = "";
        int taille = 0;
        for (int i = 0; i < 1000;i++){
            taille = d.getDistributedRandomNumber();
            while (taille == 1){
                taille = d.getDistributedRandomNumber();
            }
            for(int j = 0 ; j<taille ; j++){
                int r2 = r.nextInt(25);
                s = s + alphabet.get(r2);
            }
            words.add(s);
            s = "";
        }
        //System.out.println(words);
       return words;
    }

    //Cette fonction genere un fichier de taille passé en parametre avec les mots construit avant
    public static void generateFileEnglish(int taille,String fileName, File dir) throws Exception{
        int cpt = 0;
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(dir, fileName)));
        for(int i = 0; i<words.size();i++){
            writer.write( words.get(i) + " ");
             if(i%18==0){
                writer.write("\n");  
            }
        }
        while(cpt < taille-1000){
            Random r = new Random();
            int result = r.nextInt(words.size());
            writer.write( words.get(result) + " ");
            cpt++;
            if(cpt%18==0){
                writer.write("\n");  
            }
        }
        writer.close();
    }

     public static ArrayList<ArrayList<Integer>> mainM1(ArrayList<String> lines, DFA d, String regEx) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        Search search = new Search();

        int i = 0;
        for (String line : lines) {
            i++;
            // System.err.println("line : " + line);

            ArrayList<ArrayList<Integer>> matching = search.searchWithDFA(d, line, regEx, i, 0);
            if (matching.size() > 0)
                result.addAll(matching);
        }
     // System.out.println("[M1] word match: " + result);
        return result;
    }

    public static ArrayList<ArrayList<Integer>> mainKMP(ArrayList<String> lines) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        Search search = new Search();
        int[] retenue = search.retenue(regEx);

        int i = 0;
        for (String line : lines) {
            i++;
            ArrayList<ArrayList<Integer>> matching = search.matchingWords(regEx.toCharArray(), retenue,
                    line.toCharArray(), i - 1);
            if (matching.size() > 0)
                result.addAll(matching);
        }
     //  System.out.println(">>>>>>>>>> result: " + result.size());

        return result;
    }

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
         //   System.out.println(line);

           /* System.out.println(
                    ANSI_RESET +
                            line.substring(0, index - 1) +
                            ANSI_RED +
                            (index > 0 ? line.substring(index - 1, index + reg.length() - 1) : "")
                            + ANSI_RESET +
                            ((index + reg.length() - 1) > line.length() ? line.substring(index + reg.length() - 1, line.length()) : ""));
            */
        }
    }

    public static void printWordsInColorKMP(String reg, ArrayList<String> lines, ArrayList<ArrayList<Integer>> ti) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[42m";
        for (int i = 0; i < ti.size(); i++) {

            // System.out.println(index);

            String line = lines.get(ti.get(i).get(0) - 1);

            /*System.out.println(ANSI_RESET +
                    (ti.get(i).get(1) > 1 ? line.substring(0, ti.get(i).get(1) - 1) : "") +
                    RED +
                    line.substring(ti.get(i).get(1) - 1, ti.get(i).get(1) + reg.length() - 1) +
                    ANSI_RESET +
                    (ti.get(i).get(1) + reg.length() < line.length() ? line.substring(ti.get(i).get(1) + reg.length() - 1, line.length() - 1) : ""));
            */
        }
    }

    public static void printWordsInColorM1(ArrayList<String> lines, ArrayList<ArrayList<Integer>> ti, boolean BEG, boolean END) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[42m";
        for (int i = 0; i < ti.size(); i++) {


            String line = lines.get(ti.get(i).get(0) - 1);

            if (BEG)
                line = line.replace("^", "");

            if (END)
                line = line.replace("$", "");
            /*System.out.println(
                    (ti.get(i).get(1) > 1 ? line.substring(0, ti.get(i).get(1) - 1) : "") +
                            RED +
                            line.substring(ti.get(i).get(1) - 1, ti.get(i).get(2) - 1) +
                            ANSI_RESET +
                            (line.length() > (ti.get(i).get(2)-1) ? line.substring(ti.get(i).get(2)-1, line.length()) : ""));
                            */
        }
    }

    public static ArrayList<String> getLinesFiles(String file, boolean BEG, boolean END) throws FileNotFoundException {
        // System.out.println(" BEG LINES : " + BEG);
        // System.out.println(" END LINES : " + END);
        ArrayList<String> lines = new ArrayList<String>();
        try {
            int i = 0;
            Scanner scanner = new Scanner(new File(file));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.length() != 0) {
                    if (BEG)
                        line = "^" + line;
                    if (END)
                        line = line + "$";

                    lines.add(line);
                    // System.out.println(i + ">>>>>>> : " + line);
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

    private static boolean isRegex(String str) {
        return ((!str.equals("")) && (str != null) && (str.contains(".") || str.contains("*") || str.contains("|")
                || str.contains("(") || str.contains(")") || str.contains("^") || str.contains("?") || str.contains("$") || str.contains("\\n")));
    }

    // FROM REGEX TO SYNTAX TREE
    private static RegExTree parse(String regEx, boolean allChar) throws Exception {
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
                // System.out.println(">>>>>>>>>>> !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! .......................");
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

    private static ArrayList<RegExTree> processParenthese(ArrayList<RegExTree> trees) throws Exception {
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

class DistributedRandomNumberGenerator {

    private HashMap<Integer, Double> distribution;
    private double distSum;

    public DistributedRandomNumberGenerator() {
        distribution = new HashMap<>();
    }

    public void addNumber(int value, double distribution) {
        if (this.distribution.get(value) != null) {
            distSum -= this.distribution.get(value);
        }
        this.distribution.put(value, distribution);
        distSum += distribution;
    }

    public int getDistributedRandomNumber() {
        double rand = Math.random();
        double ratio = 1.0f / distSum;
        double tempDist = 0;
        for (Integer i : distribution.keySet()) {
            tempDist += distribution.get(i);
            if (rand / ratio <= tempDist) {
                return i;
            }
        }
        return 0;
    }

}

// Class pour gérér les automates finies avec des epsilon transition
class NFA {
    protected ArrayList<Integer> q = new ArrayList<Integer>();
    protected ArrayList<Integer> Sigma = new ArrayList<Integer>();
    protected int q0 = -1;
    protected int f = -1;
    protected HashMap<Integer, ArrayList<Tuple>> Transitions = new HashMap<Integer, ArrayList<Tuple>>();

    public NFA(ArrayList<Integer> Q, ArrayList<Integer> Sigma, int Q0, int F,
               HashMap<Integer, ArrayList<Tuple>> Transitions) {
        this.q = Q; // les etats existant
        this.Sigma = Sigma; //
        this.q0 = Q0; // etat initial
        this.f = F; // etat finale
        this.Transitions = Transitions;
    }

    public ArrayList<Integer> getQ() {
        return q;
    }

    public void add_state(int q) {
        this.q.add(q);
    }

    public void add_trans(int s) {
        Sigma.add(s);
    }

    public void set_final_state(int f) {
        f = f;
    }

    public void set_initial_state(int q0) {
        q0 = q0;
    }

    public void set_transitions(int t, int s1, int s2) {
        Tuple tu = new Tuple(s2, t);
        ArrayList<Tuple> tmp = Transitions.get(s1);
        if (tmp == null) {
            tmp = new ArrayList<Tuple>();
        }
        tmp.add(tu);
        Transitions.put(s1, tmp);
    }

    public HashMap<Integer, ArrayList<Tuple>> merge(HashMap<Integer, ArrayList<Tuple>> t1,
                                                    HashMap<Integer, ArrayList<Tuple>> t2) {

        HashMap<Integer, ArrayList<Tuple>> m = t1;
        t2.forEach((key, value) -> m.put(key, value));

        return m;
    }

    public NFA concaten(NFA A2) {
        q.addAll(A2.q); // On rajoute les étas de A2

        set_transitions(0, f, A2.q0);
        HashMap<Integer, ArrayList<Tuple>> m = merge(Transitions, A2.Transitions);
        f = A2.f; // L'état final est celui du deuxieme
        Sigma.add(0);
        for (int i : A2.Sigma) {
            if (Sigma.contains(i) == false) {
                Sigma.add(i);
            }
        }
        // print_transitions();
        // HashSet<Integer> Set = new HashSet<>( Arrays.asList(Sigma));
        return new NFA(q, Sigma, q0, f, m);
    }

    public NFA etoil() {
        int r1 = 0;
        int r2 = 0;
        while (r1 == r2 || r1 == 0 || r2 == 0 || q.contains(r1) || q.contains(r2)) {
            Random rand = new Random();
            r1 = rand.nextInt(1000);
            r2 = rand.nextInt(1000);
        }
        q.add(r1);
        q.add(r2);
        if (Sigma.contains(0) == false) {
            Sigma.add(0);
        }
        set_transitions(0, r1, q0);
        set_transitions(0, f, r2);
        set_transitions(0, r1, r2);
        set_transitions(0, f, q0);
        f = r2;
        q0 = r1;
        return new NFA(q, Sigma, q0, f, Transitions);
    }

    public NFA altern(NFA A2) {
        int r1 = 0;
        int r2 = 0;
        while (r1 == r2 || r1 == 0 || r2 == 0 || q.contains(r1) || q.contains(r2)) {
            Random rand = new Random();
            r1 = rand.nextInt(100);
            r2 = rand.nextInt(100);
        }
        q.add(r1);
        q.add(r2);
        q.addAll(A2.q);
        if (Sigma.contains(0) == false) {
            Sigma.add(0);
        }
        for (int i : A2.Sigma) {
            if (Sigma.contains(i) == false) {
                Sigma.add(i);
            }
        }
        // print_transitions();
        set_transitions(0, r1, q0);
        set_transitions(0, r1, A2.q0);
        set_transitions(0, f, r2);
        set_transitions(0, A2.f, r2);
        HashMap<Integer, ArrayList<Tuple>> m = merge(Transitions, A2.Transitions);
        q0 = r1;
        f = r2;
        return new NFA(q, Sigma, q0, f, m);
    }

    public void print_transitions() {
        for (int key : this.Transitions.keySet()) {
            ArrayList<Tuple> value = Transitions.get(key);
          System.out.print("  " + key + ": ");
            for (Tuple t : value) {
                System.out.print(t.toString() + ", ");
            }
            System.out.println();
        }
        System.out.println("}");
    }

    public void print() {
        System.out.println("{  Q :" + this.getQ());
        System.out.println("  Q0 :" + q0);
        System.out.println("  F :" + f);
        System.out.print("  Sigma : { ");
        for (int i : Sigma) {
            System.out.print(Character.toString((char) i) + " ");
        }
        System.out.println("} ");
        print_transitions();
    }

    public ArrayList<Integer> eclosure(int i) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        res.add(i);
        ArrayList<Tuple> a = Transitions.get(i);
        for (Tuple t : a) {
            if (t.getB() == 0) {
                res.add(t.getA());
            }
        }
        return res;
    }

    public ArrayList<Integer> state_can_travers(int state, int alphabet) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        ArrayList<Tuple> a = Transitions.get(state);
        ArrayList<Integer> tovisit = new ArrayList<Integer>();

        if (a != null) {
            for (Tuple t : a) {
                if (t.getB() == alphabet) {
                    res.add(t.getA());
                    tovisit.add(t.getA());
                    while (tovisit.size() > 0) {
                        if (Transitions.get(tovisit.get(0)) == null) {
                            return res;
                        } else {
                            for (Tuple ti : Transitions.get(tovisit.get(0))) {
                                if (ti.getB() == 0) {
                                    tovisit.add(ti.getA());
                                    res.add(ti.getA());
                                }
                            }
                            tovisit.remove(0);
                            // System.out.println(tovisit);
                        }
                    }
                }
            }
        }
        return res;
    }

    // {[2 3 1] : {[5 6 7]:a , [8 9 10]:b}}

    public DFA to_DFA() {
        ArrayList<Integer> eclosures = eclosure(q0); // On a calculer les e-closures des états
        ArrayList<ArrayList<Integer>> new_states = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> a = new ArrayList<Integer>();
        HashMap<ArrayList<Integer>, HashMap<Integer, ArrayList<Integer>>> new_transitions = new HashMap<ArrayList<Integer>, HashMap<Integer, ArrayList<Integer>>>();
        ArrayList<ArrayList<Integer>> start = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> fi = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> Q = new ArrayList<ArrayList<Integer>>();
        new_states.add(eclosures);
        // System.out.println(eclosures);
        Q.add(eclosures);
        while (new_states.size() > 0) {
            for (int j : Sigma) {
                if (j != 0) {
                    a = new ArrayList<Integer>();
                    for (int i : new_states.get(0)) {
                        ArrayList<Integer> tim = state_can_travers(i, j);
                        if (tim != null) {
                            a.addAll(tim);
                        }
                    }
                }
                if (j != 0) {
                    if (a.size() > 0) {
                        if (!Q.contains(a)) {
                            Q.add(a);
                            new_states.add(a);
                        }
                        HashMap<Integer, ArrayList<Integer>> tmp = new_transitions.get(new_states.get(0));
                        if (tmp == null) {
                            tmp = new HashMap<Integer, ArrayList<Integer>>();
                        }
                        tmp.put(j, a);
                        new_transitions.put(new_states.get(0), tmp);
                    }
                }
            }
            if (new_states.size() > 1) {
                if (new_states.get(0).equals(new_states.get(1))) {
                    return new DFA(Q, Sigma, get_states(Q, q0), get_states(Q, f), new_transitions);
                }
            }
            new_states.remove(0); // On marque l'état d'avant comme deja lu
            // System.out.println(new_states);

        }

        return new DFA(Q, Sigma, get_states(Q, q0), get_states(Q, f), new_transitions);
    }

    public boolean has_start_state(ArrayList<Integer> arr, int s) {
        for (int a : arr) {
            if (a == s) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<ArrayList<Integer>> get_states(ArrayList<ArrayList<Integer>> arri, int s) {
        ArrayList<ArrayList<Integer>> m = new ArrayList<ArrayList<Integer>>();
        for (ArrayList a : arri) {
            if (has_start_state(a, s)) {
                m.add(a);
            }
        }
        return m;
    }

}

class Tuple {
    protected int a;
    protected int b;

    public Tuple(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public void addA(int a) {
        a = a;
    }

    public void addB(int b) {
        b = b;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public String toString() {
        if (b == 0) {
            return "va vers " + a + " avec un epsilon";
        } else {
            return "va vers " + a + " avec un " + Character.toString((char) b);
        }
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

class DFA {
    protected ArrayList<ArrayList<Integer>> q = new ArrayList<ArrayList<Integer>>();
    protected ArrayList<Integer> Sigma = new ArrayList<Integer>();
    protected ArrayList<ArrayList<Integer>> q0;
    protected ArrayList<ArrayList<Integer>> f;
    protected HashMap<ArrayList<Integer>, HashMap<Integer, ArrayList<Integer>>> Transitions = new HashMap<ArrayList<Integer>, HashMap<Integer, ArrayList<Integer>>>();

    public DFA(ArrayList<ArrayList<Integer>> Q, ArrayList<Integer> Sigma, ArrayList<ArrayList<Integer>> Q0,
               ArrayList<ArrayList<Integer>> F,
               HashMap<ArrayList<Integer>, HashMap<Integer, ArrayList<Integer>>> Transitions) {
        this.q = Q;
        this.Sigma = Sigma;
        this.q0 = Q0;
        this.f = F;
        this.Transitions = Transitions;
    }

    // etape 4
    /*public void minDFA() {

        int nbStatesVisited = 1;
        int nbStates = this.Transitions.size();
        ArrayList<Integer> nextState = new ArrayList<Integer>();

        ArrayList<Integer> stateInA = new ArrayList<Integer>();
        ArrayList<Integer> stateInB = new ArrayList<Integer>();

        ArrayList<Integer> stateOutA = new ArrayList<Integer>();
        ArrayList<Integer> stateOutB = new ArrayList<Integer>();

        int idTransiVersA = -1;
        int idTransiVersB = -1;

        // on commence par les états init
        for (ArrayList<Integer> q0State : this.q0) {
            // idTransiVersA = curTrans;
            System.out.println("--------------------------------------------");
            System.out.println("q0State  : " + q0State );

            System.out.println("    truc : " + this.Transitions.get(q0State));
            System.out.println("    truc 2 : " + this.Transitions.get(q0State).keySet());

            // pour chaque etat avec la transition
            for (Integer curIdTrans : this.Transitions.get(q0State).keySet()) {
                System.out.println("    les transistion je me deplase de q0 : " + curIdTrans);
                System.out.println("    truc 3 : " + this.Transitions.get(q0State).get(curIdTrans));

                // pour chaque etats où je peut aller, je vérifie si je lui resemble
                for (ArrayList<Integer> curState : this.Transitions.get(q0State).get(curIdTrans)) {
                    System.out.println("    les etats où je peut aller a partir de q0 : " + curIdTrans);
                }
            }
        }
    }*/

    public void minDFA() {
        /**
         * pour chaque transition de A vers B avec x, on vérifie si A est un etat
         * finale, si B est un etat finale, de-là on supprime la transiton (B,c) et on
         * rajoute la transition (A,c) et si on utilise plus l'etat B alors on le
         * supprime de part tout(q, f, Transitions)
         */

        int nbStatesVisited = 1;
        int nbStates = this.Transitions.size();

        for (ArrayList<Integer> key : this.Transitions.keySet()) {
            nbStatesVisited += 1;
            HashMap<Integer, ArrayList<Integer>> value = Transitions.get(key);

            if (this.f.contains(key)) {
                for (Integer x : value.keySet()) {
                    ArrayList<Integer> valueX = value.get(x);

                    if (!key.equals(valueX) && this.f.contains(valueX)) {
                        ArrayList<Integer> B = Transitions.get(key).get(x);
                        Transitions.get(key).put(x, key);
                        Transitions.get(B).remove(x);

                        if (Transitions.containsKey(B) && Transitions.get(B).isEmpty()) {
                            Transitions.remove(B);
                            this.f.remove(B);
                            this.q.remove(B);
                            nbStates -= 1;
                        }
                    }
                }
            }
            if (nbStatesVisited == nbStates)
                break;
        }
    }

    public void print() {
        System.out.println("{  Q :" + q);
        System.out.println("  Q0 :" + q0);
        System.out.println("  F :" + f);
        System.out.print("  Sigma : { ");
        for (int i : Sigma) {
            System.out.print(Character.toString((char) i) + " ");
        }
        System.out.println("} ");
        System.out.println(Transitions);
    }
}