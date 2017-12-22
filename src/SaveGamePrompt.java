/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Ashiqur Rahman
 */
public class SaveGamePrompt extends javax.swing.JDialog{

    // Variables declaration - do not modify
    private javax.swing.JLabel label;
    private javax.swing.JButton no;
    public javax.swing.JButton yes;
    // End of variables declaration
    private SaveGameRequest sgr;

    public void setSaveGameRequest(SaveGameRequest ob){
        sgr = ob;
    }

    public void setLabel(String str){
        label.setText(str);
    }

    public SaveGamePrompt(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: 7/23/2016 Serialize then exit
                if(sgr != null){
                    sgr.getBool(true);
                }
            }
        });
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(sgr != null){
                    sgr.getBool(false);
                }
            }
        });
    }

    private void initComponents() {

        label = new javax.swing.JLabel();
        yes = new javax.swing.JButton();
        no = new javax.swing.JButton();

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setAlwaysOnTop(true);
        setPreferredSize(new java.awt.Dimension(350, 200));
        setResizable(false);

        label.setFont(new java.awt.Font("Tempus Sans ITC", 1, 15)); // NOI18N
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setText("Save Game?");

        yes.setBackground(new java.awt.Color(0, 204, 0));
        yes.setFont(new java.awt.Font("Tempus Sans ITC", 1, 15)); // NOI18N
        yes.setText("Yes");
        yes.setPreferredSize(new java.awt.Dimension(100, 30));

        no.setBackground(new java.awt.Color(255, 0, 0));
        no.setFont(new java.awt.Font("Tempus Sans ITC", 1, 15)); // NOI18N
        no.setText("No");
        no.setPreferredSize(new java.awt.Dimension(100, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(yes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(no, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(138, 138, 138)
                                .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                .addGap(116, 116, 116))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(label)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(yes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(no, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(46, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    /*public static void main (String[] a){
        SaveGamePrompt dialog = new SaveGamePrompt(new javax.swing.JFrame(), true);
        dialog.setVisible(true);
    }*/
}
