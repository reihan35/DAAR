
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Exception;
import java.util.Arrays;
import java.util.Random;
import java.util.HashSet;
import java.util.*;

public class RegEx {
    //MACROS
    static final int CONCAT = 0xC04CA7;
    static final int ETOILE = 0xE7011E;
    static final int ALTERN = 0xA17E54;
    static final int PROTECTION = 0xBADDAD;

    static final int PARENTHESEOUVRANT = 0x16641664;
    static final int PARENTHESEFERMANT = 0x51515151;
    static final int DOT = 0xD07;

    //REGEX
    private static String regEx;

    //CONSTRUCTOR
    public RegEx() {
    }

/*
    public static void main(String arg[]) {
        ArrayList<Integer> Q = new ArrayList<Integer>();
        Q.add(0);
        Q.add(1);
        Q.add(2);
        Q.add(3);
        Q.add(4);
        Q.add(5);
        Q.add(6);
        Q.add(7);
        Q.add(8);
        Q.add(9);
        ArrayList<Integer> Sigma = new ArrayList<Integer>();
        Sigma.add((int) 'a');
        Sigma.add((int) 'b');
        Sigma.add((int) 'c');
        Sigma.add(0);
        //Pour 0
        // pour l'état 0: va vers 1 avec epsi ((int)0)
        Tuple t01 = new Tuple(1, (int) 0);
        // pour l'état 0: va vers 4 avec epsi ((int)0)
        Tuple t04 = new Tuple(4, (int) 0);
        // a0 representer les transition à partir de l'état 0
        ArrayList<Tuple> a0 = new ArrayList<Tuple>();
        a0.add(t01);
        a0.add(t04);

        //Pour 1
        Tuple t12 = new Tuple(2, (int) 'a');
        ArrayList<Tuple> a1 = new ArrayList<Tuple>();
        a1.add(t12);
        //Pour 2
        Tuple t23 = new Tuple(3, (int) 0);
        ArrayList<Tuple> a2 = new ArrayList<Tuple>();
        a2.add(t23);
        //Pour 4
        Tuple t45 = new Tuple(5, (int) 'b');
        ArrayList<Tuple> a4 = new ArrayList<Tuple>();
        a4.add(t45);
        //pour 5
        Tuple t56 = new Tuple(6, (int) 0);
        ArrayList<Tuple> a5 = new ArrayList<Tuple>();
        a5.add(t56);
        //pour 6
        Tuple t67 = new Tuple(7, (int) 0);
        Tuple t69 = new Tuple(9, (int) 0);
        ArrayList<Tuple> a6 = new ArrayList<Tuple>();
        a6.add(t67);
        a6.add(t69);
        //pour 7
        Tuple t78 = new Tuple(8, (int) 'c');
        ArrayList<Tuple> a7 = new ArrayList<Tuple>();
        a7.add(t78);
        // pour 8
        Tuple t87 = new Tuple(7, (int) 0);
        Tuple t89 = new Tuple(9, (int) 0);
        ArrayList<Tuple> a8 = new ArrayList<Tuple>();
        a8.add(t87);
        a8.add(t89);
        // pour 9
        Tuple t93 = new Tuple(3, (int) 0);
        ArrayList<Tuple> a9 = new ArrayList<Tuple>();
        a9.add(t93);

        HashMap<Integer, ArrayList<Tuple>> Transitions = new HashMap<Integer, ArrayList<Tuple>>();
        Transitions.put(0, a0);
        Transitions.put(1, a1);
        Transitions.put(2, a2);
        Transitions.put(4, a4);
        Transitions.put(5, a5);
        Transitions.put(6, a6);
        Transitions.put(7, a7);
        Transitions.put(8, a8);
        Transitions.put(9, a9);


        NFA nfa = new NFA(Q, Sigma, 0, 3, Transitions);
        nfa.print();
        System.out.println(nfa.eclosure(0));
        //System.out.println(nfa.state_can_travers(4,(int)'b'));
        System.out.println("...................................................;....");
        System.out.println("DFA");
        nfa.to_DFA().print();
        System.out.println();
        System.out.println("...................................................;....");
        System.out.println("minDFA");
        DFA dfa = nfa.to_DFA();
        dfa.minDFA();
        dfa.print();
        System.out.println();
        System.out.println("...................................................;....");
        System.out.println();
        dfa.search("accabcbccc");
    }
    /*public static void main1(String arg[]) {
        ArrayList<Integer> Q = new ArrayList<Integer>();
        Q.add(0);
        Q.add(1);
        Q.add(2);
        Q.add(3);
        Q.add(4);
        Q.add(5);
        Q.add(6);

        ArrayList<Integer> Sigma = new ArrayList<Integer>();
        Sigma.add((int) 'a');
        Sigma.add((int) 'b');
        Sigma.add((int) 'c');

        //Pour 0
        // pour l'état 0: va vers 0 avec b ((int)'b')
        Tuple t0c = new Tuple(0, (int) 'c');
        Tuple t0b = new Tuple(0, (int) 'b');
        Tuple t0a = new Tuple(1, (int) 'a');
        // a0 represente les transitions à partir de l'état 0
        ArrayList<Tuple> a0 = new ArrayList<Tuple>();
        a0.add(t0c);
        a0.add(t0b);
        a0.add(t0a);

        //Pour 1
        Tuple t1a = new Tuple(1, (int) 'a');
        Tuple t1b = new Tuple(2, (int) 'b');
        Tuple t1c = new Tuple(0, (int) 'c');
        ArrayList<Tuple> a1 = new ArrayList<Tuple>();
        a1.add(t1a);
        a1.add(t1b);
        a1.add(t1c);

        //Pour 2
        Tuple t2a = new Tuple(3, (int) 'a');
        Tuple t2b = new Tuple(0, (int) 'b');
        Tuple t2c = new Tuple(0, (int) 'c');
        ArrayList<Tuple> a2 = new ArrayList<Tuple>();
        a2.add(t2a);
        a2.add(t2b);
        a2.add(t2c);

        //Pour 3
        Tuple t3b = new Tuple(1, (int) 'a');
        Tuple t3a = new Tuple(4, (int) 'b');
        Tuple t3c = new Tuple(0, (int) 'c');
        ArrayList<Tuple> a3 = new ArrayList<Tuple>();
        a3.add(t3a);
        a3.add(t3b);
        a3.add(t3c);

        //Pour 4
        Tuple t4a = new Tuple(5, (int) 'a');
        Tuple t4b = new Tuple(0, (int) 'b');
        Tuple t4c = new Tuple(0, (int) 'b');
        ArrayList<Tuple> a4 = new ArrayList<Tuple>();
        a4.add(t4a);
        a4.add(t4b);
        a4.add(t4c);

        //pour 5
        Tuple t5a = new Tuple(1, (int) 'a');
        Tuple t5b = new Tuple(4, (int) 'b');
        Tuple t5c = new Tuple(6, (int) 'c');
        ArrayList<Tuple> a5 = new ArrayList<Tuple>();
        a5.add(t5a);
        a5.add(t5b);
        a5.add(t5c);


        HashMap<Integer, ArrayList<Tuple>> Transitions = new HashMap<Integer, ArrayList<Tuple>>();
        Transitions.put(0, a0);
        Transitions.put(1, a1);
        Transitions.put(2, a2);
        Transitions.put(4, a4);
        Transitions.put(5, a5);

        NFA nfa = new NFA(Q, Sigma, 0, 6, Transitions);
        nfa.print();
        System.out.println(nfa.eclosure(0));
        //System.out.println(nfa.state_can_travers(4,(int)'b'));
        System.out.println("...................................................;....");
        System.out.println("DFA");
        nfa.to_DFA().print();
        System.out.println();
        System.out.println("...................................................;....");
        System.out.println("minDFA");
        DFA dfa = nfa.to_DFA();
        dfa.minDFA();
        dfa.print();
        System.out.println();
        System.out.println("...................................................;....");
        System.out.println();
        dfa.search("accabcbccc");
    }

    /*
    public static void main(String arg[]) {
        ArrayList<Integer> Q = new ArrayList<Integer>();
        Q.add(0);
        Q.add(1);
        Q.add(2);
        Q.add(3);
        Q.add(4);
        Q.add(5);
        Q.add(6);

        ArrayList<Integer> Sigma = new ArrayList<Integer>();
        Sigma.add((int)'a');
        Sigma.add((int)'b');
        Sigma.add(0);
        //Pour 0
        // pour l'état 0: va vers 1 avec epsi ((int)0)
        Tuple t01 = new Tuple(1,(int)0);
        // pour l'état 0: va vers 4 avec epsi ((int)0)
        Tuple t04 = new Tuple(4,(int)0);
        // a0 representer les transition à partir de l'état 0
        ArrayList<Tuple> a0 = new ArrayList<Tuple>();
        a0.add(t01);
        a0.add(t04);

        //Pour 1
        Tuple t12 = new Tuple(2,(int)'a');
        ArrayList<Tuple> a1 = new ArrayList<Tuple>();
        a1.add(t12);

        //Pour 2
        Tuple t23 = new Tuple(3,(int)0);
        ArrayList<Tuple> a2 = new ArrayList<Tuple>();
        a2.add(t23);

        //Pour 4
        Tuple t45 = new Tuple(5,(int)'b');
        ArrayList<Tuple> a4 = new ArrayList<Tuple>();
        a4.add(t45);

        //pour 5
        Tuple t56 = new Tuple(6,(int)0);
        ArrayList<Tuple> a5 = new ArrayList<Tuple>();
        a5.add(t56);


        HashMap<Integer,ArrayList<Tuple>> Transitions = new HashMap<Integer, ArrayList<Tuple>>();
        Transitions.put(0,a0); // les transitions qu'on peut faire à partir de l'état 0
        Transitions.put(1,a1);
        Transitions.put(2,a2);
        Transitions.put(4,a4);
        Transitions.put(5,a5);


        NFA nfa = new NFA(Q,Sigma,0,3,Transitions);
        nfa.print();
        System.out.println(nfa.eclosure(0));
        System.out.println(nfa.state_can_travers(4,(int)'b'));
        nfa.to_DFA().print();
    }
    */
  
  //MAIN
   /* private static String readLineByLineJava8(String filePath)
{
    StringBuilder contentBuilder = new StringBuilder();
    try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
    {
        stream.forEach(s -> contentBuilder.append(s).append("\n"));
    }
    catch (IOException e)
    {
        e.printStackTrace();
    }
    return contentBuilder.toString();
}*/
 
  public static void main(String arg[]) throws Exception{
    System.out.println("Welcome to Bogota, Mr. Thomas Anderson.");
    if (arg.length!=0) {
      regEx = arg[0];
    } else {
      Scanner scanner = new Scanner(System.in);
      System.out.print("  >> Please enter a regEx: ");
      regEx = scanner.next();
    }
    System.out.println("  >> Parsing regEx \""+regEx+"\".");
    System.out.println("  >> ...");
    
    if (regEx.length()<1) {
      System.err.println("  >> ERROR: empty regEx.");
    } else {
      System.out.print("  >> ASCII codes: ["+(int)regEx.charAt(0));
      for (int i=1;i<regEx.length();i++) System.out.print(","+(int)regEx.charAt(i));
      System.out.println("].");
      try {
        RegExTree ret = parse();
        System.out.println("  >> Tree result: "+ret.toString()+".");
        System.out.println("  >> Here is the NFA of the tree : ");
        NFA n = ret.toAutomaton();
        n.print();
        System.out.println("  >> Here is the DFA of the tree : ");
        DFA d = n.to_DFA();
        d.print();
        System.out.println("  >> Here is the DFA min of the tree : ");
        d.minDFA();
        d.print();
        Scanner scanner = new Scanner(System.in);
        System.out.print("  >> Please enter the name of your file: ");
        /*String file = scanner.next();
        d.search(readLineByLineJava8(file));
        String file = scanner.next();*/
        d.search("abc");
        } catch (Exception e) {
        System.err.println("  >> ERROR: syntax error for regEx \""+regEx+"\".");
      }
    }

    System.out.println("  >> ...");
    System.out.println("  >> Parsing completed.");
    System.out.println("Goodbye Mr. Anderson.");
  }

    //FROM REGEX TO SYNTAX TREE
    private static RegExTree parse() throws Exception {
        //BEGIN DEBUG: set conditionnal to true for debug example
        if (false) throw new Exception();
        RegExTree example = exampleAhoUllman();
        if (false) return example;
        //END DEBUG

        ArrayList<RegExTree> result = new ArrayList<RegExTree>();
        for (int i = 0; i < regEx.length(); i++)
            result.add(new RegExTree(charToRoot(regEx.charAt(i)), new ArrayList<RegExTree>()));

        return parse(result);
    }

    private static int charToRoot(char c) {
        if (c == '.') return DOT;
        if (c == '*') return ETOILE;
        if (c == '|') return ALTERN;
        if (c == '(') return PARENTHESEOUVRANT;
        if (c == ')') return PARENTHESEFERMANT;
        return (int) c;
    }

    private static RegExTree parse(ArrayList<RegExTree> result) throws Exception {
        while (containParenthese(result)) result = processParenthese(result);
        while (containEtoile(result)) result = processEtoile(result);
        while (containConcat(result)) result = processConcat(result);
        while (containAltern(result)) result = processAltern(result);

        if (result.size() > 1) throw new Exception();

        return removeProtection(result.get(0));
    }

    private static boolean containParenthese(ArrayList<RegExTree> trees) {
        for (RegExTree t : trees) if (t.root == PARENTHESEFERMANT || t.root == PARENTHESEOUVRANT) return true;
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
                    } else content.add(0, result.remove(result.size() - 1));
                if (!done) throw new Exception();
                found = true;
                ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
                subTrees.add(parse(content));
                result.add(new RegExTree(PROTECTION, subTrees));
            } else {
                result.add(t);
            }
        }
        if (!found) throw new Exception();
        return result;
    }

    private static boolean containEtoile(ArrayList<RegExTree> trees) {
        for (RegExTree t : trees) if (t.root == ETOILE && t.subTrees.isEmpty()) return true;
        return false;
    }

    private static ArrayList<RegExTree> processEtoile(ArrayList<RegExTree> trees) throws Exception {
        ArrayList<RegExTree> result = new ArrayList<RegExTree>();
        boolean found = false;
        for (RegExTree t : trees) {
            if (!found && t.root == ETOILE && t.subTrees.isEmpty()) {
                if (result.isEmpty()) throw new Exception();
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
            if (firstFound) if (t.root != ALTERN) return true;
            else firstFound = false;
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
        for (RegExTree t : trees) if (t.root == ALTERN && t.subTrees.isEmpty()) return true;
        return false;
    }

    private static ArrayList<RegExTree> processAltern(ArrayList<RegExTree> trees) throws Exception {
        ArrayList<RegExTree> result = new ArrayList<RegExTree>();
        boolean found = false;
        RegExTree gauche = null;
        boolean done = false;
        for (RegExTree t : trees) {
            if (!found && t.root == ALTERN && t.subTrees.isEmpty()) {
                if (result.isEmpty()) throw new Exception();
                found = true;
                gauche = result.remove(result.size() - 1);
                continue;
            }
            if (found && !done) {
                if (gauche == null) throw new Exception();
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
        if (tree.root == PROTECTION && tree.subTrees.size() != 1) throw new Exception();
        if (tree.subTrees.isEmpty()) return tree;
        if (tree.root == PROTECTION) return removeProtection(tree.subTrees.get(0));

        ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
        for (RegExTree t : tree.subTrees) subTrees.add(removeProtection(t));
        return new RegExTree(tree.root, subTrees);
    }

    //EXAMPLE
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


//Class pour gérér les automates finies avec des epsilon transition
class NFA {
    protected ArrayList<Integer> q = new ArrayList<Integer>();
    protected ArrayList<Integer> Sigma = new ArrayList<Integer>();
    protected int q0 = -1;
    protected int f = -1;
    protected HashMap<Integer, ArrayList<Tuple>> Transitions = new HashMap<Integer, ArrayList<Tuple>>();

    public NFA(ArrayList<Integer> Q, ArrayList<Integer> Sigma, int Q0, int F, HashMap<Integer, ArrayList<Tuple>> Transitions) {
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

    public HashMap<Integer, ArrayList<Tuple>> merge(HashMap<Integer, ArrayList<Tuple>> t1, HashMap<Integer, ArrayList<Tuple>> t2){
        
        HashMap<Integer, ArrayList<Tuple>> m = t1;
        System.out.println("yoooo");
        t2.forEach((key, value) -> m.put(key,value));
        
        return m;
    }


    public NFA concaten(NFA A2) {
        System.out.println("je suis correcte");
        q.addAll(A2.q); //On rajoute les étas de A2

        set_transitions(0, f, A2.q0);
        HashMap<Integer, ArrayList<Tuple>> m = merge(Transitions,A2.Transitions);
        f = A2.f; //L'état final est celui du deuxieme
        Sigma.add(0);
        for (int i : A2.Sigma) {
            if (Sigma.contains(i) == false) {
                Sigma.add(i);
            }
        }
        System.out.println("je viens ici");
        //print_transitions();
        //HashSet<Integer> Set = new HashSet<>( Arrays.asList(Sigma));
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
        print_transitions();
        set_transitions(0, r1, q0);
        set_transitions(0, r1, A2.q0);
        set_transitions(0, f, r2);
        set_transitions(0, A2.f, r2);
        HashMap<Integer, ArrayList<Tuple>> m = merge(Transitions,A2.Transitions);
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
        //System.out.println("  Sigma :" + Sigma);
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
                        //System.out.println("HO");
                        if (Transitions.get(tovisit.get(0)) == null) {
                            return res;
                        } else {
                            for (Tuple ti : Transitions.get(tovisit.get(0))) {
                                // System.out.println("YO");
                                if (ti.getB() == 0) {
                                    tovisit.add(ti.getA());
                                    res.add(ti.getA());
                                }
                            }
                            tovisit.remove(0);
                            //System.out.println(tovisit);
                        }
                    }
                }
            }
        }
        return res;
    }
    /*
    ArrayList<Integer> res = new ArrayList<Integer>();
    ArrayList<Tuple> a = Transitions.get(state);
    for ( Tuple t : a ) {
      if (t.getB() == alphabet){
        res.add(t.getA());
          for ( Tuple t2 : Transitions.get(t.getA())){
            if (t2.getB() == 0){
              System.out.println("OUI");
              res.addAll(state_can_travers(t2.getA(),0));
              return res;
            }
          }
      }*/

    // {[2 3 1] : {[5 6 7]:a , [8 9 10]:b}}
    public DFA to_DFA() {
        ArrayList<Integer> eclosures = eclosure(q0); //On a calculer les e-closures des états
        ArrayList<ArrayList<Integer>> new_states = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> a = new ArrayList<Integer>();
        HashMap<ArrayList<Integer>, HashMap<Integer, ArrayList<Integer>>> new_transitions = new HashMap<ArrayList<Integer>, HashMap<Integer, ArrayList<Integer>>>();
        ArrayList<ArrayList<Integer>> start = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> fi = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> Q = new ArrayList<ArrayList<Integer>>();
        new_states.add(eclosures);
        System.out.println("al al la la ");
        System.out.println(eclosures);
        Q.add(eclosures);
        while (new_states.size() > 0) {
            System.out.println("C'est chaud");
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
                if(j!=0){
                    if (a.size() > 0) {
                        new_states.add(a);
                        if (!Q.contains(a)) {
                            Q.add(a);
                        }
                        HashMap<Integer, ArrayList<Integer>> tmp = new_transitions.get(new_states.get(0));
                        if (tmp == null) {
                            tmp = new HashMap<Integer, ArrayList<Integer>>();
                        }
                        tmp.put(j, a);
                        new_transitions.put(new_states.get(0), tmp);
                        System.out.println("BOBOB" + j);
                        System.out.println(new_states);
                    }
                }
            }
            System.out.println("yo" + new_states.get(0));
            System.out.println("yo2" + new_states.get(1));
            if (new_states.get(0).equals(new_states.get(1))) {
                return new DFA(Q, Sigma, get_states(Q, q0), get_states(Q, f), new_transitions);
            }
            new_states.remove(0); //On marque l'état d'avant comme deja lu
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


//UTILITARY CLASS
class RegExTree {
    protected int root;
    protected ArrayList<RegExTree> subTrees;

    public RegExTree(int root, ArrayList<RegExTree> subTrees) {
        this.root = root;
        this.subTrees = subTrees;
    }
    //FROM TREE TO PARENTHESIS

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

        //simple_char();

        ArrayList<Integer> Q = new ArrayList<Integer>();
        int r1 = 0;
        int r2 = 0;
        while (r1 == r2 || r1 == 0 || r2 == 0) {
            Random rand = new Random();
            r1 = rand.nextInt(1000);
            r2 = rand.nextInt(1000);
        }

        //Si on est là c'est qu'on tombe les feuilles de l'arbre (des lettre)

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
        if (subTrees.isEmpty()) return rootToString();
        String result = rootToString() + "(" + subTrees.get(0).toString();
        for (int i = 1; i < subTrees.size(); i++) result += "," + subTrees.get(i).toString();
        return result + ")";
    }

    private String rootToString() {
        if (root == RegEx.CONCAT) return ".";
        if (root == RegEx.ETOILE) return "*";
        if (root == RegEx.ALTERN) return "|";
        if (root == RegEx.DOT) return ".";
        return Character.toString((char) root);
    }

}


class DFA {
    protected ArrayList<ArrayList<Integer>> q = new ArrayList<ArrayList<Integer>>();
    protected ArrayList<Integer> Sigma = new ArrayList<Integer>();
    protected ArrayList<ArrayList<Integer>> q0;
    protected ArrayList<ArrayList<Integer>> f;
    protected HashMap<ArrayList<Integer>, HashMap<Integer, ArrayList<Integer>>> Transitions = new HashMap<ArrayList<Integer>, HashMap<Integer, ArrayList<Integer>>>();

    public DFA(ArrayList<ArrayList<Integer>> Q, ArrayList<Integer> Sigma, ArrayList<ArrayList<Integer>> Q0, ArrayList<ArrayList<Integer>> F, HashMap<ArrayList<Integer>, HashMap<Integer, ArrayList<Integer>>> Transitions) {
        this.q = Q;
        this.Sigma = Sigma;
        this.q0 = Q0;
        this.f = F;
        this.Transitions = Transitions;
    }

    // etape 4
    public void minDFA() {
        /**
         * pour chaque transition de A vers B avec x, on vérifie
         *      si A est un etat finale,
         *      si B est un etat finale,
         *      de-là on supprime la transiton (B,c) et on rajoute la transition (A,c)
         *      et si on utilise plus l'etat B alors on le supprime de part tout(q, f, Transitions)
         */

        int nbStatsVisited = 1;
        int nbStats = this.Transitions.size();

        for (ArrayList<Integer> key : this.Transitions.keySet()) {
            nbStatsVisited += 1;
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
                            nbStats -= 1;
                        }
                    }
                }
            }
            if (nbStatsVisited == nbStats)
                break;
        }
    }

    // etape 5
    public String search(String txt) {
        int N = txt.length();
        Set<String> resutls = new HashSet<String>();
        ArrayList<Integer> currentState = null;
        ArrayList<Integer> nextState = null;
        String currentChar = "";
        String maxMatching = "";

        for(ArrayList<Integer> state : this.q0){
            for(int i=0; i<N; i++){
                // System.out.println("1 txt: " + txt.charAt(i));
                // System.out.println("    state: " + state);

                if(Transitions.containsKey(state)) {
                    currentState = Transitions.get(state).get((int) txt.charAt(i));
                    if(currentState!=null)
                        currentChar = currentChar + txt.charAt(i);
                    // System.out.println("    currentState: " + currentState);

                    if (Transitions.containsKey(currentState) && i < N - 1) {
                        nextState = Transitions.get(currentState).get((int) txt.charAt(i + 1));
                        // System.out.println("    next txt: " + txt.charAt(i + 1));
                        // System.out.println("    0 nextState: " + nextState);

                        while (nextState != null && i < N - 1) {
                            // System.out.println("    ***************");
                            i += 1;
                            nextState = Transitions.get(currentState).get((int) txt.charAt(i));
                            // System.out.println("    1 nextState: " + nextState);
                            if (nextState != null)
                                currentChar = currentChar + txt.charAt(i);
                        }
                        if (nextState == null)
                            i -= 1;
                        resutls.add(currentChar);
                        if(maxMatching.length() < currentChar.length())
                            maxMatching = currentChar;
                        currentChar = "";
                    } else {
                        if(currentChar!="") {
                            resutls.add(currentChar);
                            if(maxMatching.length() < currentChar.length())
                                maxMatching = currentChar;
                        }
                        currentChar = "";
                    }
                }
            }
        }
        System.out.println("results: " + resutls);
        System.out.println("maxMatching: " + maxMatching);

        return maxMatching;
    }


    public void print() {
        System.out.println("{  Q :" + q);
        //System.out.println("  Sigma :" + Sigma);
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



