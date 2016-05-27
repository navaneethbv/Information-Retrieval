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
public class Query<T1,T2> {
    int qid;
    TreeMap<T1,T2> tw1;
    TreeMap<T1,T2> tw2;
    public Query(int qid){
        this.qid = qid;
        tw1 = new TreeMap<T1,T2>();
        tw2 = new TreeMap<T1,T2>();
    }
}
