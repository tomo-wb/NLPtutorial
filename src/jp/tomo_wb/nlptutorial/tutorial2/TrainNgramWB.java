
package jp.tomo_wb.nlptutorial.tutorial2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import jp.tomo_wb.nlptutorial.util.FileIO;
import static jp.tomo_wb.nlptutorial.tutorial2.TrainNgramLI.ArrayToString;
import static jp.tomo_wb.nlptutorial.tutorial2.TrainNgramLI.PutBOSsymbol;
import static jp.tomo_wb.nlptutorial.tutorial2.TrainNgramLI.getContext;

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
        String lambdaName = "lambda.model";
        FileIO FIO = new FileIO();
        ArrayList<String> TextsArray = FIO.FileReader(fileName);
        
        contextCounts.put("", 0);
        CountsWordFreq(TextsArray);
        ArrayList<String> arrayList = CalcProb();
        FIO.ArrayListPrint(arrayList, modelName);
        
        
        //FIO.HashMapPrint(typesCount, typesName);
        addStartSymbol();
        HashMap<String, Double> ngramLambda = getLambda(counts, typesCount);
        FIO.HashMapPrint(ngramLambda, lambdaName);
    }
    
    private static ArrayList<String> CalcProb(){
        ArrayList<String> arrayList = new ArrayList<>();
        Set keySet = counts.keySet();
        Iterator it = keySet.iterator();
        
        while(it.hasNext()){
            String key = it.next().toString();
            double count = counts.get(key);
            String context = "";
            int c = 1;
            if(key.contains(" ")){
                String[] words = key.split(" ");
                ArrayList<String> list = new ArrayList<> (Arrays.asList(words));
                list.remove(list.size()-1);
                context = ArrayToString(list);
                if(typesCount.containsKey(context)){
                    c = typesCount.get(context) + 1;
                }
            }
            else{
                if(typesCount.containsKey(context)){
                    c = typesCount.get(context) + 1;
                }
            }
            typesCount.put(context, c);
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
    
    private static void addStartSymbol(){
        counts.put("", contextCounts.get(""));
        if(n > 1){
            ArrayList<String> list = new ArrayList<>();
            for(int i = 0; i < n - 1; i++){
                list.add(0, "<s>");
            }
            String context = ArrayToString(list);
            counts.put(context, counts.get("</s>"));
        }   
    }
    
    private static HashMap<String, Double> getLambda(HashMap<String, Integer> token, HashMap<String, Integer> types){
        Set keySet = types.keySet();
        Iterator it = keySet.iterator();
        
        HashMap<String, Double> ngramLambda = new HashMap<>();
        
        while(it.hasNext()){
            String key = it.next().toString();
            double u = types.get(key);  // types number of wi-1
            double c = token.get(key);  // token number of wi-1
            double lambda = 1 - (u / (u + c));
            ngramLambda.put(key,lambda);
        }
        //list.remove(list.size()-1);
        //String context = ArrayToString(list);
        //double u = types.get(context);  // types number of wi-1 
        //double c = token.get(context);  // token number of wi-1
        //double lambda = 1 - (u / (u + c));
        double lambda = 0.0; 
        return ngramLambda;
    }
    
    private static void println(Object obj) { System.out.println(obj); }
}
