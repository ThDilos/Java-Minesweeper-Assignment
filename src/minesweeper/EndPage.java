package minesweeper;

import java.awt.*;
import javax.swing.*;

public class EndPage extends JPanel {
    private JButton addToRank; // First you see a single button at the bottom, which is this one
    private JTextField userName; // If you clicked, you are brought to a new window (Override the previous button) // Within you can see this textfield that you can input your user name
    private JLabel note; // The label on the left telling you what's going on
    private JButton submit; // And the submit button on the right

    // Once you clicked submit, if successed, this whole section is replaced with a single label saying "New record added", else, error message replacing note

    // The getter army
    public JButton getAddToRank() {
        return addToRank;
    }

    public String getNameInput() {
        return userName.getText();
    }

    public void setNoteTitle(String input) {
        note.setText(input);
    }

    public String getNoteTitle() {
        return note.getText();
    }

    public JButton getSubmit() {
        return submit;
    }

    public EndPage() {
        super();
        removeAll();        
        setLayout(new FlowLayout());
        add(new JLabel()); // Empty placeholder
        addToRank = new JButton("Add to Rank");
        add(addToRank); // Add in the button
        add(new JLabel()); // Another Empty placeholder
    }

    public void infoInputSection() {
        removeAll(); // Remove the previous button
        setLayout(new FlowLayout());
        note = new JLabel("Enter your user name: ");
        add(note);

        userName = new JTextField(15);
        add(userName);

        submit = new JButton("Submit");
        add(submit);
    }
}
