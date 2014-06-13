package tutorial1;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author TomoyaMizumoto
 */
public class FileIO {
    public FileIO(){
        
    }
    
    ArrayList<String> FileSTDIN(){
        ArrayList<String> TextsArray = new ArrayList<>();  // stored texts
        
        // input text from stdin
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            TextsArray = new ArrayList<>();
            String str = br.readLine();
            while(str != null){
                TextsArray.add(str);
                str = br.readLine();
            }
            br.close();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
        return TextsArray;
    }
    
    public ArrayList<String> FileReader(String fileName){
        ArrayList<String> TextsArray = new ArrayList<>();    
        File file = new File(fileName);
        
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str = br.readLine();
            while(str != null){
                TextsArray.add(str);
                str = br.readLine();
            }
            br.close();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
        
        return TextsArray;
    }
    
    public void ArrayListPrint(ArrayList<String> arrayList, String filename){
        try{
            File file = new File(filename);
            FileWriter filewriter = new FileWriter(file);
            for(int i = 0; i < arrayList.size();i++){
                filewriter.write(arrayList.get(i)+"\n");
            }
            filewriter.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }
    
    void StringListPrint(String[] texts, String filename){
        try{
            File file = new File(filename);
            FileWriter filewriter = new FileWriter(file);
            for(int i = 0; i < texts.length; i++){
                filewriter.write(texts[i]+"\n");
            }
            filewriter.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }
    
    public void HashMapPrint(HashMap<String, Integer> hm, String filename){
        try{
            File file = new File(filename);
            FileWriter filewriter = new FileWriter(file);
            Set keySet = hm.keySet();
            Iterator it = keySet.iterator();
            while(it.hasNext()){
                String key = it.next().toString();
                String output = key + "\t" + hm.get(key);
                filewriter.write(output+"\n");
            }
            filewriter.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }
    
    private static void println(Object obj) { System.out.println(obj); }
}
