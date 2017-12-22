import java.io.Serializable;
import java.util.*;

/**
 * Created by Ashiqur Rahman on 2/8/2017.
 */
public class MyTimer implements Serializable {
    public int timeLimit;
    public int type; //0 for notification, 4 for shield, 5 for score boost
    public boolean active;
    public int value;

    transient public Timer timer;
    transient private TimerTask task;

    public MyTimer(int timeLimit, int type, int value) {
        this.timeLimit = timeLimit;
        this.type = type;
        this.active = true;
        this.value = value;
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

}

