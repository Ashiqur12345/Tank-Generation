import javax.imageio.ImageIO;
import java.awt.*;
import java.io.Serializable;
import java.util.Random;

/**
 * Created by Ashiqur Rahman on 4/9/2016.
 */
public class ImportImages implements Serializable{

    public Image fieldImage;
    public Image borderImage;
    public Image instructionImage;
    public Image flameImage;
    public Image shieldGlowImage;
    public Image x2Image;
    public Image x3Image;

    public Image nukeImage;
    public Image mineImage;
    public Image shieldImage;
    public Image healthImage;
    public Image scoreBoostImage;

    public Image[] obstacleImages;

    public Random rand;

    public ImportImages() {
        rand = new Random();
        try {
            fieldImage = ImageIO.read(getClass().getResource("images/field.png"));
            borderImage = ImageIO.read(getClass().getResource("images/border.png"));
            instructionImage = ImageIO.read(getClass().getResource("images/instructions.png"));
            flameImage = ImageIO.read(getClass().getResource("images/flame.png"));
            shieldGlowImage = ImageIO.read(getClass().getResource("images/shieldGlow.png"));
            x2Image = ImageIO.read(getClass().getResource("images/x2.png"));
            x3Image = ImageIO.read(getClass().getResource("images/x3.png"));

            nukeImage = ImageIO.read(getClass().getResource("images/gift/nuke.png"));
            mineImage = ImageIO.read(getClass().getResource("images/gift/mine.png"));
            shieldImage = ImageIO.read(getClass().getResource("images/gift/shield.png"));
            healthImage = ImageIO.read(getClass().getResource("images/gift/health.png"));
            scoreBoostImage = ImageIO.read(getClass().getResource("images/gift/scoreBoost.png"));

            obstacleImages = new Image[12];
            obstacleImages[0] = ImageIO.read(getClass().getResource("images/obstacles/obstacle0.png"));
            obstacleImages[1] = ImageIO.read(getClass().getResource("images/obstacles/obstacle1.png"));
            obstacleImages[2] = ImageIO.read(getClass().getResource("images/obstacles/obstacle2.png"));
            obstacleImages[3] = ImageIO.read(getClass().getResource("images/obstacles/obstacle3.png"));
            obstacleImages[4] = ImageIO.read(getClass().getResource("images/obstacles/obstacle4.png"));
            obstacleImages[5] = ImageIO.read(getClass().getResource("images/obstacles/obstacle5.png"));
            obstacleImages[6] = ImageIO.read(getClass().getResource("images/obstacles/obstacle6.png"));
            obstacleImages[7] = ImageIO.read(getClass().getResource("images/obstacles/obstacle7.png"));
            obstacleImages[8] = ImageIO.read(getClass().getResource("images/obstacles/obstacle8.png"));
            obstacleImages[9] = ImageIO.read(getClass().getResource("images/obstacles/obstacle9.png"));
            obstacleImages[10] = ImageIO.read(getClass().getResource("images/obstacles/obstacle10.png"));
            obstacleImages[11] = ImageIO.read(getClass().getResource("images/obstacles/obstacle11.png"));
        }
        catch (Exception e){
            System.err.println("Image Import Error!");
        }
    }

    public Image getRandomObstacleImage(){
        return obstacleImages[rand.nextInt(12)];
    }

}
