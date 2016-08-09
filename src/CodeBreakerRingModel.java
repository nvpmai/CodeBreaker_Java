import java.io.*;
import java.util.*;

public class CodeBreakerRingModel {
    //set up variables
    private int final_score;
    private int current_hole_position = 0;
    private int current_choice = 0;
    private int attempts = 0;
    private int [] colors = {1, 2, 3, 4, 5, 6, 7, 8};
    private int [] secret_code = new int[6];
    private int [] player_choice = new int[6];
    private int [] correct_answers = new int[2];
    // Store player's score
    private ArrayList<ArrayList> scoreboard = new ArrayList(10);
    
    //constructor
    public CodeBreakerRingModel() {
        //default when model is created, it will shuffle the colors arrays to come up with secret codes
        shuffleArray(colors);   
        for (int i = 0;i < 6 ; i++) {
           secret_code[i] = colors[i];
        }
        readFile();
    }
    
                                    /****ACCESSORS****/
    //get the current choice of users on 8 balls
    public int getCurrentChoice() {
        return current_choice;
    }
    //get the value of the ball that player choose base on the index
        //8 numbers 1 to 8 will stand for 8 different balls
    public int getPlayerChoice(int index) {
        return player_choice[index];
    }
    //return attempts that users have made
    public int getAttempts(){
        return attempts;
    }
    //get the specific value of the ball in the array contains secret code
    public int getSecretCode(int index) {
        return secret_code[index];
    }
    //get the current hole position
    public int getCurrentHolePosition() {
        return current_hole_position;
    }
    //get the final score when players win
    public int getScore() {
        return final_score;
    }
    //return the number of correct answers from the users
    public int getNumberOfCorrect(){
        return correct_answers[0];
    }
    //return the number of half correct from the users
    public int getNumberOfHalfCorrect() {
        return correct_answers[1];
    }
   
                                /****MUTATORS****/
    //set the current choice of users on specific ball
    public void setCurrentChoice(int marble) {
        current_choice = marble;
    }
    //set the current hole position
    public void setCurrentPosition(int position) {
        current_hole_position = position;
    }
    //set the number of correct answer of users
    public void setCorrectAnswer(int num) {
        correct_answers[0] = num;
    }
    //set the number of half correct answer from users
    public void setHalfCorrectAnswer(int num) {
        correct_answers[1] = num;
    }
    
    
                        /********CORE FUNCTIONS********/
    //return true if the attempts is
    public boolean isStarted() {
        return attempts != 0;
    }
    //increase the attempt when ever player click accept
    public void attemptsAdd(){
        attempts++;
    }
    //check if players win or not based on the number of correct answers
    public boolean isWin() {
        boolean check = false;
        if (correct_answers[0] == 6)
            check = true;
        return check;   
    }
    
    public void updateModel() {
        player_choice[current_hole_position] = current_choice;
        countCorrect();
    }
    
    // shuffle the array
    static void shuffleArray(int[] array)
    {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
          int index = rand.nextInt(i + 1);
          // Simple swap
          int a = array[index];
          array[index] = array[i];
          array[i] = a;
        }
    }
    
    // Check if one value is in the given array and change that value into -1
       public boolean isContain(int x, int[] array){
        for (int i = 0;i < 6; i++) {
            if (x == array[i]) {
                array[i] = -1;
                return true;
            }
        }
        return false;
    }
       
       // Count the number of correct answer of users
    public void countCorrect() {
        correct_answers[0] = 0;
        correct_answers[1] = 0;
        
        int [] clone_secret_code = secret_code.clone();
        //correct answers
        for (int i = 0; i < 6; i++) {
            if (player_choice[i] == clone_secret_code[i]) {
                correct_answers[0]++;
                clone_secret_code[i] = -1;
            }
        }
        //half correct answers
        for(int i = 0; i < 6; i++) {
            if (isContain(player_choice[i], clone_secret_code)) {
                correct_answers[1]++;    
            }
        }
    }
       
       //change the position of the holes
    public void switchFocus() {
        if (current_hole_position == 5) //because we have 5 holes so when the increament reachs 5 it become 0
         current_hole_position = 0;
        else
         current_hole_position++;
    }
       
       // Provide score based on the attempts users have made
    public void setScore() {
        if (getAttempts() == 6 )
            final_score = 1000;
        else if (getAttempts() <= 12)
            final_score = 750;
        else if (getAttempts() <=36 )
            final_score = 500;
        else if (getAttempts() <=42 )
            final_score = 250;
        else if (getAttempts() <=60 )
            final_score = 100;
        else if (getAttempts() <=72 )
            final_score = 50;
        else
            final_score = 0;
    }
    
    // Sort scoreboard in descending order
    public void sortScoreboard() 
    {
        Collections.sort(scoreboard, new Comparator<ArrayList> ()
        {
            public int compare(ArrayList l1, ArrayList l2) {
                return ((Integer)l2.get(1)).compareTo((Integer)l1.get(1));
        }});
    }
    
    // Read scoreboard file
    public void readFile() 
    {
        try (Scanner myScanner = new Scanner(new File("scoreboard.txt"))) 
        {
            int index = 0;
            while (myScanner.hasNextLine()) 
            {
                String[] line = myScanner.nextLine().split(", ");
                if (line.length == 2) 
                {
                    scoreboard.add(new ArrayList());
                    scoreboard.get(index).add(line[0]);
                    scoreboard.get(index).add(Integer.parseInt(line[1]));
                    index++;
                }
            }
        }
        catch (IOException ex) { System.out.println(ex.getMessage()); }
        if (scoreboard.size() != 0) sortScoreboard();
    }
    
    // Write player's score to text file 
    public void writeFile() 
    {
        try (PrintWriter myWriter = new PrintWriter(new FileWriter("scoreboard.txt", false)))  
        {
            for (ArrayList name_score: scoreboard) 
                myWriter.println(name_score.get(0) + ", " + name_score.get(1));
        }
        catch (IOException ex) { System.out.println(ex.getMessage()); }
    }
    
    // Get scoreboard
    public String getScoreboard() 
    {
        StringBuffer scoreboard_string = new StringBuffer();
        for (ArrayList name_score: scoreboard) 
            scoreboard_string.append(name_score.get(0) + ":  " + name_score.get(1) + "\n");
        return scoreboard_string.toString();
    }
    
    // Check whether the score can be stored
    public boolean checkScoreboard() 
    {
        int scoreboard_size = scoreboard.size();
        if (scoreboard_size < 10 || final_score > (Integer)scoreboard.get(scoreboard_size - 1).get(1)) 
            return true;
        return false;
    }
    
    // Save player's new record
    public void savePlayerName(String playerName) 
    {
        if (scoreboard.size() == 10)
            scoreboard.remove(9);
        scoreboard.add(new ArrayList());
        scoreboard.get(scoreboard.size() - 1).add(playerName);
        scoreboard.get(scoreboard.size() - 1).add(final_score);
        sortScoreboard();
    }
}
