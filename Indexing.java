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


public class Indexing{

    public static void main(String[] args) {
        /*ArrayList<String> s = new ArrayList<String>();
        s.add("bonjour je m'appele jean-francois");
        s.add("je suis tres jeune");
        s.add("j'ecris des mails avec bonjour haha");
        //System.out.println(toHashMap(s));
        System.out.println(sortByValue(toHashInt(s)));
        //System.out.println(sort(s));
        try{
            toFile(s);
        }catch(Exception e){
            System.out.println("erreur");
        }*/
        /*Trie trie = new Trie();
        ArrayList<Integer> ar1 = new ArrayList<Integer>();
        ar1.add(1);
        ar1.add(1);
        ArrayList<Integer> ar2 = new ArrayList<Integer>();
        ar2.add(3);
        ar2.add(24);
        ArrayList<ArrayList<Integer>> ar4= new  ArrayList<ArrayList<Integer>> ();
        ArrayList<ArrayList<Integer>> ar3= new  ArrayList<ArrayList<Integer>> ();
        ar3.add(ar1);
        ar3.add(ar2);
        ar4.add(ar2);
        trie.insert("bonjour",ar3);
        trie.insert("bon",ar4);
        System.out.println(trie.search("bo"));
        File f = new File("cash");
        trie.insertFromFile(f);*/
        File file = new File("text1");
        File cash = new File("cache_text1");
        try{
            makeCash(FileToStrings(file),"text1");
        }catch(Exception e){
            System.out.println("ERREUR" + e);
        }
        Trie t = trieFromFile(cash);
        System.out.println(t.search("Sargon"));

    }

    public static void makeCash(ArrayList<String> s, String filename) throws Exception{
        HashMap<String,ArrayList<ArrayList<Integer>>> occurences = toHashMap(s);
        PrintWriter writer = new PrintWriter("cache_" + filename, "UTF-8");
        
        List ListofKeys = new ArrayList(sortByValue(toHashInt(s)).keySet());
        for (int i=0; i<ListofKeys.size();i++){
            writer.println((String)ListofKeys.get(i) +" "+ occurences.get((String) ListofKeys.get(i) ));
        }

        writer.close();

    }

    public static void makeCash2(ArrayList<String> s, String filename) throws Exception{
        HashMap<String,ArrayList<ArrayList<Integer>>> occurences = toHashMap2(s);
        PrintWriter writer = new PrintWriter("cache_" + filename, "UTF-8");
        
        List ListofKeys = new ArrayList(sortByValue(toHashInt(s)).keySet());
        for (int i=0; i<ListofKeys.size();i++){
            writer.println((String)ListofKeys.get(i) +" "+ occurences.get((String) ListofKeys.get(i) ));
        }

        writer.close();

    }


    public static HashMap<String, Integer> toHashInt (ArrayList<String> s){
    HashMap<String, Integer> hint= new HashMap<String, Integer>();
        Iterator it = toHashMap(s).entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                hint.put((String)pair.getKey(),((ArrayList<ArrayList<Integer>>)pair.getValue()).size());
            }
        return hint;
    }
    
    public static HashMap<String, Integer> toHashInt2 (ArrayList<String> s){
    HashMap<String, Integer> hint= new HashMap<String, Integer>();
        Iterator it = toHashMap2(s).entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                hint.put((String)pair.getKey(),((ArrayList<ArrayList<Integer>>)pair.getValue()).size());
            }
        return hint;
    }


     public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) 
    { 
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Integer> > list = 
               new LinkedList<Map.Entry<String, Integer> >(hm.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
            public int compare(Map.Entry<String, Integer> o1,  
                               Map.Entry<String, Integer> o2) 
            { 
                return (o1.getValue()).compareTo(o2.getValue()); 
            } 
        }); 
          
        // put data from sorted list to hashmap  
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
        for (Map.Entry<String, Integer> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    } 

    public static HashMap<String,ArrayList<ArrayList<Integer>>> toHashMap2(ArrayList<String> lines){
        
        HashMap<String,ArrayList<ArrayList<Integer>>> occurences = new HashMap<String,ArrayList<ArrayList<Integer>>>();
        
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

        for(int j=0; j<lines.size(); j++){
            String s = lines.get(j); 
            cpt = 1;
            for (int i=0; i<s.length();i++){
                    char c = s.charAt(i);
                    if(Character.isDigit(c)){
                        break;
                    }
                    if (separators.contains(c) || i==s.length()-1){
                        String mot = "";
                        ArrayList<Integer> tuple = new ArrayList<Integer>();
                        if (separators.contains(c)==false && i==s.length()-1){
                            mot = s.substring(i-(cpt-1),i+1);
                            tuple.add(j+1); //numero de ligne
                            //System.out.println();
                            tuple.add((i+1)-(cpt-1));
                        }else{
                            //System.out.println(i);
                            mot = s.substring(i-(cpt-1),i);
                            tuple.add(j+1); //numero de ligne
                           // System.out.println();
                            tuple.add(i-(cpt-2));

                        }
                    //System.out.println(mot);
                    String mot2 = "";
                    for (int l = 0;l<mot.length();l++){
                        Character c2 = mot.charAt(l);
                        mot2 = mot2 + c2;
                    }
                    ArrayList<ArrayList<Integer>> t = occurences.get(mot2);
                    if (t == null){
                         t = new ArrayList<ArrayList<Integer>>();
                    }

                    t.add(tuple);
                    occurences.put(mot2,t);
                    cpt = 1;
                    //System.out.println(occurences);
                }
                else {
                    //System.out.println(cpt);
                    cpt++;
                }
            }
        }
        return occurences;
    }

    public static HashMap<String,ArrayList<ArrayList<Integer>>> toHashMap(ArrayList<String> lines){
        
        HashMap<String,ArrayList<ArrayList<Integer>>> occurences = new HashMap<String,ArrayList<ArrayList<Integer>>>();
        
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

        for(int j=0; j<lines.size(); j++){
            String s = lines.get(j); 
            cpt = 1;
            for (int i=0; i<s.length();i++){
                    char c = s.charAt(i);
                    if(Character.isDigit(c)){
                        break;
                    }
                    if (separators.contains(c) || i==s.length()-1){
                        String mot = "";
                        ArrayList<Integer> tuple = new ArrayList<Integer>();
                        if (separators.contains(c)==false && i==s.length()-1){
                            mot = s.substring(i-(cpt-1),i+1);
                            tuple.add(j+1); //numero de ligne
                            //System.out.println();
                            tuple.add((i+1)-(cpt-1));
                        }else{
                            //System.out.println(i);
                            mot = s.substring(i-(cpt-1),i);
                            tuple.add(j+1); //numero de ligne
                           // System.out.println();
                            tuple.add(i-(cpt-2));

                        }
                    //System.out.println(mot);
                    String mot2 = "";
                    for (int l = 0;l<mot.length();l++){
                        Character c2 = mot.charAt(l);
                        mot2 = mot2 + Character.toLowerCase(c2);
                    }
                    ArrayList<ArrayList<Integer>> t = occurences.get(mot2);
                    if (t == null){
                         t = new ArrayList<ArrayList<Integer>>();
                    }

                    t.add(tuple);
                    occurences.put(mot2,t);
                    cpt = 1;
                    //System.out.println(occurences);
                }
                else {
                    //System.out.println(cpt);
                    cpt++;
                }
            }
        }
        return occurences;
    }

    public static ArrayList<String> FileToStrings(File fileName){
        try {
            String line = null;
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Trie t = new Trie();
            ArrayList<String> strings = new ArrayList<String>();

            while((line = bufferedReader.readLine()) != null) {
                strings.add(line);
            }
            return strings;

        }
        catch(Exception e){
            return null;
        }
    }

    public static Trie trieFromFile(File fileName){
        try {
            String line = null;
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Trie t = new Trie();

            while((line = bufferedReader.readLine()) != null) {
                char c = line.charAt(0);
                int i=0;
                String mot = "";  
                while(c!='['){
                    c = line.charAt(i++);
                    mot = mot + c;    
                }
                //System.out.println(mot);
                mot = mot.substring(0,i-2);

                ArrayList a = new ArrayList<ArrayList<Integer>>();
                char d = line.charAt(0);
                String tuple = "";
                i++;
                while(i != line.length()-1){
                    d = line.charAt(i++);
                    if (d == '['){
                        tuple = "";
                    }
                    if (d==']'){
                        List<String> myList = new ArrayList<String>(Arrays.asList(tuple.split(",")));
                        a.add(myList);
                    }
                    if(d!='[' && d!=']'){
                        tuple = tuple + d;  
                    }
                    
                }
                t.insert(mot,a);
                //System.out.println("salt");
                //System.out.println(mot + t.search("m'appele"));
                //System.out.println(mot + t.search(mot));

            }   

            //System.out.println("OK so");
            //System.out.println(t.search("bonjour"));
            // Always close files.
            bufferedReader.close();
            return t;      
        }
        catch(Exception ex) {
            System.out.println("je rentre ici" + ex);
            return null;               
        }        


    }

}

class TrieNode {
    private Character c;
    private HashMap<Character, TrieNode> children = new HashMap<>();
    private ArrayList<ArrayList<Integer>> wordOccurences;

    public TrieNode() {}

    public TrieNode(Character c){
        this.c = c;
        wordOccurences = null;
    }

    public Character getContent(){
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
    /*private HashMap<String, TrieNode> children = new HashMap<>();
    private ArrayList<ArrayList<Integer>> wordOccurences;

    public TrieNode() {}

    public TrieNode(String c){
        this.c = c;
        wordOccurences = null;
    }

    public String getContent(){
        return c;
    }

    public HashMap<String, TrieNode> getChildren() {
        return this.children;
    }

    public void setChildren(HashMap<String, TrieNode> children) {
        this.children = children;
    }

    public ArrayList<ArrayList<Integer>> getWordOccurences() {
        return this.wordOccurences;
    }

    public void setWordOc(ArrayList<ArrayList<Integer>> wo) {
        this.wordOccurences = wo;
    }*/
}

class Trie {

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    /*public Stirng upToHere(String s,HashMap<String, TrieNode> children){
        Iterator it = mp.entrySet().iterator();
        int cpt = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(pair.getKey().length() < s.length()){
                for(int i = 0; i <s.length(); i++){
                    if (pair.getKey().charAt(i)==s.charAt(i)){
                        cpt++;
                    }
                }
            }
            else {
                for(int i = 0; i < pair.getKey().length(); i++){
                    if (pair.getKey().charAt(i)==s.charAt(i)){
                        cpt++;
                    }itories 6 follow
                }
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return cpt;
    }

    public void insert(String word, ArrayList<ArrayList<Integer>> occ) {
        HashMap<Character, TrieNode> children = root.getChildren();
        //System.out.println("AHAHAHAHAH");
        //System.out.println(word);
        //System.out.println(occ);
        String node_bef = "";
        int cpt = upToHere(word,children)
        if(cpt < word.length()){
            node = node.getChildren().get();

        }
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            TrieNode node;
            if(children.containsKey(c)) {
                node = children.get(c);
            } else { 
                node = new TrieNode(node_bef+c);
                node_bef = node.getContent();
                System.out.println(node_bef);
                children.put(c, node);
            }
            children = node.getChildren();

            if(i == word.length() - 1) {
               //System.out.println("!!!!!!!" + occ);
                node.setWordOc(occ);
            }

        }
    }

    public ArrayList<ArrayList<Integer>> search(String word) {
        HashMap<Character, TrieNode> children = root.getChildren();
        //System.out.println(children);
        TrieNode node = null;
         String mot = "";
        for (int i = 0;i<word.length();i++){
            Character c = word.charAt(i);
            mot = mot + Character.toLowerCase(c);
        }
        for(int i = 0; i < mot.length(); i++) {
            char c = mot.charAt(i);
            if(children.containsKey(c)) {
                node = children.get(c);
                children = node.getChildren();
            } else { 
                node = null;
                break;
            }
        }
        if(node != null && node.getWordOccurences()!=null) {
            return node.getWordOccurences();
        } else {
            return null;
        }
    }*/
    

    public void insert(String word, ArrayList<ArrayList<Integer>> occ) {
        HashMap<Character, TrieNode> children = root.getChildren();
        //System.out.println("AHAHAHAHAH");
        //System.out.println(word);
        //System.out.println(occ);
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            TrieNode node;
            if(children.containsKey(c)) {
                node = children.get(c);
            } else { 
                node = new TrieNode(c);
                //System.out.println(node_bef);
                children.put(c, node);
            }
            children = node.getChildren();

            if(i == word.length() - 1) {
               //System.out.println("!!!!!!!" + occ);
                node.setWordOc(occ);
            }

        }
    }

    public ArrayList<ArrayList<Integer>> search2(String mot) {
        HashMap<Character, TrieNode> children = root.getChildren();
        //System.out.println(children);
        ArrayList<Character> a = new ArrayList<Character>();
        TrieNode node = null;
       
        for(int i = 0; i < mot.length(); i++) {
            char c = mot.charAt(i);
            if(children.containsKey(c)) {
                node = children.get(c);
                children = node.getChildren();
            } else { 
                node = null;
                break;
            }
        }
        if(node != null && node.getWordOccurences()!=null && node.getChildren().size()==0 ) {
            return node.getWordOccurences();
        } else {
            if(node!=null && node.getChildren().size()>0){
                ArrayList<ArrayList<Integer>> wo= new ArrayList<ArrayList<Integer>>();
                if(node.getWordOccurences()!=null)
                    wo.addAll(node.getWordOccurences());
                for (Character c : node.getChildren().keySet()) {
                    wo.addAll(search(mot + Character.toString(c)));
                }
                return wo;
            }
            return null;
            }
        }

    public ArrayList<ArrayList<Integer>> search(String word) {
               // System.out.println("CAFEEEEEEE" );

        HashMap<Character, TrieNode> children = root.getChildren();
        //System.out.println(children);
        ArrayList<Character> a = new ArrayList<Character>();
        TrieNode node = null;
         String mot = "";
        for (int i = 0;i<word.length();i++){
            Character c = word.charAt(i);
            mot = mot + Character.toLowerCase(c);
        }
        //System.out.println("CAFEEEEEEE" + mot);
        for(int i = 0; i < mot.length(); i++) {
            char c = mot.charAt(i);
            if(children.containsKey(c)) {
                node = children.get(c);
                children = node.getChildren();
            } else { 
                node = null;
                break;
            }
        }
        if(node != null && node.getWordOccurences()!=null && node.getChildren().size()==0 ) {
            return node.getWordOccurences();
        } else {
            if(node!=null && node.getChildren().size()>0){
                ArrayList<ArrayList<Integer>> wo= new ArrayList<ArrayList<Integer>>();
                if(node.getWordOccurences()!=null)
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
