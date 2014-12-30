package jp.tomo_wb.nlptutorial.tutorial2;

import jp.tomo_wb.nlptutorial.util.FileIO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author TomoyaMizumoto
 */
public class TrainNgramLI {
    private static final HashMap<String, Integer> counts = new HashMap<>();
    private static final HashMap<String, Integer> contextCounts = new HashMap<>();
    private static final int n = 2;
    
    public static void main(String[] args){
        //String fileName = "nlp-programming/test/01-train-input.txt";
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
                ArrayList<String> list = new ArrayList<> (Arrays.asList(words));
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
    
    public static String getContext(String text){
        String context = "";
        if(text.contains(" ")){
            String[] words = text.split(" ");
            ArrayList<String> list = new ArrayList<> (Arrays.asList(words));
            list.remove(list.size()-1);
            context = ArrayToString(list);
        }
        return context;
    }
    
    public static ArrayList<String> PutBOSsymbol(String text, int ngram){
        String[] words = text.split(" ");
        ArrayList<String> list = new ArrayList<> (Arrays.asList(words));
        for(int i = 0; i < ngram - 1; i++){
            list.add(0, "<s>");
        }
        list.add("</s>");
        return list;
    }
    
    public static String ArrayToString(ArrayList<String> arrayString){
        String stringLine = "";
        if(arrayString.size() > 0){
            stringLine = arrayString.get(0);
            for(int i = 1; i < arrayString.size(); i++){
                stringLine = stringLine + " " + arrayString.get(i);
            }
        }
        return stringLine;
    }
    
    private static void println(Object obj) { System.out.println(obj); }
}
