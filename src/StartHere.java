import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StartHere {

    private JFrame window;
    private UIPanel uiPanel;
    private GamePanel gamePanelOb;

    public static void main(String[] args) {
        new StartHere();
    }

    public StartHere(){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                initFrame();

                uiPanel.setGamePanelObjectEmitter(new GamePanelObjectEmitter() {
                    @Override
                    public void objectReceiver(GamePanel ob) {
                        gamePanelOb = ob;
                        window.remove(uiPanel);
                        window.add(gamePanelOb);
                        window.revalidate();
                        gamePanelOb.grabFocus();
                        gamePanelOb.setMainMenu(new MainMenu() {
                            @Override
                            public void goToMainMenu(boolean bool) {
                                if(bool){
                                    window.remove(gamePanelOb);
                                    gamePanelOb = null;
                                    window.add(uiPanel);
                                    window.revalidate();
                                    uiPanel.grabFocus();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private void initFrame() {
        window = new JFrame("Tank Generation");
        window.setSize(800,590);
        window.setMinimumSize(new Dimension(600,590));
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLayout(new BorderLayout());
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(gamePanelOb != null && gamePanelOb.getLifeStatus() > 0){
                    SaveGamePrompt dialog = new SaveGamePrompt(new javax.swing.JFrame(), true);
                    dialog.setSaveGameRequest(new SaveGameRequest() {
                        @Override
                        public void getBool(boolean bool) {
                            if(bool){
                                FileManagement fileMan = new FileManagement();
                                fileMan.saveGame(gamePanelOb.getSaveGame());
                            }
                            System.exit(0);
                        }
                    });
                    dialog.setVisible(true);
                }
                //System.exit(0);
            }
        });

        uiPanel = new UIPanel();
        window.add(uiPanel);
    }

}
