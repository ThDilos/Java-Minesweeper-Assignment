package minesweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JButton;
/**
 * The Cell class model the cells of the MineSweeper, by customizing (subclass)
 *   the javax.swing.JButton to include row/column and states.
 */
public class Cell extends JButton {
    private static final long serialVersionUID = 1L;  // to prevent serial warning
    // Define named constants for JButton's colors and fonts
    //  to be chosen based on cell's state
    private ImageIcon flagIcon = new ImageIcon("sprites/images/mflag.png");
    private ImageIcon mineIcon = new ImageIcon("sprites/images/mmine.png");
    public static final Color BG_NOT_REVEALED =  new Color(10, 10, 10);;
    public static final Color FG_NOT_REVEALED = Color.RED;    // flag, mines
    public static final Color BG_REVEALED = new Color(160, 160, 160); 
    public static final Color BG_REVEALEDNUMBERS = new Color(70, 70, 70); 
    public static final Color FG_REVEALED = Color.YELLOW; // number of mines
    public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);

    // Define properties (package-visible)
    /** The row and column number of the cell */
    int row, col;
    /** Already revealed? */
    boolean isRevealed;
    /** Is a mine? */
    boolean isMined;
    /** Is Flagged by player? */
    boolean isFlagged;

    /** Constructor */
    public Cell(int row, int col) {
        super();   // JTextField
        this.row = row;
        this.col = col;
        // Set JButton's default display properties
        super.setFont(FONT_NUMBERS);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Always call super.paintComponent to handle default painting

        if (isFlagged && !isRevealed) {
            setIcon(flagIcon);
            setText("");
        } else if (isMined && isRevealed) {
            setIcon(mineIcon);
            setText("");
        } else if (isRevealed) {  // Cell is revealed
            setIcon(null);  // No icon for a number
            // Check if the cell is supposed to display a number (i.e., it is not a mine and it is adjacent to mines)
            if (!getText().equals("") && !getText().equals(" ")) {
                setBackground(BG_REVEALEDNUMBERS);  // Set the specific background for cells with numbers
            } else {
                setBackground(BG_REVEALED);  // Set the default background for revealed cells without numbers
            }
        } else {
            setIcon(null);
            setText("");
            setBackground(BG_NOT_REVEALED);  // Default background for unrevealed cells
        }
    }
    
    /** Reset this cell, ready for a new game */
    public void newGame(boolean isMined) {
        this.isRevealed = false; // default
        this.isFlagged = false;  // default
        this.isMined = isMined;  // given
        super.setEnabled(true);// enable button
        super.setText("");  // display blank
    }
}