//Kevin Babashov babas007 and Takuya Paipoovong paipo001
//Import Section
import java.util.Scanner;



public class main {
    public static void main(String[] args) {
        Minefield minefield; //initializes a minefield for all the classes for conditionals to use.
        Scanner myScanner = new Scanner(System.in); // creates a scanner object for user input that is about to be received.
        System.out.println("What difficulty would you like to play on (Easy, Medium, Hard)? Any other input will be assumed easy mode.");
        String difficulty = myScanner.nextLine();
        System.out.println("Do you want the game in debug mode? (true or false)");
        boolean debug = myScanner.nextBoolean(); // These lines take in the user input for difficulty level and if they would like debug mode on (seeing both the board of the game and the fully revealed board at the same time.
        if (difficulty.equals("easy") || difficulty.equals("Easy")) { // These next few conditionals are all basically copy and paste, but for different difficulty levels so I will explain one.
            minefield = new Minefield(5, 5, 5); // initializes cell board
            System.out.println("Where do you want to start? Enter x coordinate. (if out of bound given, then you will start at 0 0)");
            int xCoord = myScanner.nextInt();
            System.out.println("Enter y coordinate");
            int yCoord = myScanner.nextInt(); // collects x and y coordinate for start
            if (xCoord <= 0 || xCoord > 4 || yCoord <= 0 || yCoord >5) { // checks if those values are in bounds, if not, then you get 0 0 as your start.
                xCoord = 0;
                yCoord = 0;
            }
            minefield.createMines(xCoord, yCoord, 5); // creates mines of 5 since this is an easy mode and makes sure that you can place a mine on the start through the method called
            minefield.evaluateField();
            minefield.revealStartingArea(xCoord, yCoord); // functions that essentially set up the board
        } else if (difficulty.equals("Medium") || difficulty.equals("medium")) { // same as easy mode but medium so slight changes
            minefield = new Minefield(9, 9, 12);
            System.out.println("Where do you want to start? Enter x coordinate. (if out of bound given, then you will start at 0 0)");
            int xCoord = myScanner.nextInt();
            System.out.println("Enter y coordinate");
            int yCoord = myScanner.nextInt();
            if (xCoord <= 0 || xCoord > 9 || yCoord <= 0 || yCoord >9) {
                xCoord = 0;
                yCoord = 0;
            }
            minefield.createMines(xCoord, yCoord, 12);
            minefield.evaluateField();
            minefield.revealStartingArea(xCoord, yCoord);
        } else if (difficulty.equals("Hard") || difficulty.equals("hard")){ // same as medium, but hard and with a few changes to size and such.
            minefield = new Minefield(20, 20, 40);
            System.out.println("Where do you want to start? Enter x coordinate. (if out of bound given, then you will start at 0 0)");
            int xCoord = myScanner.nextInt();
            System.out.println("Enter y coordinate");
            int yCoord = myScanner.nextInt();
            if (xCoord <= 0 || xCoord > 19 || yCoord <= 0 || yCoord >19) {
                xCoord = 0;
                yCoord = 0;
            }
                minefield.createMines(xCoord, yCoord, 40);
                minefield.evaluateField();
                minefield.revealStartingArea(xCoord, yCoord);
            }
        else { // this else block executes if you fail to type in one of the 6 options, it will then just give you an easy board.
            minefield = new Minefield(5, 5, 5);
            System.out.println("Where do you want to start? Enter x coordinate. (if out of bound given, then you will start at 0 0)");
            int xCoord = myScanner.nextInt();
            System.out.println("Enter y coordinate");
            int yCoord = myScanner.nextInt();
            if (xCoord <= 0 || xCoord > 4 || yCoord <= 0 || yCoord >4) {
                xCoord = 0;
                yCoord = 0;
            }
            minefield.createMines(xCoord, yCoord, 5);
            minefield.evaluateField();
            minefield.revealStartingArea(xCoord, yCoord);
        }
        while (!minefield.gameOver()) { // this is the while loop that actually runs until the game is over. it keeps running until the gameOver method returns true.
            System.out.print(minefield); // prints out the board at each turn.
            if (debug) // checks if debug is being used, if it is, then we print out the revealed board alongside the game board.
                minefield.debug();
            System.out.println("Where do you want to guess? Enter x coordinate.");
            int x = myScanner.nextInt();
            System.out.println("Enter y coordinate");
            int y = myScanner.nextInt();
            System.out.println("Would you like to use a flag? (true or false)");
            boolean flagUse = myScanner.nextBoolean(); // lines 75 through 80 are used to get info to enter for the guess
            if (minefield.guess(x, y, flagUse)) {// both checks the return value of guess for the conditional and prints statements in the method if you lose the game which are specific to why you lost.
                System.out.println("You hit a mine, you lost."); // this runs if the guess returns true meaning that you selected a mine without putting a flag on it.
                break; // breaks the loop to print out the board one last time
            }
            }
        System.out.println(minefield); // prints the board to show what caused you to win or lose
        myScanner.close(); // closes scanner after its been used for all the input.
        }
    }

