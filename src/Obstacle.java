import java.awt.*;
import java.io.Serializable;

/**
 * Created by Ashiqur Rahman on 3/16/2017.
 */
public class Obstacle implements Serializable{
    public int x;
    public int y;
    public int width;
    public int height;
    public int type;
    public int health;
    transient public Image image;

    public Obstacle(int x, int y, int width, int height, int type, int health, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.health = health;
        this.image = image;
    }

    public void setImage(Image image){
        this.image = image;
    }
}
