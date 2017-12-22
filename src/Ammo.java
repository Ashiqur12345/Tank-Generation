import java.io.Serializable;

/**
 * Created by Ashiqur Rahman on 3/18/2017.
 */
public class Ammo implements Serializable{
    public int x;
    public int y;
    public int hitPoint;

    public Ammo(int x, int y, int hitPoint) {
        this.x = x;
        this.y = y;
        this.hitPoint = hitPoint;
    }
}
