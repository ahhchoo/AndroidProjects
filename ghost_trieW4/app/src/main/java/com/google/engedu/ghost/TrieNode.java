package com.google.engedu.ghost;

import android.util.Log;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    /**
     * Constructor for TrieNode
     */
    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    /**
     * Adds a string in Trie data structure. Recursively traverses nodes
     * then adds nodes until entire string is traversed.
     * @param s String value to observe
     */
    public void add(String s) {
        char c = s.charAt(0);
        String concat = Character.toString(c);

        if(children.containsKey(concat)){
            TrieNode t = children.get(concat);
            c = s.charAt(1);
            concat = Character.toString(c);
            s = s.substring(1);
            t.add(s);
        }else {
            TrieNode temp = new TrieNode();
            children.put(concat, temp);
            if(s.length()==1){
                temp.setTrue();
                return ;
            }else {
                s = s.substring(1);
                temp.add(s);
            }
        }
    }

    void setTrue(){
        isWord = true;
    }

    /**
     * Recursively traverses tree until it reaches a leaf
     * and returns isWord variable of leaf node
     * @param s String to traverse
     * @return Result of traversal to leaf node
     */
    public boolean isWord(String s) {
        boolean check;
        char c = s.charAt(0);
        String concat = Character.toString(c);

        if(children.containsKey(concat)){
            TrieNode t = children.get(concat);
            if(s.length() == 1) {
                return t.isWord;
            }else{
                concat = s.substring(1);
                check = t.isWord(concat);
            }
        }else {
            return false;
        }

        return check;
    }

    /**
     * Uses preorder traversal to find next letter needed for string s
     * @param s String to use to search for next letter
     * @return String containing next letter
     */
    public String getAnyWordStartingWith(String s) {
        char c = s.charAt(0);
        String concat = Character.toString(c);

        if(children.containsKey(concat)){
            TrieNode temp = children.get(concat);
            if(s.length()==1) {
                int childSize = temp.children.size();
                Object[] arr =  temp.children.keySet().toArray();
                Random r = new Random();
                int num = r.nextInt(childSize);
                String sb = arr[num].toString();
                return concat + sb; //child of concat
            }else {
                String cut = s.substring(1);
                return concat + temp.getAnyWordStartingWith(cut);
            }
        }else{
            return "NOWORD";
        }
    }

    /**
     * Uses parameter s to initialize traversal of tree. Once reaching the
     * end of traversal, continues traversal until it reaches a leaf node.
     * @param s Prefix word to search for
     * @return Result of word search
     */
    public String getGoodWordStartingWith(String s) {
        char c = s.charAt(0);
        String concat = Character.toString(c);
        boolean what = children.containsKey(concat);
        if (children.containsKey(concat)) {
            TrieNode temp = children.get(concat);
            if (s.length() == 1) {
                return concat + temp.findRestOfWord();
            } else {
                String test = s.substring(1);
                return concat + temp.getGoodWordStartingWith(test);
            }
        } else {
            return "NOWORD";
        }
    }

    /**
     * Helper function for getGoodWordStartingWith(String s). Continues traversal
     * of tree until reaches leaf node
     * @return latter end of word found
     */
    public String findRestOfWord(){
        if(isWord){
            return "";
        }else {
            Random r = new Random();
            if(children.size() == 0){
                return "NOWORD";
            }else {
                Object[] arr = children.keySet().toArray();
                int index = r.nextInt(children.size());

                String let = arr[index].toString();
                TrieNode temp = children.get(let);
                return let + temp.findRestOfWord();
            }
        }
    }

}
