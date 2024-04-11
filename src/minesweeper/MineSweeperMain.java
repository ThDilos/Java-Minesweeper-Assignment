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
   private GameBoardPanel board = new GameBoardPanel(this);

   private StatusSection statusSection = new StatusSection(this); // The top panel
   private StartMenu startMenu = new StartMenu(this); // The Start Menu
   private SettingPage settingPage = new SettingPage(this);

   private Container cp; // Moved the container to a bigger level for more access

   private int difficulty; // This is changed by SettingPage
   // Easy = 0, Normal = 1, Hard = 2

   // Constructor to set up all the UI and game components
   public MineSweeperMain() {
      this.difficulty = 0;
      cp = this.getContentPane();           // JFrame's content-pane
      cp.setLayout(new BorderLayout()); // in 10x10 GridLayout
      cp.add(startMenu, BorderLayout.CENTER); // Open Start Menu on initialisation

      startMenu.getStartButton().addActionListener(new ActionListener() { // When you pressed "Start":
         @Override
         public void actionPerformed(ActionEvent evt) {
             cp.removeAll();
             board.newGame(); // Generate a new set of game on the Game Board
             statusSection.getTimer().start(); // Start the timour :D

             cp.add(statusSection, BorderLayout.NORTH); // Add the Status Section on top of the Container
             cp.add(board, BorderLayout.CENTER); // Add the main game board to the center of the Container
             pack(); // Adjust size to the standard one
         }
      });

      startMenu.getSettingButton().addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            cp.removeAll();
            cp.repaint();
            cp.add(settingPage, BorderLayout.CENTER);
            settingPage.paintButton();
            pack();

            settingPage.getReturnButton().addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent owoWhatsDis) {
                  cp.removeAll();
                  cp.repaint();
                  cp.add(startMenu, BorderLayout.CENTER);
                  pack();
               }
            });
         }
      });

      pack(); // Set size to the buttons in Start menu
     // Pack the UI components, instead of setSize()
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
      setTitle("Minesweeper");
      setVisible(true);   // show it
      setLocationRelativeTo(null); // Start the JFrame at the center of screen
   }

   /* Methods for enclosure purpose */
   public GameBoardPanel getBoard() {
      return this.board;
   }

   public Timer getTimer() {
      return this.statusSection.getTimer();
   }

   public StatusSection getStatusSection() {
      return this.statusSection;
   }

   public void setDifficulty(int val) {
      this.difficulty = val;
      System.out.println("Difficulty has been set to \"" + val + "\"");
   }

   public int getDifficulty() {
      return this.difficulty;
   }

   // The entry main() method
   public static void main(String[] args) {
      javax.swing.SwingUtilities.invokeLater(new Runnable() { // Whatever this is, it is good to have for some particular reason that I completely forgot
         @Override
         public void run() {
            new MineSweeperMain(); // The core
         }
      });
   }
}