
package jp.tomo_wb.nlptutorial.tutorial3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import jp.tomo_wb.nlptutorial.util.FileIO;
import static jp.tomo_wb.nlptutorial.tutorial3.TestPerceptron.TestPerceptron;

/**
 *
 * @author TomoyaMizumoto
 */
public class TrainPerceptron {
    public static void main(String[] args){
        FileIO FIO = new FileIO();
        //String fileName = "nlp-programming/test/03-train-input.txt";
        String fileName = "nlp-programming/data/titles-en-train.labeled";
        String modelName = "perceptron.model";
        
        ArrayList<String> TextsArray = FIO.FileReader(fileName);
        HashMap<String, Double> weights = TrainPercptron(TextsArray);
        FIO.HashMapPrint(weights, modelName);
    }
    
    private static HashMap<String, Double> TrainPercptron(ArrayList<String> TextsArray){
        HashMap<String, Double> weights = new HashMap<>();
        for(int itNum = 0; itNum < 100; itNum++){
            println("iteration " +itNum);
            for(int i = 0; i < TextsArray.size(); i++){
                String[] labelSent = TextsArray.get(i).split("\t");
                weights = TrainOne(weights, labelSent[1], Integer.parseInt(labelSent[0]));
            }
            TestPerceptron(TextsArray, weights);
        }
        return weights;
    }
    
    private static HashMap<String, Double> TrainOne(HashMap<String, Double> weights, String text, int label){
        HashMap<String, Integer> features = ExtractFeature(text);
        int predictLabel = PredictOne(weights, features);
        if(label != predictLabel){
            weights = UpdateWeights(weights, features, label);
        }
        return weights;
    }
    
    private static HashMap<String, Double> UpdateWeights(HashMap<String, Double> weights, HashMap<String, Integer> features, int label){
        Set keySet = features.keySet();
        Iterator it = keySet.iterator();
        while(it.hasNext()){
            String key = it.next().toString();
            double val = weights.get(key);

            val = val + features.get(key) * (double) label;
            weights.put(key, val);
            //println(key + "\t" + val);
        }
        return weights;
    }
    
    public static int PredictOne(HashMap<String, Double> weights, HashMap<String, Integer> features){
        Set keySet = features.keySet();
        Iterator it = keySet.iterator();
        int label = 1;
        
        double value = 0.0;
        while(it.hasNext()){
            String key = it.next().toString();
            if(!weights.containsKey(key)){
                weights.put(key, 0.0);
            }
            value += weights.get(key) * features.get(key);
        }
        
        if(value < 0){
            label = -1;
        }
        return label;
    }
    
    public static HashMap<String, Integer> ExtractFeature(String text){
        HashMap<String, Integer> features = new HashMap<>();
        features = Unigram(text, features);
        return features;
    }
    
    private static HashMap<String, Integer> Unigram(String text, HashMap<String, Integer> features){
        String[] words = text.split(" ");
        for(int i = 0; i < words.length; i++){
            String feature = "UNI: " + words[i];
            if(features.containsKey(feature)){
                int count = features.get(feature) + 1;
                features.put(feature, count);
            }
            else{
                features.put(feature, 1);
            }
        }
        return features;
    }
    
    private static void println(Object obj) { System.out.println(obj); }
}
