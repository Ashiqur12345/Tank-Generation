import java.awt.*;

/**
 * Created by Ashiqur Rahman on 7/23/2016.
 */
public class Shape {
    public int x, y, length, lengthMax, increaseRate;
    public Color color;

    public Shape(int x, int y, int length, Color color) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.color = color;
        lengthMax = 800;
        increaseRate = 7;
    }
}
