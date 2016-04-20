
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Navaneeth
 */

import java.util.*;
public class Term {
    int DocFrequency;
    
    String TermName;
    TreeMap postingfile;
    public Term(String name){
        this.TermName = name;
        postingfile = new TreeMap();
    }
    public String getName(){
        return TermName;
    }
}
