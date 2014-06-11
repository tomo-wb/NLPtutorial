
import java.util.ArrayList;
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
        String fileName = "../nlp-programming/test/01-train-input.txt";
        String modelName = "train.model";
        FileIO FIO = new FileIO();
        ArrayList<String> TextsArray = FIO.FileReader(fileName);
        
        HashMap<String, Integer> wordCounts = new HashMap<>();
        for(int i = 0; i < TextsArray.size(); i++){
            String text = TextsArray.get(i) + " </s>";
            wordCounts = CountsWordFreq(wordCounts, text);
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
        for(int i = 0; i < words.length; i++){
            if(wordCounts.containsKey(words[i])){
                int counts = wordCounts.get(words[i]) + 1;
                wordCounts.put(words[i], counts);
            }
            else{             
                wordCounts.put(words[i], 1);
            }
            totalCounts += 1;
        }
        return wordCounts;
    }
    
    private static void println(Object obj) { System.out.println(obj); }
}
