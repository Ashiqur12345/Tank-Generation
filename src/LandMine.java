import java.io.Serializable;

/**
 * Created by Ashiqur Rahman on 3/18/2017.
 */
public class LandMine extends Ammo implements Serializable {

    public int width;
    public int height;

    public LandMine(int x, int y, int hitPoint) {
        super(x, y, hitPoint);
        this.width = 30;
        this.height = 30;
    }
}
