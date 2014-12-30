
package jp.tomo_wb.nlptutorial.tutorial2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import jp.tomo_wb.nlptutorial.util.FileIO;
import static jp.tomo_wb.nlptutorial.tutorial1.TestUnigram.Array2Hash;
import static jp.tomo_wb.nlptutorial.tutorial1.TestUnigram.log2;
import static jp.tomo_wb.nlptutorial.tutorial2.TrainNgramLI.ArrayToString;
import static jp.tomo_wb.nlptutorial.tutorial2.TrainNgramLI.PutBOSsymbol;

/**
 *
 * @author TomoyaMizumoto
 */
public class TestNgramWB {
    private static int n = 2;
    private static double W = 0.0;
    private static double H = 0.0;
    private static final double V = 1000000;
    
    public static void main(String[] args){
        FileIO FIO = new FileIO();
         
        String fileName = "nlp-programming/test/01-test-input.txt";
        //String fileName = "nlp-programming/data/wiki-en-test.word";
        String modelName = "train.model";
        String lambdaName = "lambda.model";
        
        ArrayList<String> TextsArray = FIO.FileReader(fileName);
        HashMap<String, Double> model = getHashMap(modelName, FIO);
        HashMap<String, Double> lambda = getHashMap(lambdaName, FIO);
        
        evalNgram(TextsArray, model, lambda);
     }
    
     private static void evalNgram(ArrayList<String> arrayList, HashMap<String, Double> model, 
             HashMap<String, Double> lambdas){
         for(int i = 0; i < arrayList.size(); i++){
            ArrayList<String> list = PutBOSsymbol(arrayList.get(i), n);
            for(int j = n - 1 ; j < list.size(); j++){
                W += 1;
                // make ngrams
                String[] ngram = new String[n];
                int num = 0;
                ngram[num] = list.get(j);
                for(int k = 1; k < n; k++){
                    ngram[num+1] = list.get(j-num-1) + " " + ngram[num];
                    num++;
                }
                // initialized probability
                double[] P = new double[n];
                double lambda = getLambda(ngram[0], lambdas);
                P[0] = (1 - lambda) / V;
                for(int k = 1; k < n; k++){
                    lambda = getLambda(ngram[k], lambdas);
                    P[k] = (1 - lambda) * P[k-1];
                }
                
                for(int k = 0; k < n; k++){
                    if(model.containsKey(ngram[k])){
                        lambda = getLambda(ngram[0], lambdas);
                        P[k] += lambda * model.get(ngram[k]);
                        if(k != n - 1){
                            P[k+1] = (1 - lambda) * P[k];
                        }
                    }
                }
                H  = H - log2(P[n-1]);
            }
         }
         double entropy = H / W;
         println("entropy = " + entropy);
     }
     private static double getLambda(String text, HashMap<String, Double> lambdas){
         String[] words = text.split(" ");
         ArrayList<String> list = new ArrayList<> (Arrays.asList(words));
         list.remove(list.size()-1);
         String context = ArrayToString(list);
         println(context);
         double lambda = lambdas.get(context);
         
         return lambda;
     }
     
     public static HashMap<String, Double> getHashMap(String filename, FileIO FIO){
         ArrayList<String> al = FIO.FileReader(filename);
         HashMap<String, Double> hm = Array2Hash(al);
         return hm;
     }
     
     private static void println(Object obj) { System.out.println(obj); }
}
