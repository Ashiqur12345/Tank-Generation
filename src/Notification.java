import java.awt.*;

/**
 * Created by Ashiqur Rahman on 3/21/2017.
 */
public class Notification {
    public String message;
    public Color color;
    public Font font;
    public float x;
    public float y;

    public Notification(String message, Color color, Font font, int x, int y) {
        this.message = message;
        this.color = color;
        this.font = font;
        this.x = (float)x;
        this.y = (float)y;
    }
}
