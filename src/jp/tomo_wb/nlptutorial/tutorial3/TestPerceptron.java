
package jp.tomo_wb.nlptutorial.tutorial3;

import java.util.ArrayList;
import java.util.HashMap;
import jp.tomo_wb.nlptutorial.util.FileIO;
import static jp.tomo_wb.nlptutorial.tutorial2.TestNgramWB.getHashMap;
import static jp.tomo_wb.nlptutorial.tutorial3.TrainPerceptron.ExtractFeature;
import static jp.tomo_wb.nlptutorial.tutorial3.TrainPerceptron.PredictOne;

/**
 *
 * @author TomoyaMizumoto
 */
public class TestPerceptron {
    public static void main(String[] args){
        FileIO FIO = new FileIO();
        //String fileName = "nlp-programming/test/03-train-input.txt";
        String fileName = "nlp-programming/data/titles-en-test.labeled";
        String modelName = "perceptron.model";
        
        ArrayList<String> TextsArray = FIO.FileReader(fileName);
        HashMap<String, Double> model = getHashMap(modelName, FIO);
        TestPerceptron(TextsArray, model);
    }
    
    public static void TestPerceptron(ArrayList<String> TextsArray, HashMap<String, Double> model){
        int corrects = 0;
        int sentNum = TextsArray.size();
        for(int i = 0; i < TextsArray.size(); i++){
            String[] labelSent = TextsArray.get(i).split("\t");
            HashMap<String, Integer> features = ExtractFeature(labelSent[1]);
            int label = Integer.parseInt(labelSent[0]);
            int predictLabel = PredictOne(model, features);
            if(label == predictLabel){
                corrects += 1;
            }
        }
        double acc = (double) corrects / sentNum;
        println("Accuracy: "+acc);
    }
    
    private static void println(Object obj) { System.out.println(obj); }
}
