package tutorial1;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author TomoyaMizumoto
 */
public class TrainUnigram {
    private static int totalCounts = 0;
    
    public static void main(String[] args){
        //String fileName = "nlp-programming/test/01-train-input.txt";
        String fileName = "nlp-programming/data/wiki-en-train.word";
        String modelName = "train.model";
        FileIO FIO = new FileIO();
        ArrayList<String> TextsArray = FIO.FileReader(fileName);
        
        HashMap<String, Integer> wordCounts = new HashMap<>();
        for(int i = 0; i < TextsArray.size(); i++){
            //String text = TextsArray.get(i) + " </s>";
            wordCounts = CountsWordFreq(wordCounts, TextsArray.get(i));
        }
        ArrayList<String> arrayList = CalcProb(wordCounts);
        FIO.ArrayListPrint(arrayList, modelName);
    }
    
    private static ArrayList<String> CalcProb(HashMap<String, Integer> wordCounts){
        ArrayList<String> arrayList = new ArrayList<>();
        Set keySet = wordCounts.keySet();
        Iterator it = keySet.iterator();
        while(it.hasNext()){
            String key = it.next().toString();
            float prob = (float) wordCounts.get(key)/ totalCounts;
            String output = key+"\t"+prob;
            arrayList.add(output);
        }
        return arrayList;
    }
    
    private static HashMap<String, Integer> CountsWordFreq(HashMap<String, Integer> wordCounts, String text){
        String[] words = text.split(" ");
        ArrayList<String> list = new ArrayList<String> (Arrays.asList(words));
        list.add("</s>");
        
        for(int i = 0; i < list.size(); i++){
            String word = list.get(i);
            if(wordCounts.containsKey(word)){
                int counts = wordCounts.get(word) + 1;
                wordCounts.put(word, counts);
            }
            else{             
                wordCounts.put(word, 1);
            }
            totalCounts += 1;
        }
        return wordCounts;
    }
    
    private static void println(Object obj) { System.out.println(obj); }
}
