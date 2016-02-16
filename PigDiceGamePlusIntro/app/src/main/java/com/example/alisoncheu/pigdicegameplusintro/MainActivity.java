package com.example.alisoncheu.pigdicegameplusintro;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static int playerOverallScore;
    private static int playerTurnScore;
    private static int computerOverallScore;
    private static int computerTurnScore;
    private static Random random = new Random();
    private AnimationDrawable diceAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * Handler for user selected Roll button
     * @param view
     */
    public void sendRoll(final View view){
        int diceNum = rollDice();
        if (diceNum != 1) {
            callSwitch(view, diceNum, "player");
        } else {
            updatePlayerValues();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 10 seconds
                    computerTurn(view);

                }
            }, 500);
        }
    }

    /**
     * Function that returns a number on a dice
     * @return  int value of dice
     */
    public int rollDice(){
        int diceNum;
        do{
            diceNum = random.nextInt();
        }while(diceNum < 0);

        return diceNum%6+1;
    }

    /**
     * Function that adjusts the content_main.xml file to correspond to the values
     * rolled and determines winner (whoever reaches a score of 100 first).
     * @param view  View to be adjusted
     * @param diceNum Value of dice to be displayed
     * @param whoTurn String containing which player's turn it is
     */
    public void callSwitch(View view,int diceNum, String whoTurn ){
        ImageView img= (ImageView) findViewById(R.id.dice);
        TextView trnVw= (TextView) findViewById(R.id.turnScore);

        if(whoTurn.equals("player")){
            playerTurnScore += diceNum;
            if(playerOverallScore + playerTurnScore >= 100){
                updatePlayerValues();
                trnVw.setText("Turn score:" + playerTurnScore);
                TextView winView = (TextView) findViewById(R.id.winner);
                winView.setTextColor(Color.GREEN);
                winView.setText("Winner: Player! Hit Reset to Replay");
                stopHold();
                stopRoll();

            }else{
                trnVw.setText("Turn score:" + playerTurnScore);
            }

        }else{
            computerTurnScore +=diceNum;
            if(computerOverallScore + computerTurnScore >= 100){
                updateComputerValues();
                trnVw.setText("Turn score:" + computerTurnScore);
                TextView winView = (TextView) findViewById(R.id.winner);
                winView.setTextColor(Color.RED);
                winView.setText("Winner: Computer! Hit Reset to Replay");
                stopHold();
                stopRoll();
            }else {
                trnVw.setText("Turn score:" + computerTurnScore);
            }
        }

        switch(diceNum){
            case 2:
                img.setImageResource(R.drawable.dice2);
                break;
            case 3:
                img.setImageResource(R.drawable.dice3);
                break;
            case 4:
                img.setImageResource(R.drawable.dice4);
                break;
            case 5:
                img.setImageResource(R.drawable.dice5);
                break;
            case 6:
                img.setImageResource(R.drawable.dice6);
                break;
        }
    }

    /**
     * Function that operates to stimulate computer's turn.
     * computerTurn first prevents roll and hold buttons from being clickable
     * by user. Next, runs a separate thread for the computer roll to ensure that
     * the rolled value is displayed to user for 500 milliseconds each time. Thread continues
     * until diceNum is equal to 1.
     */

    public void computerTurn(final View view){
        stopRoll();
        stopHold();

        final Handler timerHandler = new Handler();
        final Runnable timerRunnable = new Runnable(){
            @Override
            public void run() {
                int diceNum = rollDice();
                if (diceNum != 1) {
                    if(diceNum + computerTurnScore < 20){
                        callSwitch(view, diceNum, "computer");
                        timerHandler.postDelayed(this, 500);
                    }else {
                        updateComputerValues();
                        Thread.currentThread().interrupt();
                    }
                } else {
                    updateComputerValues();
                    Thread.currentThread().interrupt();
                }
            }

        };

        timerHandler.postDelayed(timerRunnable,0);
        resetTurnView();

        allowRoll();
        allowHold();
    }

    /**
     * Handler for user selected Hold button.
     * @param view
     */
    public void sendHold(final View view){
        updatePlayerValues();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                computerTurn(view);

            }
        }, 500);
    }

    /**
     * Handler with user selected Reset button
     * @param view
     */
    public void sendReset(View view){
        playerOverallScore = 0;
        playerTurnScore = 0;
        computerOverallScore = 0;
        computerTurnScore = 0;

        TextView plyrView = (TextView) findViewById(R.id.playerScore);
        plyrView.setText("Your score: " + playerOverallScore);
        TextView compView = (TextView) findViewById(R.id.compScore);
        compView.setText("Computer score:" + computerOverallScore);
        TextView turnView = (TextView) findViewById(R.id.turnScore);
        turnView.setText("");

        ImageView imgView = (ImageView) findViewById(R.id.dice);
        imgView.setImageResource(R.drawable.dice1);

        TextView winView = (TextView) findViewById(R.id.winner);
        winView.setText("");
        allowRoll();
        allowHold();
    }

    /**
     * Updates player overall values by adding turn score and resetting the turn view.
     */
    public void updatePlayerValues(){
        resetTurnView();

        playerOverallScore += playerTurnScore;
        playerTurnScore = 0;

        TextView plyrView = (TextView) findViewById(R.id.playerScore);
        plyrView.setText("Your score: " + playerOverallScore);


    }

    /**
     * Updates computer overall values by adding turn score and resetting the turn view.
     */
    public void updateComputerValues(){
        computerOverallScore +=computerTurnScore;
        computerTurnScore = 0;
        TextView compView = (TextView) findViewById(R.id.compScore);
        compView.setText("Computer score: " + computerOverallScore);
        resetTurnView();
    }

    /**
     * Resets to other player's turn by removing Turn points and setting
     * dice to default image.
     */
    public void resetTurnView(){
        TextView turnView = (TextView) findViewById(R.id.turnScore);
        turnView.setText("");
        ImageView imgView = (ImageView) findViewById(R.id.dice);
        imgView.setImageResource(R.drawable.dice1);
    }

    /**
     * Stops the Roll Button from being clickable
     */
    public void stopRoll(){
        Button stopRoll = (Button) findViewById(R.id.button1);
        stopRoll.setClickable(false);
    }

    /**
     * Sets the Roll Button to be clickable
     */
    public void allowRoll(){
        Button stopRoll = (Button) findViewById(R.id.button1);
        stopRoll.setClickable(true);
    }

    /**
     * Stops the Hold Button from being clickable
     */
    public void stopHold(){
        Button stopHold = (Button) findViewById(R.id.button2);
        stopHold.setClickable(false);
    }

    /**
     * Sets the Hold Button to be clickable.
     */
    public void allowHold(){
        Button stopHold = (Button) findViewById(R.id.button2);
        stopHold.setClickable(true);
    }

}
