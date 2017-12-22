import java.io.Serializable;

/**
 * Created by Ashiqur Rahman on 4/29/2016.
 */
public class Bullet extends Ammo implements Serializable{

    public int radius;
    public int speed;
    public int moveDir;             // 0-up  1-down 2-left  3-right


    public Bullet(int x, int y, int radius, int speed, int moveDir, int hitPoint) {
        super(x,y,hitPoint);
        this.radius = radius;
        this.speed = speed;
        this.moveDir = moveDir;
    }

    public void moveBullet(){
        if(this.moveDir == 0) this.y -= this.speed;
        else if(this.moveDir == 1) this.y += this.speed;
        else if(this.moveDir == 2) this.x -= this.speed;
        else if(this.moveDir == 3) this.x += this.speed;
    }
}
