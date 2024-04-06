package minesweeper;

import java.awt.*;        // Use AWT's Layout Manager
import java.awt.event.*;
import javax.swing.*;     // Use Swing's Containers and Components
/**
 * The Mine Sweeper Game.
 * Left-click to reveal a cell.
 * Right-click to plant/remove a flag for marking a suspected mine.
 * You win if all the cells not containing mines are revealed.
 * You lose if you reveal a cell containing a mine.
 */
public class MineSweeperMain extends JFrame {
   private static final long serialVersionUID = 1L;  // to prevent serial warning

   // private variables
   GameBoardPanel board = new GameBoardPanel();
   CustomTimerAction ActualTimer; // The actual timer recording the parameters 
   Timer timer; // The internal timer
   StatusSection statusSection = new StatusSection(); // The top panel

   // Constructor to set up all the UI and game components
   public MineSweeperMain() {
      Container cp = this.getContentPane();           // JFrame's content-pane
      cp.setLayout(new BorderLayout()); // in 10x10 GridLayout

      cp.add(statusSection, BorderLayout.NORTH);
      cp.add(board, BorderLayout.CENTER);

      board.newGame();

      pack();  // Pack the UI components, instead of setSize()
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
      setTitle("Minesweeper");
      setVisible(true);   // show it
   }

   public class StatusSection extends JPanel { // This is the top section panel added into the minesweeper
      //Private variables
      private JButton Restart; // Restart Button
      private JLabel LabelTimer; // Timer Label
      
      public StatusSection() { //Constructor
         super.setLayout(new GridLayout(1, 3, 250, 1)); //Set up the panel into gridlayout
         Restart = new JButton("Restart"); //Initialise the Button with text "Restart"
         super.add(Restart); //Add start to the top panel

         // To achieve the restart function
         Restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               board.newGame();
               ActualTimer.Reset(); 
               System.out.println("Game Restarted!"); // Output to log
            }
         });

         LabelTimer = new JLabel("0s"); //Initialise the Label
         ActualTimer  = new CustomTimerAction(LabelTimer); //Initialise the ActualTimer, used to record down second, minute, hour
         timer = new Timer(1000, ActualTimer); // Initialise the timer, and add ActualTimer to the actionPerformed
         timer.start(); //Start Timer
         super.add(LabelTimer); // Add the label onto the top panel
      }
   }

   public class CustomTimerAction implements ActionListener {
      //Private variables
      private int second, minute, hour;
      private JLabel label;

      @Override //This actionPerformed runs every second
      public void actionPerformed(ActionEvent e) {
         second++; //Classic Time manipulation
         if(second == 60) {
            second = 0;
            minute++;
         }
         if(minute == 60) {
            second = 0;
            minute = 0;
            hour++;
         }
         
         label.setText(this.toString()); // Set the timer label per second

         if(board.hasLost()) {
            label.setText("You've Lost!"); // Replace the timer with "You've Lost!"
            timer.stop(); // Stop the internal timer
         }
         if(board.hasWon()) {
            label.setText("Time Spent: " + this.toString());
            timer.stop(); // Stop the internal timer
         }
      }

      public CustomTimerAction(JLabel UpdatingLabel) {
         this.label = UpdatingLabel; // Point to the Timer label to modify
      }

      // This is used to reset the timer
      public void Reset() {
         this.second = 0;
         this.minute = 0;
         this.hour = 0; //Reset the 3 parameters
         timer.restart(); //Restart the 1s internal timer, negligible
         label.setText("0s"); //Reset Label to 0s
      }

      public String toString() { //Return in desired Time Format
         if(hour > 0) // Dumb method for output 00 format. There is definitely a String.format way to do but I'm too lazy to find out ;)
            return hour + " h " + (minute < 10 ? "0"+ minute : minute) + " min " + (second < 10 ? "0"+ second : second) + " s";
         else if(minute > 0) 
            return minute + " min " + (second < 10 ? "0"+ second : second) + " s";
         else
            return second + " s";
      }
   }

   // The entry main() method
   public static void main(String[] args) {
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new MineSweeperMain();
         }
      });
   }
}