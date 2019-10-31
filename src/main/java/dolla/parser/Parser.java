package dolla.parser;

import dolla.ModeStringList;
import dolla.Time;
import dolla.ui.EntryUi;
import dolla.ui.SortUi;
import dolla.ui.Ui;
import dolla.ui.ModifyUi;
import dolla.command.Command;
import dolla.command.ErrorCommand;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parser is an abstract class that loads the appropriate command according to the user's input.
 * It also ensures that the user's input for the command is valid, such as by checking the format
 * of the input, and the utilisation of correct terms.
 */
public abstract class Parser implements ParserStringList, ModeStringList {

    protected String mode;
    protected LocalDate date;
    protected String description;
    protected String inputLine;
    protected String[] inputArray;
    protected String commandToRun;
    protected static final String SPACE = " ";
    protected static int undoFlag = 0;
    protected static int redoFlag = 0;
    protected static int prevPosition;

    /**
     * Creates an instance of a parser.
     * @param inputLine The entire string containing the user's input.
     */
    public Parser(String inputLine) {
        this.inputLine = inputLine;
        this.inputArray = inputLine.split(SPACE);
        this.commandToRun = inputArray[0];
    }


    public abstract Command parseInput();

    /**
     * Splits the input from the user and assigns the relevant data into description and date variables.
     * If the incorrect format is given in the input, the corresponding alert will be printed.
     */
    public void extractDescTime() throws Exception {
        // dataArray[0] is command, amount and description, dataArray[1] is time and tag
        String[] dataArray = inputLine.split(" /on ");
        String dateString = (dataArray[1].split(" /tag "))[0];
        description = dataArray[0].split(inputArray[2] + " ")[1];
        try {
            date = Time.readDate(dateString);
        } catch (ArrayIndexOutOfBoundsException e) {
            // TODO: Shouldn't happen anymore, need to test if this will happen still
            Ui.printMsg("Please add '/at <date>' after your task to specify the entry date.");
            throw new Exception("missing date");
        }  catch (DateTimeParseException e) {
            Ui.printDateFormatError();
            throw new Exception("invalid date");
        }
    }

    /**
     * Returns a double variable from the specified string.
     * <p>
     *     Returns 0 if the specified string is not of a number.
     * </p>
     * <p>
     *     Mainly used for using the specified string for calculations in the command.
     *     IE. Accessing arrays.
     * </p>
     * @param str String (of number) to be converted into integer type.
     * @return Integer type of the specified string.
     */
    public double stringToDouble(String str) {
        double newDouble = 0.0;
        try {
            newDouble = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            Ui.printInvalidNumberError(str);
        }
        return newDouble;
    }

    /**
     * Alerts the user that the input is invalid, and returns an ErrorCommand.
     * @return an ErrorCommand
     */
    public Command invalidCommand() {
        Ui.printInvalidCommandError();
        return new ErrorCommand();
    }

    /**
     * Checks if the first word after 'add' is either 'income' or 'expense'.
     * @param s String to be analysed.
     * @return Either 'expense' or 'income' if either are passed in.
     * @throws Exception ???
     */
    public static String verifyAddType(String s) throws Exception {
        if (s.equals("income") || s.equals("expense")) {
            return s;
        } else {
            EntryUi.printInvalidEntryType();
            throw new Exception("invalid type");
        }
    }

    /**
     * Returns true if no error occurs while creating the required variables for 'addEntryCommand'.
     * Also splits description and time components in the process.
     * @return true if no error occurs.
     */
    public boolean verifyAddCommand() {
        try {
            verifyAddType(inputArray[1]);
            stringToDouble(inputArray[2]);
            extractDescTime();
        } catch (IndexOutOfBoundsException e) {
            EntryUi.printInvalidEntryFormatError();
            return false;
        } catch (Exception e) {
            return false; // If error occurs, stop the method!
        }
        return true;
    }

    /**
     * Returns true if the only element in the input that follows 'modify' is a number.
     * @return true if the only element in the input that follows 'modify' is a number.
     */
    public boolean verifyFullModifyCommand() {
        try {
            Integer.parseInt(inputArray[1]);
        } catch (Exception e) {
            ModifyUi.printInvalidModifyFormatError();
            return false;
        }
        return true;
    }

    //@@author yetong1895

    /**
     * This method will check if the input contain an type to sort.
     * @return true is inputArray[1] contain something, false if inputArray[1] is invalid.
     */
    protected boolean verifySort() {
        try {
            String temp = inputArray[1];
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            SortUi.printInvalidSort(mode);
            return false;
        }
    }


    /**
     * This method will set the prevPosition int in this class.
     * @param prevPosition the prevPosition of a deleted input.
     */
    public static void setPrevPosition(int prevPosition) {
        Parser.prevPosition = prevPosition;
        undoFlag = 1;
    }

    /**
     * THis method will set prevPosition to -1 and undoFlag to 0.
     */
    public static void resetUndoFlag() {
        Parser.prevPosition = -1;
        undoFlag = 0;
    }

    /**
     * This method will set redoFlag to 1.
     */
    public static void setRedoFlag() {
        redoFlag = 1;
    }

    /**
     * This method will set redoFlag to 0.
     */
    public static void resetRedoFlag() {
        redoFlag = 0;
    }
}
