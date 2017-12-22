import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Created by Ashiqur Rahman on 7/22/2016.
 */
public class UIPanel extends JPanel implements ActionListener {

    // Variables declaration - do not modify
    private javax.swing.JPanel btnPan;
    private javax.swing.JButton creditsBtn;
    private javax.swing.JPanel diffBtnPan;
    private javax.swing.JButton easyBtn;
    private javax.swing.JButton exitBtn;
    private javax.swing.JButton hardBtn;
    private javax.swing.JLabel infoLabel1, infoLabel2, infoLabel3;
    private javax.swing.JButton mediumBtn;
    private javax.swing.JButton newGameBtn;
    private javax.swing.JButton resumeBtn;
    private javax.swing.JButton topScoreBtn;
    private javax.swing.JButton audioBtn;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration


    // Other Variables
    private FileManagement fileManagement;
    private GamePanelObjectEmitter gamePanelObjectEmitter;
    private ImportAudioClips importAudioClips;

    private Timer timer;
    private Random rand;
    private Shape shape;
    private Tank tank;
    private boolean changeShape;
    private boolean enableAudio;
    private Color backGroundColor;
    private int shapeChangeCounter;

    //Constructor
    public UIPanel() {
        initComponents();
        initOtherComponents();


        // ActionListeners
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                infoLabel1.setText("");
                infoLabel2.setText("");
                infoLabel3.setText("");
                diffBtnPan.remove(easyBtn);
                diffBtnPan.remove(mediumBtn);
                diffBtnPan.remove(hardBtn);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    changeShape = !changeShape;
                }
                else if(e.getButton() == MouseEvent.BUTTON2){
                    backGroundColor = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
                }

                if (changeShape) {
                    int mvDir = rand.nextInt(4);
                    if (mvDir == 0 || mvDir == 1) {
                        tank = new Tank(e.getX(), e.getY(), 10, mvDir, 50, 40, 1, 1, new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                    } else {
                        tank = new Tank(e.getX(), e.getY(), 10, mvDir, 40, 50, 1, 1, new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                    }
                } else {
                    shape = new Shape(e.getX(), e.getY(), rand.nextInt(50), new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                }
            }
        });
        resumeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diffBtnPan.removeAll();
                if (!fileManagement.gameFileExist()) {
                    infoLabel1.setText("");
                    infoLabel2.setForeground(Color.BLACK);
                    infoLabel2.setText("No Saved Data Found!");
                    infoLabel3.setText("");
                } else {
                    SaveGame save = (SaveGame) fileManagement.getSavedGame();
                    if(save == null){
                        infoLabel1.setText("");
                        infoLabel2.setForeground(Color.RED);
                        infoLabel2.setText("Saved Data Is Corrupted!");
                        infoLabel3.setText("");
                    }
                    else {
                        if (enableAudio) importAudioClips.backgroundUI.stop();
                        gamePanelObjectEmitter.objectReceiver(new GamePanel(save, fileManagement.getTopScore(), enableAudio, importAudioClips));
                    }
                }
            }
        });
        newGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gamePanelObjectEmitter != null) {
                    diffBtnPan.add(easyBtn, java.awt.BorderLayout.PAGE_START);
                    diffBtnPan.add(mediumBtn, java.awt.BorderLayout.CENTER);
                    diffBtnPan.add(hardBtn, java.awt.BorderLayout.PAGE_END);
                    diffBtnPan.revalidate();
                    infoLabel1.setText("");
                    infoLabel2.setForeground(new java.awt.Color(255, 24, 48));
                    infoLabel2.setText("Caution! Starting New Game May Affect Previous Save");
                    infoLabel3.setText("");
                }
            }
        });
        topScoreBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diffBtnPan.removeAll();
                //infoLabel1.setForeground(new java.awt.Color(0, 0, 0));
                //infoLabel1.setText(fileManagement.getTopScorer() + ": " + fileManagement.getTopScore());
                infoLabel2.setForeground(new java.awt.Color(0, 0, 0));
                infoLabel2.setText(fileManagement.getTopScorer() + ": " + fileManagement.getTopScore());
                //infoLabel3.setForeground(new java.awt.Color(0, 0, 0));
                //infoLabel3.setText(fileManagement.getTopScorer() + ": " + fileManagement.getTopScore());
            }
        });

        audioBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(enableAudio){
                    audioBtn.setText("Audio: Off");
                    importAudioClips.backgroundUI.stop();
                }
                else{
                    audioBtn.setText("Audio: On");
                    importAudioClips.backgroundUI.loop();
                }
                enableAudio = ! enableAudio;
            }
        });
        creditsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diffBtnPan.removeAll();

                infoLabel1.setForeground(Color.BLACK);
                infoLabel2.setForeground(Color.BLACK);
                infoLabel3.setForeground(Color.BLACK);

                infoLabel3.setText("#011 142 014");
                infoLabel1.setText("Ashiqur Rahman");
                infoLabel2.setText("United International University");

            }
        });
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        easyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gamePanelObjectEmitter != null){
                    if(enableAudio)importAudioClips.backgroundUI.stop();
                    gamePanelObjectEmitter.objectReceiver(new GamePanel(fileManagement.getTopScore(),3,enableAudio, importAudioClips));
                    diffBtnPan.remove(easyBtn);
                    diffBtnPan.remove(mediumBtn);
                    diffBtnPan.remove(hardBtn);
                    infoLabel1.setText("");
                    infoLabel2.setText("");
                    infoLabel3.setText("");
                }
            }
        });
        mediumBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gamePanelObjectEmitter != null){
                    if(enableAudio)importAudioClips.backgroundUI.stop();
                    gamePanelObjectEmitter.objectReceiver(new GamePanel(fileManagement.getTopScore(),2,enableAudio, importAudioClips));
                    diffBtnPan.remove(easyBtn);
                    diffBtnPan.remove(mediumBtn);
                    diffBtnPan.remove(hardBtn);
                    infoLabel1.setText("");
                    infoLabel2.setText("");
                    infoLabel3.setText("");
                }
            }
        });
        hardBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gamePanelObjectEmitter != null){
                    if(enableAudio)importAudioClips.backgroundUI.stop();
                    gamePanelObjectEmitter.objectReceiver(new GamePanel(fileManagement.getTopScore(),1,enableAudio, importAudioClips));
                    diffBtnPan.remove(easyBtn);
                    diffBtnPan.remove(mediumBtn);
                    diffBtnPan.remove(hardBtn);
                    infoLabel1.setText("");
                    infoLabel2.setText("");
                    infoLabel3.setText("");
                }
            }
        });

    }

    public void paintComponent(Graphics g) {
        g.setColor(backGroundColor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        if (!changeShape) {
            g.setColor(shape.color);
            g.fillOval(shape.x - shape.length / 2, shape.y - shape.length / 2, shape.length, shape.length);
        } else {
            g.setColor(tank.color);
            if (tank.moveDirection == 0) {
                g.fillRoundRect(tank.x, tank.y + 10, tank.width, tank.height, tank.bodyArc, tank.bodyArc);
                g.fillOval(tank.x + 20, tank.y, 10, 15);
            } else if (tank.moveDirection == 1) {
                g.fillRoundRect(tank.x, tank.y, tank.width, tank.height, tank.bodyArc, tank.bodyArc);
                g.fillOval(tank.x + 20, tank.y + 35, 10, 15);
            } else if (tank.moveDirection == 2) {
                g.fillRoundRect(tank.x + 10, tank.y, tank.width, tank.height, tank.bodyArc, tank.bodyArc);
                g.fillOval(tank.x, tank.y + 20, 15, 10);
            } else if (tank.moveDirection == 3) {
                g.fillRoundRect(tank.x, tank.y, tank.width, tank.height, tank.bodyArc, tank.bodyArc);
                g.fillOval(tank.x + 35, tank.y + 20, 15, 10);
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        shapeChangeCounter--;
        if(shapeChangeCounter <= 0){
            changeShape = !changeShape;
            if (changeShape){
                int mvDir = rand.nextInt(4);
                if (mvDir == 0 || mvDir == 1) {
                    tank = new Tank(rand.nextInt(this.getWidth()-500)+450, rand.nextInt(this.getHeight()-100)+50, 10, mvDir, 50, 40, 1, 1, new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                } else {
                    tank = new Tank(rand.nextInt(this.getWidth()-500)+450, rand.nextInt(this.getHeight()-100)+50, 10, mvDir, 40, 50, 1, 1, new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                }
            }
            else{
                shape = new Shape(rand.nextInt(this.getWidth()), rand.nextInt(this.getHeight()), rand.nextInt(50), new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
            }
            shapeChangeCounter = rand.nextInt(200) + 100;
        }

        if (!changeShape) {
            if (shape.length <= shape.lengthMax) {
                shape.length += shape.increaseRate;
            } else {
                shape = new Shape(rand.nextInt(this.getWidth()), rand.nextInt(this.getHeight()), rand.nextInt(50), new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                shape.lengthMax = rand.nextInt(this.getHeight()) + shape.length;
            }
        } else {
            tank.tankDirectionChange(0, 0, 2);
            tank.moveTank();
            //If out of border, reset tank
            if ((tank.x <= 0) || (tank.x > this.getWidth()) || (tank.y < 0) || (tank.y >= this.getHeight())) {
                int mvDir = rand.nextInt(4);
                if (mvDir == 0 || mvDir == 1) {
                    tank.resetTankState(rand.nextInt(this.getWidth()-500)+450, rand.nextInt(this.getHeight()), 10, mvDir, 50, 40, 1, 1, new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                } else {
                    tank.resetTankState(rand.nextInt(this.getWidth()-500)+450, rand.nextInt(this.getWidth()), 10, mvDir, 40, 50, 1, 1, new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                }
            }
        }
        repaint();
    }


    public void setGamePanelObjectEmitter(GamePanelObjectEmitter ob) {
        gamePanelObjectEmitter = ob;
    }

    private void initOtherComponents() {
        this.grabFocus();
        fileManagement = new FileManagement();
        /*if (fileManagement.gameFileExist() == false) {
            //resumeBtn.setVisible(false);
            resumeBtn.setBackground(new Color(110, 110, 100));
            resumeBtn.setForeground(new Color(80, 75, 70));
        }*/
        timer = new Timer(20, this);
        rand = new Random();
        timer.start();
        backGroundColor = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        changeShape = false;
        enableAudio = true;
        importAudioClips = new ImportAudioClips();
        importAudioClips.backgroundUI.loop();
        shapeChangeCounter = 200;
        shape = new Shape(rand.nextInt(400), rand.nextInt(400), rand.nextInt(50), new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
    }

    /********************************************/
    private void initComponents() {

        btnPan = new javax.swing.JPanel();
        resumeBtn = new javax.swing.JButton();
        newGameBtn = new javax.swing.JButton();
        topScoreBtn = new javax.swing.JButton();
        creditsBtn = new javax.swing.JButton();
        exitBtn = new javax.swing.JButton();
        audioBtn = new javax.swing.JButton();
        infoLabel1 = new javax.swing.JLabel();
        infoLabel2 = new javax.swing.JLabel();
        infoLabel3 = new javax.swing.JLabel();
        diffBtnPan = new javax.swing.JPanel();
        easyBtn = new javax.swing.JButton();
        mediumBtn = new javax.swing.JButton();
        hardBtn = new javax.swing.JButton();

        jSeparator1 = new javax.swing.JSeparator();

        setBackground(new java.awt.Color(0, 155, 50));
        setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(155, 155, 255)));
        setLayout(new java.awt.BorderLayout());

        btnPan.setBackground(new java.awt.Color(0, 204, 204));
        btnPan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        resumeBtn.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        resumeBtn.setText("Resume");
        resumeBtn.setFocusable(false);
        resumeBtn.setPreferredSize(new java.awt.Dimension(150, 50));

        newGameBtn.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        newGameBtn.setText("New Game");
        newGameBtn.setFocusable(false);
        newGameBtn.setPreferredSize(new java.awt.Dimension(150, 50));

        topScoreBtn.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        topScoreBtn.setText("Top Score");
        topScoreBtn.setFocusable(false);
        topScoreBtn.setPreferredSize(new java.awt.Dimension(150, 50));

        audioBtn.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        audioBtn.setText("Audio: On");
        audioBtn.setFocusable(false);
        audioBtn.setPreferredSize(new java.awt.Dimension(150, 50));

        creditsBtn.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        creditsBtn.setText("Credits");
        creditsBtn.setFocusable(false);
        creditsBtn.setPreferredSize(new java.awt.Dimension(150, 50));

        exitBtn.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        exitBtn.setText("Exit");
        exitBtn.setFocusable(false);
        exitBtn.setPreferredSize(new java.awt.Dimension(150, 50));

        jSeparator1.setBackground(new java.awt.Color(238, 77, 26));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        infoLabel1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        infoLabel2.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        infoLabel3.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N

        diffBtnPan.setBackground(new java.awt.Color(0, 204, 204));
        diffBtnPan.setDoubleBuffered(false);
        diffBtnPan.setPreferredSize(new java.awt.Dimension(75, 75));
        diffBtnPan.setLayout(new java.awt.BorderLayout());

        easyBtn.setFont(new java.awt.Font("Tempus Sans ITC", 1, 13)); // NOI18N
        easyBtn.setForeground(new java.awt.Color(0, 153, 51));
        easyBtn.setText("Easy");
        easyBtn.setFocusable(false);
        easyBtn.setPreferredSize(new java.awt.Dimension(80, 25));
        //diffBtnPan.add(easyBtn, java.awt.BorderLayout.PAGE_START);

        mediumBtn.setFont(new java.awt.Font("Tempus Sans ITC", 1, 13)); // NOI18N
        mediumBtn.setForeground(new java.awt.Color(150, 150, 0));
        mediumBtn.setText("Medium");
        mediumBtn.setFocusable(false);
        mediumBtn.setPreferredSize(new java.awt.Dimension(80, 25));
        //diffBtnPan.add(mediumBtn, java.awt.BorderLayout.CENTER);

        hardBtn.setFont(new java.awt.Font("Tempus Sans ITC", 1, 13)); // NOI18N
        hardBtn.setForeground(new java.awt.Color(150, 0, 0));
        hardBtn.setText("Hard");
        hardBtn.setFocusable(false);
        hardBtn.setPreferredSize(new java.awt.Dimension(80, 25));
        //diffBtnPan.add(hardBtn, java.awt.BorderLayout.PAGE_END);

        javax.swing.GroupLayout btnPanLayout = new javax.swing.GroupLayout(btnPan);
        btnPan.setLayout(btnPanLayout);
        btnPanLayout.setHorizontalGroup(
                btnPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnPanLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(btnPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(newGameBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(topScoreBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(resumeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(audioBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(creditsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(btnPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(diffBtnPan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(infoLabel1)
                                        .addComponent(infoLabel2)
                                        .addComponent(infoLabel3))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        btnPanLayout.setVerticalGroup(
                btnPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(btnPanLayout.createSequentialGroup()
                                .addGroup(btnPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(btnPanLayout.createSequentialGroup()
                                                .addGap(40, 40, 40)
                                                .addGroup(btnPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(btnPanLayout.createSequentialGroup()
                                                                .addGap(27, 27, 27)
                                                                .addComponent(resumeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(newGameBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(topScoreBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(audioBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(creditsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(btnPanLayout.createSequentialGroup()
                                                .addGap(140, 140, 140)
                                                .addComponent(diffBtnPan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(infoLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(infoLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(infoLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))

                                .addContainerGap(155, Short.MAX_VALUE))
        );

        add(btnPan, java.awt.BorderLayout.LINE_START);
    }// </editor-fold>

}

