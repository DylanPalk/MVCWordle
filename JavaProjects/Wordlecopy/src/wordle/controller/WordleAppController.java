package wordle.controller;
import wordle.model.WordleBrain;
import wordle.view.WordleFrame;
import javax.swing.JButton;

public class WordleAppController {

	private WordleFrame appframe;
	private WordleBrain model;
	
	public void start() {
		model = new WordleBrain();
		appframe = new WordleFrame(this, model);
	}
	
	/*
	 * Action listeners on the GUI determine what method is called, within the model
	 * 
	 */
	
	//Adds a single letter
	public void keyCalls(String letter) {
		String AddLetter = letter;
		model.addLetter(AddLetter);
	}
	
	//Delete a single letter
	public void deleteLetter() {
		model.deleteLetter();
	}
	
	//submits the word to the model
	public void submitWord() {
		model.submitCheck();
	}
	
	//gets target Word
	public void targetWord() {
		model.wordPrint();
	}
	
	//Resets the game
	public void resetGame() {
		model.resetGame();	
	}
	
}
