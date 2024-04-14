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
    private Float[] score;
    private int index; // This index is the amount of inputs we have
    private File localRankings;
    

    public LeaderBoard() {
        name = new String[rankReadLimit]; // Initialise the name array
        score = new Float[rankReadLimit]; // Initialise the score array
        index = 0; // Initialise the index to 0
        localRankings = new File("localRankings.txt");
        
        System.out.println(localRankings.getAbsolutePath());

        readCurrentFile();
    }

    // Fresh the index, name and score String array after constructing and an operation
    private void readCurrentFile() {
        index = 0;
        try {
            // Create the file if existsn't
            if (!localRankings.exists()) {
                localRankings.createNewFile();
                System.out.println("\"localRanking.txt\" not found in your local machine\nSo this file has been created");
            }
            RandomAccessFile tempRAF = new RandomAccessFile(localRankings, "r"); // The temporary pointer to CRUD the localRankings.txt
            if (tempRAF.length() > 0) {
                // Read whatever data we have right now
                while (tempRAF.getFilePointer() < tempRAF.length() && rankReadLimit > index) { // Loop until pointer is at last position
                    
                    String[] splittedStrings = tempRAF.readLine().split("!"); // Split name and score with "!". As in the file, a data string is in the format "name!score"
                    name[index] = splittedStrings[0];
                    score[index] = Float.parseFloat(splittedStrings[1]);
                    System.out.println("Data set with index of " + index + " has been read. With name = " + name[index] + ", score = " + score[index]); // Debug output
                    ++index;
                }
                System.out.println("A total amount of " + index + " set of data have being found");
            }
            else {
                System.out.println("There is no data exist yet");
            }
            tempRAF.close(); // Close da pointer
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

    // Get index
    public int getIndex() {
        readCurrentFile();
        return index;
    }

    // Adding new record, insert in order of descending scores
    public String addNewRecord(String name, float score) {
        try {
            if (!localRankings.exists()) {
                localRankings.createNewFile();
                System.out.println("\"localRanking.txt\" not found in your local machine\nDo not delete this stuff! >:(\nSo a new file has been created");
            }

            // Input validation, if not accept, return the exception
            if(!inputValidation(name).equals("True")) {
                return inputValidation(name);
            }
            else {
                RandomAccessFile tempRAF = new RandomAccessFile(localRankings, "rw"); // The temporary pointer to CRUD the localRankings.txt
                if(tempRAF.length() == 0) {
                    tempRAF.writeBytes(name + "!" + score);
                }
                else {
                    int tempIndex = 0;
                    // Move cursor to the correct position
                    while(tempIndex < this.index) {
                        if(this.score[tempIndex] > score) 
                            tempRAF.readLine();
                        else
                            break;
                        System.out.println(score + " is lesser than " + this.score[tempIndex]);
                        ++tempIndex; 
                    }
                    if (tempIndex == this.index) {
                        System.out.println("Appendition performed");
                        tempRAF.writeBytes("\n" + name + "!" + score);
                    }
                    else {
                        long insertPosition = tempRAF.getFilePointer();
                        String theRest = getTheRestRecords(tempRAF);
                        System.out.println("Insertion Performed");
                        tempRAF.seek(insertPosition);
                        tempRAF.writeBytes(name + "!" + score + theRest); // Write new record
                    }
                }
                tempRAF.close(); // Close da pointer
                System.out.println("A new record of [name:" + name + ";score:" + score + "] has been added");
                readCurrentFile();
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
                RandomAccessFile tempRAF = new RandomAccessFile(localRankings, "rw");
                List<String> lines = new ArrayList<>();
                String line;

                while((line = tempRAF.readLine()) != null) {
                    lines.add(line);
                }
                lines.remove(index);
                tempRAF.close();

                localRankings.delete();
                localRankings.createNewFile();
                tempRAF = new RandomAccessFile(localRankings, "rw");
                for (int i = 0; i < lines.size(); ++i) {
                    tempRAF.writeBytes(lines.get(i));
                    if (i < lines.size() - 1) 
                        tempRAF.writeBytes("\n");
                }
                tempRAF.close();
                readCurrentFile();
                return "Deletion completed";
            }
        }
        catch(Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }

    // Clear all the rankings
    public void clearRankings() {
        try {
            localRankings.delete();
            localRankings.createNewFile();
            RandomAccessFile tempRAF = new RandomAccessFile(localRankings, "rw");
            tempRAF.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        readCurrentFile();
    }

    // Validate user name input
    private String inputValidation(String name) {
        if (name.length() < 1)
            return "You didn't enter your name!";
        else if (name.contains("!"))
            return "Your name must not contain \"!\"";
        else if (name.length() > nameCharLimit)
            return "You can only have at most " + nameCharLimit + " characters in your name!";
        else
            return "True";
    }

    // Return a String containing all the records after, to connect with the new insert one
    private String getTheRestRecords(RandomAccessFile tempRAF) { // RandomAccessFile has no insert function :(
        String everythingElse = "";
        try {
            while(tempRAF.getFilePointer() < tempRAF.length()) {
                everythingElse += "\n" + tempRAF.readLine();
            }
        }
        catch(Exception e) {
            return "what can possibily go wrong?";
        }
        return everythingElse;
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
