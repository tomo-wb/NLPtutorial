
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author TomoyaMizumoto
 */
public class TestUnigram {
    private static final double lambda = 0.95;
    private static final double lambdaUNK = 1 - lambda;
    private static double W = 0.0;
    private static double V = 1000000;
    private static double H =0.0;
    
    public static void main(String[] args){
        String fileName = "../nlp-programming/test/01-test-input.txt";
        String modelName = "train.model";
        FileIO FIO = new FileIO();
        
        ArrayList<String> TextsArray = FIO.FileReader(fileName);
        ArrayList<String> ModelArray = FIO.FileReader(modelName);
        HashMap<String, Double> model = Array2Hash(ModelArray);
        evalUnigram(TextsArray, model);
    }
    
    private static void evalUnigram(ArrayList<String> arrayList, HashMap<String, Double> model){
        int unk = 0;
        for(int i = 0; i < arrayList.size(); i++){
            String text = arrayList.get(i) + " </s>";
            String[] words = text.split(" ");
            for(int j = 0; j < words.length; j++){
                W += 1;
                double P = lambdaUNK / V;
                
                if(model.containsKey(words[j])){
                    P += lambda * model.get(words[j]);
                }
                else{
                    unk += 1;   
                }
                H  = H - log2(P);
            }
        }
        double entropy = H / W;
        double coverage = (W-unk) / W;
        
        println("entropy = " + entropy);
        println("coverage = " + coverage);
    }
    
    private static double log2(double p){
        double log2 = Math.log(p)/Math.log(2.0);
        return log2;
    }
    
    private static HashMap<String, Double> Array2Hash(ArrayList<String> arrayList){
        HashMap<String, Double> hashMap = new HashMap<>();
        for(int i = 0; i < arrayList.size(); i++){
            String[] wordProb = arrayList.get(i).split("\t");
            hashMap.put(wordProb[0], Double.parseDouble(wordProb[1]));
        }
        return hashMap;
    }
    private static void println(Object obj) { System.out.println(obj); }
}
