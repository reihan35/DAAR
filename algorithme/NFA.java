import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Exception;
import java.util.Random;
import java.io.*;
import java.time.*;

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
        this.Sigma = Sigma; //L'alphanet
        this.q0 = Q0; // etat initial
        this.f = F; // etat finale
        this.Transitions = Transitions;  //tableau de transition

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

    //Cette fontion rajoute les transitions d'un tableau t2 en un tableau t1
    public HashMap<Integer, ArrayList<Tuple>> merge(HashMap<Integer, ArrayList<Tuple>> t1,
                                                    HashMap<Integer, ArrayList<Tuple>> t2) {

        HashMap<Integer, ArrayList<Tuple>> m = t1;
        t2.forEach((key, value) -> m.put(key, value));

        return m;
    }

    //Cette fonction traduit l'algo du livre Aho-Ullman pour la concatentaion
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

    //Cette fonction traduit l'algo du livre Aho-Ullman pour *
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

    //Cette fonction traduit l'algo du livre Aho-Ullman pour |
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
        HashMap<Integer, ArrayList<Tuple>> m = merge(Transitions, A2.Transitions);
        q0 = r1;
        f = r2;
        return new NFA(q, Sigma, q0, f, m);
    }
     //Fonction d'affichage des transitions
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

    //Fonction d'affichage de NFA
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

    //Fonction qui calcule la valaur eclosur d'un etat
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
    }//Fonction qui calcule la valaur eclosur d'un etat

    //Cette fonction renvoie touts les etats qui peuvent etre travaerses à partir d'un etat state avec un symbole
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
    
    //Cette fontion transforme un automate fini non deterministe avec des epsilon transition en automate determinist
    public DFA to_DFA() {
        ArrayList<Integer> eclosures = eclosure(q0); // On a calculer les e-closures des états
        ArrayList<ArrayList<Integer>> new_states = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> a = new ArrayList<Integer>();
        HashMap<ArrayList<Integer>, HashMap<Integer, ArrayList<Integer>>> new_transitions = new HashMap<ArrayList<Integer>, HashMap<Integer, ArrayList<Integer>>>();
        ArrayList<ArrayList<Integer>> start = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> fi = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> Q = new ArrayList<ArrayList<Integer>>();
        new_states.add(eclosures);
        System.out.println(eclosures);
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
            System.out.println(new_states);

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
