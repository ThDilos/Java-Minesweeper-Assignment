package minesweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RankPage extends JPanel{
    private JLabel[] indexes, names, scores;
    private JButton[] deletions;
    private JButton goBack, clearRanks;
    private LeaderBoard localRankings;
    private MineSweeperMain controlMain;

    public RankPage(MineSweeperMain controlMain) {
        this.controlMain = controlMain; // Link up to the main container
        super.setLayout(new BorderLayout());

        localRankings = new LeaderBoard();
        super.add(topList(), BorderLayout.NORTH);
        super.add(content(), BorderLayout.CENTER);
        super.add(bottomSection(), BorderLayout.SOUTH);
    }

    public JPanel topList() { // The Top Labels
        JPanel temp = new JPanel();
        temp.setLayout(new GridLayout(1, 4, 0, 1));
        temp.add(new JLabel("Index", JLabel.CENTER));
        temp.add(new JLabel("Name", JLabel.CENTER));
        temp.add(new JLabel("Score", JLabel.CENTER));
        temp.add(new JLabel("Deletion", JLabel.CENTER));
        return temp;
    }

    public JPanel bottomSection() { // The Bottom Sections with 2 buttons
        JPanel temp = new JPanel();
 
        temp.setLayout(new FlowLayout());

        goBack = new JButton("Return");
        clearRanks = new JButton("Clear All");

        temp.add(goBack);
        temp.add(clearRanks);
        return temp;
    }

    private JPanel content() { // The middle contents with all the rankings
        JPanel temp = new JPanel();
        temp.setLayout(new GridLayout(10, 4, 0, 1));
        indexes = new JLabel[10];
        names = new JLabel[10];
        scores = new JLabel[10];
        deletions = new JButton[10];

        for(int i = 0; i < 10; ++i) {
            indexes[i] = new JLabel(Integer.toString(i+1));
            indexes[i].setHorizontalAlignment(SwingConstants.CENTER);
            names[i] = new JLabel(localRankings.getPair(i)[0]);
            names[i].setHorizontalAlignment(SwingConstants.CENTER);
            scores[i] = new JLabel(localRankings.getPair(i)[1]);
            scores[i].setHorizontalAlignment(SwingConstants.CENTER);

            temp.add(indexes[i]);
            temp.add(names[i]);
            temp.add(scores[i]);

            if(!localRankings.getPair(i)[1].equals("-")) {
                deletions[i] = new JButton("DEL " + Integer.toString(i));
                deletions[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String[] buttonLabel = e.getActionCommand().split(" ");
                        localRankings.deleteRecord(Integer.parseInt(buttonLabel[1]));
                        controlMain.rankUpdater();
                    }
                });
                temp.add(deletions[i]);
            }
            else
                temp.add(new JLabel("-"));
        }
        return temp;
    }

    public JButton getReturnButton() {
        return this.goBack;
    }

    public JButton getClearAllButton() {
        return this.clearRanks;
    }

    public LeaderBoard getLeaderBoard() {
        return this.localRankings;
    }
}
