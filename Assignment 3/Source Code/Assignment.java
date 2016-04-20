/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Navaneeth
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Comparator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Navaneeth
 */
class ValueComparator implements Comparator {

    Map map;

    public ValueComparator(Map map) {
        this.map = map;
    }

    public int compare(Object keyA, Object keyB) {
        Comparable valueA = (Comparable) map.get(keyA);
        Comparable valueB = (Comparable) map.get(keyB);
        return valueB.compareTo(valueA);
    }
}

public class Assignment {

    /**
     * @param args the command line arguments
     */
    static ArrayList<Query> QueryList;
    static ArrayList<Query> DocumentList;
    static ArrayList<Term> uncompressed_v1;
    static ArrayList<Term> query_uncompressed_v1;
    static TreeMap<Integer, TreeMap> doc_max_tf;
    static TreeMap<Integer, TreeMap> query_max_tf;
    static TreeMap<Integer, Integer> doc_len;
    static TreeMap<Integer, Integer> query_len;
    static TreeMap<Integer, String> doc_title;
    static TreeMap<Integer, String> doc_external;
    static TreeMap<Integer, Double> doc_w1;
    static TreeMap<Integer, Double> doc_w2;
    static TreeMap<Integer, Double> tw1TreeMap;
    static TreeMap<Integer, Double> tw2TreeMap;
    static TreeMap<Integer, Double> dtw1TreeMap;
    static TreeMap<Integer, Double> dtw2TreeMap;
    static String stopwords;
    static ArrayList<String> queries;
    static double avgDocLen;

    public static void PrintVector1(Query q) {
        TreeMap<String, Double> tmap = q.tw1;
        Iterator it = tmap.entrySet().iterator();
	System.out.println();
        System.out.println("Lemma Version of Query");
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.print(pair.getKey() + " ");
        }
	System.out.println();
        System.out.println("Vector Representation of Query");
        it = tmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.print(pair.getValue() + ",");
        }
        System.out.println();
    }

    public static void PrintVector2(Query q) {
        TreeMap<String, Double> tmap = q.tw2;
        Iterator it = tmap.entrySet().iterator();
	System.out.println();
        System.out.println("Lemma Version of Query");
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.print(pair.getKey() + " ");
        }
        System.out.println();
        System.out.println("Vector Representation of Query");
        it = tmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.print(pair.getValue() + ",");
        }
        System.out.println();
    }

    public static void PrintVector1D(Query q) {
        TreeMap<String, Double> tmap = q.tw1;
        Iterator it = tmap.entrySet().iterator();
	System.out.println();
        System.out.println("Lemmas of the Document");
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.print(pair.getKey() + " ");
        }
	System.out.println();
        System.out.println("Vector Representation of Document");
        it = tmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.print(pair.getValue() + ",");
        }
        System.out.println();
    }

    public static void PrintVector2D(Query q) {
        TreeMap<String, Double> tmap = q.tw2;
        Iterator it = tmap.entrySet().iterator();
	System.out.println();
        System.out.println("Lemma Version of Query");
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.print(pair.getKey() + " ");
        }
        System.out.println();
        System.out.println("Vector Representation of Query");
        it = tmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.print(pair.getValue() + ",");
        }
        System.out.println();
    }
    
    public static double findandreturnweightw1(String termName, Query document) {
        TreeMap<String, Double> tmap = document.tw1;
        Iterator it = tmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String termNameD = (String) pair.getKey();
            if (termName.equals(termNameD)) {
                return (Double) pair.getValue();
            }

        }
        return 0.0;
    }

    public static Query findDocument(int id){
        for(Query q:DocumentList){
            if(q.qid==id){
                return q;
            }
        }
        return null;
    }
    public static void computeCosineSimilarityW1() throws Exception{
        int qcount = 1;
        for (Query q : QueryList) {

            HashMap<Integer, Double> weightmap = new HashMap<Integer, Double>();
            for (Query d : DocumentList) {
                double sum = 0.0;
                TreeMap<String, Double> qweights = q.tw1;
                Iterator it = qweights.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    String termName = (String) pair.getKey();
                    double temp = findandreturnweightw1(termName, d);
                    sum += (Double) pair.getValue() * temp;
                }
                double q1mag = tw1TreeMap.get(q.qid);
                double d1mag = dtw1TreeMap.get(d.qid);
                double result = sum / (Math.sqrt(q1mag) * Math.sqrt(d1mag));
                weightmap.put(d.qid, result);
            }
            
           weightmap = sortByValue((HashMap)weightmap.clone());
           
             //TreeMap hm = weightmap.postingfile;
            //TreeMap tmp = (TreeMap) index.clone();
            Iterator it = weightmap.entrySet().iterator();
            int counter = 0;
            System.out.println("Q:" + qcount);
	    System.out.println("Original Query:");
	    System.out.println(queries.get(q.qid));
            PrintVector1(q);
            System.out.println("Rank,Score,Title,External Identifier");
            writeToFile(q.qid, q, "Documentsvector.txt", true, true,true);

            while (it.hasNext()) {
                if (counter == 5) {
                    break;
                }
                Map.Entry pair = (Map.Entry) it.next();
                int docid = (Integer) pair.getKey();
                double score = (Double) pair.getValue();
                String doctitle = doc_title.get(docid);
                String externalIdentifier = doc_external.get(docid);
                Query doc = findDocument(docid);
                writeToFile(q.qid, doc, "Documentsvector.txt", true, true,false);
                int rank = counter + 1;
                System.out.println(rank + "," + score + "," + doctitle + "," + externalIdentifier);
                counter++;
            }
            qcount++;
        }
    }
    public static void writeToFile(int qid,Query d,String fileName,boolean isTw1,boolean append,boolean printQueryNumber) throws Exception{
        if(printQueryNumber){
             File f = new File(fileName);
        if(!f.exists()){
            f.createNewFile();
        }
        FileWriter fw = new FileWriter(f.getName(),append);
        BufferedWriter bufferWritter = new BufferedWriter(fw);
       
        int num  =qid+1;
            bufferWritter.write("\n");
            if(isTw1){
                bufferWritter.write("Using Scheme W1");
                bufferWritter.write("\n");
            }
            else{
                bufferWritter.write("Using Scheme W2");
                bufferWritter.write("\n");
            }
            bufferWritter.write("Query :"+num);
            bufferWritter.close();
         
            return;
        }
        File f = new File(fileName);
        if(!f.exists()){
            f.createNewFile();
        }
        FileWriter fw = new FileWriter(f.getName(),append);
        BufferedWriter bufferWritter = new BufferedWriter(fw);
        TreeMap<String,Double> tmap = new TreeMap<String,Double>();
        if(isTw1){
            tmap = d.tw1;
        }
        else{
            tmap = d.tw2;
        }
            Iterator itr = tmap.entrySet().iterator();
            ArrayList<String> terms = new ArrayList<String>();
            ArrayList<Double> weights = new ArrayList<Double>();
            while(itr.hasNext()){
                Map.Entry<String,Double> pair= (Map.Entry)itr.next();
                terms.add(pair.getKey());
                weights.add(pair.getValue());
                
            }
            bufferWritter.write("\n");
            bufferWritter.write("Doc ID:"+d.qid);
            bufferWritter.write("\n");
            
            for(String term:terms){
                bufferWritter.write(term+",");
            }
            bufferWritter.write("\n");
            for(double weight:weights){
                bufferWritter.write(weight+",");
            }
            bufferWritter.write("\n");
            
        
        
    	//bufferWritter.write("");
    	bufferWritter.close();
    }

    public static double findandreturnweightw2(String termName, Query document) {
        TreeMap<String, Double> tmap = document.tw2;
        Iterator it = tmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String termNameD = (String) pair.getKey();
            if (termName.equals(termNameD)) {
                return (Double) pair.getValue();
            }

        }
        return 0.0;
    }

    public static void computeCosineSimilarityW2() throws Exception {
        int qcount = 1;
        for (Query q : QueryList) {
            HashMap<Integer, Double> weightmap = new HashMap<Integer, Double>();
            for (Query d : DocumentList) {
                double sum = 0.0;
                TreeMap<String, Double> qweights = q.tw2;
                Iterator it = qweights.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    String termName = (String) pair.getKey();
                    double temp = findandreturnweightw1(termName, d);
                    sum += (Double) pair.getValue() * temp;
                }
                double q1mag = tw2TreeMap.get(q.qid);
                double d1mag = dtw2TreeMap.get(d.qid);
                double result = sum / (Math.sqrt(q1mag) * Math.sqrt(d1mag));
                weightmap.put(d.qid, result);
            }
            
           // HashMap tmp = new HashMap();
            weightmap = sortByValue((HashMap)weightmap.clone());
            
            //weightmap.putAll(tmp);
             //TreeMap hm = weightmap.postingfile;
            //TreeMap tmp = (TreeMap) index.clone();
            Iterator it = weightmap.entrySet().iterator();
            int counter = 0;
            System.out.println("Q:" + qcount);
	    System.out.println("Original Query:");
            System.out.println(queries.get(q.qid));

            PrintVector2(q);
            writeToFile(q.qid, q, "Documentsvector.txt", false, true,true);
            System.out.println("Rank,Score,Title,External Identifier");
            while (it.hasNext()) {
                if (counter == 5) {
                    break;
                }
                Map.Entry pair = (Map.Entry) it.next();
                int docid = (Integer) pair.getKey();
                double score = (Double) pair.getValue();
                String doctitle = doc_title.get(docid);
                String externalIdentifier = doc_external.get(docid);
                int rank = counter + 1;
                            writeToFile(q.qid, q, "Documentsvector.txt", false, true,false);

                System.out.println(rank + "," + score + "," + doctitle + "," + externalIdentifier);
                counter++;
            }
            qcount++;
        }
    }

public static HashMap sortByValue(HashMap unsortMap) {	 
	List list = new LinkedList(unsortMap.entrySet());
 
	Collections.sort(list, new Comparator() {
		public int compare(Object o2, Object o1) {
			return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
		}
	});
 
	HashMap sortedMap = new LinkedHashMap();
	for (Iterator it = list.iterator(); it.hasNext();) {
		Map.Entry entry = (Map.Entry) it.next();
		sortedMap.put(entry.getKey(), entry.getValue());
	}
	return sortedMap;
}

    public static TreeMap<String, Term> convertToTreeMap(ArrayList<Term> list) {
        TreeMap<String, Term> tmap = new TreeMap<String, Term>();
        for (int i = 0; i < list.size(); i++) {
            Term t = list.get(i);
            tmap.put(t.TermName, t);
        }
        return tmap;
    }

    public static void saveToFile(String fileName, ArrayList<Term> term) {
        try {
            File newTextFile = new File(fileName);
            BufferedWriter writer = null;

            writer = new BufferedWriter(new FileWriter(newTextFile, false));
            writer.write("key,docfreq,postinglist");
            writer.newLine();
            for (int i = 0; i < term.size(); i++) {
                Term t = term.get(i);
                writer.write(t.getName() + "," + t.DocFrequency);
                TreeMap hm = t.postingfile;
                //TreeMap tmp = (TreeMap) index.clone();
                Iterator it = hm.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    writer.write("," + pair.getKey() + "->" + pair.getValue());

                }
                writer.newLine();

            }
            writer.close();

        } catch (Exception e) {

        }
    }

    public static TreeMap getMaxTf(TreeMap tokens) {
        TreeMap tmp = (TreeMap) tokens.clone();
        Iterator it = tmp.entrySet().iterator();
        int max = -1;
        String term = "";
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if ((Integer) pair.getValue() > max) {
                max = (Integer) pair.getValue();
                term = (String) pair.getKey();
            }
        }
        TreeMap<String, Integer> tmphm = new TreeMap<String, Integer>();
        tmphm.put(term, max);
        return tmphm;
    }

    public static int calculateTermFrequency(Term t) {
        TreeMap hm = t.postingfile;
        Iterator itr = hm.entrySet().iterator();
        int sum = 0;
        while (itr.hasNext()) {
            Map.Entry pair = (Map.Entry) itr.next();

            Integer value = (Integer) pair.getValue();
            sum = sum + value;
        }
        return sum;
    }

    public static void generateStatistics(ArrayList<Term> uncompressed, ArrayList<String> words) {
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            String stem = word.toLowerCase().trim();
            for (int j = 0; j < uncompressed.size(); j++) {
                Term t = uncompressed.get(j);
                String TermName = t.TermName.trim();
                if (TermName.equals(word) || TermName.equals(stem)) {
                    System.out.println(t.TermName + "=>" + t.DocFrequency + "," + calculateTermFrequency(t));
                }
            }

        }
    }

    public static void version1(String target_dir) throws Exception {
        uncompressed_v1 = new ArrayList<Term>();
        doc_max_tf = new TreeMap<Integer, TreeMap>();
        doc_len = new TreeMap<Integer, Integer>();
        doc_title = new TreeMap<Integer, String>();
        doc_external = new TreeMap<Integer, String>();

        //static TreeMap<Integer, String> doc_title;
        int docid = 0;
        File dir = new File(target_dir);
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                try (BufferedReader inputStream = new BufferedReader(
                        new FileReader(f))) {
                    String line;
                    Tokenize t = new Tokenize();
                    t.setTokens();
                    String title = "";
                    boolean titleflag = false;
                    while ((line = inputStream.readLine()) != null) {
                        //System.out.println(line);
                        if (line.contains("</TITLE>")) {
                            titleflag = false;
                        }
                        if (titleflag) {
                            title = title + line;
                        }
                        if (line.contains("<TITLE>")) {
                            titleflag = true;
                        }
                        String arr[] = line.split(" ");
                        //for each word loop
                        for (int i = 0; i < arr.length; i++) {
                            // System.out.println(arr[i]);
                            //String word = arr[i];
                            //Tokenize

                            t.tokenize(arr[i]);

                        }
                    }

                    HashMap tokens1 = t.getTokens();

                    TreeMap tokens = new TreeMap();
                    tokens.putAll(tokens1);

                    indexv1 ind1 = new indexv1();
                    uncompressed_v1 = ind1.buildv1Index(uncompressed_v1, tokens, false, docid,stopwords);

                    //indexv2 ind2 = new indexv2();
                    //uncompressed_v2 = ind2.buildv2Index(uncompressed_v2, tokens, false, docid);
                    doc_max_tf.put(docid, getMaxTf(tokens));
                    StopWords s = new StopWords(stopwords);
                    s.readStopWords();

                    tokens1 = s.removeStopWords(tokens1);

                    Iterator itr = tokens1.entrySet().iterator();
                    int sum = 0;
                    while (itr.hasNext()) {
                        Map.Entry pair = (Map.Entry) itr.next();

                        Integer value = (Integer) pair.getValue();
                        sum = sum + value;
                    }

                    doc_len.put(docid, sum);
                    doc_title.put(docid, title);
                    doc_external.put(docid, f.getName());
                }

                docid++;
            }
        }

    }

    public static void computeWeights(ArrayList<Term> index, int id) {
        Query<String, Double> q = new Query<String, Double>(id);
        ArrayList<Term> miniindex = new ArrayList<Term>();

        //Calculate Max TF for query
        int tmax = 0;
        TreeMap maxtfmap = query_max_tf.get(id);
        Iterator itr_maxtf = maxtfmap.entrySet().iterator();
        while (itr_maxtf.hasNext()) {
            Map.Entry pair = (Map.Entry) itr_maxtf.next();
            tmax = (Integer) pair.getValue();
        }

        //Generate Mini Index for Query
        for (Term t : index) {
            TreeMap postingList = t.postingfile;
            Iterator itr = postingList.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry pair = (Map.Entry) itr.next();
                Integer key = (Integer) pair.getKey();
                Integer value = (Integer) pair.getValue();
                if (key == id) {
                    miniindex.add(t);
                    break;
                }
            }

        }

        //Calculate Query Length
        int qlen = 0;
        for (Term t : miniindex) {
            int df = t.DocFrequency;
            TreeMap tpost = t.postingfile;
            Iterator itr = tpost.entrySet().iterator();
            int tf = 0;
            while (itr.hasNext()) {
                Map.Entry pair = (Map.Entry) itr.next();
                if (id == (Integer) pair.getKey()) {
                    tf = (Integer) pair.getValue();
                    qlen = qlen + tf;

                }
            }
        }

        //Calcualte Average Query Length
        Iterator itr_doc_len = query_len.entrySet().iterator();
        int sum = 0;
        while (itr_doc_len.hasNext()) {
            Map.Entry pairmaxtf = (Map.Entry) itr_doc_len.next();
            sum = sum + (Integer) pairmaxtf.getValue();
        }
        double avgquerylen = sum / query_len.size();

        //For each term in query calculate weight
        for (Term t : miniindex) {
            int df = t.DocFrequency;
            TreeMap tpost = t.postingfile;
            Iterator itr = tpost.entrySet().iterator();
            int tf = 0;
            while (itr.hasNext()) {
                Map.Entry pair = (Map.Entry) itr.next();
                if(id==(Integer)pair.getKey()){
                    tf = (Integer) pair.getValue();
                }
            }
            int qf = t.DocFrequency;

            int docFreq = returnDocFreq(t.getName());
            
            if(docFreq!=0){
            double w1 = W1(tf, tmax, doc_len.size(), docFreq);
            double w2 = W2(tf, tmax, doc_len.size(), docFreq, qlen, avgDocLen);

            q.tw1.put(t.getName(), w1);
            q.tw2.put(t.getName(), w2);
                
            }
            
            
        }
        QueryList.add(q);
    }

    public static int returnDocFreq(String term){
        for(Term t:uncompressed_v1){
            if(t.getName().equals(term)){
                return t.DocFrequency;
            }
        }
        return 0;
    }
    public static void computeMagnitude(ArrayList<Query> queries) {
        tw1TreeMap = new TreeMap<Integer, Double>();
        tw2TreeMap = new TreeMap<Integer, Double>();

        for (Query query : queries) {
            TreeMap<String, Double> tw1 = query.tw1;
            TreeMap<String, Double> tw2 = query.tw2;
            double sumtw1 = 0.0;
            double sumtw2 = 0.0;
            Iterator itr = tw1.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry pair = (Map.Entry) itr.next();
                sumtw1 += (Double) pair.getValue() * (Double) pair.getValue();
            }
            tw1TreeMap.put(query.qid, sumtw1);
            Iterator itrtw2 = tw2.entrySet().iterator();
            while (itrtw2.hasNext()) {
                Map.Entry pair = (Map.Entry) itrtw2.next();
                sumtw2 += (Double) pair.getValue() * (Double) pair.getValue();
            }
            tw2TreeMap.put(query.qid, sumtw2);
        }
    }

    public static void computeMagnitudeD(ArrayList<Query> queries) {
        dtw1TreeMap = new TreeMap<Integer, Double>();
        dtw2TreeMap = new TreeMap<Integer, Double>();

        for (Query query : queries) {
            TreeMap<String, Double> tw1 = query.tw1;
            TreeMap<String, Double> tw2 = query.tw2;
            double sumtw1 = 0.0;
            double sumtw2 = 0.0;
            Iterator itr = tw1.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry pair = (Map.Entry) itr.next();
                sumtw1 += (Double) pair.getValue() * (Double) pair.getValue();
            }
            dtw1TreeMap.put(query.qid, sumtw1);
            Iterator itrtw2 = tw2.entrySet().iterator();
            while (itrtw2.hasNext()) {
                Map.Entry pair = (Map.Entry) itrtw2.next();
                sumtw2 += (Double) pair.getValue() * (Double) pair.getValue();
            }
            dtw2TreeMap.put(query.qid, sumtw2);
        }
    }

    public static void computeWeightsD(ArrayList<Term> index, int id) {
        Query<String, Double> d = new Query<String, Double>(id);

        ArrayList<Term> miniindex = new ArrayList<Term>();

        //Calculate Max TF for query
        int tmax = 0;
        TreeMap maxtfmap = doc_max_tf.get(id);
        Iterator itr_maxtf = maxtfmap.entrySet().iterator();
        while (itr_maxtf.hasNext()) {
            Map.Entry pair = (Map.Entry) itr_maxtf.next();
            tmax = (Integer) pair.getValue();
        }

        //Generate Mini Index for Query
        for (Term t : index) {
            TreeMap postingList = t.postingfile;
            Iterator itr = postingList.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry pair = (Map.Entry) itr.next();
                Integer key = (Integer) pair.getKey();
                Integer value = (Integer) pair.getValue();
                if (key == id) {
                    miniindex.add(t);
                    break;
                }
            }

        }

        //Calculate Query Length
        int qlen = 0;
        for (Term t : miniindex) {
            int df = t.DocFrequency;
            TreeMap tpost = t.postingfile;
            Iterator itr = tpost.entrySet().iterator();
            int tf = 0;
            while (itr.hasNext()) {
                Map.Entry pair = (Map.Entry) itr.next();
                //int did = (Integer)pair.getKey();
                if (id == (Integer) pair.getKey()) {
                    tf = (Integer) pair.getValue();
                    qlen = qlen + tf;

                }
            }
        }

        //Calcualte Average Query Length
        Iterator itr_doc_len = doc_len.entrySet().iterator();
        int sum = 0;
        while (itr_doc_len.hasNext()) {
            Map.Entry pairmaxtf = (Map.Entry) itr_doc_len.next();
            sum = sum + (Integer) pairmaxtf.getValue();
        }
        double avgquerylen = sum / doc_len.size();

        avgDocLen = avgquerylen;
        //For each term in query calculate weight
        for (Term t : miniindex) {
            int df = t.DocFrequency;
            TreeMap tpost = t.postingfile;
            Iterator itr = tpost.entrySet().iterator();
            int tf = 0;
            while (itr.hasNext()) {
                Map.Entry pair = (Map.Entry) itr.next();
                if(id==(Integer)pair.getKey()){
                    tf = (Integer) pair.getValue();
                }
                
            }
            int qf = t.DocFrequency;

            double w1 = W1(tf, tmax, doc_len.size(), qf);
            double w2 = W2(tf, tmax, doc_len.size(), qf, qlen, avgquerylen);
            d.tw1.put(t.getName(), w1);
            d.tw2.put(t.getName(), w2);

        }
        DocumentList.add(d);
    }

    public static void version1Q(String target_dir) throws Exception {
        query_uncompressed_v1 = new ArrayList<Term>();
        query_max_tf = new TreeMap<Integer, TreeMap>();
        query_len = new TreeMap<Integer, Integer>();

        int queryid = 0;

        File f = new File(target_dir);
        BufferedReader inputStream = new BufferedReader(new FileReader(f));
        String line;
        ArrayList<String> queries = new ArrayList<String>();
        String tmp = "";
        boolean flag = false;
        while ((line = inputStream.readLine()) != null) {
            if (line.contains("Q") && line.contains(":")) {
                if (flag) {
                    queries.add(tmp);
                    //System.out.println(tmp);
                    flag = true;
                    tmp = "";

                }
                flag = true;

                continue;
            }
            tmp = tmp + " " + line;

        }
        queries.add(tmp);
        // return queries;

        int counter = 0;

        for (String query : queries) {

            Tokenize t = new Tokenize();
            t.setTokens();
            line = query;

            String arr[] = line.split(" ");

            //for each word loop
            for (int i = 0; i < arr.length; i++) {
                // System.out.println(arr[i]);
                //String word = arr[i];
                //Tokenize

                t.tokenize(arr[i]);

            }

            HashMap tokens1 = t.getTokens();

            TreeMap tokens = new TreeMap();
            tokens.putAll(tokens1);

            indexv1 ind1 = new indexv1();
            query_uncompressed_v1 = ind1.buildv1Index(query_uncompressed_v1, tokens, false, queryid,stopwords);

            query_max_tf.put(queryid, getMaxTf(tokens));
            StopWords s = new StopWords(stopwords);
            s.readStopWords();

            tokens1 = s.removeStopWords(tokens1);

            Iterator itr = tokens1.entrySet().iterator();
            int sum = 0;
            while (itr.hasNext()) {
                Map.Entry pair = (Map.Entry) itr.next();

                Integer value = (Integer) pair.getValue();
                sum = sum + value;
            }

            query_len.put(queryid, sum);

            queryid++;

        }

    }

    public static ArrayList<String> QueryParser(String fileName) throws Exception {
        File f = new File(fileName);
        try (BufferedReader inputStream = new BufferedReader(
                new FileReader(f))) {
            String line;
            ArrayList<String> queries = new ArrayList<String>();
            String tmp = "";
            boolean flag = false;
            while ((line = inputStream.readLine()) != null) {
                if (line.contains("Q") && line.contains(":")) {
                    if (flag) {
                        queries.add(tmp);
                        //System.out.println(tmp);
                        flag = true;
                        tmp = "";

                    }
                    flag = true;

                    continue;
                }
                tmp = tmp + " " + line;

            }
            queries.add(tmp);
            return queries;
        } catch (Exception e) {
            return new ArrayList<String>();
        }

    }

    public static ArrayList<String> createLemmas(ArrayList<String> index) {
        ArrayList tmp = new ArrayList<String>();

        for (int i = 0; i < index.size(); i++) {
            String line = index.get(i);

            String arr[] = line.split(" ");
            String tmpLine = "";
            for (int j = 0; j < arr.length; j++) {
                if (arr[j].equals(" ")) {
                    continue;
                }
                arr[j] = Lemmatizer.getInstance().getLemma(arr[j]);

                tmpLine = tmpLine + " " + arr[j];

            }
            tmp.add(tmpLine);

        }
        return tmp;
    }

    public static void generateStatistics(String target_dir) throws Exception {
        File dir = new File(target_dir);
        File[] files = dir.listFiles();
        int docid = 0;
        for (File f : files) {
            if (f.isFile()) {
                try (BufferedReader inputStream = new BufferedReader(
                        new FileReader(f))) {
                    String line;
                    Tokenize t = new Tokenize();
                    t.setTokens();
                    while ((line = inputStream.readLine()) != null) {
                        //System.out.println(line);
                        String arr[] = line.split(" ");
                        //for each word loop
                        for (int i = 0; i < arr.length; i++) {
                            t.tokenize(arr[i]);
                        }
                    }

                    HashMap tokens1 = t.getTokens();

                    TreeMap tokens = new TreeMap();
                    tokens.putAll(tokens1);

//                    indexv1 ind1 = new indexv1();
//                    uncompressed_v1 = ind1.buildv1Index(uncompressed_v1, tokens, false, docid);
                    //indexv2 ind2 = new indexv2();
                    //uncompressed_v2 = ind2.buildv2Index(uncompressed_v2, tokens, false, docid);
                    //                  doc_max_tf.put(docid, getMaxTf(tokens));
                }

                //            docid++;
            }
        }

    }

    public static double W1(int tf, int maxtf, int collectionsize, int df) {
        double t1 = (0.4 + 0.6 * Math.log10(tf + 0.5) / Math.log10(maxtf + 1.0))*(Math.log10(collectionsize / df) / Math.log10(collectionsize));
       
        return t1 ;
    }

    public static double W2(int tf, int maxtf, int collectionsize, int df, int doclen, double avgdoclen) {
        double t1 = (0.4 + 0.6 * (tf / (tf + 0.5 + 1.5 * (doclen / avgdoclen)))*Math.log10(collectionsize / df) / Math.log10(collectionsize));
        
        return t1;
    }

    public static double compute(String word) {

        //double val= 0.4 + 0.6
        for (int i = 0; i < uncompressed_v1.size(); i++) {
            Term t = uncompressed_v1.get(i);
            if (t.getName().equals(word)) {
                int df = t.DocFrequency;
                TreeMap postingList = t.postingfile;
                Iterator it = postingList.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    int docid = (Integer) pair.getKey();
                    int tf = (Integer) pair.getValue();
                    TreeMap maxtfMap = doc_max_tf.get(docid);
                    //doc_len.get(docid);
                    Iterator itr = maxtfMap.entrySet().iterator();
                    int maxtf = 0;
                    while (itr.hasNext()) {
                        Map.Entry pairmaxtf = (Map.Entry) itr.next();
                        maxtf = (Integer) pairmaxtf.getValue();
                    }
                    Iterator itr_doc_len = doc_len.entrySet().iterator();
                    int sum = 0;
                    while (itr_doc_len.hasNext()) {
                        Map.Entry pairmaxtf = (Map.Entry) itr_doc_len.next();
                        sum = sum + (Integer) pairmaxtf.getValue();
                    }
                    double w1 = W1(tf, maxtf, doc_max_tf.size(), t.DocFrequency);
                    double w2 = W2(tf, maxtf, doc_max_tf.size(), t.DocFrequency, doc_len.get(docid), sum / doc_len.size());
                    if (doc_w1.containsKey(docid)) {
                        doc_w1.put(docid, doc_w1.get(docid) + w1);
                    } else {
                        doc_w1.put(docid, w1);
                    }
                    if (doc_w2.containsKey(docid)) {
                        doc_w2.put(docid, doc_w2.get(docid) + w2);
                    } else {
                        doc_w2.put(docid, w2);
                    }
                }

            }
        }
        return 0.0;
    }

    public static void main(String[] args) throws Exception {

        double x = Math.log10(10);
        // Read Stop Words
        String target_dir;
        avgDocLen = 0.0;
        QueryList = new ArrayList<Query>();
        DocumentList = new ArrayList<Query>();
        String queryFile ="";
        
        if (args.length == 3) {
            target_dir = args[0];
            queryFile = args[1];
            stopwords = args[2];
        } else {
            target_dir = "Cranfield";
            queryFile = "hw3.queries";
            stopwords = "resourcesIR//stopwords";
        }

	queries = QueryParser(queryFile);

        version1(target_dir);

        version1Q(queryFile);

           for (int i = 0; i < doc_len.size(); i++) {

            computeWeightsD(uncompressed_v1, i);
        }
        for (int i = 0; i < query_len.size(); i++) {

            computeWeights(query_uncompressed_v1, i);
        }
     

        computeMagnitude(QueryList);
        computeMagnitudeD(DocumentList);

        System.out.println("-----------------------------Using W1------------------------");
        computeCosineSimilarityW1();

        System.out.println();
        
        System.out.println("-----------------------------Using W2------------------------");
        computeCosineSimilarityW2();

        System.out.println();
        
	
    }

}
