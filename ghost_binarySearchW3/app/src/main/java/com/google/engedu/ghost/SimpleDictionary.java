package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        int count = 0;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
                words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        Random random = new Random();
        if (prefix == null) {//if computer is starting use random letter in alphabet
            Random r = new Random();
            char c = (char) (r.nextInt(26) + 'a');
            return Character.toString(c);
        }else{
            int lo = 0;
            int hi = words.size() - 1;
            int count = 0;
            while (lo <= hi) {
                // Key is in a[lo..hi] or not present.
                int mid = lo + (hi - lo) / 2;
                int result = prefix.compareTo(words.get(mid));
                if(words.get(mid).startsWith(prefix)){
                    return words.get(mid);
                }
                if      (result < 0) hi = mid - 1;
                else if (result > 0) lo = mid + 1;
            }
        }
        return "NOWORD";
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        //either find it or don't
        String word = getAnyWordStartingWith(prefix);
        if(word.equals("NOWORD")){
            return "NOWORD";
        }else{
            return word;
        }
    }

}
