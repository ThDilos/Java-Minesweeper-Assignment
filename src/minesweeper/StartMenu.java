package minesweeper;

import javax.swing.*;


import java.awt.*;

public class StartMenu extends JPanel { // This is the start menu you see on initialisation
    private JButton start, settings, rankings;
    private JLabel bigTitle;
    private Dimension ButtonDimension = new Dimension(200, 30);
    private ImageIcon titleIcon = new ImageIcon("C:\\Users\\kaust\\OneDrive\\Documents\\IM1003\\MineSweeper\\src\\minesweeper\\fonts\\Minesweeper.jpg");



    

    public StartMenu(MineSweeperMain controlMain) {
        super.setBackground(Color.LIGHT_GRAY);
        super.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0; // grid x and y change the relative position of the component, with x being row, y being column
        gbc.gridy = 0;
        gbc.ipady = 10; // This changes the upper lower padding of individual components
        gbc.anchor = GridBagConstraints.CENTER; // This serves no purpose, but good to have maybe
        gbc.insets = new Insets(10,0,10,0); // The first int changes the distance between two components, dk about others havn't tried 

        super.setBackground(new Color(175, 175, 175));

        bigTitle = new JLabel(titleIcon);
        bigTitle.setHorizontalAlignment(SwingConstants.CENTER);        
        super.add(bigTitle, gbc);

        //bigTitle = new JLabel("MINESWEEPER YIPEE");
        //bigTitle.setHorizontalAlignment(SwingConstants.CENTER);
       // bigTitle.setFont(new Font("Monospaced", Font.BOLD, 50));
    
        super.add(bigTitle, gbc);

        gbc.gridy = 1;
        start = new JButton("Start");
        start.setPreferredSize(ButtonDimension);
        //start.setFont(new Font("Arial", Font.PLAIN, (int)ButtonDimension.getHeight() - 2)); 
        start.setFont(UIManager.getFont("Button.font"));
        start.setBackground(Color.WHITE); // Set the button color
        start.setFocusPainted(false);
        super.add(start, gbc);
        
        
        settings = new JButton("Settings");
        settings.setPreferredSize(new Dimension(200, 30));
        //settings.setFont(new Font("Arial", Font.PLAIN, (int)ButtonDimension.getHeight() - 2));
        settings.setFont(UIManager.getFont("Button.font"));
        settings.setBackground(Color.WHITE); // Set the button color
        gbc.gridy = 2;
        super.add(settings, gbc);

        rankings = new JButton("Rankings");
        gbc.gridy = 3;
        rankings.setPreferredSize(ButtonDimension);
        rankings.setBackground(Color.WHITE); // Set the button color
        rankings.setFocusPainted(false);
        //rankings.setFont(new Font("Arial", Font.PLAIN, (int)ButtonDimension.getHeight() - 2));
        rankings.setFont(UIManager.getFont("Button.font"));
        super.add(rankings, gbc);


        controlMain.setPreferredSize(new Dimension(854, 480)); // This is the size of Minecraft :3
     
    }
    
       
    public JButton getStartButton() { // You can't put removeAll() in a button that is about to be removed apparently :(
        return this.start;
    }

    public JButton getSettingButton() {
        return this.settings;
    }

    public JButton getRankingButton() {
        return this.rankings;
    }
 }
