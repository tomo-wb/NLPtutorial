package tutorial2;


import tutorial1.FileIO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author TomoyaMizumoto
 */
public class TrainBigramLI {
    private static HashMap<String, Integer> counts = new HashMap<>();
    private static HashMap<String, Integer> contextCounts = new HashMap<>();
    
    public static void main(String[] args){
        //String fileName = "nlp-programming/test/02-train-input.txt";
        String fileName = "nlp-programming/data/wiki-en-train.word";
        String modelName = "train.model";
        FileIO FIO = new FileIO();
        ArrayList<String> TextsArray = FIO.FileReader(fileName);
        
        contextCounts.put("", 0);
        CountsWordFreq(TextsArray);
        ArrayList<String> arrayList = CalcProb();
        FIO.ArrayListPrint(arrayList, modelName);
    }
    
    private static ArrayList<String> CalcProb(){
        ArrayList<String> arrayList = new ArrayList<>();
        Set keySet = counts.keySet();
        Iterator it = keySet.iterator();
        while(it.hasNext()){
            String key = it.next().toString();
            double count = counts.get(key);
            String context = "";
            if(key.contains(" ")){
                String[] words = key.split(" ");
                ArrayList<String> list = new ArrayList<String> (Arrays.asList(words));
                list.remove(list.size()-1);
                context = ArrayToString(list);
            }
            double prob = count / contextCounts.get(context);
            String output = key+"\t"+prob;
            arrayList.add(output);
        }
        
        return arrayList;
    }
    
    private static void CountsWordFreq(ArrayList<String> TextsArray){
        for(int i = 0; i < TextsArray.size(); i++){
            String[] words = TextsArray.get(i).split(" ");
            ArrayList<String> list = new ArrayList<String> (Arrays.asList(words));
            list.add(0, "<s>");
            list.add("</s>");
        
            for(int j = 1; j < list.size(); j++){
                String word = list.get(j);
                String wordj_1 = list.get(j-1);
                String bigram = wordj_1 + " " + word;
                int c = 1;
                if(counts.containsKey(bigram)){
                    c = counts.get(bigram) + 1;
                }
                counts.put(bigram, c);
                
                c = 1;
                if(contextCounts.containsKey(wordj_1)){
                    c = contextCounts.get(wordj_1) + 1;
                }
                contextCounts.put(wordj_1, c);
                
                c = 1;
                if(counts.containsKey(word)){
                    c = counts.get(word) + 1;
                }
                counts.put(word, c);
                
                c = contextCounts.get("") + 1;
                contextCounts.put("", c);
            }  
        }
    }
    
    public static String ArrayToString(ArrayList<String> arrayString){
        String stringLine = arrayString.get(0);
        for(int i = 1; i < arrayString.size(); i++){
            stringLine = stringLine + " " + arrayString.get(i);
        }
        return stringLine;
    }
    
    private static void println(Object obj) { System.out.println(obj); }
}
