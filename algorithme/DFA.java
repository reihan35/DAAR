import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Exception;
import java.util.Random;
import java.io.*;
import java.time.*;

public class DFA {
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
    //Class pour les automates finie deterministe

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