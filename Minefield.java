//Kevin Babashov babas007 and Takuya Paipoovong paipo001
import java.util.Random;

public class Minefield {
    /**
     * Global Section
     */
    private Cell[][] minefield; // lines 8 through 21 are global variables for the Minefield class
    private int[] firstMine = new int[2];
    private int rows;
    private int columns;
    private int currFlag;
    public static final String ANSI_MAGENTA = "\u001B[35m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";

    public Minefield(int rows, int columns, int flags) { // constructor for the minefield class
        minefield = new Cell[rows][columns]; // initializes the minefield to the size of the rows and columns that are entered
        currFlag = flags; // sets currFlags to the number of flags allowed (equal to the number of mines)
        for (int i = 0; i < minefield.length; i++) { // for loops iterate through the field and sets each cell to false revealed and 0
            for (int j = 0; j < minefield[0].length; j++) {
                minefield[i][j] = new Cell(false, "0"); //Setting minefield to '0', we do this because we need to be able to manipulate the String in the evaluated field.
            }
        }
        this.rows = rows; // sets global variables to values passed into constructor.
        this.columns = columns;
    }
    public void evaluateField() { // this part of the method iterates through the entire 2D array and checks each spot for a mine.
        for (int i = 0; i < minefield.length; i++) {
            for (int j = 0; j < minefield[0].length; j++) {
                if (minefield[i][j].getStatus().equals("M")) { // checking each cell to see if the value is equal to mine.
                    helperEval(i, j); // calls the helper function to evaluate all the values around each mine that is what is listed below.
                }
            }
        }
    }
    public void helperEval(int x, int y) { // This is a helper function for method EvaluateField to update the surrounding tiles once a mine has been found
        for (int i = x - 1; i <= x + 1; i++) { //loops relative to where Mine is in the 3x3 2D Array for X-Cord
            for (int j = y - 1; j <= y + 1; j++) { //loops relative to where Mine is in the 3x3 2D Array for Y-Cord
                if (i >= 0 && i < minefield.length && j < minefield[0].length && j >= 0) { //Check if i and j are in bounds relative to where Mine is
                    if (!minefield[i][j].getStatus().equals("M") && !minefield[i][j].getStatus().equals("F")) { //Checks to see if the current tile IS NOT "M" mine and "F" Flag
                        int number = Integer.parseInt(minefield[i][j].getStatus()); //changes the string of the number into a type int and saves it
                        number++; //increases the number to account for the mine
                        String str = "" + number; //converts the number of type int into type string
                        minefield[i][j].setStatus(str); //sets the current position to the updated string
                    }
                }
            }
        }
    }
    public void createMines(int x, int y, int mines) { // this method sets all the mines on the board.
        Random rand = new Random(); // established random instances that are used later for getting random values between 0 and the max row/column
        int count = 0; // sets count of how many mines are placed
        while (count != mines) { // while mines placed are less than mines had.
            int temp = rand.nextInt(0, rows); // gets random int for x coordinate.
            int temp2 = rand.nextInt(0, columns); // gets random int for y coordinate
            if (x == temp && y == temp2) { // checks if the x and y that were passed into createMines is the same as the random generated one, if so, then it ignores the rest and continues with the next iteration.
                continue;
            }
            if (!"M".equals(minefield[temp][temp2].getStatus())) { // checks if a mine has already been placed there
                minefield[temp][temp2].setStatus("M"); // sets the status of that cell to M for mine.
                count++;
            }
        }
    }
    public boolean guess(int x, int y, boolean flag) { // method that takes in user guesses from the main class.
        if (x < minefield.length && x >= 0 && y < minefield.length && y >= 0) { //checks bounds for x and y.
            if (flag && currFlag > 0) { // checks if you wanted to place a flag, and you have a flag to place.
                minefield[x][y].setStatus("F"); // sets status of cell to flag
                currFlag--;
                minefield[x][y].setRevealed(true);
                return false; // returns false since the space that is being accessed currently is always F so no need to check.
            } else { // if you chose no flag, then you go into this case
                if (minefield[x][y].getStatus().equals("0")) { // if value is equal then it goes to the revealZeroes stack structure that used DFS to unlock all the zeroes.
                    revealZeroes(x, y);
                }
                minefield[x][y].setRevealed(true);
                return minefield[x][y].getStatus().equals("M"); // returns if that space was a mine or not.
            }
        }
        System.out.println("Out of bounds, same board will be printed out."); // prints out if the board boundaries given were out of range
        return false; // returns false for the guess.
    }
    public boolean gameOver() { // checks if the game is over
        for (int i = 0; i < minefield.length; i++) {
            for (int j = 0; j < minefield[0].length; j++) {
                if (minefield[i][j].getRevealed()) { // loops through the cell 2D array and checks if the cell is revealed
                    if (minefield[i][j].getStatus().equals("M")) {
                        if (i != firstMine[0] || j != firstMine[1]) { // if the cell is a mine and is not the original mine (the one you see first when revealing starts). then return true.
                            System.out.println("You hit a mine, you lost."); // you selected a mine that was
                            return true;
                        }
                    }
                }
            }
        }
        if (currFlag == 0) { // checks if all the flags are used
            for (int i = 0; i < minefield.length; i++) {
                for (int j = 0; j < minefield[0].length; j++) {
                    if (minefield[i][j].getStatus().equals("M")) { // iterates through a cell 2d array and checks all cells that are mines, and if there is one that hasn't been updated to a flagged, then you lost.
                        System.out.println("Ran out of flags, lost the game");
                        return true;
                    }
                }
            }
            System.out.println("You selected all mines with flags, you won!"); // otherwise, the for loop finished, and you used all flags correctly, meaning you won.
            return true;
        }
        return false;
    }
    public void revealZeroes(int x, int y) {
        Stack1Gen<int[]> stack = new Stack1Gen<int[]>();
        int[] CordArray = new int[]{x, y}; //creating a new integer array that initializes the stack with a tuple
        stack.push(CordArray);
        while (!stack.isEmpty()) { // while the stack is still full, we check the surrounding coordinates
            int[] data = stack.pop(); //pops the top of the stack
            int xCord = data[0];
            int yCord = data[1];
            minefield[xCord][yCord].setRevealed(true); // reveals the cell selected with the assigned variables
            if (xCord - 1 >= 0 && minefield[xCord - 1][yCord].getStatus().equals("0") && !minefield[xCord - 1][yCord].getRevealed()) { //Checking if x Coordinate is inbounds then adding the tuple into the new int array
                stack.push(new int[]{xCord - 1, yCord}); // if the value is zero and has not been revealed, yet then the cell is added to the stack
            }
            if (xCord + 1 < minefield.length && minefield[xCord + 1][yCord].getStatus().equals("0") && !minefield[xCord + 1][yCord].getRevealed()) { //Checking if x Coordinate is inbounds then adding the tuple into the new int array
                stack.push(new int[]{xCord + 1, yCord});
            }
            if (yCord + 1 < minefield[0].length && minefield[xCord][yCord + 1].getStatus().equals("0") && !minefield[xCord][yCord + 1].getRevealed()) { //Checking if x Coordinate is inbounds then adding the tuple into the new int array
                stack.push(new int[]{xCord, yCord + 1});
            }
            if (yCord - 1 >= 0 && minefield[xCord][yCord - 1].getStatus().equals("0") && !minefield[xCord][yCord - 1].getRevealed()) { //Checking if x Coordinate is inbounds then adding the tuple into the new int array
                stack.push(new int[]{xCord, yCord - 1});
            }
        }
    }
    public void revealStartingArea(int x, int y) {
        Q1Gen<int[]> queue = new Q1Gen<int[]>(); // initializes new queue
        int[] CordArr = new int[]{x, y}; // coordinate array
        queue.add(CordArr); // adds the coordinate array to the queue
        while (queue.length() != 0) { // goes until the queue is empty
            int[] cord = queue.remove();
            int row = cord[0]; // separates the first addition by row and column and then starts the direction of each later add to the queue data structure.
            int col = cord[1];
            if (minefield[row][col].getStatus().equals("M")) { // checks if its equal to a mine.
                firstMine[0] = row;
                firstMine[1] = col;
                minefield[row][col].setRevealed(true);
                return; // this is where the program ends when it finds the first mine, it then prints out the board for the user to start their game.
            }
            minefield[row][col].setRevealed(true);
            if (row - 1 >= 0) { // each of these cases of these conditionals checks each direction but through BFS in each of the cardinal directions
                int[] up = new int[]{row - 1, col};
                queue.add(up);
            }
            if (row + 1 < minefield.length) {
                int[] down = new int[]{row + 1, col};
                queue.add(down);
            }
            if (col - 1 >= 0) {
                int[] left = new int[]{row, col - 1};
                queue.add(left);
            }
            if (col + 1 < minefield[0].length) {
                int[] right = new int[]{row, col + 1};
                queue.add(right);
            }
        }
    }
    public void debug() { //Can copy from toString
        for (Cell[] cells : minefield) {
            for (int j = 0; j < minefield[0].length; j++) { // goes through each cell
                switch (cells[j].getStatus()) { // uses switch to determine which color to use for each value
                    case "0" -> System.out.print(ANSI_GREEN + "0" + ANSI_GREY_BACKGROUND);
                    case "1" -> System.out.print(ANSI_BLUE + "1" + ANSI_GREY_BACKGROUND);
                    case "2" -> System.out.print(ANSI_CYAN + "2" + ANSI_GREY_BACKGROUND);
                    case "3" -> System.out.print(ANSI_PURPLE + "3" + ANSI_GREY_BACKGROUND);
                    case "4" -> System.out.print(ANSI_YELLOW + "4" + ANSI_GREY_BACKGROUND);
                    case "5" -> System.out.print(ANSI_PURPLE + "5" + ANSI_GREY_BACKGROUND);
                    case "6" -> System.out.print(ANSI_BLUE + "6" + ANSI_GREY_BACKGROUND);
                    case "7" -> System.out.print(ANSI_GREEN + "7" + ANSI_GREY_BACKGROUND);
                    case "8" -> System.out.print(ANSI_YELLOW + "8" + ANSI_GREY_BACKGROUND);
                    case "9" -> System.out.print(ANSI_CYAN + "9" + ANSI_GREY_BACKGROUND);
                    case "M" -> System.out.print(ANSI_RED + "M" + ANSI_GREY_BACKGROUND);
                    case "F" -> System.out.print(ANSI_MAGENTA + "F" + ANSI_GREY_BACKGROUND);
                }
            }
            System.out.print("\n");
        }
    }
    public String toString() { //Need to color the cell to the color, so we need to check if the cell is a particular number/mine
        StringBuilder m = new StringBuilder(); // string builder used to append colors
        for (Cell[] cells : minefield) {
            for (int j = 0; j < minefield[0].length; j++) { // goes through cells.
                if (cells[j].getRevealed()) { // if they are revealed then they will be assigned a value
                    switch (cells[j].getStatus()) {
                        case "0" -> m.append(ANSI_GREEN + "0" + ANSI_GREY_BACKGROUND); // appends to the sting
                        case "1" -> m.append(ANSI_BLUE + "1" + ANSI_GREY_BACKGROUND);
                        case "2" -> m.append(ANSI_CYAN + "2" + ANSI_GREY_BACKGROUND);
                        case "3" -> m.append(ANSI_PURPLE + "3" + ANSI_GREY_BACKGROUND);
                        case "4" -> m.append(ANSI_YELLOW + "4" + ANSI_GREY_BACKGROUND);
                        case "5" -> m.append(ANSI_PURPLE + "5" + ANSI_GREY_BACKGROUND);
                        case "6" -> m.append(ANSI_BLUE + "6" + ANSI_GREY_BACKGROUND);
                        case "7" -> m.append(ANSI_GREEN + "7" + ANSI_GREY_BACKGROUND);
                        case "8" -> m.append(ANSI_YELLOW + "8" + ANSI_GREY_BACKGROUND);
                        case "9" -> m.append(ANSI_CYAN + "9" + ANSI_GREY_BACKGROUND);
                        case "M" -> m.append(ANSI_RED + "M" + ANSI_GREY_BACKGROUND);
                        case "F" -> m.append(ANSI_MAGENTA + "F" + ANSI_GREY_BACKGROUND);
                    }
                } else {
                    m.append('-'); // if there is a blank here, then it just appends -.
                }
            }
            m.append("\n"); // appends newline for each row.
        }
        m.append("\n"); // appends newline for when you have both the regular board and debug board up to know the line between the two
        return m.toString(); // returns the finished string.
    }
}