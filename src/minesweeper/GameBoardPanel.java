package minesweeper;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

public class GameBoardPanel extends JPanel {
   private static final long serialVersionUID = 1L;  // to prevent serial warning

   private int cell_size, glob_row, glob_col, canvas_height, canvas_width, numMines;
   private CellMouseListener listener = new CellMouseListener(); // TODO 3: a generic listener for all
   private spawnProtection firstClick = new spawnProtection(); // Spawn Protection so you won't die immediately
   private MineSweeperMain controlMain;
   private RevealActionListener revealActionListener;
   // Get a new MineMap
   private MineMap mineMap = new MineMap();
   // Define properties (package-visible)
   /** The game board composes of glob_rowxglob_col cells */
   Cell cells[][];
   /** Constructor */
   public GameBoardPanel(MineSweeperMain controlMain) {
      this.controlMain = controlMain;
      super.setLayout(new GridLayout(10, 10, 2, 2));  // JPanel
      // Set the size of the content-pane and pack all the components
      //  under this container.
      super.setPreferredSize(new Dimension(canvas_width, canvas_height));
   }

   // Initialize and re-initialize a new game
   public void newGame() {
      removeAll();
      switch (controlMain.getDifficulty()) {
         case 0:
            this.glob_row = minesweeper.MineSweeperConstants.EASY_ROWS;
            this.glob_col = minesweeper.MineSweeperConstants.EASY_COLS;
            this.numMines = minesweeper.MineSweeperConstants.EASY_MINE_NUM;
            this.cell_size = minesweeper.MineSweeperConstants.EASY_CELL_SIZE;
            System.out.println("Initialised Easy Difficulty");
            break;
         case 1:
            this.glob_row = minesweeper.MineSweeperConstants.NORMAL_ROWS;
            this.glob_col = minesweeper.MineSweeperConstants.NORMAL_COLS;
            this.numMines = minesweeper.MineSweeperConstants.NORMAL_MINE_NUM;
            this.cell_size = minesweeper.MineSweeperConstants.NORMAL_CELL_SIZE;
            System.out.println("Initialised Normal Difficulty");
            break;
         case 2:
            this.glob_row = minesweeper.MineSweeperConstants.HARD_ROWS;
            this.glob_col = minesweeper.MineSweeperConstants.HARD_COLS;
            this.numMines = minesweeper.MineSweeperConstants.HARD_MINE_NUM;
            this.cell_size = minesweeper.MineSweeperConstants.HARD_CELL_SIZE;
            System.out.println("Initialised Hard Difficulty");
            break;
         default:
            this.glob_row = 10;
            this.glob_col = 10;
            this.numMines = 10;
            this.cell_size = 40;
      }

      cells = new Cell[glob_row][glob_col];

      canvas_height = cell_size * glob_row;
      canvas_width = cell_size * glob_col;

      controlMain.setPreferredSize(new Dimension(canvas_width, canvas_height));
      controlMain.repaint();
      repaint();
      // Allocate the 2D array of Cell, and added into content-pane and this common listener
      for (int row = 0; row < glob_row; ++row) {
         for (int col = 0; col < glob_col; ++col) {
            cells[row][col] = new Cell(row, col);
            super.add(cells[row][col]);
            cells[row][col].repaint();
         }
      }
      super.setLayout(new GridLayout(glob_row, glob_col, 2, 2));  // JPanel
      // Set the size of the content-pane and pack all the components
      //  under this container.
      super.setPreferredSize(new Dimension(canvas_width, canvas_height));
      for (int row = 0; row < glob_row; row++) {
         for (int col = 0; col < glob_col; col++) {
            cells[row][col].addActionListener(firstClick);
         }
      }
   }

   
   public class spawnProtection implements ActionListener {
      private int firstRow, firstCol;

      @Override
      public void actionPerformed(ActionEvent e) {
         SoundManager.playSoundEffect("sprites/sounds/leftclick.wav",0f); // Play mine hit sound effect
         Cell sourcCell = (Cell) e.getSource();
         this.firstRow = sourcCell.row;
         this.firstCol = sourcCell.col;

         mineMap.newMineMap(numMines, glob_row, glob_col, firstRow, firstCol);

         // Reset cells, mines, and flags
         for (int row = 0; row < glob_row; row++) {
            for (int col = 0; col < glob_col; col++) {
               // Revoke the spawn protection
               cells[row][col].removeActionListener(firstClick);
               // Initialize each cell with/without mine
               cells[row][col].newGame(mineMap.isMined[row][col]);
               //Every cell adds back this common listener
               cells[row][col].addMouseListener(listener);
               //Changed to unRevealed
               cells[row][col].isRevealed = false;
            }
         }
         revealCell(sourcCell.row, sourcCell.col);
      }
   }

   // Return the number of mines [0, 8] in the 8 neighboring cells
   //  of the given cell at (srcRow, srcCol).
   private int getSurroundingMines(int srcRow, int srcCol) {
      int numMines = 0;
      for (int row = srcRow - 1; row <= srcRow + 1; row++) {
         for (int col = srcCol - 1; col <= srcCol + 1; col++) {
            // Need to ensure valid row and column numbers too
            if (row >= 0 && row < glob_row && col >= 0 && col < glob_col) {
               if (cells[row][col].isMined) numMines++;
            }
         }
      }
      return numMines;
   }

   // Reveal the cell at (srcRow, srcCol)
   // If this cell has 0 mines, reveal the 8 neighboring cells recursively
   private void revealCell(int srcRow, int srcCol) {
      int numMines = getSurroundingMines(srcRow, srcCol);
      revealSingleCell(cells[srcRow][srcCol]);
      if (numMines == 0) {
        // Recursively reveal the 8 neighboring cells
         for (int row = srcRow - 1; row <= srcRow + 1; row++) {
            for (int col = srcCol - 1; col <= srcCol + 1; col++) {
               // Need to ensure valid row and column numbers too
               if (row >= 0 && row < glob_row && col >= 0 && col < glob_col) {
                  if (!cells[row][col].isRevealed) revealCell(row, col);
               }
            }
         }
      }
   }

   // Return true if the player has won (all cells have been revealed or were mined)
   public boolean hasWon() {
      for (int row = 0; row < glob_row; row++) {
         for (int col = 0; col < glob_col; col++) {
            // Initialize each cell with/without mine
            if(!cells[row][col].isRevealed == !cells[row][col].isMined)
               return false;
         }
      }
      return true;
   }

   // Return true if the player has lost (a mine has been revealed)
   public boolean hasLost() {
      for (int row = 0; row < glob_row; row++) {
         for (int col = 0; col < glob_col; col++) {
            // Initialize each cell with/without mine
            if(cells[row][col].isRevealed && cells[row][col].isMined)
               return true;
         }
      }
      return false;
   }

   // Define a Listener Inner Class
   private class CellMouseListener extends MouseAdapter {
      @Override
      public void mouseClicked(MouseEvent e) {         // Get the source object that fired the Event
         Cell sourceCell = (Cell)e.getSource();

         if (sourceCell.isFlagged && e.getButton() == MouseEvent.BUTTON1) // Prevent clicking on flagged cell
            return;
         // For debugging
         System.out.println("You clicked on (" + sourceCell.row + "," + sourceCell.col + ")");

         // Left-click to reveal a cell; Right-click to plant/remove the flag.
         if (e.getButton() == MouseEvent.BUTTON1) {  // Left-button clicked
            updateMineFlagged();
            // if you hit a mine, game over
            // else reveal this cell
            if (sourceCell.isMined) 
               {
                  SoundManager.stopBackgroundMusic(); 
                  SoundManager.playSoundEffect("sprites/sounds/explosion-01.wav",3);
                  revealSingleCell(sourceCell);
                  controlMain.getTimer().stop();
                  controlMain.getStatusSection().getActualTimer().replaceLabel();
                  System.out.println("User has taken an L");
                  FancyReveal();
                  System.out.println("Player scored " + controlMain.getStatusSection().getActualTimer().getScore());
                  
                  for (int row = 0; row < glob_row; row++) {
                     for (int col = 0; col < glob_col; col++) {
                        cells[row][col].setBackground(Color.RED); //gives red animtion after the mine is pressed 
                     }
                  }
                  // JOptionPane.showMessageDialog(null, "Game Over");
                  //controlMain.addEndPage();

                  try {
                     // Load and register the custom font
                     Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("sprites/fonts/PixelifySans-VariableFont_wght.ttf")).deriveFont(30f);
                     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                     ge.registerFont(customFont);
         
                     // Customize UIManager properties
                     UIManager.put("OptionPane.messageFont", customFont);
                     UIManager.put("OptionPane.buttonFont", customFont);
                     UIManager.put("OptionPane.background", Color.WHITE);
                     UIManager.put("Panel.background", Color.WHITE);
                     UIManager.put("OptionPane.messageForeground", Color.BLACK);

                     JLabel messageLabel = new JLabel("Game Over", SwingConstants.CENTER);
                     messageLabel.setFont(customFont);
                     messageLabel.setForeground(Color.BLACK);
               
                     // Display the JOptionPane with the custom label
                     JOptionPane.showMessageDialog(null, messageLabel, "Game Over", JOptionPane.ERROR_MESSAGE);
           
    
                  } catch (IOException | FontFormatException p) {
                     p.printStackTrace();
                  }
                  controlMain.addEndPage();
               }
            else {
               SoundManager.playSoundEffect("sprites/sounds/leftclick.wav",0f); // Play mine hit sound effect
               revealCell(sourceCell.row, sourceCell.col);
            }
         } 
         else if (e.getButton() == MouseEvent.BUTTON3) { // right-button clicked
            sourceCell.isFlagged = !sourceCell.isFlagged;
            sourceCell.repaint();
            SoundManager.playSoundEffect("sprites/sounds/flag.wav",0f);
         }

         if(hasWon()) {
            for (int row = 0; row < glob_row; row++) {
               for (int col = 0; col < glob_col; col++) {
                  cells[row][col].removeMouseListener(listener);
               }
            }
            SoundManager.stopBackgroundMusic(); 
            SoundManager.playSoundEffect("sprites/sounds/winbeat.wav",3);
            controlMain.getTimer().stop();
            controlMain.getStatusSection().getActualTimer().replaceLabel();
            System.out.println("User has obtained another victory");
            System.out.println("Player scored " + controlMain.getStatusSection().getActualTimer().getScore());
            JOptionPane.showMessageDialog(null, "You've Won!");
            controlMain.addEndPage();
         }
      }
   }

   // The basic single cell reveal
   public void revealSingleCell(Cell sourceCell) {
      numMines = getSurroundingMines(sourceCell.row, sourceCell.col);
      sourceCell.setText(sourceCell.isMined ? " " : ((numMines == 0 ? "" : numMines)) + "");
      sourceCell.setFont(new Font("Calamity", Font.BOLD, 18)); 
      if (!sourceCell.isMined)
         switch (numMines) {
            case 1:
               sourceCell.setForeground(Color.GREEN);
               break;
            case 2:
               sourceCell.setForeground(Color.YELLOW);
               break;
            case 3:
               sourceCell.setForeground(Color.ORANGE);
               break;
            case 4:
               sourceCell.setForeground(Color.MAGENTA);
               break;
            case 5:
               sourceCell.setForeground(Color.PINK);
               break;
            case 6:
               sourceCell.setForeground(Color.RED);
               break;
            case 7:
               sourceCell.setForeground(Color.LIGHT_GRAY);
               break;
            case 8:
               sourceCell.setForeground(Color.BLACK);
            default:
               break;
         }
      else{}
      sourceCell.setForeground(Color.BLACK);
      sourceCell.isRevealed = true;
      sourceCell.repaint();  // based on isRevealed
      sourceCell.removeMouseListener(listener);
   }

   // Custom Action Listener for Auto Revealing
   public class RevealActionListener implements ActionListener {
      private int row, col, style;
      private int diffRow = 0, diffCol = 0;
      private Timer sourceTimer;

      public RevealActionListener(int style) {
         this.style = style;
         this.row = 0;
         this.col = 0;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         sourceTimer = (Timer) e.getSource(); // Get Source Timer

         // Style switch
         switch (style) {
            case 0:
               upDownSingleReveal();
               break;
            case 1:
               leftRightMultiReveal();
               break;
            case 2:
               centerReveal();
               break;
            case 3:
               randomReveal();
               break;
            default:
               break;
         }

         // Revoke all listener
         for (int row = 0; row < glob_row; row++) {
            for (int col = 0; col < glob_col; col++) {
               cells[row][col].removeMouseListener(listener);
            }
         }
      }

      // Reveal from Up to down, Single cell
      public void upDownSingleReveal() {
         while(cells[row][col].isRevealed) {
            if (col == glob_col - 1) {
               col = 0;
               row++;
            }
            else
               col++;
            if (row == glob_row - 1 && col == glob_col - 1)
               break;
         }
         // Reveal the cell
         revealSingleCell(cells[row][col]);

         // Auto increment
         if (col == glob_col - 1) {
            this.col = 0;
            row++;
         }
         else
            col++;

         //Stop condition
         if (row == glob_row) {
            sourceTimer.stop();
         } 
      }

      // Reveal from left to right, column by column
      public void leftRightMultiReveal() {
         for (row = 0; row < glob_row; row++)
            revealSingleCell(cells[row][col]);
         if (col < glob_col - 1)
            col++;
         else
            sourceTimer.stop();
      }

      // Reveal from center to outer
      public void centerReveal() {
         row = glob_row / 2;
         col = glob_col / 2;
         for (int stepRow = 0; stepRow <= diffRow ; stepRow++) {
            for (int stepCol = 0; stepCol <= diffCol; stepCol++) {
               revealSingleCell(cells[row-diffRow+stepRow-1][col-diffCol+stepCol-1]);
               revealSingleCell(cells[row+diffRow-stepRow][col-diffCol+stepCol-1]);
               revealSingleCell(cells[row+diffRow-stepRow][col+diffCol-stepCol]);
               revealSingleCell(cells[row-diffRow+stepRow-1][col+diffCol-stepCol]);
            }
         }
         diffRow++;
         diffCol++;

         if(diffRow == row || diffCol == col) {
            sourceTimer.stop();
            diffRow = 0;
            diffCol = 0;
         }
      }

      // Random reveal (Pattern unrecognisable) (Theoratically it's following the traditional revealCell() method starting from cells[0][0]. But everything is too quick that it appears random)
      public void randomReveal() {
         for (row = 0; row < glob_row; ++row){
            for(col = 0; col < glob_col; ++col){
               revealCell(row, col);
            }
         }
         sourceTimer.stop();
      }
   }

   // Cool Reveal Pattern Division
   private Timer revealTimer; // The timer that control the delay of reveal
   private Boolean timerSetUp = false;

   public void stopDelayTimer() { // To end the delay when you clicked restart
      if(timerSetUp)
         revealTimer.stop();
   }

   public void FancyReveal() {
      timerSetUp = true;
      int difficultyDelay, style;
      switch (controlMain.getDifficulty()) {
         case 0:
            difficultyDelay = 10;
            break;
         case 1:
            difficultyDelay = 6;
            break;
         case 2:
            difficultyDelay = 2;
            break;
         default:
            difficultyDelay = 20;
            break;
      } 

      style = Math.round((float)((3 + 0) * Math.random()));
      revealActionListener = new RevealActionListener(style);
      revealTimer = new Timer(difficultyDelay, revealActionListener);
      revealTimer.start();
   }

   public int getDifficulty() {
      return controlMain.getDifficulty();
   }

   private int mineFlagged = 0;
   private void updateMineFlagged() {
      int count = 0;
      for (int row = 0; row < glob_row; ++row) {
         for (int col = 0; col < glob_col; ++col) {
            if (cells[row][col].isFlagged && cells[row][col].isMined)
               ++count;
         }
      }
      mineFlagged = count;
   }

   public int getMineFlagged() {
      return mineFlagged;
   }
}
