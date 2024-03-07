package be.kuleuven.candycrush.model;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;

import be.kuleuven.CheckNeighboursInGrid;

public class CandyCrushModel {
    private String playerName;
    private int score;
    private int highScore;
    private int height;
    private int width;

    private ArrayList<Integer> candyArray;

    public CandyCrushModel(int height, int width, String playerName) {
        this.score = 0;
        this.highScore = 0;
        this.height = height;
        this.width = width;
        this.playerName = playerName;
        candyArray = new ArrayList<Integer>();
        generateCandyArray(height, width);
    }

    public void generateCandyArray(int height, int width){
        candyArray.clear();
        for(int i =0; i<height*width; i++){
            candyArray.add((int)(Math.random()*5)+1);
        }
    }
    public void removeSameNeighbours(int index) {
        Iterable<Integer> sameNeighboursIndices = CheckNeighboursInGrid.getSameNeighboursIds(candyArray, width, height, index);
        int neighbourCounter = 1;
        for (Object i : sameNeighboursIndices) {
            neighbourCounter++;
        }
        System.out.println(neighbourCounter+"");
        if(neighbourCounter >= 3){
            Iterator<Integer> iterator = sameNeighboursIndices.iterator();
            while (iterator.hasNext()) {
                int neighbourIndex = iterator.next();
                candyArray.set(neighbourIndex, (int) (Math.random() * 5)+1);
            }
            candyArray.set(index, (int) (Math.random() * 5)+1);
            increaseScore(neighbourCounter);
        }
    }

    public void increaseScore(int amount) {
        this.score += amount;
        if (this.score > this.highScore) {
            this.highScore = this.score;
        }
    }


    public int getScore() {
        return this.score;
    }

    public int getHighScore() {
        return this.highScore;
    }

    public int getHeight() {
        return this.height;
    }
    public int getWidth() {
        return this.width;
    }

    public ArrayList<Integer> getCandyArray() {
        return candyArray;
    }

    public String getName() {
        return playerName;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
