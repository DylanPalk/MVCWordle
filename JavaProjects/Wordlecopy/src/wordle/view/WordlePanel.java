package wordle.view;

import javax.swing.*;

import wordle.controller.WordleAppController;

public class WordlePanel extends JPanel{
	
	private WordleAppController baseController;
	private JButton keyboard;
	private JButton guess;
	
	//constructor for the panel
	public WordlePanel(WordleAppController baseController) {
		this.baseController = baseController;			
	}
	
}

