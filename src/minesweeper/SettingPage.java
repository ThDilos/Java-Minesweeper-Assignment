package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SettingPage extends JPanel {
    private JButton easy, normal, hard, goBack;
    private MineSweeperMain controlMain;
    private ActionListener lis = new DifficultyActionListener();

    public SettingPage(MineSweeperMain controlMain) {
        this.controlMain = controlMain; // Link up to the main container

        super.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 1; // grid x and y change the relative position of the component, with x being row, y being column
        gbc.gridy = 0;
        gbc.ipady = 10; // This changes the upper lower padding of individual components
        gbc.anchor = GridBagConstraints.CENTER; // This serves no purpose, but good to have maybe
        gbc.insets = new Insets(10,0,0,0); // The first int changes the distance between two components, dk about others havn't tried
        super.add(new JLabel("Choose your difficulty"), gbc);

        gbc.gridy = 1;

        gbc.gridx = 0;
        easy = new JButton("Easy");
        easy.addActionListener(lis);
        super.add(easy, gbc);

        normal = new JButton("Normal");
        normal.addActionListener(lis);
        gbc.gridx = 1;
        super.add(normal, gbc);

        hard = new JButton("Hard");
        hard.addActionListener(lis);
        gbc.gridx = 2;
        super.add(hard, gbc);

        goBack = new JButton("Return");
        gbc.gridx = 1;
        gbc.gridy = 3;
        super.add(goBack, gbc);

        super.setPreferredSize(new Dimension(854, 480));
    }

    public class DifficultyActionListener implements ActionListener {
        @Override 
        public void actionPerformed(ActionEvent lmai) {
            String sourceString = lmai.getActionCommand();
            switch (sourceString) {
                case "Easy":
                    controlMain.setDifficulty(0);
                    normal.setBackground(null);
                    hard.setBackground(null);
                    easy.setBackground(Color.orange);
                    break;
                
                case "Normal":
                    controlMain.setDifficulty(1);
                    easy.setBackground(null);
                    hard.setBackground(null);
                    normal.setBackground(Color.orange);
                    break;

                case "Hard":
                    controlMain.setDifficulty(2);
                    normal.setBackground(null);
                    easy.setBackground(null);
                    hard.setBackground(Color.orange);
                    break;

                default:
                    System.out.println(" wtf you just pressed an invisible button ?!? don't do that! ");
                    break;
            }
        }
    }

    public void paintButton() {
        switch (controlMain.getDifficulty()) {
            case 0:
                easy.setBackground(Color.orange);
                break;
            case 1:
                normal.setBackground(Color.orange);
                break;
            case 2:
                hard.setBackground(Color.orange);
                break;
            default:
                System.out.println("wtf is your difficulty at idk lmao");
        }
    }

    public JButton getReturnButton() {
        return this.goBack;
    }
}
