
package jp.tomo_wb.nlptutorial.tutorial2;

import java.util.ArrayList;
import java.util.HashMap;
import jp.tomo_wb.nlptutorial.util.FileIO;
import static jp.tomo_wb.nlptutorial.tutorial1.TestUnigram.Array2Hash;
import static jp.tomo_wb.nlptutorial.tutorial1.TestUnigram.log2;
import static jp.tomo_wb.nlptutorial.tutorial2.TrainNgramLI.PutBOSsymbol;

/**
 *
 * @author TomoyaMizumoto
 */
public class TestNgramLI {
    private static int n = 2;
    private static final double[] lambdas = new double[n];
    private static final double[] lambdaUNKs = new double[n];
    private static double W = 0.0;
    private static final double V = 1000000;
    private static double H = 0.0;
    
    public static void main(String[] args){
        //String fileName = "nlp-programming/test/01-test-input.txt";
        String fileName = "nlp-programming/data/wiki-en-test.word";
        String modelName = "train.model";
        FileIO FIO = new FileIO();
        
        for(int i = 0; i < n; i++){
            lambdas[i] = 0.85;
            lambdaUNKs[i] = 1 - lambdas[i];
        }
        
        ArrayList<String> TextsArray = FIO.FileReader(fileName);
        ArrayList<String> ModelArray = FIO.FileReader(modelName);
        HashMap<String, Double> model = Array2Hash(ModelArray);
        evalNgram(TextsArray, model);
    }
    private static void evalNgram(ArrayList<String> arrayList, HashMap<String, Double> model){
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
                P[0] = lambdaUNKs[0] / V;
                for(int k = 1; k < n; k++){
                    P[k] = lambdaUNKs[k] * P[k-1];
                }
                
                for(int k = 0; k < n; k++){
                    if(model.containsKey(ngram[k])){
                        P[k] += lambdas[k] * model.get(ngram[k]);
                        if(k != n - 1){
                            P[k+1] = lambdaUNKs[k+1] * P[k];
                        }
                    }
                }
                H  = H - log2(P[n-1]);
            }
        }
        double entropy = H / W;
        println("entropy = " + entropy);
    }
    
    private static void println(Object obj) { System.out.println(obj); }
}
