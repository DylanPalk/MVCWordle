package WordleAll;

import java.util.List;
import java.util.Scanner;

public class CommandLine {

	private WordleBrain model;
	private Methods runnable;
	
	// Constructor for the CommandLine Class
	public CommandLine() {
		model = new WordleBrain();
		runnable = new Methods();
	}
	
	public static void main(String[] args) {
		Methods run = new Methods();
		run.start();
	}
	
}//End of Program
