
package tutorial2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import tutorial1.FileIO;
import static tutorial1.TestUnigram.Array2Hash;
import static tutorial1.TestUnigram.log2;

/**
 *
 * @author TomoyaMizumoto
 */
public class TestBigramLI {
    private static final double lambda1 = 0.95;
    private static final double lambdaUNK1 = 1 - lambda1;
    private static final double lambda2 = 0.95;
    private static final double lambdaUNK2 = 1 - lambda2;
    private static double W = 0.0;
    private static final double V = 1000000;
    private static double H = 0.0;
    
    public static void main(String[] args){
        //String fileName = "nlp-programming/test/02-test-input.txt";
        String fileName = "nlp-programming/data/wiki-en-test.word";
        String modelName = "train.model";
        FileIO FIO = new FileIO();
        
        ArrayList<String> TextsArray = FIO.FileReader(fileName);
        ArrayList<String> ModelArray = FIO.FileReader(modelName);
        HashMap<String, Double> model = Array2Hash(ModelArray);
        evalBigram(TextsArray, model);
    }
    private static void evalBigram(ArrayList<String> arrayList, HashMap<String, Double> model){
        for(int i = 0; i < arrayList.size(); i++){
            String[] words = arrayList.get(i).split(" ");
            ArrayList<String> list = new ArrayList<String> (Arrays.asList(words));
            list.add(0, "<s>");
            list.add("</s>");
            for(int j = 1; j < list.size(); j++){
                W += 1;
                
                double P1 = lambdaUNK1 / V;
                double P2 = lambdaUNK2 * P1;
                if(model.containsKey(list.get(j))){
                    String bigram = list.get(j-1) + " " + list.get(j);
                    P1 += lambda1 * model.get(list.get(j));
                    P2 = lambdaUNK2 * P1;
                    if(model.containsKey(bigram)){
                        P2 += lambda2 * model.get(bigram);
                    }
                }
                H  = H - log2(P2);
            }
        }
        double entropy = H / W;
        println("entropy = " + entropy);
    }
    
    private static void println(Object obj) { System.out.println(obj); }
}
