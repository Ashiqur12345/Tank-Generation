import java.io.Serializable;
import java.util.*;

/**
 * Created by Ashiqur Rahman on 2/8/2017.
 */
public class Gift implements Serializable {
    public int timeLimit;
    public int type; // 0 for nothing,1 for life, 2 for nuke, 3 for landmine, 4 for shield, 5 for score boost
    public boolean active;
    public int value;

    public int x;
    public int y;
    transient public Timer timer;
    transient private TimerTask task;

    public Gift(int timeLimit, int type, int value, int x, int y ) {
        this.timeLimit = timeLimit;
        this.type = type;
        this.value = value;
        this.x = x;
        this.y = y;
        this.active = true;
    }

    private void runThis() {
        //System.out.println("Running: "+this);
        if(!active)pause();

        if(timeLimit > 0) {
            timeLimit --;
            //System.out.println("Time Remaining: "+ timeLimit);
        }
        else{
            timer.cancel();
            active = false;
            return;
        }
    }

    public void start(){
        //System.out.println("Starting");
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                runThis();
            }
        };
        timer.schedule(task, 0, 1000);
    }

    public void pause(){
        //System.out.println("Pausing");
        this.timer.cancel();
    }

    public void resume(){
        //System.out.println("Resuming");
        timer = new Timer();
        timer.schedule( new TimerTask(){
            public void run(){
                runThis();
            }
        },0,  1000 );
    }

    /*public static void main (String[] a){
        Gift gift = new Gift(5, 0, 0, 0);
        gift.start();
        gift.pause();
        gift.resume();
    }*/
}







    /*public void requestLife(int gameLevel, int windowX, int windowY) {

        if(gameLevel > lifeGivenLevel){
            this.lifeGift = true;
            this.lifeGivenLevel = gameLevel;
            lifeGiftCounter = this.gameDifficulty * 10;
            this.lifeGiftX = rand.nextInt(windowX - 150) + 50;
            this.lifeGiftY = rand.nextInt(windowY - 150) + 50;
        }
    }

    public void requestRandomItem(int gameLevel, int x, int y){
        this.lifeGift = true;
        this.lifeGivenLevel = gameLevel;
        lifeGiftCounter = this.gameDifficulty * 10;
        this.lifeGiftX = x;
        this.lifeGiftY = y;
    }*/
