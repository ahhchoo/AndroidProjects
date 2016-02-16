package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private FastDictionary fastDictionary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        try {
            InputStream inputS = getAssets().open("words.txt");
            fastDictionary = new FastDictionary(inputS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        onStart(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char pressedKey = (char) event.getUnicodeChar();
        String newFrag;
        if(Character.isLetter(pressedKey)) {
            TextView wordFrag = (TextView) findViewById(R.id.ghostText);
            newFrag = wordFrag.getText() + Character.toString(pressedKey);
            wordFrag.setText(newFrag);
            computerTurn();
            return true;
        }else{
            return super.onKeyUp(keyCode, event);
        }

    }

    /**
     * Simulates computer's turn on game. If computer starts, generates a random letter
     * in the alphabet and adds it.
     *
     * Computer WINS if conditions a and b, or c are met with the word fragment:
     * a) is a word in the dictionary
     * b) length is greater than or equal to 4
     * c) no other word can be made
     *
     *  Otherwise, concatenates a new letter to the current fragment.
     */
    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView wordFrag = (TextView) findViewById(R.id.ghostText);

        String word = wordFrag.getText().toString();
        int wordLength = wordFrag.getText().length();

        //CASE TO CHECK IF EMPTY
        if(wordLength < 1) {
            Random r = new Random();
            char c = (char) (r.nextInt(26) + 'a');
            wordFrag.setText(Character.toString(c));
        }else {
            boolean isWord = fastDictionary.isWord(word);
            if (isWord && (wordLength >= 4)) {
                label.setText("Computer Wins");
            } else {
                String resultSearch = fastDictionary.getAnyWordStartingWith(word);
                if (resultSearch.contains("NOWORD")) {
                    label.setText("Computer Wins");
                    //diff than if statement because pc knows word doesn't exist
                    //and not whether user's word is valid
                } else {
                    wordFrag.setText(resultSearch);
                    // Do computer turn stuffs then make it the user's turn again
                    userTurn = true;
                    label.setText(USER_TURN);
                }
            }
        }
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    /**
     * Handler for button Challenge.
     * Determines whether current word fragment is an actual word or not.
     * Based off the results found, adjusts for the winner accordingly.
     *
     * @param view
     */
    public void goChallenge(View view){
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView wordFrag = (TextView) findViewById(R.id.ghostText);
        String word = wordFrag.getText().toString();

        boolean isWord = fastDictionary.isWord(word);
        int length = word.length();

        if(isWord && length >=4){
            label.setText("User wins! Word is valid");
        }else{
            String resultSearch = fastDictionary.getGoodWordStartingWith(word);
            if(resultSearch.equals("NOWORD")){
                label.setText("User wins! Cannot find word formed with fragment" + word +" as prefix");
            }else{
                label.setText("Computer wins! Possible word: " + resultSearch);

            }
        }
    }
}
