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
   private RankPage rankPage = new RankPage(this);
   private EndPage endPage = new EndPage();

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
            endPage = new EndPage();
            board.newGame(); // Generate a new set of game on the Game Board
            statusSection.resetTimer();
            statusSection.getTimer().start(); // Start the timour :D
            cp.repaint();

            cp.add(statusSection, BorderLayout.NORTH); // Add the Status Section on top of the Container
            cp.add(board, BorderLayout.CENTER); // Add the main game board to the center of the Container
            pack(); // Adjust size to the standard one
            setLocationRelativeTo(null);

            statusSection.getGoBackButton().addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent evt) {
                  cp.removeAll();
                  cp.repaint();
                  cp.add(startMenu, BorderLayout.CENTER);
                  setPreferredSize(new Dimension(854, 480));
                  pack();
                  setLocationRelativeTo(null);
               }
            });
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
            setLocationRelativeTo(null);

            settingPage.getReturnButton().addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent owoWhatsDis) {
                  cp.removeAll();
                  cp.repaint();
                  cp.add(startMenu, BorderLayout.CENTER);
                  pack();
                  setLocationRelativeTo(null);
               }
            });
         }
      });

      startMenu.getRankingButton().addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            cp.removeAll();
            cp.repaint();
            rankUpdater();
            cp.add(rankPage,BorderLayout.CENTER);
            pack();
            setLocationRelativeTo(null);

            rankPage.getReturnButton().addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent owoWhatsDis) {
                  cp.removeAll();
                  cp.repaint();
                  cp.add(startMenu, BorderLayout.CENTER);
                  pack();
                  setLocationRelativeTo(null);
               }
            });

            rankPage.getClearAllButton().addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent uwu) {
                  rankPage.getLeaderBoard().clearRankings();
                  cp.removeAll();
                  cp.repaint();
                  rankUpdater();
                  pack();
                  setLocationRelativeTo(null);
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

   public void rankUpdater() {
      cp.removeAll();
      cp.repaint();
      rankPage = new RankPage(this);
      cp.add(rankPage,BorderLayout.CENTER);
      pack();
      setLocationRelativeTo(null);

      rankPage.getReturnButton().addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent owoWhatsDis) {
            cp.removeAll();
            cp.repaint();
            cp.add(startMenu, BorderLayout.CENTER);
            pack();
            setLocationRelativeTo(null);
         }
      });
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

   public RankPage getRankPage() {
      return this.rankPage;
   }


   // End Page Section
   public void addEndPage() {
      add(endPage, BorderLayout.SOUTH);
      pack();
      endPage.getAddToRank().addActionListener(buttonListener);
   }

   // Used when restart
   public void removeEndPage() {
      remove(endPage);
      pack();
      endPage = new EndPage();
      repaint();
   }

      // The custom Button listener
      private ButtonListener buttonListener = new ButtonListener();

      private class ButtonListener implements ActionListener {
         @Override
         public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
               case "Add to Rank":
                  endPage.infoInputSection();
                  endPage.getSubmit().addActionListener(buttonListener);
                  add(endPage, BorderLayout.SOUTH);
                  pack();
                  repaint();
                  break;
    
               case "Submit":
                  String output = rankPage.getLeaderBoard().addNewRecord(endPage.getNameInput(), statusSection.getActualTimer().getScore());
                  if (output.equals("New record added")) {
                     remove(endPage);
                     endPage.removeAll();
                     endPage.add(new JLabel("New record added"));                   
                  }
                  else {
                     endPage.setNoteTitle(output);
                  }
                     add(endPage, BorderLayout.SOUTH);
                     repaint();
                     pack();
                  break;
               default:
                  break;
              }
          }
       };

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