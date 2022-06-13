package WordleAll;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Methods {

	/*
	 * 
	 * initialising variables
	 */
	private WordleBrain model;
	String targetWord;
	int guessCount;
	List<String> submittedGuesses;
	List<Character> correctLetters;
	List<Character> almostLetters;
	List<Character> wrongLetters;
	List<String> remainingChar = new ArrayList<String>();
	String allowedCharacters[] = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
			"r", "s", "t", "u", "v", "w", "x", "y", "z" };
	int removeIndex;
	
	// Constructor calls pullVar
	public Methods() {
		model = new WordleBrain();
		PullVar();
	}

	//Pulls values from the model and assign to variables
	public void PullVar() {
		targetWord = model.getTargetWord();
		guessCount = model.getGuessCount();
		submittedGuesses = model.getSubmittedGuesses();
		correctLetters = new ArrayList<Character>();
		almostLetters = new ArrayList<Character>();
		wrongLetters = new ArrayList<Character>();
	}
	
	//Populate the arrayList with the alphabet in order
	public void populateList() {
		for(int i = 0; i <allowedCharacters.length;i++) {
			remainingChar.add(allowedCharacters[i]);
		}
	}

	// Gets the input from the User
	public String getInput(Scanner input, String question) {
		System.out.print(question);
		// Create an empty String
		String container = "";
		while (true) {
			container = input.nextLine();
			if (isAccepted(container) == true) {
				break;
			}
			System.out.print("please enter an accepted word: \n");
		}
		return container;
	}

	// Checks the input values from the user, ensures its the right length + characters
	private boolean isAccepted(String str) {
		boolean test = false;

		// Check that the object passed is a string
		if (str instanceof String) {
			// https://stackoverflow.com/questions/10575624/java-string-see-if-a-string-contains-only-numbers-and-not-letters
			if (str.matches("[a-z]+") && str.length() == 5) {
				test = true;
			} else {
				test = false;
			}

		}
		return test;
	}
	//Splits the word submitted from the user into strings containing a single character for the model
	public String submitWord(String str) {
		String userInput = str;
		for (int i = 0; i < userInput.length(); i++) {
			model.addLetter(String.valueOf(userInput.charAt(i)));
		}
		return str;
	}
	/*
	 * when called, will start the program, a while loop is contains the functionality
	 * This programm will keep running until they have succeeded or guessCount = 5
	 * Logic is applied to the word the user guessed to determine if the letter is sent to 1 of 4 arrays
	 * Each one has a role is showing the user whats available,correct,incorrect,correct(wrong index)
	 */
	public void start() {
		populateList();
		Scanner userObj = new Scanner(System.in); // Create a Scanner object
		System.out.print(targetWord);
		while (guessCount <= 4) {
			String userInput = getInput(userObj, "Please enter a 5 letter word: \n");
			System.out.print("Would you like to submit the guess?: Please enter 'yes' or 'no' ");
			String responce = userObj.nextLine();

			while (!responce.equals("yes")) {
				if (responce.equals("no")) {
					userInput = getInput(userObj, "Please enter a 5 letter word: \n");
					System.out.print("Would you like to submit the guess?: Please enter 'yes' or 'no' ");
					responce = userObj.nextLine();
				} else {
					responce = userObj.nextLine();
					System.out.println("Please enter 'yes' or 'no' ");
				}
			}

			if (responce.equals("yes")) {
				submitWord(userInput);
				model.submitCheck();

				if (model.getStringUpdate().equals("Enter a Valid Word")) {
					System.out.println("Please enter a valid word");
					for (int a = 0; a < 5; a++) {
						model.deleteLetter();
					}
				} else {

					for (int i = 0; i < targetWord.length(); i++) {
						if (targetWord.charAt(i) == userInput.charAt(i)) {
							// System.out.println("The Letter: " + targetWord.charAt(i) + " is correct in
							// position: " + i); // Indexing
							if (!correctLetters.contains(userInput.charAt(i))) {
								correctLetters.add(userInput.charAt(i));
								
								removeIndex = remainingChar.indexOf(String.valueOf(userInput.charAt(i)));
								//Try catch to stop the program attempting to remove a letter previously removed.
								try {
									remainingChar.remove(removeIndex);
									}
									catch(Exception e) {
									}						
							}
							// set
							// for
							// comp
							// sci
							// people
						} else if (targetWord.contains(userInput.substring(i, i + 1))) {
							// System.out.println("You've gussed: " + userInput.charAt(i) + " correctly, in
							// the wrong position.");
							if (!almostLetters.contains((userInput.charAt(i)))) {
								almostLetters.add(userInput.charAt(i));
								removeIndex = remainingChar.indexOf(String.valueOf(userInput.charAt(i)));
								try {
									remainingChar.remove(removeIndex);
									}
									catch(Exception e) {
									}	
							}

						} else {
							if (!wrongLetters.contains((userInput.charAt(i)))) {
								wrongLetters.add(userInput.charAt(i));
								removeIndex = remainingChar.indexOf(String.valueOf(userInput.charAt(i)));
								try {
									remainingChar.remove(removeIndex);
									}
									catch(Exception e) {
									}	
							}
						}
					}
					if (targetWord.equals(userInput)) {
						guessCount = 5;
						System.out.println("You won!");
					}
				}

				System.out.println("You have guessed " + model.getGuessCount() + " times");
				System.out.println("Guessed word is: " + submittedGuesses.get(submittedGuesses.size() - 1));
				System.out.println("Correct Letters in right position are: " + correctLetters);
				System.out.println("Correct Letters in wrong position are: " + almostLetters); 
				System.out.println("Wrong letters are: " + wrongLetters);
				System.out.println("Available letters are: " + remainingChar);
				System.out.println("You have guessed the following words: " + submittedGuesses);
			}
			guessCount = model.getGuessCount();
		}
	}
}
