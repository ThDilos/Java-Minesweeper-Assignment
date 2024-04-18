package minesweeper;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class LeaderBoard {
    // Some FINAL variables
    private final int rankReadLimit = 10;
    private final int nameCharLimit = 30;

    // Private variables
    private String[] name; // This two arrays store respective data
    private float[] score;
    private int index; // This index is the amount of inputs we have
    private File localRankings;
    
    private int difficulty; // The difficulty, to be updated every time the user change difficulty

    // The 3 readers for different difficulties
    private List<String> totalFile  = new ArrayList<>();
    private List<String> easyList = new ArrayList<>();
    private List<String> normalList = new ArrayList<>();

    public LeaderBoard(int difficulty) {
        name = new String[rankReadLimit]; // Initialise the name array
        score = new float[rankReadLimit]; // Initialise the score array

        this.difficulty = difficulty;

        // Initialise the 3 readers
        totalFile  = new ArrayList<>();
        easyList = new ArrayList<>();
        normalList = new ArrayList<>(); 

        index = 0; // Initialise the index to 0
        localRankings = new File("localRankings.txt");

        readCurrentFile();
        readingSequence();
        rankFileRefreshing();
    }

    // Create a new file containing standard format String
    private void createNewStandardFile() {
        try {
            localRankings.delete();
            localRankings.createNewFile();
            RandomAccessFile tempRAF = new RandomAccessFile(localRankings, "rw");
            tempRAF.writeBytes("#\n#");
            tempRAF.close();
        }
        catch(Exception e) {}
    }

    // Fresh the index, name and score String array after constructing and an operation
    private void readCurrentFile() {
        index = 0;
        try {
            // Create the file if existsn't
            if (!localRankings.exists()) {
                createNewStandardFile();
                System.out.println("\"localRanking.txt\" not found in your local machine\nSo this file has been created");
            }
            readingSequence();
            System.out.println("Difficulty is at " + difficulty + ". Reading ranks...");
            switch (difficulty) {
                // Easy Rank
                case 0:
                    if(easyList.isEmpty()) 
                        System.out.println("No entry found in Easy Ranking.");
                    else {
                        while(!easyList.isEmpty()) {
                            String[] splittedStrings = easyList.remove(0).split("!");
                            name[index] = splittedStrings[0];
                            score[index] = Float.parseFloat(splittedStrings[1]);
                            ++index;
                        }
                        System.out.println("Easy rank updated, found " + index + " entries.");
                    }
                    break;

                // Normal Rank
                case 1:
                    if(normalList.isEmpty())
                        System.out.println("No entry found in Normal Ranking.");
                    else {
                        while(!normalList.isEmpty()) {
                            String[] splittedStrings = normalList.remove(0).split("!");
                            name[index] = splittedStrings[0];
                            score[index] = Float.parseFloat(splittedStrings[1]);
                            ++index;
                        }
                        System.out.println("Normal rank updated, found " + index + " entries.");
                    }
                    break;

                // Hard Rank
                case 2:
                    if (totalFile.isEmpty())
                        System.out.println("No entry found in Hard Ranking.");
                    else {
                        while(!totalFile.isEmpty()) {
                            String[] splittedStrings = totalFile.remove(0).split("!");
                            name[index] = splittedStrings[0];
                            score[index] = Float.parseFloat(splittedStrings[1]);
                            ++index;
                        }
                        System.out.println("Hard rank updated, find " + index + " entries.");
                    }
                    break;

                // Non-existent rank
                default:
                    System.err.println("Unknown Difficulty number");
                    break;
            }
        }
        catch(Exception wtf) {
            System.out.println(wtf); // Output wtf has just happened, as per required by the java.io.File and java.io.RandomAccessFile 
        }
    }

    // Return a length 2 array, [0] is name, [1] is score
    public String[] getPair(int index) {
        if (index < this.index) 
            return new String[] {name[index],Float.toString(score[index]),Integer.toString(index)};
        else
            return new String[] {"-","-","-"};
    }

    // Adding new record, insert in order of descending scores
    public String addNewRecord(String name, float score) {
        try {
            if (!localRankings.exists()) {
                createNewStandardFile();
                System.out.println("\"localRanking.txt\" not found in your local machine\nDo not delete this stuff! >:(\nSo a new file has been created");
            }

            // Input validation, if not accept, return the exception
            if(!inputValidation(name).equals("True")) {
                return inputValidation(name);
            }
            else {
                readingSequence();
                switch (difficulty) {
                    case 0:
                        if (!easyList.isEmpty())
                            for (String str : easyList) {
                                Float listScore = Float.parseFloat(str.split("!")[1]);
                                int listIndex = easyList.indexOf(str);
                                if (score >= listScore){
                                    easyList.add(listIndex, name + "!" + score);
                                    break;
                                }
                                if (listIndex + 1 == easyList.size() && score < listScore) {
                                    easyList.add(name + "!" + score);
                                    break;
                                }
                            }
                        else
                            easyList.add(name + "!" + score);
                        System.out.println("A new record of [name:" + name + ";score:" + score + "] has been added into Easy Rank");

                        break;

                    case 1:
                        if (!normalList.isEmpty())
                            for (String str : normalList) {
                                Float listScore = Float.parseFloat(str.split("!")[1]);
                                int listIndex = normalList.indexOf(str);
                                if (score >= listScore){
                                    normalList.add(listIndex, name + "!" + score);
                                    break;
                                }
                                if (listIndex + 1 == normalList.size() && score < listScore) {
                                    normalList.add(name + "!" + score);
                                    break;
                                }
                            }
                        else
                            normalList.add(name + "!" + score);
                        System.out.println("A new record of [name:" + name + ";score:" + score + "] has been added into Normal Rank");

                        break;

                    case 2:
                        if (!totalFile.isEmpty())
                            for (String str : totalFile) {
                                Float listScore = Float.parseFloat(str.split("!")[1]);
                                int listIndex = totalFile.indexOf(str);
                                if (score >= listScore){
                                    totalFile.add(listIndex, name + "!" + score);
                                    break;
                                }
                                if (listIndex + 1 == totalFile.size() && score < listScore) {
                                    totalFile.add(name + "!" + score);
                                    break;
                                }
                            }
                        else
                            totalFile.add(name + "!" + score);
                        System.out.println("A new record of [name:" + name + ";score:" + score + "] has been added into Hard Rank");

                        break;

                    default:
                        break;
                }

                rankFileRefreshing();
                return "New record added";
            }
        }
        catch(Exception huh) {
            System.out.println(huh); // Output huh
            return huh.toString(); // Return a huh String, idw deal with this shi either
        }
    }

    // Row deletion
    public String deleteRecord(int index) {
        try{
            if (index > this.index - 1)
                return "Index exceeding current record counts.";
            else {
                readingSequence();

                switch (difficulty) {
                    case 0:
                        System.out.println(easyList.remove(index) + " has being removed from Easy Rank");
                        break;
                    case 1:
                        System.out.println(normalList.remove(index) + " has being removed from Normal Rank");
                        break;
                    case 2:
                        System.out.println(totalFile.remove(index) + " has being removed from Hard rank");
                        break;
                    default:
                        break;
                }

                rankFileRefreshing(); // Generate a new set of ranking based on the 3 readers
                readCurrentFile();
                return "Deletion completed";
            }
        }
        catch(Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }

    // Clear all the rankings for this difficulty
    public void clearRankings() {
        try {
            switch (difficulty) {
                case 0:
                    easyList.clear();
                    break;
                case 1:
                    normalList.clear();
                    break;
                case 2:
                    totalFile.clear();
                    break;
                default:
                    break;
            }
        rankFileRefreshing();
        readCurrentFile();
        } catch (Exception e) {}
    }

    // Update the current rank from file
    private void readingSequence() {
        try {
            RandomAccessFile tempRAF = new RandomAccessFile(localRankings, "rw"); // The temporary pointer to CRUD the localRankings.txt

            easyList.clear();
            normalList.clear();
            totalFile.clear();

            // Read whatever data we have right now
            while (tempRAF.getFilePointer() < tempRAF.length() && rankReadLimit > index)// Loop until all file is read
                totalFile.add(tempRAF.readLine());

            tempRAF.close();

            while(totalFile.indexOf("#") != 0) // Copy the data in totalFile that belongs to easy mode to easy
                easyList.add(totalFile.remove(0));
            totalFile.remove(0); // Remove the first #

            while(totalFile.indexOf("#") != 0) // Copy the data in totalFile that belongs to normal mode to normal
                normalList.add(totalFile.remove(0));
            totalFile.remove(0); // Then the rest of totalFile is hardList
        }
        catch(Exception e) {}
    }

    // Generate a new set of ranking based on the 3 readers
    private void rankFileRefreshing() {
        try {
            localRankings.delete();
            localRankings.createNewFile();
            RandomAccessFile tempRAF = new RandomAccessFile(localRankings, "rw");
            for (int i = 0; i < easyList.size(); ++i) {
                tempRAF.writeBytes(easyList.get(i) + "\n");
            }
            tempRAF.writeBytes("#\n");

            for (int i = 0; i < normalList.size(); ++i) {
                tempRAF.writeBytes(normalList.get(i) + "\n");
            }
            if(totalFile.isEmpty())
                tempRAF.writeBytes("#");
            else
                tempRAF.writeBytes("#\n");

            for (int i = 0; i < totalFile.size(); ++i) {
                tempRAF.writeBytes(totalFile.get(i));
                if (i < totalFile.size() - 1) 
                    tempRAF.writeBytes("\n");
            }
            tempRAF.close();
            readingSequence();
        } catch (Exception e) {}
    }

    // Validate user name input
    private String inputValidation(String name) {
        if (name.length() < 1)
            return "You didn't enter your name!";
        else if (name.contains("!") || name.contains("#"))
            return "Your name must not contain \"!\" or \"#\"";
        else if (name.length() > nameCharLimit)
            return "You can only have at most " + nameCharLimit + " characters in your name!";
        else
            return "True";
    }

    // Update Difficulty
    public void updateDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    // toString for all inputs
    public String toString() {
        String temp = "[    Index    Name    Score   ]";
        for(int i = 0; i < index; ++i) {
            temp += "\n" + "       " + i + "     " + name[i] + "  " + score[i];
        }
        return temp;
    }

    // toString for a particular index. Not really used but a nice thing to have
    public String toString(int index) {
        return "Record:[index:" + index + ",name:\"" + name[index] + "\",score:\"" + score[index] + "\"]";
    }
}
