public class Kmp{
    public static  int first_time(char[] f, char c){
        for(int i=0; i<f.length;i++){
            if(f[i]==c){
                return i;
            }
        }
        return -1;
    }

    public static int[] retenu(char[] facteur){
        int[] retenus = new int[facteur.length];
        
        for(int i = 0; i<facteur.length;i++){
            if(facteur[i] == facteur[0]){
                retenus[i] = -1;
            }
            else{
                if(i-1 == first_time(facteur, facteur[i-1])){
                    System.out.println("je suis ");
                    retenus[i] = 0;
                }
                else{
                    System.out.println( "i = " + i + "firsttime " + first_time(facteur, facteur[i-1]) + "facteur i-1 " + facteur[i-1] );
                    retenus[i] = (i-1)-first_time(facteur, facteur[i-1]) - 1;
                }
            }
        }
        //retenus[1] = 0;
        return retenus;
    }
}
