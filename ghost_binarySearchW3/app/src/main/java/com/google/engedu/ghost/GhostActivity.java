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
    private SimpleDictionary simpleDictionary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        try {
            InputStream inputS = getAssets().open("words.txt");
            simpleDictionary = new SimpleDictionary(inputS);
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


    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView wordFrag = (TextView) findViewById(R.id.ghostText);

        String word = wordFrag.getText().toString();
        int wordLength = wordFrag.getText().length();

        boolean isWord = simpleDictionary.isWord(word);

        if(isWord && wordLength>=4){
            label.setText("Computer Wins");
        }else{
            String resultSearch = simpleDictionary.getAnyWordStartingWith(word);
            if(resultSearch.contains("NOWORD")){
                label.setText("Computer Wins");
                //diff than if statement because pc knows word doesn't exist
                //and not whether user's word is valid
            }else{
                String addWord = resultSearch.substring(0,word.length()+1);
                wordFrag.setText(addWord);
                // Do computer turn stuff then make it the user's turn again
                userTurn = true;
                label.setText(USER_TURN);
            }
        }
    }

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


    public void goChallenge(View view){
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView wordFrag = (TextView) findViewById(R.id.ghostText);
        String word = wordFrag.getText().toString();

        boolean isWord = simpleDictionary.isWord(word);
        int length = word.length();

        if(isWord && length >=4){
            label.setText("User wins! Word is valid");
        }else{
            String resultSearch = simpleDictionary.getGoodWordStartingWith(word);
            if(resultSearch.equals("NOWORD")){
                label.setText("User wins! Cannot find word formed with fragment" + word +" as prefix");
            }else{
                label.setText("Computer wins! Possible word: " + resultSearch);

            }
        }
    }
}
