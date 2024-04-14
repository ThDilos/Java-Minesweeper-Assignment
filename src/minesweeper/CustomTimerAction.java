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
            label.setText("Score: " + String.format("%.2f", getScore()) + "/100 in " + this.toString());
        else if (this.board.hasLost())
            label.setText("Score: " + String.format("%.2f", getScore()) + " You've Lost!"); // Replace the timer with "You've Lost!"
    }

    public float getScore() {
        int timeTaken = second + minute * 60 + hour * 60;
        int difficulty = board.getDifficulty();
        int mineFlagged = board.getMineFlagged();

        if (this.board.hasWon()) {
            switch (difficulty) {

                // Easy Difficulty
                case 0:
                    if(timeTaken < 15)
                        return 100f;
                    else if(timeTaken < 120)
                        return (float)(-(4.0 / 441.0) * (timeTaken - 15) * (timeTaken - 15) + 100); // Powered by ChatGPT, this expression will result in an increasingly decreasing function when x > 15, and reaches 0 when x = 120
                    else
                        return 0.0f;
            
                //Normal Difficulty
                case 1:
                    if(timeTaken < 60)
                        return 100f;
                    else if(timeTaken < 180)
                        return (float)(-(1.0 / 114.0) * (timeTaken - 60) * (timeTaken - 60) + 100); // Same thing. The formula can be calculated by solving y = a(x-60)^2 + b, but I don't bother :/
                    else
                        return 0.0f;

                //Hard Difficulty
                case 2:
                    if(timeTaken < 180)
                        return 100f;
                    else if(timeTaken < 300)
                        return (float)(-(1.0 / 144.0) * (timeTaken - 180) * (timeTaken - 180) + 100); // Ok, I did some math, a = 100 - (300 - 180)^2, b = 100 always
                    else
                        return 0.0f;

                //Whatever this difficulty is
                default:
                    System.out.println("You chose a non-existing difficulty and there is no score can measure your greatness...");
                    return -1.0f;
            }
        }
        else // Award player the number of flagged mine if they lose
            return (float)mineFlagged;
    }
 }