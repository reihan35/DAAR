import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Exception;
import java.util.Arrays;
import java.util.Random;
import java.util.HashSet;
import java.util.*;
import java.io.*;
import java.util.regex.Pattern;

/**
 * Ce fichier continent les classs suivant :
 *   Indexing : qui implement la méthode de recherche à partir d'un cache
 *   TrieNode : corespond à chaquqe noeud de notre trie
 *   Trie : représente la structure de notre arbre
 */
public class Indexing {
    /**
     * CLASS AVEC LA METHODE DE RECHERCHE DE RADIX TREE
     */

    public static void main(String[] args) {
        /*File file = new File("text1");
        File cash = new File("cache_text1");
        try{
            makeCash(FileToStrings(file),"text1");
        }catch(Exception e){
            System.out.println("ERREUR" + e);
        }
        Trie t = trieFromFile(cash);
        System.out.println(t.search("Sargon"));*/

    }

    public static void makeCash(ArrayList<String> s, String filename) throws Exception {
        /**
         * Cette fonction prend en entree la liste des mots du texte l'ordonne en ordre corissant et ecrit le résultat dans fichier qui sera le cache
         *
         * s : Liste de mots du texte
         * filename : fichier qui correspend au cache
         */
        HashMap<String, ArrayList<ArrayList<Integer>>> occurences = toHashMap(s);
        PrintWriter writer = new PrintWriter(filename + "_cache", "UTF-8");

        List ListofKeys = new ArrayList(sortByValue(toHashInt(s)).keySet());
        for (int i = 0; i < ListofKeys.size(); i++) {
            writer.println((String) ListofKeys.get(i) + " " + occurences.get((String) ListofKeys.get(i)));
        }

        writer.close();

    }

    public static HashMap<String, Integer> toHashInt(ArrayList<String> s) {
        /**
         * Cette fonction renvoie une hashmap avec les mots comme clé et le nombre d'occurences en valeur
         *
         * s : Liste de mots
         */
        HashMap<String, Integer> hint = new HashMap<String, Integer>();
        Iterator it = toHashMap(s).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            hint.put((String) pair.getKey(), ((ArrayList<ArrayList<Integer>>) pair.getValue()).size());
        }
        return hint;
    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        /**
         * Cette fonction ordonne les élements d'une hashmap en fonction de leur valeur
         *
         * hm : hashmap<mots, occurence>
         */

        // Faire une liste à partir des element du hashmap
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

        // l'ordonne 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // mettre les resultat de la liste ordonee dans la hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static HashMap<String, ArrayList<ArrayList<Integer>>> toHashMap(ArrayList<String> lines) {
        /**
         * Cette fonction parcours la liste des linges du fichier la pars en hashmap de mots du fichier : la liste de ses position qui correspond au num de ligne et celui de colonne
         *
         * lines : cache
         */

        HashMap<String, ArrayList<ArrayList<Integer>>> occurences = new HashMap<String, ArrayList<ArrayList<Integer>>>();

        int index = 0;
        int cpt = 1;
        ArrayList<Character> separators = new ArrayList<Character>();
        separators.add(' ');
        separators.add(',');
        separators.add('+');
        separators.add('*');
        separators.add('"');
        separators.add('.');
        separators.add(':');
        separators.add(';');
        separators.add('#');
        separators.add('(');
        separators.add(')');
        separators.add('[');
        separators.add(']');
        separators.add('=');
        separators.add('_');
        separators.add('&');
        separators.add('\'');
        separators.add('-');

        for (int j = 0; j < lines.size(); j++) {
            String s = lines.get(j);
            cpt = 1;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (Character.isDigit(c)) {
                    break;
                }
                if (separators.contains(c) || i == s.length() - 1) {
                    String mot = "";
                    ArrayList<Integer> tuple = new ArrayList<Integer>();
                    if (separators.contains(c) == false && i == s.length() - 1) {
                        mot = s.substring(i - (cpt - 1), i + 1);
                        tuple.add(j + 1); //numero de ligne
                        tuple.add((i + 1) - (cpt - 1));
                    } else {
                        mot = s.substring(i - (cpt - 1), i);
                        tuple.add(j + 1); //numero de ligne
                        tuple.add(i - (cpt - 2));

                    }
                    String mot2 = "";
                    for (int l = 0; l < mot.length(); l++) {
                        Character c2 = mot.charAt(l);
                        mot2 = mot2 + Character.toLowerCase(c2);
                    }
                    ArrayList<ArrayList<Integer>> t = occurences.get(mot2);
                    if (t == null) {
                        t = new ArrayList<ArrayList<Integer>>();
                    }

                    t.add(tuple);
                    occurences.put(mot2, t);
                    cpt = 1;
                } else {
                    cpt++;
                }
            }
        }
        return occurences;
    }

    public static ArrayList<String> FileToStrings(File fileName) {
        /**
         * Cette fonction renvoie la liste des lignes d'un fichier
         *
         * filename : Fichier
         */
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

    public static Trie trieFromFile(File fileName) {
        /**
         * Cette fonction cree le trie à partir du fichier cache
         *
         * fileName: cache
         */

        try {
            String line = null;
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Trie t = new Trie();

            while ((line = bufferedReader.readLine()) != null) {
                char c = line.charAt(0);
                int i = 0;
                String mot = "";
                while (c != '[') {
                    c = line.charAt(i++);
                    mot = mot + c;
                }
                mot = mot.substring(0, i - 2);

                ArrayList a = new ArrayList<ArrayList<Integer>>();
                char d = line.charAt(0);
                String tuple = "";
                i++;
                while (i != line.length() - 1) {
                    d = line.charAt(i++);
                    if (d == '[') {
                        tuple = "";
                    }
                    if (d == ']') {
                        List<String> myList = new ArrayList<String>(Arrays.asList(tuple.split(",")));
                        a.add(myList);
                    }
                    if (d != '[' && d != ']') {
                        tuple = tuple + d;
                    }

                }
                t.insert(mot, a);
            }
            bufferedReader.close();
            return t;
        } catch (Exception ex) {
            System.out.println("je rentre ici" + ex);
            return null;
        }


    }

}


class TrieNode {
    /**
     *Cette classe corespond à chaquqe noeud de notre trie
     *
     */

    private Character c;
    private HashMap<Character, TrieNode> children = new HashMap<>();
    private ArrayList<ArrayList<Integer>> wordOccurences; //L'occurences du mot dans le texte si null c'est que noeud ne correspond pas à fin de mot

    public TrieNode() {
    }

    public TrieNode(Character c) {
        this.c = c;
        wordOccurences = null;
    }

    public Character getContent() {
        return c;
    }

    public HashMap<Character, TrieNode> getChildren() {
        return this.children;
    }

    public void setChildren(HashMap<Character, TrieNode> children) {
        this.children = children;
    }

    public ArrayList<ArrayList<Integer>> getWordOccurences() {
        return this.wordOccurences;
    }

    public void setWordOc(ArrayList<ArrayList<Integer>> wo) {
        this.wordOccurences = wo;
    }

}

class Trie {
    /**
     * Structure de notre arbre
     */

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    //Insertion d'un noeud dans le trie
    public void insert(String word, ArrayList<ArrayList<Integer>> occ) {
        HashMap<Character, TrieNode> children = root.getChildren();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            TrieNode node;
            if (children.containsKey(c)) {
                node = children.get(c);
            } else {
                node = new TrieNode(c);
                children.put(c, node);
            }
            children = node.getChildren();

            if (i == word.length() - 1) {
                node.setWordOc(occ);
            }

        }
    }

    //Recherche d'un mot dans le trie sans prefixe
    public ArrayList<ArrayList<Integer>> searchEntier(String word) {

        HashMap<Character, TrieNode> children = root.getChildren();
        ArrayList<Character> a = new ArrayList<Character>();
        TrieNode node = null;
        String mot = "";
        for (int i = 0; i < word.length(); i++) {
            Character c = word.charAt(i);
            mot = mot + Character.toLowerCase(c);
        }
        for (int i = 0; i < mot.length(); i++) {
            char c = mot.charAt(i);
            if (children.containsKey(c)) {
                node = children.get(c);
                children = node.getChildren();
            } else {
                node = null;
                break;
            }
        }
        if (node != null && node.getWordOccurences() != null) {
            return node.getWordOccurences();
        } else {

            return null;

        }

    }

    //Recherche d'un mot dans le trie avec prefixe
    public ArrayList<ArrayList<Integer>> search(String word) {
        System.out.println("je rentre dans search");

        HashMap<Character, TrieNode> children = root.getChildren();
        ArrayList<Character> a = new ArrayList<Character>();
        TrieNode node = null;
        String mot = "";
        for (int i = 0; i < word.length(); i++) {
            Character c = word.charAt(i);
            mot = mot + Character.toLowerCase(c);
        }
        for (int i = 0; i < mot.length(); i++) {
            char c = mot.charAt(i);
            if (children.containsKey(c)) {
                node = children.get(c);
                children = node.getChildren();
            } else {
                node = null;
                break;
            }
        }
        if (node != null && node.getWordOccurences() != null && node.getChildren().size() == 0) {
            return node.getWordOccurences();
        } else {
            if (node != null && node.getChildren().size() > 0) {
                ArrayList<ArrayList<Integer>> wo = new ArrayList<ArrayList<Integer>>();
                System.out.println("je comprend bien qu'il faut venir ici");
                if (node.getWordOccurences() != null)
                    wo.addAll(node.getWordOccurences());
                for (Character c : node.getChildren().keySet()) {
                    wo.addAll(search(mot + Character.toString(c)));
                }
                return wo;
            }
            return null;
        }
    }
}
