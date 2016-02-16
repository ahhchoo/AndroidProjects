package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class FastDictionary implements GhostDictionary {

    private TrieNode root;

    public FastDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        root = new TrieNode();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
                root.add(line.trim());
        }
    }
    @Override
    public boolean isWord(String word) {
        return root.isWord(word);

    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        String result = root.getAnyWordStartingWith(prefix);
        if (result.contains("NOWORD")) {
            return "NOWORD";
        } else {
            return result;
        }

    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return root.getGoodWordStartingWith(prefix);
    }
}
