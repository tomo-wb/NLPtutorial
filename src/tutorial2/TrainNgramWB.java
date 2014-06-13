
package tutorial2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import tutorial1.FileIO;
import static tutorial2.TrainNgramLI.ArrayToString;
import static tutorial2.TrainNgramLI.PutBOSsymbol;
import static tutorial2.TrainNgramLI.getContext;

/**
 *
 * @author TomoyaMizumoto
 */
public class TrainNgramWB {
    private static final HashMap<String, Integer> counts = new HashMap<>();
    private static final HashMap<String, Integer> contextCounts = new HashMap<>();
    private static final int n = 2;
    private static final HashMap<String, Integer> typesCount = new HashMap<>();
    
    public static void main(String[] args){
        //String fileName = "nlp-programming/data/wiki-en-train.word";
        String fileName = "nlp-programming/test/01-train-input.txt";
        String modelName = "train.model";
        String typesName = "types.count";
        FileIO FIO = new FileIO();
        ArrayList<String> TextsArray = FIO.FileReader(fileName);
        
        contextCounts.put("", 0);
        CountsWordFreq(TextsArray);
        ArrayList<String> arrayList = CalcProb();
        FIO.ArrayListPrint(arrayList, modelName);
        FIO.HashMapPrint(typesCount, typesName);
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
                int c = 1;
                if(typesCount.containsKey(context)){
                    c = typesCount.get(context) + 1;
                }
                typesCount.put(context, c);
            }
            double prob = count / contextCounts.get(context);
            String output = key+"\t"+prob;
            arrayList.add(output);
        }
        
        return arrayList;  
    }
    private static void CountsWordFreq(ArrayList<String> TextsArray){
        for(int i = 0; i < TextsArray.size(); i++){
            ArrayList<String> list = PutBOSsymbol(TextsArray.get(i), n);
            
            for(int j = n-1; j < list.size(); j++){
                String[] ngram = new String[n];
                int num = 0;
                ngram[num] = list.get(j);
                for(int k = 1; k < n; k++){
                    ngram[num+1] = list.get(j-num-1) + " " + ngram[num];
                    num++;
                }
                for(int k = 0; k < n; k++){
                    int c = 1;
                    if(counts.containsKey(ngram[k])){
                        c = counts.get(ngram[k]) + 1;
                    }
                    counts.put(ngram[k], c);
                    
                    c = 1;
                    String context = getContext(ngram[k]);
                    if(contextCounts.containsKey(context)){
                        c = contextCounts.get(context) + 1;
                    }
                    contextCounts.put(context, c);
                }
            }  
        }
    }
    
    
    private static void println(Object obj) { System.out.println(obj); }
}
