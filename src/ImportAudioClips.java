import javax.swing.*;
import java.applet.AudioClip;

/**
 * Created by Ashiqur Rahman on 2/2/2017.
 */
public class ImportAudioClips {

    public AudioClip shootPlayer;
    public AudioClip shootEnemy;
    public AudioClip emptyBullet;
    public AudioClip reload;
    public AudioClip reload2;
    public AudioClip obstacleDamage;

    public AudioClip collisionTankTank;
    public AudioClip hitPlayerTank;
    public AudioClip hitEnemyTank;

    public AudioClip destroyEnemy;
    public AudioClip destroyPlayer;

    public AudioClip nukeExplosion;

    public AudioClip danger, levelUp;
    public AudioClip backgroundInGame, backgroundUI;
    public AudioClip gameOver;

    public ImportAudioClips() {
        shootPlayer = JApplet.newAudioClip(getClass().getResource("audios/fireShell.wav"));
        shootEnemy = JApplet.newAudioClip(getClass().getResource("audios/shootEnemy.wav"));
        emptyBullet = JApplet.newAudioClip(getClass().getResource("audios/emptyBullet.wav"));
        reload = JApplet.newAudioClip(getClass().getResource("audios/reload.wav"));
        reload2 = JApplet.newAudioClip(getClass().getResource("audios/reload2.wav"));
        obstacleDamage = JApplet.newAudioClip(getClass().getResource("audios/obstacleDamage.wav"));

        collisionTankTank = JApplet.newAudioClip(getClass().getResource("audios/collisionTankTank.wav"));
        hitEnemyTank = JApplet.newAudioClip(getClass().getResource("audios/hitEnemyTank.wav"));
        hitPlayerTank = JApplet.newAudioClip(getClass().getResource("audios/hitPlayerTank.wav"));

        destroyEnemy = JApplet.newAudioClip(getClass().getResource("audios/bigExplosion2.wav"));
        destroyPlayer = JApplet.newAudioClip(getClass().getResource("audios/bigExplosion.wav"));

        nukeExplosion = JApplet.newAudioClip(getClass().getResource("audios/nukeExplosion.wav"));

        danger = JApplet.newAudioClip(getClass().getResource("audios/danger.wav"));
        levelUp = JApplet.newAudioClip(getClass().getResource("audios/levelUp.wav"));

        backgroundInGame = JApplet.newAudioClip(getClass().getResource("audios/backgroundInGame.wav"));
        backgroundUI = JApplet.newAudioClip(getClass().getResource("audios/backgroundUI.wav"));
        gameOver = JApplet.newAudioClip(getClass().getResource("audios/gameOver.wav"));

    }
}
