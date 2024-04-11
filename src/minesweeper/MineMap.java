package minesweeper;
/**
 * Define the locations of mines
 */

public class MineMap {
   // package access
   int numMines;
   boolean[][] isMined;

   
         // default is false

   // Constructor
   public MineMap() {
      super(); /*Construct itself for GameBoardPanel to use*/
   }

   // Allow user to change the rows and cols
   public void newMineMap(int numMines, int rowinput, int colinput, int firstRow, int firstCol) {

      isMined = new boolean[rowinput][colinput];
      this.numMines = numMines;
      // Hardcoded for illustration and testing, assume numMines=10
      /*
      isMined[0][0] = true;
      isMined[5][2] = true;
      isMined[9][5] = true;
      isMined[6][7] = true;
      isMined[8][2] = true;
      isMined[2][4] = true;
      isMined[5][7] = true;
      isMined[7][7] = true;
      isMined[3][6] = true;
      isMined[4][8] = true;
      */

      // Randomly Generating 
      for (int i = 0; i < numMines; i++) {
         int rows = getARandomInt(0, rowinput);
         int cols = getARandomInt(0, colinput);
         if (!isMined[rows][cols] && !((rows >= firstRow - 1 && rows <= firstRow + 1) && (cols >= firstCol - 1 && cols <= firstCol + 1)))
            isMined[rows][cols] = true;
         else
            i--;
      }
   }

   // Math.random returns a double between 0.0 - 1.0
   public int getARandomInt(int min, int max) {
      return (int)( min + Math.random() * (max - min) ); // To determine the range of generation
   }
}