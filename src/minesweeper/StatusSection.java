package minesweeper;

import java.awt.*;        // Use AWT's Layout Manager
import java.awt.event.*;
import javax.swing.*;     // Use Swing's Containers and Components

public class StatusSection extends JPanel { // This is the top section panel added into the minesweeper
    //Private variables
    private JButton restart, goBack; // restart Button & Back to Menu Button
    private JLabel LabelTimer; // Timer Label
    private CustomTimerAction ActualTimer; // The actual timer recording the parameters 
    private Timer timer; // The internal timer
    private MineSweeperMain controlCenter; // Get the main method
    
    public StatusSection(MineSweeperMain controlCenter) { //Constructor
        this.controlCenter = controlCenter;
        super.setLayout(new GridLayout(1, 3, 25, 1)); //Set up the panel into gridlayout
        restart = new JButton("Restart"); //Initialise the Button with text "Restart"
        restart.setFont(UIManager.getFont("Button.font"));

        goBack = new JButton("Return");
        goBack.setFont(UIManager.getFont("Button.font"));

        super.add(goBack);

        

        // To achieve the restart function
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.playBackgroundMusic("sprites/sounds/newgamebeat.wav", true,3);
                controlCenter.getBoard().newGame();
                controlCenter.removeEndPage();
                resetTimer();
                System.out.println("Game restarted!"); // Output to log
            }
        });

        LabelTimer = new JLabel("000"); //Initialise the Label
        ActualTimer  = new CustomTimerAction(this); //Initialise the ActualTimer, used to record down second, minute, hour
        timer = new Timer(1000, ActualTimer); // Initialise the timer, and add ActualTimer to the actionPerformed
        LabelTimer.setHorizontalAlignment(SwingConstants.CENTER);
        LabelTimer.setFont(UIManager.getFont("Label.font"));
        super.add(LabelTimer); // Add the label onto the top panel
        super.add(restart); //Add start to the top panel
    }

    public Timer getTimer() {
        return this.timer;
    }

    public JLabel getLabel() {
        return this.LabelTimer;
    }

    public GameBoardPanel getBoard() {
        return controlCenter.getBoard();
    }

    public CustomTimerAction getActualTimer() {
        return this.ActualTimer;
    }

    public JButton getGoBackButton() {
        return this.goBack;
    }

    public void resetTimer() {
        controlCenter.getBoard().stopDelayTimer();
        timer.restart();
        this.ActualTimer.Reset();
    }
}