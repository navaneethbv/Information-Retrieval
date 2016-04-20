/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;

/**
 *
 * @author Navaneeth
 */
public class indexv1 {

    public TreeMap createLemma(TreeMap index) {

        TreeMap tmp = (TreeMap) index.clone();
        Iterator it = index.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            tmp.remove((String) pair.getKey());
            //String word = Stem((String) pair.getKey());
            String word = Lemmatizer.getInstance().getLemma((String) pair.getKey());
            word = word.trim();
            if(word.equals("reynold")){
              //  System.out.println(word);
            }
            if(!word.equals(pair.getKey())){
                // System.out.println(word);
            }
            // System.out.println(word);
            if (tmp.containsKey(word)) {
                int c = (int) tmp.get(word);
                tmp.put(word.toLowerCase(), c + (int) pair.getValue());
            } else {
                tmp.put(word.toLowerCase(), (int) pair.getValue());
            }

        }
        return tmp;

    }

 

    public int getDocLength(TreeMap hm) {
        Iterator it = hm.entrySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            //System.out.println(pair.getKey() + " => " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
            count = count + (Integer) pair.getValue();
        }
        return count;
    }

    public ArrayList updateDictionary(ArrayList<Term> terms, TreeMap index, int docid) {
        Iterator it = index.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            //Iterate Terms
            boolean flag = true;
            for (Term t : terms) {
                if (t.getName().equals((String) pair.getKey())) {
                    flag = false;
                    terms.remove(t);
                    t.DocFrequency = t.DocFrequency + 1;
                    t.postingfile.put(docid, (Integer) pair.getValue());
                    terms.add(t);
                    break;
                }

            }
            if (flag) {
                Term t1 = new Term((String) pair.getKey());
                t1.DocFrequency = 1;
                t1.postingfile.put(docid, (Integer) pair.getValue());
                terms.add(t1);
            }

        }
        return terms;

    }

    public ArrayList buildv1Index(ArrayList<Term> terms, TreeMap index, boolean isCompress, int docid,String stopwords) throws Exception {
        TreeMap tmp = (TreeMap) index.clone();
        int doclength = getDocLength(tmp);

        //Remove StopWords from Index
        StopWords s = new StopWords(stopwords);
        s.readStopWords();
       // index = s.removeStopWords(index);
         
        HashMap hmtmp = new HashMap() ;
        hmtmp.putAll(index);
        hmtmp = s.removeStopWords(hmtmp);
        
        index.clear();
        index.putAll(hmtmp);

        //Create Lemmas
        index = createLemma((TreeMap) index.clone());

        //Create Dictionary Index
        terms = updateDictionary((ArrayList) terms.clone(), (TreeMap) index.clone(), docid);

        return terms;

    }
}
