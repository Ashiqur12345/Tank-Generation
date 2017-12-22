import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ashiqur Rahman on 7/25/2016.
 */
public class SaveGame implements Serializable{
    public int score;
    public int level;
    public int noOfEnemyTank, noOfAliveEnemyTank;

    public ArrayList <Tank> enemyTanks;
    public ArrayList <Bullet> enemyBullets;

    public Tank goodTank;
    public int mineCounter;
    public int nukeCounter;
    public ArrayList <LandMine> mines;
    public ArrayList <Bullet> bullets;

    public ArrayList <Gift> gifts;
    public MyTimer[] myTimers;
    public int difficulty;
    public Dimension dim;
    public ArrayList <Obstacle> obstacles;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNoOfEnemyTank() {
        return noOfEnemyTank;
    }

    public void setNoOfEnemyTank(int noOfEnemyTank) {
        this.noOfEnemyTank = noOfEnemyTank;
    }

    public int getNoOfAliveEnemyTank() {
        return noOfAliveEnemyTank;
    }

    public void setNoOfAliveEnemyTank(int noOfAliveEnemyTank) {
        this.noOfAliveEnemyTank = noOfAliveEnemyTank;
    }

    public ArrayList<Tank> getEnemyTanks() {
        return enemyTanks;
    }

    public void setEnemyTanks(ArrayList<Tank> enemyTanks) {
        this.enemyTanks = enemyTanks;
    }

    public ArrayList<Bullet> getEnemyBullets() {
        return enemyBullets;
    }

    public void setEnemyBullets(ArrayList<Bullet> enemyBullets) {
        this.enemyBullets = enemyBullets;
    }

    public Tank getGoodTank() {
        return goodTank;
    }

    public void setGoodTank(Tank goodTank) {
        this.goodTank = goodTank;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public ArrayList<LandMine> getMines() {
        return mines;
    }

    public void setMines(ArrayList<LandMine> mines) {
        this.mines = mines;
    }

    public int getMineCounter() {
        return mineCounter;
    }

    public void setMineCounter(int mineCounter) {
        this.mineCounter = mineCounter;
    }

    public int getNukeCounter() {
        return nukeCounter;
    }

    public void setNukeCounter(int nukeCounter) {
        this.nukeCounter = nukeCounter;
    }

    public ArrayList<Gift> getGifts() {
        return gifts;
    }

    public void setGifts(ArrayList<Gift> gifts) {
        this.gifts = gifts;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Dimension getDim() {
        return dim;
    }

    public void setDim(Dimension dim) {
        this.dim = dim;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public void setObstacles(ArrayList<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    public MyTimer[] getMyTimers() {
        return myTimers;
    }

    public void setMyTimers(MyTimer[] myTimers) {
        this.myTimers = myTimers;
    }
}
