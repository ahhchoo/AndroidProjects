package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;

import android.util.Log;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static HashSet<String> wordSet = new HashSet<>();
    private static HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private static HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();
    private static Integer wordLength = new Integer(DEFAULT_WORD_LENGTH);

    /**
     * Constructor for AnagramDictionary that takes word and puts it into three different structures
     *  with lettersToWord with key being the alphbetical order of word,
     *  sizeToWords with key being the length of word, and wordSet being a HashSet of words.
     *
     * @param wordListStream    inputstream reading words
     * @throws IOException      exception handling
     */
    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        Integer tempWordLength;

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            String order = getOrder(word);

            //HashMap according to word length
            tempWordLength = new Integer(word.length());
            if(sizeToWords.containsKey(tempWordLength)){
                sizeToWords.get(word.length()).add(word);
            }else{
                ArrayList<String> enter = new ArrayList<>();
                enter.add(word);
                sizeToWords.put(tempWordLength, enter);
            }

            //HashMap according to alphabetical order
            if(lettersToWord.containsKey(order)){
                lettersToWord.get(order).add(word);
            }else {
                ArrayList<String> enter = new ArrayList();
                enter.add(word);
                lettersToWord.put(order, enter);
            }

        }

    }

    /**
     * Returns a String of the input in alphabetical order
     * @param word  input to be ordered
     * @return      String that is the word in alphabetical order
     */
    public String getOrder(String word){
        char[] arr = word.toLowerCase().toCharArray();
        Arrays.sort(arr);
        String temp = new String(arr);
        return temp;
    }

    /**
     * Determines whether input word exists in Dictionary and
     * if so, does not contain the input base.
     * @param word
     * @param base
     * @return
     */
    public boolean isGoodWord(String word, String base) {
        boolean exist = wordSet.contains(word);
        if(exist){
           boolean hasBase =  word.contains(base);
            if(hasBase){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    /**
     * Iterating through the alphabet, function appends a letter to the parameter word,
     * retrieves ArrayList if alphabetical ordering of word exists and appends it to the result.
     * Afterwards, result is iterated over to remove any values that contain the ordering of
     * @param word
     * @return
     */
    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        String find = word;
        String ordr;
        for(int i =0; i< alphabet.length; i++){
            find = word + Character.toString(alphabet[i]);
            ordr  = getOrder(find);
            boolean doesContainKey = lettersToWord.containsKey(ordr);
            if(doesContainKey){
                result.addAll(lettersToWord.get(ordr));
            }else;

        }

        for(int i =0; i<result.size(); ++i){
            String temp = result.get(i);
            if(temp.contains(word)) {
                result.remove(i);
                --i;
            }
        }
        return result;
    }

    /**
     * Returns a word that has length of wordLength and has at least min num of Anagrams.
     * Every call of this function will increase the wordLength. If wordLength has reached
     * the MAX_WORD_LENGTH, value will reset to DEFAULT_WORD_LENGTH. If initial word does not
     * meet the min num of Anagrams, function will go iterate to next index of set of words until
     * appropriate one is found.
     * @return  String for starter word
     */
    public String pickGoodStarterWord() {

        if(wordLength.intValue() > MAX_WORD_LENGTH){
            wordLength = new Integer(DEFAULT_WORD_LENGTH);
        }

        int size = sizeToWords.get(wordLength).size();
        int rand = random.nextInt();

        //generates appropriate random number
        while(rand < 0 ){
            rand = random.nextInt();
        }

        int index = rand%size;

        String checkWord = sizeToWords.get(wordLength).get(index);
        ArrayList<String> result = getAnagramsWithOneMoreLetter(checkWord);

        while(result.size() < MIN_NUM_ANAGRAMS){
            index++;
            if(index == sizeToWords.get(wordLength).size()){
                index = 0;
            }
            checkWord = sizeToWords.get(wordLength).get(index);
            result = getAnagramsWithOneMoreLetter(checkWord);
        }
        wordLength = new Integer(wordLength.intValue()+1);
        return checkWord;

    }
}
