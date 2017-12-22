import java.awt.Color;
import java.io.Serializable;
import java.util.Random;

/**
 * Created by Ashiqur Rahman on 5/8/2016.
 */
public class Tank implements Serializable{
    public int x;
    public int y;
    public int speed;
    public int moveDirection;   // 0-up  1-down 2-left  3-right
    public int width;
    public int height;
    public int bodyArc;
    public int life;
    public int level;
    public Color color;

    private Random rand;

    public Tank(int x, int y, int speed, int movDir, int width, int height, int life, int level, Color color) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.moveDirection = movDir;
        this.width = width;
        this.height = height;
        this.life = life;
        this.level = level;
        this.color = color;
        this.bodyArc = 10; //Body corner roundness
        rand = new Random();
    }

    public void resetTankState(int x, int y, int speed, int movDir, int width, int height, int life, int level, Color color){
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.moveDirection = movDir;
        this.width = width;
        this.height = height;
        this.life = life;
        this.level = level;
        this.color = color;
    }

    public void moveTank(){
        if(this.moveDirection == 0) this.y -= this.speed;
        else if(this.moveDirection == 1) this.y += this.speed;
        else if(this.moveDirection == 2) this.x -= this.speed;
        else if(this.moveDirection == 3) this.x += this.speed;
    }

    public void tankDirectionChange_Obstacle(){

        int dir = rand.nextInt(4);
        for (int i = 0; i < 10; i++) {
            if(dir == this.moveDirection){
                dir = rand.nextInt(4);
            }
            else{
                break;
            }
        }
        setDirectionAndFixDiameter(dir);
    }


    public void tankDirectionChange_Border(int fx1, int fx2, int fy1, int fy2){
        if(this.x < fx1){
            setDirectionAndFixDiameter(rand.nextInt(4));
            this.x = fx1 + 10;
        }
        else if(this.x > fx2){
            setDirectionAndFixDiameter(rand.nextInt(4));
            this.x = fx2 - 10;
        }

        if(this.y < fy1){
            setDirectionAndFixDiameter(rand.nextInt(4));
            this.y = fy1 + 10;
        }
        else if(this.y >= fy2){
            setDirectionAndFixDiameter(rand.nextInt(4));
            this.y = fy2 -10;
        }
    }

    public void tankDirectionChange(int enemyX, int enemyY, int difficulty){

        int generatedDirection = -1;
        int randomRange = 500;

        int ifPrimeChangeDir = rand.nextInt(randomRange/this.level) + 2;

        if(ifPrimeChangeDir == 11 || ifPrimeChangeDir == 17 || ifPrimeChangeDir == 19 || ifPrimeChangeDir == 37 || ifPrimeChangeDir == 47){
            if((difficulty == 3 && this.level <= 10) || (difficulty == 2 && this.level <= 5)){
                generatedDirection = directionGenerator_random(this.moveDirection);
            }
            else{
                generatedDirection = directionGenerator_AI1(this.x, this.y, enemyX, enemyY);
            }
        }

        if(generatedDirection != -1){
            setDirectionAndFixDiameter(generatedDirection);
        }
    }


    private int directionGenerator_AI1(int tankX, int tankY, int enemyX, int enemyY) {
        System.out.println("directionGenerator_AI1");
        int yDistance = enemyY - tankY;
        int xDistance = enemyX - tankX;

        if(yDistance >= 0){
            if(xDistance >= 0){
                if(yDistance >= xDistance)return 1;
                else return 3;
            }
            else{
                xDistance *= -1;
                if(yDistance >= xDistance)return 1;
                else return 2;
            }
        }
        else{
            yDistance *= -1;
            if(xDistance >= 0){
                if(yDistance >= xDistance)return 0;
                else return 3;
            }
            else{
                xDistance *= -1;
                if(yDistance >= xDistance)return 0;
                else return 2;
            }
        }
    }

    private int directionGenerator_random(int prevDirection){
        return rand.nextInt(4);
    }

    public void setDirectionAndFixDiameter(int newDirection) {

        int temp;
        if((newDirection == 0 || newDirection == 1) && !(this.moveDirection == 0 || this.moveDirection == 1)){
            temp = this.height;
            this.height = this.width;
            this.width = temp;
        }
        if((newDirection == 2 || newDirection == 3) && !(this.moveDirection == 2 || this.moveDirection == 3)){
            temp = this.height;
            this.height = this.width;
            this.width = temp;
        }
        this.moveDirection = newDirection; // Assign new Direction
    }


    @Override
    public String toString() {
        return "Tank{" +
                "life=" + life +
                '}';
    }
}


