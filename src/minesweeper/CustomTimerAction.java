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
            label.setText("Time Spent: " + this.toString());
        else if (this.board.hasLost())
            label.setText("You've Lost!"); // Replace the timer with "You've Lost!"
    }
 }