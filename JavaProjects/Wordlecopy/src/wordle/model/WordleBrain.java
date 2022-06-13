package wordle.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;

import wordle.view.WordleFrame;
import wordle.view.WordlePanel;

public class WordleBrain extends Observable {

	String[][] boardState; // Nested array containing the grid row and column
	boolean deleteLetter; // Boolean variable to determine if the user wants to delete a letter
	boolean submitWord; // Boolean variable to determine if the user wants to submit a word
	boolean deleteAll; // Boolean variable to determine weather the user wants to reset the game
	boolean flagUniqueWord; //Determines if a word is unique or not
	boolean flagMessage; //Extra (disables messages) from popping up
	boolean flagShowWord; // boolean to determine if the user wants to see the target word (for testing)
	boolean flagError; // Boolean to determine if the user is assisted from allWords file.
	int guessCount; // Integer counting each guess the user makes
	String[] letterIndex; // A string array used to break down the submitted word
	String guess; // contains the guess, gets overwritten on each new submittion
	String targetWord; // String containing a string of the word to be guessed 
	String[] targetWordArr; //Uses to make it into a string array from character input
	String updateString; // String containing the different messages outputed by the program
	List<String> acceptedWords; // List string, containing common.txt
	List<String> testedWords; // List string, containing words.txt
	List<String> allWords; // List string, union of accepted words & tested words
	List<String> submittedGuesses; //list string, containing all submitted words
	
	// Add a constructor(s) that will run upon creation of WordleBrain
	public WordleBrain() {
		startGame(); //Initialises all variables 
		fileRead(); //Reads both files to determine if the game has data
		generateTargetWord(); // Generates a target words upon creation
		showWord(); // Will print target word to console if flag is true
	}

	/*
	 * These variables are initalised within this method, called by the constructor. 
	 * 
	 */
	public void startGame() {
		boardState = new String[7][5];
		deleteLetter = false;
		submitWord = false;
		deleteAll = false;
		flagUniqueWord = true;
		flagMessage = true;
		flagShowWord = false;
		flagError = true;
		guessCount = 0;
		letterIndex = new String[5];
		guess = "";
		targetWord = "";
		targetWordArr = new String[5];
		updateString = "";
		acceptedWords = new ArrayList<String>();
		testedWords = new ArrayList<String>();
		allWords = new ArrayList<String>();
		submittedGuesses = new ArrayList<String>();
		setChanged();
		notifyObservers();
	}

	/*
	 * A method, that sets the boolean DeleteAll to true 
	 * This method, stops creating another object & will 'reset' current variables + generate a new target word
	 * Data grabbed by the view will redo the GUI
	 */ 
	public void resetGame() {
		deleteAll = true;
		deleteAll();
		startGame();
		fileRead();
		generateTargetWord();
		showWord();
	}

	/*
	 * 	Method AddLetter, 
	 * First this method checks to see if its possible to enter a letter, by checking the boardState (row)
	 * IT won't add a letter upon a full row, stating that the user must delete or submit the word(letters)
	 * 
	 * @Parameter: Letter = a string containing a 'char' between [a-z]
	 * @Pre: Checks that the string passed to the user is filled and doesn't contain null
	 * @Post: Checks that the string passsed only contains values from a-z
	 */
	public void addLetter(String Letter) {
		assert Letter != null : "Letter requires a value";
		if (boardState[guessCount][4] == null) {
			BoardState(Letter);
		} else {
			updateString = ("Please Submit the Word or Delete letters");
		}
		//// https://stackoverflow.com/questions/10575624/java-string-see-if-a-string-contains-only-numbers-and-not-letters
		assert Letter.matches("[a-z]+") : "Letter entered is a valid character";
	}

	/*
	 * Called by the controller, 
	 * Sets boolean delete letter to true and Calls the method boardState to update the boardState
	 */
	public void deleteLetter() {
		deleteLetter = true;
		BoardState(null);
	}

	/*
	 * Called by the controller
	 * Sets boolean SubmitWord to true, calls the method submitWord to submit the word. 
	 */
	public void submitCheck() {
		submitWord = true;
		submitWord();
	}

	/*
	 * If user selects 'targetword' within the GUI, the controller will call this function
	 * This method set the message to the target word.
	 */
	public void wordPrint() {
		updateString = targetWord;
		setChanged();
		notifyObservers();
	}
	
	/*FLag controlled,
	 * if the flag is set to true for flagShowWord, it will print the chosen target word upon start up
	 * 
	 */
	public void showWord() {
		if(flagShowWord) {
			System.out.print(targetWord);
		}
	}

	
	/*
	 * This method is called by the WordleBrain constructor. It has no arguements and contains a try catch exception to read two files
	 * Each file contains words that are read by a scanner and stored, Later used as the target word or accepteble words 
	 * 
	 * @Pre: Checking that the both files contain an actual file
	 * @Post: Checking that after the file has been scanned and read line by line. The Array list stored isn't empty
	 */
	public void fileRead() {
		
		try {
			// To read in both the CSV values
			File acceptWord = new File("C:\\Users\\Dylan\\Downloads\\common.txt");
			File testWord = new File("C:\\Users\\Dylan\\Downloads\\words.txt");
			assert acceptWord != null;
			assert testWord != null;
			// Scan the file, Read line by Line and add to an Array List
			Scanner accdWords = new Scanner(acceptWord);
			while (accdWords.hasNext()) {
				acceptedWords.add(accdWords.nextLine());
			}
			assert acceptedWords != null : "File is empty";
			
			Scanner testWords = new Scanner(testWord);
			while (testWords.hasNext()) {
				testedWords.add(testWords.nextLine());
			}
			assert testedWords != null : "File is empty";
			
			updateString = ("Files Read successfully!");

			/*
			 * Adding all Available words to a single List
			 */
			allWords.addAll(acceptedWords);
			allWords.addAll(testedWords);

		} catch (FileNotFoundException ex) {
			updateString = ("Unable to find files!");
		}
	}
	
	/*
	 * BoardState is a method that update the boardState nested array. It has two systems inside
	 * The first Delete a letter, it will index back through the array and set the first value it finds that not null to null
	 * Second is add letter, it will index up through the array and replace the first null value it finds with the letter 
	 * @Parameter: a string containing a single 'character' 
	 * @Pre: Checks that the string is filled i.e not a null value
	 * @Post: Two post conditions
	 * 		  Delete: determines weather the backspace has been set back to false to stop multiple deletes from happening
	 * 		  Add: Check to see the value of letter in not null, stopping it from counting ahead or repeatidly adding letters
	 */		  
	
	public void BoardState(String Letter) {
		assert Letter != "" : "String cannot equal Null";
		// System.out.println(deleteLetter);
		// Counts backwards through the nested array and removes the first non Null
		// value
		if (deleteLetter == true) {
			loop1: 
			for (int i = boardState.length - 1; i >= 0; i--) {
				for (int j = boardState[i].length - 1; j >= 0; j--) {
					if (boardState[i][j] != null) {
							if(guessCount >= 1 && boardState[guessCount-1][4] != null && boardState[guessCount][0] == null) {
								updateString = ("Can't Delete locked in Words!");
							}
							else {
								boardState[i][j] = null;
								// System.out.print("Deleted Recent Letter \n");
								setChanged();
								notifyObservers();
								break loop1;
					}
					}

				}
			}
			deleteLetter = false;
			//Post condition Assert
			assert deleteLetter == false : "You can't delete multiple letters per backspace";
		}

		// The Grid Board State Each row (6) has 5 potential values. Here it checks if a
		// value has been entered
		// Loop through the nested Array and find an empty value
		loop1: for (int i = 0; i < boardState.length; i++) {
			for (int j = 0; j < boardState[i].length; j++) {
				if (boardState[i][j] == null) {
					boardState[i][j] = Letter;
					// System.out.println(Arrays.deepToString(boardState));
					setChanged();
					notifyObservers();
					//Post condition 
					assert boardState[i][j] == Letter : "You can't enter a null value";
					break loop1;
				}
			}

		}
		
	}

	/*
	 * Submit word is a method called by the controlled when a user has clicked submit
	 * Initially it will take the row of the current guess and convert it into a List String
	 * This List is used to determine weather the row contains any false values (such as null)
	 * If successful (contains valid characters) the method splits the row into individial characters
	 * Afterwards it gets set to a temp string called g, this variable is used to check if the value is correct
	 * valid or not a valid word. A flag is here to determine if words not contained within allWords are counted.
	 * 
	 * @Pre: Checking to see if the guessCount is a positive value under 5.
	 * @Post: Checks to see if the temp guess is 5 letters in length
	 */
	
	public void submitWord() {
		assert guessCount >= 0 :"guessCount can't be a negative value";
		assert guessCount <= 5 : "You can't guess over the grid value";
		
		List<String> wordSub = Arrays.asList(boardState[guessCount]);
		if (wordSub.contains(null)) {
			updateString = ("Word to Small");
			submitWord = false;
			setChanged();
			notifyObservers();
		} else {
			
			// Get the value from the player submitted word
			for (int a = 0; a <= guessCount; a++) {
				for (int b = 0; b < 5; b++) {
					letterIndex[b] = boardState[a][b];
				}
			}
			
			// Add the array of letters to a String
			for (int e = 0; e < letterIndex.length; e++) {
				guess += letterIndex[e];
			}

			// Set the String to Lowercase;
			String g = guess.toLowerCase();

			// Checks if the word is correct
			if (targetWord.contains(g)) {
				updateString = ("You guessed Correctly!");
				guessCount += 1;
				setChanged();
				notifyObservers();
				submitWord = false;
			} else if (allWords.contains(g)) {
				updateString = ("You have guessed incorrectly!");
				guessCount += 1;
				setChanged();
				notifyObservers();
				submitWord = false;
			} else {
				if(flagError) {
				updateString = ("Enter a Valid Word");
				submitWord = false;
				setChanged();
				notifyObservers();
				}
				else {
					guessCount += 1;
					setChanged();
					notifyObservers();
					submitWord = false;
				}
			}
			submittedGuesses.add(guess);
			assert guess.length() == 5 : "Guess must contain 5 letters";
			guess = "";
		}
	}
	
	
	/*
	 * Generate target word will use a random int to choose what word will be generated.
	 * the word is generated from acceptredWords (this contains all quiered words not accepted words)
	 * a flag is set here to determine weather a new word is generated each time, or same word each generation
	 * 
	 * @Pre: Determining that the boolean contains a valid value,
	 * @Post: that the targetWord generated is 5 letters. No error choosing a value
	 */
	public void generateTargetWord() {
		
		assert flagUniqueWord == true || flagUniqueWord == false : "flag not set";
		
		// Chooses a random word within the CSV
		// Change uniqueWord to True to get a new Random Word each time
		if (flagUniqueWord == true) {
			Random rand = new Random();
			int index = rand.nextInt(acceptedWords.size());
			targetWord = acceptedWords.get(index);
			
			// https://stackoverflow.com/questions/5235401/split-string-into-array-of-character-strings
			targetWordArr = targetWord.split("(?!^)");
		} else {
			targetWord = acceptedWords.get(0);
			
		}
		assert targetWord.length() == 5 : "Target word must equal 5 letters";
	}

	
	/*
	 * This method is called by the controller when the user selects the resert button
	 * A previous method sets delete all to True
	 * If true it will loop through boardstate backwards and turn everything into null
	 * Once completed it sets deleteAll to false
	 * 
	 */
	public void deleteAll() {
		if (deleteAll == true) {
			loop1: for (int i = boardState.length - 1; i >= 0; i--) {
				for (int j = boardState[i].length - 1; j >= 0; j--) {
					if (boardState[i][j] != null) {
							boardState[i][j] = null;
							setChanged();
							notifyObservers();
					}

				}
			}
			deleteAll = false;
		}
	}

	/*
	 * Getters
	 */
	
	public String[][] getBoardState() {
		return boardState;
	}

	public List<String> getAllWords() {
		return allWords;
	}

	public String[] getsubmitWord() {
		return letterIndex;
	}

	public String getTargetWord() {
		return targetWord;
	}

	public boolean isDeleteLetter() {
		return deleteLetter;
	}

	public boolean issubmitCheck() {
		return submitWord;
	}

	public boolean flagMessage() {
		return flagMessage;
	}

	public int getGuessCount() {
		return guessCount;
	}

	public String getStringUpdate() {
		String userMessage = updateString;
		updateString = "";
		return userMessage;
	}

	public List<String> getSubmittedGuesses() {
		return submittedGuesses;
	}

	public boolean getDeleteAll() {
		return deleteAll;
	}
}

// Call setChanged + notifyObservers()