package minesweeper;

import java.awt.event.*;
import javax.swing.*;

public class CustomTimerAction implements ActionListener {
    //Private variables
    private int second, minute, hour;
    private JLabel label;
    private GameBoardPanel board;

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
       
       label.setText(this.toSecond()); // Set the timer label per second
    }

    public CustomTimerAction(StatusSection section) {
        this.label = section.getLabel(); // Point to the Timer label to modify
        this.board = section.getBoard();
    }

    // This is used to reset the timer
    public void Reset() {
       this.second = 0;
       this.minute = 0;
       this.hour = 0; //Reset the 3 parameters

       label.setText("000"); //Reset Label to 0s
    }

    public String toString() { //Return in desired Time Format
        if(hour > 0) // Dumb method for output 00 format. There is definitely a String.format way to do but I'm too lazy to find out ;)
            return hour + " h " + (minute < 10 ? "0"+ minute : minute) + " min " + (second < 10 ? "0"+ second : second) + " s";
        else if(minute > 0) 
            return minute + " min " + (second < 10 ? "0"+ second : second) + " s";
        else
            return second + " s";
    }
    
    public String toSecond() {
       return String.format("%3s", Integer.toString(second + minute * 60 + hour * 60)).replace(" ", "0");
    }

    public void replaceLabel() {
        if (this.board.hasWon())
            label.setText("Score: " + String.format("%.2f", getScore()) + "/100");
        else if (this.board.hasLost())
            label.setText("Score: " + String.format("%.2f", getScore())); // Replace the timer with "You've Lost!"
    }

    public float getScore() {
        int timeTaken = second + minute * 60 + hour * 60;
        int difficulty = board.getDifficulty();
        int mineFlagged = board.getMineFlagged();
        float finalScore;

        if (this.board.hasWon()) {
            switch (difficulty) {

                // Easy Difficulty
                case 0:
                    if(timeTaken < 15)
                        finalScore =  100f;
                    else if(timeTaken < 120)
                        finalScore =  (float)(-(2.0 / 441.0) * (timeTaken - 15) * (timeTaken - 15) + 100); // Powered by ChatGPT, this expression will result in an increasingly decreasing function when x > 15, and reaches 50 when x = 120
                    else
                        finalScore =  50.0f;
                    break;
            
                //Normal Difficulty
                case 1:
                    if(timeTaken < 60)
                        finalScore =  100f;
                    else if(timeTaken < 180)
                        finalScore =  (float)(-(1.0 / 288.0) * (timeTaken - 60) * (timeTaken - 60) + 100); // Same thing. The formula can be calculated by solving y = a(x-60)^2 + b, but I don't bother :/
                    else
                        finalScore =  50.0f;
                    break;

                //Hard Difficulty
                case 2:
                    if(timeTaken < 180)
                        finalScore =  100f;
                    else if(timeTaken < 300)
                        finalScore =  (float)(-(1.0 / 288.0) * (timeTaken - 180) * (timeTaken - 180) + 100); // Ok, I did some math, a = 100 / (300 - 180)^2, b = 100 always
                    else
                        finalScore =  50.0f;
                        break;

                //Whatever this difficulty is
                default:
                    System.out.println("You chose a non-existing difficulty and there is no score can measure your greatness...");
                    finalScore =  -1.0f;
                    break;
            }
            // If the player goals a score that is lower than their mineFlagged, score = mineFlagged
            return finalScore;
        }
        else {
            // Award player the number of flagged mine if they lose
            int difficultyModifier; 
            switch (difficulty) {
                case 0:
                    difficultyModifier = MineSweeperConstants.EASY_MINE_NUM;
                    break;
                case 1:
                    difficultyModifier = MineSweeperConstants.NORMAL_MINE_NUM;
                    break;
                case 2:
                    difficultyModifier = MineSweeperConstants.HARD_MINE_NUM;
                    break;
                default:
                    difficultyModifier = 180;
                    break;
            }
            return (float)mineFlagged / (float)difficultyModifier * 50;
        }
    }
 }