package minesweeper;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoardPanel extends JPanel {
   private int cell_size, glob_row, glob_col, canvas_height, canvas_width, numMines;

   private static final long serialVersionUID = 1L;  // to prevent serial warning

   private CellMouseListener listener = new CellMouseListener(); // TODO 3: a generic listener for all

   private MineSweeperMain controlMain;


   // Define properties (package-visible)
   /** The game board composes of glob_rowxglob_col cells */
   Cell cells[][];
   /** Number of mines */
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
      switch (controlMain.getDifficulty()) {
         case 0:
            this.glob_row = minesweeper.MineSweeperConstants.EASY_ROWS;
            this.glob_col = minesweeper.MineSweeperConstants.EASY_COLS;
            this.numMines = minesweeper.MineSweeperConstants.EASY_MINE_NUM;
            this.cell_size = minesweeper.MineSweeperConstants.EASY_CELL_SIZE;
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

      // Allocate the 2D array of Cell, and added into content-pane and this common listener
      for (int row = 0; row < glob_row; ++row) {
         for (int col = 0; col < glob_col; ++col) {
            cells[row][col] = new Cell(row, col);
            super.add(cells[row][col]);
         }
      }
      
      super.setLayout(new GridLayout(glob_row, glob_col, 2, 2));  // JPanel

      // Set the size of the content-pane and pack all the components
      //  under this container.
      super.setPreferredSize(new Dimension(canvas_width, canvas_height));

      // Get a new mine map
      MineMap mineMap = new MineMap();
      mineMap.newMineMap(numMines, glob_row, glob_col);

      // Reset cells, mines, and flags
      for (int row = 0; row < glob_row; row++) {
         for (int col = 0; col < glob_col; col++) {
            // Initialize each cell with/without mine
            cells[row][col].newGame(mineMap.isMined[row][col]);
            //Every cell adds back this common listener
            cells[row][col].addMouseListener(listener);
            //Changed to unRevealed
            cells[row][col].isRevealed = false;
         }
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
      Cell sourceCell = cells[srcRow][srcCol];
      int numMines = getSurroundingMines(srcRow, srcCol);
      sourceCell.setText(sourceCell.isMined ? "*" : ((numMines == 0 ? "" : numMines)) + "");
      sourceCell.isRevealed = true;
      sourceCell.paint();  // based on isRevealed
      sourceCell.removeMouseListener(listener);
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

   // [TODO 7]
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

   // [TODO 2] Define a Listener Inner Class
   private class CellMouseListener extends MouseAdapter {
      @Override
      public void mouseClicked(MouseEvent e) {         // Get the source object that fired the Event
         Cell sourceCell = (Cell)e.getSource();
         // For debugging
         System.out.println("You clicked on (" + sourceCell.row + "," + sourceCell.col + ")");

         // Left-click to reveal a cell; Right-click to plant/remove the flag.
         if (e.getButton() == MouseEvent.BUTTON1) {  // Left-button clicked
            // [TODO 5] (later, after TODO 3 and 4
            // if you hit a mine, game over
            // else reveal this cell
            if (sourceCell.isMined) 
               {
                  for (int row = 0; row < glob_row; row++) {
                     for (int col = 0; col < glob_col; col++) {
                        // Initialize each cell with/without mine
                        sourceCell.removeMouseListener(listener);
                        revealCell(row, col);
                        cells[row][col].paint();
                     }
                  }
                  controlMain.getTimer().stop();
                  controlMain.getStatusSection().getActualTimer().replaceLabel();
                  System.out.println("User has taken an L");
                  JOptionPane.showMessageDialog(null, "Game Over");
               }
            else {
               revealCell(sourceCell.row, sourceCell.col);
            }
         } 
         else if (e.getButton() == MouseEvent.BUTTON3) { // right-button clicked
            sourceCell.isFlagged = !sourceCell.isFlagged;
            sourceCell.paint();
         }

         if(hasWon()) {
            controlMain.getTimer().stop();
            controlMain.getStatusSection().getActualTimer().replaceLabel();
            System.out.println("User has obtained another victory");
            JOptionPane.showMessageDialog(null, "You've Won!");
         }
      }
   }
}