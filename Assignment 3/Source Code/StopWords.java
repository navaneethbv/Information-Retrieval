/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Navaneeth
 */
import java.io.*;
import java.util.*;

/**
 *
 * @author Navaneeth
 */
public class StopWords {
    ArrayList<String> stopwords;
    String FileName;
    public StopWords(String FileName){
        this.FileName = FileName;
        this.stopwords = new ArrayList();
    }
    public void readStopWords()  throws Exception{
        File f = new File(FileName);
        FileInputStream fis = new FileInputStream(f);
        //Construct BufferedReader from InputStreamReader
	BufferedReader br = new BufferedReader(new InputStreamReader(fis));
 
	String line = null;
	while ((line = br.readLine()) != null) {
                //System.out.println(line);
		stopwords.add(line);
	}
 
	br.close();
    }
    public HashMap removeStopWords(HashMap<String,Integer> index){
        HashMap tmp = (HashMap)index.clone(); 
        Iterator it = tmp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if(stopwords.contains(pair.getKey())){
                index.remove(pair.getKey());
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return index;
    }
    public ArrayList<String> removeStopWords(ArrayList<String> index){
        ArrayList tmp = new ArrayList<String>();
        
        for(int i=0;i<index.size();i++) {
            String line = index.get(i);
            
            String arr[]= line.split(" ");
            String tmpLine = "";
            for(int j=0;j<arr.length;j++){
             if(!stopwords.contains(arr[j])){
                tmpLine = tmpLine +" "+arr[j];
            }   
            }
            tmp.add(tmpLine);
            
            
        }
        return tmp;
    }    
    public static void main(String args[]) throws Exception{
        StopWords s = new StopWords("resourcesIR//stopwords");
        s.readStopWords();
        
        
    }
}

