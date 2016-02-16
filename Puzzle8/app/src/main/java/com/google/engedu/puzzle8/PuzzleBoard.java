package com.google.engedu.puzzle8;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

//represents the current state of the puzzle board
public class PuzzleBoard {

    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };
    private ArrayList<PuzzleTile> tiles;

    PuzzleBoard(Bitmap bitmap, int parentWidth) {

        int pieces = NUM_TILES * NUM_TILES;

        tiles = new ArrayList<>(pieces);
        int newWidth = parentWidth/NUM_TILES;
        int newHeight = parentWidth/NUM_TILES;


        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, true);
        Bitmap[] imgs = new Bitmap[pieces];

        //do math

        imgs[0] = Bitmap.createBitmap(scaledBitmap, 0, 0, 80 , 80);
        imgs[1] = Bitmap.createBitmap(scaledBitmap, 80, 0, 80, 80);
        imgs[2] = Bitmap.createBitmap(scaledBitmap,160, 0, 80,80);
        imgs[3] = Bitmap.createBitmap(scaledBitmap, 0, 80, 80, 80);
        imgs[4] = Bitmap.createBitmap(scaledBitmap, 80, 80, 80,80);
        imgs[5] = Bitmap.createBitmap(scaledBitmap, 160, 80,80,80);
        imgs[6] = Bitmap.createBitmap(scaledBitmap, 0, 160, 80,80);
        imgs[7] = Bitmap.createBitmap(scaledBitmap, 80, 160,80,80);
        imgs[8] = Bitmap.createBitmap(scaledBitmap, 160,160,80,80);


        //for(int index = 0 ; index < pieces ; index++){
           // imgs[index] = Bitmap.createBitma

       // }
        for(int i =0 ; i< pieces; i++) {
           bitmap = Bitmap.createScaledBitmap(imgs[i], newWidth, newHeight, true);
            if(i == pieces-1){
                tiles.add(null);
            }else{
                PuzzleTile t = new PuzzleTile(bitmap,i+1);
                tiles.add(t);
            }
        }

    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public ArrayList<PuzzleBoard> neighbours() {
        /*
        a method that returns an ArrayList of all the PuzzleBoard configurations
         that are possible by moving one tile in the current PuzzleBoard.

         So implement the neighbours method to:
        locate the empty square in the current board
        consider all the neighbours of the empty square (using the NEIGHBOUR_COORDS array)
        if the neighbouring square is valid (within the boundaries of the puzzle),

        -make a copy of the current board (using the provided copy constructor),
        -move the tile in that square to the empty square and add this copy of the board to the list of neighbours to be returned
         */
       // for(int i =1; i < tiles.size()+1; i++){
           // tiles.
       // }
        PuzzleBoard pb = new PuzzleBoard(this);

        return null;
    }

    public int priority() {
        return 0;
    }

}
