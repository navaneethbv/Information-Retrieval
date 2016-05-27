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
public class Tokenize {
    private HashMap<String,Integer> doctokens;
    int numOfTokens;
    public void Tokenize(){
        doctokens = new HashMap<String,Integer>();
        numOfTokens = -1;
       // System.out.println();
    }
    public void setTokens(){
        Tokenize();
    }
    public HashMap getTokens(){
        return doctokens;
    }
    public static int countTokens(HashMap tokens) {
        HashMap tmp = (HashMap) tokens.clone();
        Iterator it = tmp.entrySet().iterator();
        int count = 0;
        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();
            // System.out.println(pair.getKey() + "," + pair.getValue());
            //it.remove(); // avoids a ConcurrentModificationException
            count += (Integer) pair.getValue();
        }
        return count;
    }

    public static String removeSGMLtags(String word) {
        if (word.equals("")) {
            return word;
        }
        word = word.replaceAll("<[^>]+>", "");
        return word;
    }

    public static String removeDigits(String word) {
        if (word.equals("")) {
            return word;
        }
        word = word.replaceAll("[0-9]", "");
        return word;
    }

    static String removechar(char char1, String word) {
        if (word.equals("")) {
            return word;
        }
        String str = "";
        if (word.contains(String.valueOf(char1))) {
            char[] ch = word.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                if (ch[i] == char1) {
                    if (char1 == '\\') {

                        System.out.print("");
                    }

                    continue;
                }
                str = str + String.valueOf(ch[i]);
            }

            return str;
        } else {
            return word;
        }

    }

    static String removecomma(String word) {
        if (word.equals("")) {
            return word;
        }
        String str = "";
        if (word.contains(",")) {
            char[] ch = word.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                if (ch[i] == ',') {
                    continue;
                }
                str = str + String.valueOf(ch[i]);
            }
            return str;
        } else {
            return word;
        }

    }

    static String removedots(String word) {
        if (word.equals("")) {
            return word;
        }
        String str = "";
        if (word.contains(".")) {
            char[] ch = word.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                if (ch[i] == '.') {
                    continue;
                }
                str = str + String.valueOf(ch[i]);
            }
            return str;
        } else {
            return word;
        }

    }

    static String possesives(String word) {
        if (word.equals("")) {
            return word;
        }
        String str = "";
        if (word.contains("\'s")) {
            char[] ch = word.toCharArray();
            for (int i = 0; i < ch.length - 1; i++) {
                if (ch[i] == '\'' && ch[i + 1] == 's') {
                    i = i + 1;
                    continue;
                }
                str = str + String.valueOf(ch[i]);
            }
            return str;
        } else {
            return word;
        }

    }

    static HashMap<String, Integer> tokens = new HashMap<String, Integer>();

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

    public static int occursOnce(HashMap temp) {
        HashMap<String, Integer> tokens = (HashMap) temp.clone();
        int count = 0;
        Iterator it = tokens.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            int c = (Integer) pair.getValue();
            if (c == 1) {
                count++;
            }

            it.remove(); // avoids a ConcurrentModificationException
        }
        return count;
    }
    /*
     public static String tokenize(String word) {

     //Conver to lower case
     word = word.toLowerCase();

     //Remove .
     // word = word.replaceAll("[^\\w\\s-'.!:;,]+", "");
     //Remove digits
     word = word.replaceAll("[0-9]", "");

     //Remove SGML Tags
     word = word.replaceAll("<[^>]+>", "");

     //word = word.replaceAll("['.]+", "");
     // word = word.replaceAll("['s]+", "");
     return word;
     }
     */

    public static void addToken(String word, HashMap hm) {
        if (word.equals("")) {
            return;
        }
        if (hm.containsKey(word)) {
            int count = (Integer) hm.get(word);
            count++;
            hm.put(word, count);
        } else {
            hm.put(word, 1);
        }
    }

    public static void addToken(String word) {
        if (word.equals("")) {
            return;
        }
        if (tokens.containsKey(word)) {
            int count = tokens.get(word);
            count++;
            tokens.put(word, count);
        } else {
            tokens.put(word, 1);
        }
    }

    public String tokenize(String words) {
        String word = removeDigits(words);

        word = removeSGMLtags(word);
        word = possesives(word);
        word = removechar('.', word);

        word = removechar('_', word);
        word = removechar('!', word);
        word = removechar('|', word);
        word = removechar('(', word);
        word = removechar(')', word);
        word = removechar('{', word);
        word = removechar('}', word);
        word = removechar(':', word);
        word = removechar('\'', word);
        word = removechar('\\', word);
        
        word = removechar(' ', word);
        word = removechar('=', word);
        word = removechar('?', word);
        word = removechar('+', word);
        word = removechar('*', word);
        word = removechar('/', word);
        //word = removechar('\', word);

        if (word.contains("\\/")) {
            System.out.println(word);
        }
        if (word.contains("-")) {
            String[] newTokens = word.split("-");
            for (String newToken : newTokens) {
                if (newToken.contains(",")) {
                    String[] newTokens1 = newToken.split(",");
                    for (String newToken1 : newTokens) {
                        newToken1 = removechar(',', newToken1);
                        if (!newToken1.equals("")) {

                                                    //doctokens = addToken(newToken1, doctokens);
                            //newToken1 = Stem(newToken1);
                            addToken(newToken1);

                            if (!newToken1.equals("")) {
                                if (doctokens.containsKey(newToken1)) {
                                    int count = doctokens.get(newToken1);
                                    count++;
                                    doctokens.put(newToken1, count);
                                } else {
                                    doctokens.put(newToken1, 1);
                                }

                            }
                            //doctokens.put(newToken1, 1);
                            numOfTokens++;
                        }

                    }

                } else {
                    if (!newToken.equals("")) {

                        // newToken = Stem(newToken);
                        addToken(newToken);
                        if (!newToken.equals("")) {
                            if (doctokens.containsKey(newToken)) {
                                int count = doctokens.get(newToken);
                                count++;
                                doctokens.put(newToken, count);
                            } else {
                                doctokens.put(newToken, 1);
                            }

                        }
                        numOfTokens++;
                    }
                }
            }
        } else if (word.contains(",")) {
            String[] newTokens = word.split(",");
            for (String newToken : newTokens) {
                if (!newToken.equals("")) {
                    newToken = removechar(',', newToken);
                                            //System.out.println(newToken);

                    // newToken = Stem(newToken);
                    addToken(newToken);
                    if (!newToken.equals("")) {
                        if (doctokens.containsKey(newToken)) {
                            int count = doctokens.get(newToken);
                            count++;
                            doctokens.put(newToken, count);
                        } else {
                            doctokens.put(newToken, 1);
                        }

                    }
                    numOfTokens++;
                }
                // tokenize(newToken);
            }

        } else {
            if (!word.equals("")) {

                //word = Stem(word);
                addToken(word);
                if (!word.equals("")) {
                    if (doctokens.containsKey(word)) {
                        int count = doctokens.get(word);
                        count++;
                        doctokens.put(word, count);
                    } else {
                        doctokens.put(word, 1);
                    }

                }
                numOfTokens++;
            }
        }

        return word;
    }
}
