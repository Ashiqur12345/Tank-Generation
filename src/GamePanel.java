import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Ashiqur Rahman on 4/28/2016.
 */
public class GamePanel extends JPanel implements ActionListener, Serializable{

    private int prevX;
    private int prevY;
    private float factorX;
    private float factorY;
    private int windowX;
    private int windowY;
    private int darkBorder;

    private int score;
    private int gameDifficulty;
    private int topScore;
    private int gameLevel;
    private int noOfEnemyTank;
    private int noOfAliveEnemyTank;

    private boolean gameOver;
    private boolean pause;
    private boolean audioEnabled;
    private boolean playGameOverMusic;

    private Random rand;
    private Tank goodTank;

    private Font enemyLifeFont, scoreLifeFont, loadingFont;

    private ArrayList<Gift> gifts;
    private MyTimer[] myTimers;// [0] for shield, [1] for score boost
    private ArrayList <Tank> enemyTanks;
    private MyTimer waitTimer;

    //Ammunition
    private int nukeCounter;
    private int mineCounter;
    private ArrayList <LandMine> mines;
    private ArrayList <Bullet> goodBullets;
    private ArrayList <Bullet> enemyBullets;

    private ArrayList <Obstacle> obstacles;
    private ImportAudioClips audioClips;
    private ImportImages images;
    private MainMenu mainMenu;
    private LinkedList<Notification> notifications;

    /***********************/
    public GamePanel(int top, int difficulty, boolean audioEnabled, ImportAudioClips audios){

        initGame(top, audioEnabled, audios, new Dimension(800,600));

        this.score = 0;
        this.gameLevel = 1;
        this.noOfEnemyTank = 1;
        this.noOfAliveEnemyTank = 1;
        this.gameDifficulty = difficulty;
        this.nukeCounter = 0;
        this.mineCounter = 0;
        this.mines = new ArrayList<>();
        this.goodTank = new Tank(200, 200, 10, 3, 50, 40, 100, 1, Color.BLACK);
        this.goodBullets = new ArrayList<>();
        this.enemyTanks = new ArrayList<>();
        this.enemyTanks.add(badTankCreate());
        this.enemyBullets = new ArrayList<>();
        this.gifts = new ArrayList<>();
        this.myTimers = new MyTimer[3];
        this.obstacles = new ArrayList<>();
        refillObstacles();

        revalidate();
        actionListeners();
    }
    public GamePanel(SaveGame saveGame, int top, boolean audioEnabled, ImportAudioClips audios){

        initGame(top, audioEnabled, audios, saveGame.dim);

        this.score = saveGame.score;
        this.gameLevel = saveGame.level;
        this.noOfEnemyTank = saveGame.noOfEnemyTank;
        this.noOfAliveEnemyTank = saveGame.noOfAliveEnemyTank;
        this.gameDifficulty = saveGame.difficulty;
        this.nukeCounter = saveGame.getNukeCounter();
        this.mineCounter = saveGame.mineCounter;
        this.mines = saveGame.mines;
        this.goodTank = saveGame.goodTank;
        this.goodBullets = saveGame.bullets;
        this.enemyTanks = saveGame.enemyTanks;
        this.enemyBullets = saveGame.enemyBullets;

        this.gifts = saveGame.gifts;
        for(Gift gift : gifts) gift.resume();

        this.myTimers = saveGame.myTimers;
        for(int i = 0; i < myTimers.length; i++)if(myTimers[i] != null)myTimers[i].resume();

        this.obstacles = saveGame.obstacles;
        for ( Obstacle obs : obstacles ) obs.setImage(images.getRandomObstacleImage());

        revalidate();
        actionListeners();
    }
    private void initGame(int top, boolean enableAudio, ImportAudioClips audios, Dimension dim) {
        this.rand = new Random();
        this.topScore = top;
        this.audioEnabled = enableAudio;
        this.audioClips = audios;

        this.setSize(dim);
        this.setVisible(true);
        this.windowX = getWidth();
        this.windowY = getHeight();
        this.prevX = windowX;
        this.prevY = windowY;
        this.factorX = 1;
        this.factorY = 1;
        this.darkBorder = 50;

        this.enemyLifeFont = new Font("Arial", Font.BOLD, 8);
        this.scoreLifeFont = new Font("Arial", Font.BOLD, 17);
        this.loadingFont = new Font("Arial", Font.BOLD,50);

        if(enableAudio)audioClips.backgroundInGame.loop();
        this.playGameOverMusic = true;

        this.images = new ImportImages();
        this.notifications = new LinkedList<>();

        this.gameOver = false;
        this.pause = false;

        Timer timer = new Timer(10, this);
        timer.start();
    }
    private void actionListeners() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getX() < 50 && e.getY() < 25){
                    pauseResume();
                }
                else if(pause){

                    //Return to Main Menu
                    if(e.getX() < 50 && e.getY() > 25 && e.getY() < 50){

                        if(getLifeStatus() > 0){
                            SaveGamePrompt dialog = new SaveGamePrompt(new javax.swing.JFrame(), true);
                            dialog.setSaveGameRequest(new SaveGameRequest() {
                                @Override
                                public void getBool(boolean bool) {
                                    if(bool){
                                        FileManagement fileMan = new FileManagement();
                                        fileMan.saveGame(getSaveGame());
                                    }
                                    if(mainMenu != null){
                                        if(audioEnabled){
                                            audioClips.backgroundInGame.stop();
                                            audioClips.backgroundUI.loop();
                                        }
                                        mainMenu.goToMainMenu(true);
                                    }
                                    dialog.dispose();
                                }
                            });
                            dialog.setVisible(true);

                        }
                    }
                }
                else if (gameOver){
                    //returnToMainMenu();
                }
                else{
                    if(e.getButton() == MouseEvent.BUTTON3){
                        deployLandMine();
                    }
                    else if(e.getButton() == MouseEvent.BUTTON2){
                        deployNuke();
                    }
                    else{
                        shoot();
                    }
                }
            }
        });
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_SPACE){
                    if(gameOver){
                        returnToMainMenu();
                    }
                    else{
                        pauseResume();
                    }
                }
                else if(!pause && !gameOver){

                    if(waitTimer == null || !waitTimer.active){
                        if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W){
                            goodTank.moveDirection = 0;
                            goodTank.y -= goodTank.speed;
                        }
                        else if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S){
                            goodTank.moveDirection = 1;
                            goodTank.y += goodTank.speed;
                        }

                        if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A){
                            goodTank.moveDirection = 2;
                            goodTank.x -= goodTank.speed;
                        }
                        else if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D){
                            goodTank.moveDirection = 3;
                            goodTank.x += goodTank.speed;
                        }

                    }

                    if(key == KeyEvent.VK_ENTER ){
                        shoot();
                    }

                    if(key == KeyEvent.VK_NUMPAD0 || key == KeyEvent.VK_F ){
                        deployLandMine();
                    }

                    if(key == KeyEvent.VK_ADD || key == KeyEvent.VK_N ){
                        deployNuke();
                    }

                    //GoodTank Border Management
                    if (goodTank.x < darkBorder - 20) goodTank.x = darkBorder - 20;
                    else if (goodTank.x > windowX - 2 * darkBorder + 20) goodTank.x = windowX - 2 * darkBorder + 20;
                    if (goodTank.y < darkBorder - 20) goodTank.y = darkBorder - 20;
                    else if (goodTank.y >= windowY - 80) goodTank.y = windowY - 80;


                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }

    /***********************/
    @Override
    public void actionPerformed(ActionEvent e) {

        windowX = this.getWidth();
        windowY = this.getHeight();

        factorSetUp();

        if(!pause && !gameOver){
            handleNotifications();
            if(waitTimer != null){
                if(waitTimer.active){
                    repaint();
                    return;
                }
                else{
                    waitTimer = null;
                }

            }

            for (int i = 0; i < enemyBullets.size(); i++) {
                Bullet bul = enemyBullets.get(i);

                bul.x = (int)(bul.x  *factorX );
                bul.y = (int)(bul.y  *factorY );

                bul.moveBullet();
                collisionCheck_BadBulletVsGoodTank(i, bul);

                //remove goodBullets out of battlefield
                if(bul.y <= darkBorder - 10 || bul.x <= darkBorder - 10 ||
                        bul.x >= this.windowX - darkBorder + 10 - bul.radius ||
                        bul.y >= windowY - darkBorder + 10 - bul.radius){
                    enemyBullets.remove(i);
                }
            }

            for (int i = 0; i < goodBullets.size(); i++) {
                Bullet bul = goodBullets.get(i);

                bul.x = (int)(bul.x  *factorX );
                bul.y = (int)(bul.y  *factorY );

                bul.moveBullet();
                collisionCheck_EnemyTankVsGoodBullet(i, bul);

                //remove goodBullets out of battlefield
                if(bul.y <= darkBorder - 10 || bul.x <= darkBorder - 10 ||
                        bul.x >= this.windowX - darkBorder + 10 - bul.radius ||
                        bul.y >= this.windowY - darkBorder + 10 - bul.radius){
                    goodBullets.remove(i);
                }
            }

            for (LandMine mine : mines) {
                mine.x = (int) (mine.x * factorX);
                mine.y = (int) (mine.y * factorY);
            }

            for (int i = 0; i < enemyTanks.size(); i++) {
                Tank t = enemyTanks.get(i);
                t.x = (int)(t.x  * factorX );
                t.y = (int)(t.y  * factorY );

                collisionCheck_BadTankVsMine(t);
                collisionCheck_GoodTankVsEnemyTank(t);
                badTanksMovement(t);
            }

            handleObstacles();
            levelUp();
            refillEnemyTanks();
            topScorePrompt();
            gameOver();

            handleTimers();
            handleGifts();
        }
        else{
            // TODO: 8/1/2016 Now game is paused
        }
        repaint();
    }
    @Override
    public void paintComponent(Graphics g){

        g.drawImage(images.borderImage, 0, 0, windowX, windowY, null);
        g.drawImage(images.fieldImage, 50, 50, windowX - 2 * darkBorder, windowY - 2 * darkBorder, null);

        g.setColor(Color.WHITE);
        g.setFont(scoreLifeFont);
        g.drawString("Score: "+score+"    Top Score: "+topScore, 50, 30);
        g.drawString("Life: "+goodTank.life+"    Level: "+gameLevel, windowX - 210, 20);
        g.drawString("Mine: "+mineCounter+"    Nuke: "+nukeCounter, windowX - 210, 40);

        if(!gameOver){
            //Draw Pause Button
            g.setColor(Color.WHITE);
            g.drawLine(15, 5, 25, 11);
            g.drawLine(15, 6, 25, 12);

            g.drawLine(15, 19, 25, 13);
            g.drawLine(15, 20, 25, 14);

            g.drawLine(15, 5, 15, 20);
            g.drawLine(16, 5, 16, 20);

            g.drawLine(26, 5, 26, 20);
            g.drawLine(27, 5, 27, 20);

            g.drawLine(29, 5, 29, 20);
            g.drawLine(30, 5, 30, 20);
            //End Drawing Pause Button
        }

        if(!pause){

            for (Obstacle obs : obstacles) {
                g.drawImage(obs.image, obs.x, obs.y, obs.width, obs.height,null);
                g.drawString(""+obs.health, obs.x + 22, obs.y + obs.height);
            }

            for (LandMine mine : mines ) g.drawImage(images.mineImage, mine.x, mine.y, mine.width, mine.height, null);

            for (Gift gift : gifts) {
                if(gift.active){
                    if(gift.type == 1) g.drawImage(images.healthImage, gift.x, gift.y, 50, 50, null);
                    else if(gift.type == 2) g.drawImage(images.nukeImage, gift.x, gift.y, 50, 50, null);
                    else if(gift.type == 3) g.drawImage(images.mineImage, gift.x, gift.y, 50, 50, null);
                    else if(gift.type == 4) g.drawImage(images.shieldImage, gift.x, gift.y, 50, 50, null);
                    else if(gift.type == 5) g.drawImage(images.scoreBoostImage, gift.x, gift.y, 50, 50, null);
                }
            }
            g.setFont(scoreLifeFont);

            for (Tank t : enemyTanks) {
                g.setFont(enemyLifeFont);
                g.setColor(Color.WHITE);

                if (t.life > 0) {
                    g.drawString("" + t.life, t.x + 21, t.y + 60);
                } else {
                    //g.drawString("*_*", t.x + 20, t.y + 25);
                    g.drawImage(images.flameImage, t.x - 10, t.y - 10, 70, 70, null);
                }

                g.setColor(t.color);
                if (t.moveDirection == 0) {
                    g.fillRoundRect(t.x, t.y + 10, t.width, t.height, goodTank.bodyArc, goodTank.bodyArc);
                    g.fillOval(t.x + 20, t.y, 10, 15);
                } else if (t.moveDirection == 1) {
                    g.fillRoundRect(t.x, t.y, t.width, t.height, goodTank.bodyArc, goodTank.bodyArc);
                    g.fillOval(t.x + 20, t.y + 35, 10, 15);
                } else if (t.moveDirection == 2) {
                    g.fillRoundRect(t.x + 10, t.y, t.width, t.height, goodTank.bodyArc, goodTank.bodyArc);
                    g.fillOval(t.x, t.y + 20, 15, 10);
                } else if (t.moveDirection == 3) {
                    g.fillRoundRect(t.x, t.y, t.width, t.height, goodTank.bodyArc, goodTank.bodyArc);
                    g.fillOval(t.x + 35, t.y + 20, 15, 10);
                }

            }

            g.setColor(Color.BLUE);
            for (Bullet bullet : goodBullets) g.fillOval(bullet.x, bullet.y, bullet.radius, bullet.radius);

            g.setColor(Color.RED);
            for (Bullet bullet : enemyBullets) g.fillOval(bullet.x, bullet.y, bullet.radius, bullet.radius);

            if (myTimers[0] != null)g.drawImage(images.shieldGlowImage, goodTank.x - 20, goodTank.y - 20, 90, 90, null);

            g.setColor(goodTank.color);
            if(goodTank.moveDirection == 0){
                g.fillRoundRect(goodTank.x, goodTank.y + 10, goodTank.width, goodTank.height, goodTank.bodyArc, goodTank.bodyArc);
                if(goodBullets.size() >= 5)g.setColor(Color.RED);
                g.fillOval(goodTank.x + 20, goodTank.y, 10, 15);
            }
            else if(goodTank.moveDirection == 1){
                g.fillRoundRect(goodTank.x, goodTank.y, goodTank.width, goodTank.height, goodTank.bodyArc, goodTank.bodyArc);
                if(goodBullets.size() >= 5)g.setColor(Color.RED);
                g.fillOval(goodTank.x + 20, goodTank.y + 35, 10, 15);
            }
            else if(goodTank.moveDirection == 2){
                g.fillRoundRect(goodTank.x + 10, goodTank.y, goodTank.height, goodTank.width, goodTank.bodyArc, goodTank.bodyArc);
                if(goodBullets.size() >= 5) g.setColor(Color.RED);
                g.fillOval(goodTank.x, goodTank.y + 20, 15, 10);
            }
            else if(goodTank.moveDirection == 3){
                g.fillRoundRect(goodTank.x, goodTank.y, goodTank.height, goodTank.width, goodTank.bodyArc, goodTank.bodyArc);
                if(goodBullets.size() >= 5)g.setColor(Color.RED);
                g.fillOval(goodTank.x + 35, goodTank.y + 20, 15, 10);
            }

            if(goodTank.life <= 0)g.drawImage(images.flameImage, goodTank.x - 20, goodTank.y - 20, 90, 90, null);


            if (myTimers[1] != null){
                if(myTimers[1].value == 2){
                    g.drawImage(images.x2Image, goodTank.x + 10, goodTank.y + 10, 30, 30, null );
                }
                else{
                    g.drawImage(images.x3Image, goodTank.x + 10, goodTank.y + 10, 30, 30, null );
                }
            }
            if(myTimers[2]!= null){

                for (int j = 0; j < notifications.size(); j++) {
                    Notification notification = notifications.get(j);
                    if(notification.font == null){
                        g.setFont(new Font("Arial",1, 20));
                    }
                    else{
                        g.setFont(notification.font);
                    }
                    g.setColor(notification.color);
                    g.drawString(notification.message, (int)notification.x, (int)(notification.y - j*10));
                }
            }

            if(waitTimer != null && waitTimer.active){
                g.setColor(Color.RED);
                g.setFont(loadingFont);

                g.drawString("Loading", windowX/2-90,windowY/2);
                if(waitTimer.timeLimit <= 1){
                    g.drawString("100%", windowX/2-50,windowY/2+50);
                }
                else{
                    g.drawString((100 / waitTimer.timeLimit)+"%", windowX/2-30,windowY/2+50);
                }
            }

        }
        else{
            //Draw arrow key
            g.setColor(Color.RED);
            g.drawArc(5,32,25,15,80,-120);
            g.drawArc(5,33,25,15,90,-120);

            g.drawLine(15,33,19,28);
            g.drawLine(16,33,20,28);

            g.drawLine(15,33,19,37);
            g.drawLine(16,33,20,37);

            g.drawImage(images.instructionImage, 100, 50, windowX - 200, windowY - 100, null);
        }

        if (gameOver){
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", (windowX/2 - 135), windowY/2 - 25);
            g.setFont(new Font("Arial", 1, 15));
            //g.drawString("Click here or press space to return to main menu", windowX/2-175, windowY/2 + 25);
            g.drawString("Press space to return to main menu", windowX/2-125, windowY/2 + 25);
        }
    }

    /***********************/
    private void handleNotifications(){
        for (int i = 0; i < notifications.size(); i++) {
            if(i > 10){
                notifications.remove(i);
            }
            else{
                Notification notification =notifications.get(i);
                notification.y -= 1;//notification.y/(windowY/2);
                if (notification.y < 100) notifications.remove(i);
            }
        }
    }
    private void handleObstacles() {
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obs = obstacles.get(i);
            if(obs.health <= 0)continue;

            obs.x = (int)(obs.x * factorX);
            obs.y = (int)(obs.y * factorY);

            if( obs.x < darkBorder ){
                obs.x = obs.width * rand.nextInt(10) + 1;
            }
            if(obs.y < darkBorder){
                obs.y = obs.height * rand.nextInt(10) + 1;
            }

            if( obs.x + obs.width >= goodTank.x && obs.x <= goodTank.x + 50 && obs.y + obs.height >= goodTank.y && obs.y <= goodTank.y + 50 ){

                if(goodTank.moveDirection == 0) goodTank.y = obs.y + obs.height + 2;
                else if(goodTank.moveDirection == 1) goodTank.y = obs.y - 50 - 2;
                else if(goodTank.moveDirection == 2) goodTank.x = obs.x + obs.width + 2;
                else if(goodTank.moveDirection == 3) goodTank.x = obs.x - 50 - 2;
            }

            for ( Tank t : enemyTanks ) {

                if( t.life <= 0 ) continue;

                if( obs.x + obs.width >= t.x && obs.x <= t.x + 50 && obs.y + obs.height >= t.y && obs.y <= t.y + 50 ){
                    if(t.moveDirection == 0) {
                        t.y = obs.y + obs.height + t.speed;
                    }
                    else if(t.moveDirection == 1) {
                        t.y = obs.y - 50 - t.speed;
                    }
                    else if(t.moveDirection == 2) {
                        t.x = obs.x + obs.width + t.speed;
                    }
                    else if(t.moveDirection == 3) {
                        t.x = obs.x - 50 - t.speed;
                    }


                    enemyBullets.add(bulletCreate(t));
                    t.tankDirectionChange_Obstacle();
                }
            }

            for (int j = 0; j < goodBullets.size(); j++) {
                Bullet bull = goodBullets.get(j);

                if(bull.x + 5 >= obs.x && bull.x <= obs.x + obs.width - 5 && bull.y + 5 >= obs.y && bull.y <= obs.y + obs.height - 5){
                    obs.health -= bull.hitPoint;
                    if(obs.health <= 0 ) {
                        if(audioEnabled)audioClips.obstacleDamage.play();
                        int timeLimit = rand.nextInt(11) + 5;
                        int type = rand.nextInt(6);
                        int value;
                        if(type == 2){
                            value = rand.nextInt(2) + 1;
                        }
                        else if(type == 3){
                            value = rand.nextInt(5) + 1;
                        }
                        else value = rand.nextInt(10) + 1;

                        int x = obs.x + obs.width/2 - 25;
                        int y = obs.y + obs.height/2 - 25;

                        Gift gift = new Gift(timeLimit, type, value, x,y);
                        gift.start();
                        gifts.add(gift);
                        if(!obstacles.isEmpty())obstacles.remove(i);
                    }
                    goodBullets.remove(j);
                }
            }

            for (int j = 0; j < enemyBullets.size(); j++) {
                Bullet bull = enemyBullets.get(j);

                if(bull.x + 5 >= obs.x && bull.x <= obs.x + obs.width - 5 && bull.y + 5 >= obs.y && bull.y <= obs.y + obs.height - 5){
                    enemyBullets.remove(j);
                    obs.health --;
                    if(obs.health <= 0 ) {
                        obstacles.remove(i);
                        if(audioEnabled)audioClips.obstacleDamage.play();
                    }
                }
            }
        }
    }
    private void handleTimers(){
        for (int i = 0; i < myTimers.length; i++) {
            if(myTimers[i] != null && ! myTimers[i].active){
                myTimers[i] = null;
            }
        }
    }
    private void handleGifts() {
        for (int i = 0; i < gifts.size(); i++) {
            Gift gift = gifts.get(i);

            gift.x = (int)(gift.x  *factorX );
            gift.y = (int)(gift.y  *factorY );

            if( gift.x + 50 >= goodTank.x && gift.x <= goodTank.x + 50 && gift.y + 50 >= goodTank.y && gift.y <= goodTank.y + 50){

                if(gift.type == 1) {
                    goodTank.life += gift.value;
                    notifications.add(0, new Notification("Health Improved", new Color(0x0BD0F2),null,windowX/2-69, 200));
                    if(goodTank.life > 100) goodTank.life = 100;
                }
                else if(gift.type == 2){
                    if(audioEnabled)audioClips.reload.play();
                    nukeCounter += gift.value;
                    notifications.add(0, new Notification("Nuke +"+gift.value, new Color(0xC3ABF2),null,windowX/2-30, 200));
                    if (nukeCounter > 5) nukeCounter = 5;
                }
                else if(gift.type == 3) {
                    mineCounter += gift.value;
                    notifications.add(0, new Notification("Mine +"+gift.value, new Color(0x44A8F2),null,windowX/2-28, 200));
                    if(audioEnabled)audioClips.reload2.play();
                    if (mineCounter > 10) mineCounter = 10;
                }
                else if(gift.type == 4) {
                    notifications.add(0, new Notification("Shield Enabled",new Color(0x0CF247),null,windowX/2-64, 200));
                    myTimers[0] = new MyTimer(rand.nextInt(6) + 10, 4, 0);
                    myTimers[0].start();
                }
                else if(gift.type == 5) {
                    int value = rand.nextInt(2) + 2;
                    notifications.add(0, new Notification("Score X"+value,new Color(0xCCF216),null,windowX/2-35, 200));
                    myTimers[1] = new MyTimer(rand.nextInt(11) + 10, 5, value);
                    myTimers[1].start();
                }

                gift.active = false;
                gifts.remove(i);
            }
        }

    }
    /***********************/
    private void factorSetUp() {
        if(windowX != prevX){
            factorX = (float) windowX / prevX;
            goodTank.x = (int)(goodTank.x  *factorX );
            prevX = windowX;
        }
        else {
            factorX = 1;
        }

        if(windowY != prevY){
            factorY = (float) windowY / prevY;
            goodTank.y = (int) (goodTank.y * factorY);
            prevY = windowY;
        }
        else {
            factorY = 1;
        }
    }
    /***********************/
    private void collisionCheck_BadBulletVsGoodTank(int i, Bullet bul) {
        if(bul.x + 5 >= goodTank.x && bul.x <= goodTank.x + 45 && bul.y + 5 >= goodTank.y && bul.y <= goodTank.y + 45){
            if(audioEnabled) audioClips.hitPlayerTank.play();
            enemyBullets.remove(i);

            if(myTimers[0] == null){
                goodTank.life -= bul.hitPoint * 9 / gameDifficulty;
                if(goodTank.life < 0){
                    goodTank.life = 0;
                }
            }

        }
    }
    private void collisionCheck_EnemyTankVsGoodBullet(int i, Bullet bul) {
        for (int j = 0; j < enemyTanks.size(); j++) {
            Tank t = enemyTanks.get(j);
            if(t.life > 0){

                if(bul.x + 5 >= t.x && bul.x <= t.x + 45 && bul.y + 5 >= t.y && bul.y <= t.y + 45){
                    if(audioEnabled) audioClips.hitEnemyTank.play();
                    t.life -= bul.hitPoint;

                    if(!goodBullets.isEmpty()){
                        goodBullets.remove(i);
                    }
                    if(t.life <= 0){
                        enemyDestroyed(t);
                    }
                }
            }
        }
    }
    private void collisionCheck_BadTankVsMine(Tank t) {
        for (int j = 0; j < mines.size(); j++) {
            LandMine mine = mines.get(j);
            if( mine.x + mine.width >= t.x && mine.x <= t.x + 50 && mine.y + mine.height >= t.y && mine.y <= t.y + 50  ){
                t.life -= mine.hitPoint;
                mines.remove(j);
                if(t.life <= 0){
                    enemyDestroyed(t);
                }
            }
        }
    }
    private void collisionCheck_GoodTankVsEnemyTank(Tank t) {
        if( t.x + 50 >= goodTank.x && t.x <= goodTank.x + 50 && t.y + 50 >= goodTank.y && t.y <= goodTank.y + 50){

            if(t.life > 0){
                if(audioEnabled) audioClips.collisionTankTank.play();
                if(myTimers[0] == null){
                    goodTank.life -= t.level;

                    if(goodTank.life <0){
                        goodTank.life = 0;
                    }
                }


                t.life -= goodTank.level;

                if(t.life <= 0){
                    enemyDestroyed(t);
                }

                if(t.moveDirection == 0){
                    goodTank.y -= 20;
                    goodTank.x -= 20;
                }
                else if(t.moveDirection == 1){
                    goodTank.y += 20;
                    goodTank.x += 20;
                }
                else if(t.moveDirection == 2){
                    goodTank.x -= 20;
                    goodTank.y += 20;
                }
                else {
                    goodTank.x += 20;
                    goodTank.y -= 20;
                }
            }
        }
    }
    /***********************/
    private void levelUp() {
        if(noOfEnemyTank >= 5 && noOfAliveEnemyTank <= 0){

            activateWaitTimer(2);
            notifications.add(0, new Notification("Level Up", Color.CYAN, new Font("Arial",1,40), windowX/2-80, 250));
            noOfEnemyTank = noOfAliveEnemyTank = 0;
            gameLevel++;
            refillObstacles();
            mines.clear();
            gifts.clear();
            enemyBullets.clear();
            goodBullets.clear();
            if(audioEnabled){

                if(audioEnabled){
                    audioClips.levelUp.stop();
                    audioClips.danger.play();
                }
            }
        }
    }
    private void refillEnemyTanks() {
        if(noOfAliveEnemyTank <= 0){
            if(audioEnabled){
                audioClips.hitEnemyTank.stop();
                audioClips.levelUp.play();
            }
            enemyTanks.clear();
            noOfEnemyTank++;
            for (int i = 0; i < noOfEnemyTank; i++) {
                enemyTanks.add(badTankCreate());
            }
            noOfAliveEnemyTank = noOfEnemyTank;
        }
    }
    private void refillObstacles() {
        obstacles.clear();
        for (int i = 0; i < images.obstacleImages.length; i++) {
            obstacles.add(new Obstacle(rand.nextInt(windowX - 150) + 50, rand.nextInt(windowY - 150) + 50, 50, 50, 0, 5, images.getRandomObstacleImage()));
        }

    }
    /***********************/
    private void pauseResume() {
        pause = !pause;
        if (pause) {
            for (Gift gift : gifts) gift.pause();
            for (int i = 0; i < myTimers.length; i++) {
                if(myTimers[i] != null){
                    myTimers[i].pause();
                }

            }
        }
        else{
            for (Gift gift : gifts) gift.resume();
            for (int i = 0; i < myTimers.length; i++) {
                if(myTimers[i] != null){
                    myTimers[i].resume();
                }
            }
        }
    }
    private void gameOver() {
        if(goodTank.life <= 0 && playGameOverMusic){
            gameOver = true;
            for (Gift gift: gifts ) gift.pause();
            for (int i = 0; i < myTimers.length; i++){
                if(myTimers[i] != null){
                    myTimers[i].pause();
                }
            }

            if(audioEnabled){
                playGameOverMusic = false;
                audioClips.hitPlayerTank.stop();
                audioClips.destroyPlayer.play();
                audioClips.gameOver.play();
                audioClips.backgroundInGame.stop();
            }
        }
    }
    /***********************/
    private void enemyDestroyed(Tank t){
        enemyDestroyed(t, true);
    }
    private void enemyDestroyed(Tank t, boolean bool) {
        notifications.add(0, new Notification("Enemy Destroyed",Color.WHITE,null, windowX/2-80,200));
        t.life = 0;
        if(bool)moveDeadTankToArrayListHead(t);
        if(audioEnabled)if(audioEnabled) audioClips.destroyEnemy.play();

        if(myTimers[1] != null && myTimers[1].active){
            score += myTimers[1].value * 1;
        }
        else{
            score++;
        }
        t.color = Color.gray;
        noOfAliveEnemyTank = noOfAliveEnemyTank - 1;
    }
    private void moveDeadTankToArrayListHead(Tank t) {
        enemyTanks.remove(t);
        enemyTanks.add(0,t);
    }
    /***********************/
    private Bullet bulletCreate(Tank t){
        return bulletCreate(t,false);
    }
    private Bullet bulletCreate(Tank t, boolean isGoodTank) {
        int x = 0, y = 0, radius, speed, moveDir = 0, hitPoint;

        if(isGoodTank){
            if(audioEnabled) audioClips.shootPlayer.play();
            speed = 5;
            hitPoint = 1;
        }
        else{
            if(audioEnabled) audioClips.shootEnemy.play();
            speed = 4;
            hitPoint = gameLevel;
        }
        radius = 10;

        if(t.moveDirection == 0){
            x = t.x + 20;
            y = t.y;
            moveDir = 0;
        }
        else if(t.moveDirection == 1){
            x = t.x + 20;
            y = t.y + t.height;
            moveDir = 1;
        }
        else if(t.moveDirection == 2){
            x = t.x;
            y = t.y + 20;
            moveDir = 2;
        }
        else if(t.moveDirection == 3){
            x = t.x + t.height;
            y = t.y + 20;
            moveDir = 3;
        }
        return new Bullet(x, y, radius, speed, moveDir, hitPoint);
    }
    /***********************/
    private Tank badTankCreate(){
        int x, y, speed, moveDirection, width, height, life, tankLevel;
        Color color;
        //x & y
        if(goodTank.x + 25 <= windowX/2){
            x = rand.nextInt(100) + windowX/2 + 50;
        }
        else{
            x = rand.nextInt(100) + 50;
        }
        y = rand.nextInt(windowY - 100) + 50;

        speed = 1;
        moveDirection = rand.nextInt(4);

        //Set width and height
        if(moveDirection == 0 || moveDirection == 1){
            width = 50;
            height = 40;
        }
        else{
            width = 40;
            height = 50;
        }

        life = gameLevel;
        tankLevel = gameLevel;
        color = new Color( Math.abs(250 - 15*gameLevel), 250/gameLevel, 250/gameLevel );

        Tank t = new Tank(x, y, speed, moveDirection, width, height, life, tankLevel, color);
        return t;
    }
    private void badTanksMovement(Tank t){
        if(t.life > 0){
            t.moveTank();
            t.tankDirectionChange_Border(darkBorder, windowX - 2 * darkBorder, darkBorder, windowY - 2*darkBorder);
            t.tankDirectionChange(goodTank.x, goodTank.y, gameDifficulty);

            if((t.y - goodTank.y <= 25 && t.y - goodTank.y >= 0) || (goodTank.y - t.y <= 25 && goodTank.y - t.y >= 0)){


                if(t.moveDirection == 3 && goodTank.x > t.x){
                    enemyBullets.add(bulletCreate(t));
                    t.setDirectionAndFixDiameter(0);
                }
                else if(t.moveDirection == 2 && goodTank.x < t.x){
                    enemyBullets.add(bulletCreate(t));
                    t.setDirectionAndFixDiameter(1);
                }
            }

            if((goodTank.x - t.x <= 25 && goodTank.x - t.x >= 0) || (t.x - goodTank.x >= 0 && t.x - goodTank.x <= 25)){

                if(t.moveDirection == 0 && goodTank.y < t.y){
                    enemyBullets.add(bulletCreate(t));
                    t.setDirectionAndFixDiameter(3);
                }
                else if(t.moveDirection == 1 && goodTank.y > t.y){
                    enemyBullets.add(bulletCreate(t));
                    t.setDirectionAndFixDiameter(2);
                }
            }
        }
        else {
            if (t.x < darkBorder) t.x = darkBorder;
            else if (t.x > windowX - 2 * darkBorder) t.x = windowX - 2 * darkBorder;
            if (t.y < darkBorder) t.y = darkBorder;
            else if (t.y >= this.windowY) t.y = this.windowY;

        }

    }// End of badTanksMovement
    /***********************/
    private void topScorePrompt() {
        if(topScore < score && goodTank.life <= 0){
            EventQueue.invokeLater(() -> {
                TopScorePrompt dialog = new TopScorePrompt(new JFrame(), true, score);
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e1) {
                        dialog.dispose();
                    }
                });
                dialog.setVisible(true);
            });
        }
    }
    private void returnToMainMenu() {
        if(mainMenu != null){
            if(audioEnabled){
                audioClips.gameOver.stop();
                audioClips.backgroundUI.loop();
            }
            mainMenu.goToMainMenu(true);
        }
    }
    public void setMainMenu(MainMenu menu){
        mainMenu = menu;
    }
    public SaveGame getSaveGame(){
        for (Gift gift: gifts ) gift.pause();
        for (int i = 0; i < myTimers.length; i++){
            if(myTimers[i] != null){
                myTimers[i].pause();
            }
        }

        Dimension dimension = new Dimension(windowX, windowY);
        SaveGame saveGame = new SaveGame();

        saveGame.setScore(score);
        saveGame.setLevel(gameLevel);
        saveGame.setNoOfAliveEnemyTank(noOfAliveEnemyTank);
        saveGame.setNoOfEnemyTank(noOfEnemyTank);
        saveGame.setEnemyTanks(enemyTanks);
        saveGame.setEnemyBullets(enemyBullets);
        saveGame.setBullets(goodBullets);
        saveGame.setDifficulty(gameDifficulty);
        saveGame.setDim(dimension);
        saveGame.setGoodTank(goodTank);
        saveGame.setGifts(gifts);
        saveGame.setMyTimers(myTimers);
        saveGame.setObstacles(obstacles);
        saveGame.setNukeCounter(nukeCounter);
        saveGame.setMineCounter(mineCounter);
        saveGame.setMines(mines);

        return saveGame;
    }
    public int getLifeStatus(){
        return goodTank.life;
    }
    /***********************/
    private void shoot() {
        if( waitTimer!= null && waitTimer.active)return;
        if(goodBullets.size() < 5 && !pause && !gameOver){
            goodBullets.add(bulletCreate(goodTank, true));
        }
        else{
            if(audioEnabled) audioClips.emptyBullet.play();
        }

        myTimers[2] = new MyTimer(5, 0, 0);
        //notifications.add(0, new Notification("Notification: "+goodBullets.size(), new Color(0xEE4D1A),new Font("Arial", 1, 15), 50, windowY/2));
        myTimers[2].start();

    }
    private void deployLandMine(){
        if( waitTimer!= null && waitTimer.active)return;
        if( mineCounter > 0 ){
            notifications.add(0, new Notification("Mine Deployed", Color.WHITE,null,windowX/2-63, 200));
            mineCounter --;
            mines.add(new LandMine(goodTank.x + 10, goodTank.y + 10, gameLevel));
            System.out.println("Mine Deployed");
        }
    }
    private void deployNuke(){
        if( waitTimer!= null && waitTimer.active)return;
        if( nukeCounter > 0 ){
            nukeCounter --;
            notifications.add(0, new Notification("Nuke Deployed", Color.WHITE,null,windowX/2-65, 200));
            activateWaitTimer(2);
            if(audioEnabled){
                audioClips.nukeExplosion.stop();
                audioClips.nukeExplosion.play();
            }


            for (Tank tank : enemyTanks) {
                enemyDestroyed(tank, false);
            }
            System.out.println("Nuke Deployed");
        }
    }
    /***********************/
    private void activateWaitTimer(int time){
        waitTimer = null;
        waitTimer = new MyTimer(2,-1,0);
        waitTimer.start();
    }
    /***********************/
}//End of Class