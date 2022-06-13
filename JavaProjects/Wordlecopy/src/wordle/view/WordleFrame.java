package wordle.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.*;  
import java.awt.event.*;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import wordle.controller.WordleAppController;
import wordle.model.WordleBrain;

public class WordleFrame extends JFrame implements Observer{
	
	/*
	 * Initialises the 4 different Panels that will be inside the one Frame
	 */
	private WordlePanel basePanel;
	private WordlePanel keyboardPanel;
	private WordlePanel guessGridPanel;
	private WordlePanel miscellaneousPanel;
	private WordlePanel messagePanel;
	private WordleAppController	baseController;
	private WordleBrain model;
	
	//Creating the different buttons, and the amount of buttons required for the GUI stated with each 'type'
	JButton[][] grid = new JButton[6][5]; //Creates 30 Buttons to be the 'guess' grid
	JButton[] keyboard = new JButton[28]; //Creates 28 JButtons to be the Keyboard.
	JButton[] miscellaneous = new JButton[2]; //Creates 2 JButtons to be the Keyboard.
	
	/*
	 * Creates Constructors 
	 * 
	 */
	public WordleFrame(WordleAppController baseController, WordleBrain model) {
		//Attribute base Controller = Parameter baseController.
		this.baseController=baseController;
		this.model=model;
		
		//Creating objects
		basePanel = new WordlePanel(baseController);
		guessGridPanel = new WordlePanel(baseController);
		keyboardPanel = new WordlePanel(baseController);	
		miscellaneousPanel = new WordlePanel(baseController);
		messagePanel = new WordlePanel(baseController);
	
		//Calling setup variables
		setupBasePanel();
		setupguessGridPanel();
		setupKeyboardPanel();
		setupmiscellaneousPanel();
		
		//Creates an Observer for Model
		model.addObserver(this);
		update(model,null);
	}
	
	
	/*
	 * Sets contents Base frame size and visibility
	 * 
	 */
	private void setupBasePanel() {
		this.setContentPane(basePanel);
		this.setSize(1000,1000);
		this.setLayout(new BoxLayout(basePanel, BoxLayout.PAGE_AXIS));
		this.setVisible(true);
	}

	/*
	 * Grid panel consists of the 'grid' where the guesses lay out on, it almost mirrors the nested array boardState 
	 * Rows and colums are defined the same for easy transition. This method loops through the grid and assign them a colour and size
	 */
	private void setupguessGridPanel() {
				
		for (int i = 0; i< grid.length; i++){
		     for (int j = 0; j< grid[i].length; j++){
		    	grid[i][j] = new JButton();
				grid[i][j].setBackground(Color.black);
				grid[i][j].setPreferredSize(new Dimension(60,60));
				guessGridPanel.add(grid[i][j]);
		     }
		     }
		
		//Adds to base Panel + set visable
		basePanel.add(guessGridPanel);
		guessGridPanel.setLayout(new GridLayout(6,5));
		guessGridPanel.setVisible(true);
	}	

	
/*
 * Keyboard Panel consists of creating 28 buttons (26 a-z) and 2 additional submit,delete.
 * Keyboard ID is taken from the ORD value of a character so it may be itterated.
 */
	private void setupKeyboardPanel() {
		
		// To populate the first 26 letters of the Alphabet
		for(int i = 0; i <=25; i++) {	
			int ASCiiStart = 65; //Starts off at the Letter A. Z is 90
			int ASCiiTotal = ASCiiStart + i;
			final char ASCiiValue = (char) ASCiiTotal;	
			keyboard[i] = new JButton(String.valueOf(ASCiiValue));
			keyboard[i].setBackground(Color.lightGray);
			keyboard[i].setForeground(Color.white);
			keyboard[i].setPreferredSize(new Dimension(40,40));
			keyboard[i].addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {
		        	 baseController.keyCalls(String.valueOf(ASCiiValue).toLowerCase());
		         }
		      }); 
			keyboardPanel.add(keyboard[i]);
		}
		
		/* 
		 * Creating a submit button that will notify the controller
		 */
		keyboard[26] = new JButton("Submit");
		keyboard[26].addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 baseController.submitWord();
	         }
	      }); 
		keyboardPanel.add(keyboard[26]);
		
		
		/*
		 * Adding a backspace button that will find the last letter value
		 */
		keyboard[27] = new JButton ("BackSpace");	
		keyboard[27].addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 baseController.deleteLetter();
	         }
	      }); 
		keyboardPanel.add(keyboard[27]);
		
		/*
		 * Adds the keyboard panel to the Base pannel
		 */
		basePanel.add(keyboardPanel);
		keyboardPanel.setLayout(new GridLayout(5,5));
		keyboardPanel.setVisible(true);
	}

	/*
	 * Miscellaneous Panel is in charge of the extra buttons that don't fit the grid or keyboard
	 * Here we have, 'reset' which only shows after guessCount > 1 & target word, that shows the target word
	 */
	private void setupmiscellaneousPanel() {
		
		miscellaneous[0] = new JButton ("TargetWord");	
		miscellaneous[0].addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 baseController.targetWord();
	         }
	      }); 
		miscellaneousPanel.add(miscellaneous[0]);
		
		
		miscellaneous[1] = new JButton ("Reset");	
		miscellaneous[1].addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 baseController.resetGame();
	         }
	      });
		miscellaneous[1].setVisible(false);	
		
		miscellaneousPanel.add(miscellaneous[0]);
		miscellaneousPanel.add(miscellaneous[1]);
		
		
		basePanel.add(miscellaneousPanel);
		miscellaneousPanel.setLayout(new GridLayout(1,1));
		miscellaneousPanel.setVisible(true);
	}
	
	/*
	 * The setupmessagePane exists to show messages given by the model, as a pop up
	 * 
	 */
	private void setupmessagePane(String text) {
		String update = text;
		JOptionPane.showMessageDialog(basePanel,  update);
		
	}
	
	
	public void update(Observable o, Object arg) {
		// Whenever a model has called setchanged, this will be called
		
		// Checks to see if the user pressed delete
		boolean delete = model.isDeleteLetter();
		
		// Checks to see if the user Submited word
		boolean submit = model.issubmitCheck();
		
		// Checks if reset Board
		boolean deleteAll = model.getDeleteAll();
		
		boolean flagMessage = model.flagMessage();
		
		//String of the target word that needs guessing
		String targetWord = model.getTargetWord();
		
		//Creates an Array to hold the boardState
		String temp[][] = model.getBoardState();	
		
		/*
		//To transfer between a nested array and Array to match the Grid
		String letterGrid[] = new String[30];
		*/
		
		//Gets the string of guessed words;
		String userGuess[] = model.getsubmitWord();
		
		/*
		//Gets User SubmittedGuesses
		List<String> submitGuess = (List<String>) model.getSubmittedGuesses();
		*/
		
		//Gets the amount of times the user guessed
		int guess = model.getGuessCount();
		
		/*
		//Checks guessed words are full;
		boolean b = false;
		for(int i = 0; i < userGuess.length; i++){
		    if(userGuess[i] == null){
		        b = true;
		    }
		}
		*/
		
		//Fetches any message from string update, this may be empty if no message has been given by the model
		String update = model.getStringUpdate();
		
		//If the return string isn't empty and the flag is set to true, Message will be 'poped' to the user
		if(update != "" && flagMessage == true) {
			setupmessagePane(update);
		}
		//Lets the reset button appear after 1 guess from the user
		if(guess >= 1) {
			miscellaneous[1].setVisible(true);	
		}
		
		/*
		//Cycle through the nested array and update the one dimensional array
        for(int i=0; i < temp.length; i++) {
        	int t = temp[i].length;
            for(int d = 0; d <temp[i].length;d++) {
            	letterGrid[(i*t)+d] = temp[i][d];
            }
        }  		
       */
		
		
        //Gets previous row after submit has been clicked and changed colour to show if the value is in the correct space +/index
		/*
		 * iterates through the row submitted, checks the value to the target word and updates green or orange if matching/index.
		 */
        if(submit == true) {
        	for(int j = 0; j<temp[guess].length;j++){
        		if(targetWord.charAt(j) == userGuess[j].toCharArray()[0]) {
	    			 grid[guess-1][j].setText(userGuess[j]);
	    			 grid[guess-1][j].setBackground(Color.green);	 
        		}
        		else if(targetWord.contains(userGuess[j])){
	    			 grid[guess-1][j].setText(userGuess[j]);
	    			 grid[guess-1][j].setBackground(Color.orange);

        		}
        }	   
        }
         
        /*
         * Changes the colour of the keyboard after a submission from the user 
         * Each key will change when submitted, to green if correct + index, orange if just correct or gray if niether.
         */
        for (int i=0;i<26;i++) {
            for (int j=0; j<userGuess.length;j++) {
            	if(submit == true) {	
            	//Is the letter in the Alphabet assigned
                if (keyboard[i].getText().toLowerCase().equals(userGuess[j])) {
                	//does the letter match the index of userGuess
                	if(targetWord.charAt(j) == userGuess[j].toCharArray()[0]) {
                		keyboard[i].setBackground(Color.green);
                	}
                	else if(targetWord.contains(userGuess[j])) {
                		keyboard[i].setBackground(Color.orange);
                	}
                	else {
                		keyboard[i].setBackground(Color.gray);
                	}
                }
            }
        }            
        }
        
        //This loops runs anytime a letter is clicked (if the model allows it)
        // It will simply add the letter and colour the box or allow a deletion method.
		for (int j = 0; j < temp[guess].length; j++) {
			//guess gets updated then triggers this. It will trigger the next line
	    	 if(delete==true) {
	    		 grid[guess][j].setText("");
	    		 grid[guess][j].setBackground(Color.black);
	    		 }	    	 
	    	 if(temp[guess][j]!=null) {
    			 grid[guess][j].setText(temp[guess][j]);
	    		 grid[guess][j].setBackground(Color.white); 
	    	 }
	    	 
   	 }
		/*
		 * Controls deleteAll, if so will fetch updated boardState and clearing the GRID.
		 * 
		 */
		if(deleteAll == true) {
			for (int i = 0; i< 6; i++){
			     for (int j = 0; j< temp[i].length; j++){
		    		 grid[i][j].setText("");
		    		 grid[i][j].setBackground(Color.black); 
			}
			}	
		}
		/*
		 * Clearing the Keyboard if deleteAll is true
		 */
		if(deleteAll == true) {
			for(int i = 0; i<26;i++) {
				keyboard[i].setBackground(Color.lightGray);
				keyboard[i].setForeground(Color.white);
			}
		}
		repaint();    
   }
}
	    