package wordle.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class WordleBrainTest {
	
	@Nested
    @DisplayName("Generate Word Tests")
    class testgenerateTargetWord {
    	
		
		/*
		 * This unit test will only be performed if the value of the Flag is true
		 * Generate two random Words
		 * Compare words to determine if the word is unique 
		 */
		
    	@ParameterizedTest
    	@ValueSource(booleans = {true})
    	@DisplayName("Unique Word flag(true) test")
    	void testuniqueWord(boolean flag) {
    		WordleBrain modelTest =  new WordleBrain();
    		modelTest.flagUniqueWord = flag;
    		modelTest.generateTargetWord();
    		String WordA = modelTest.getTargetWord();
    		modelTest.generateTargetWord();
    		String WordB = modelTest.getTargetWord();
    		assertFalse(WordA.equals(WordB), "Word is unique"); 
    	}
    	
		/*
		 * This unit test will only be performed if the value of the Flag is false
		 * Generate two random Words
		 * Compare words to determine if the word is unique 
		 */ 	
    	@ParameterizedTest
    	@ValueSource(booleans = {false})
    	@DisplayName("Unique Word flag(false) test")
    	void testuniqueWord1(boolean flag) {
    		
    		WordleBrain modelTest =  new WordleBrain();
    		modelTest.flagUniqueWord = flag;
    		modelTest.generateTargetWord();
    		String WordA = modelTest.getTargetWord();
    		modelTest.generateTargetWord();
    		String WordB = modelTest.getTargetWord();
    		assertTrue(WordA.equals(WordB), "Words are the same");
    		}
    	}
    	
    
	@Nested
	@DisplayName("Board State Tests")
	class testBoardState{
		
		@ParameterizedTest
		@ValueSource(strings = {"a","b"} )
		@DisplayName("Adding a letter from boardState")
		void testAdd(String letters) {
			WordleBrain modelTest =  new WordleBrain();
			
			modelTest.addLetter(letters);
			String temp [][] = modelTest.getBoardState();
			
			assertTrue(temp[0][0].equals(letters));
			
		}
		
		@ParameterizedTest
		@ValueSource(strings = {"a","b"} )
		@DisplayName("Deleting a letter from boardState")
		void testDelete(String letters) {
			
			
			WordleBrain modelTest =  new WordleBrain();
			
			
			modelTest.addLetter(letters);
			String temp [][] = modelTest.getBoardState();
			assertNotNull(temp[0][0]);
			modelTest.deleteLetter();
			temp = modelTest.getBoardState();
			assertNull(temp[0][0]);	
		}
		
	}
	
	
	@Nested
	@DisplayName("Verfication tests")
	class testVerification{
		
		@ParameterizedTest
		@ValueSource(strings = {"blush","bicep", "rebut"})
		@DisplayName("Correct values")
		void verifyCorrectWordd(String words) {
			WordleBrain modelTest =  new WordleBrain();
			
			// Set the flag to false (lock in the same word, and test 3 words that are within the target list
    		modelTest.flagUniqueWord = false;
    		modelTest.generateTargetWord();
			
			
			//Counts through the ValueSource and adds it to add letter (adds to boardstate)
			for(int i = 0; i <words.length();i++) {
				String str = words.substring(i,i+1);
				modelTest.addLetter(str);	
			}
			
			//Call the function to submit a word
			modelTest.submitWord();
			
			//If the String is populated, it won't be null (Marked True)
			assertTrue(modelTest.updateString.equals("You have guessed incorrectly!"));
			
		}
				
		@ParameterizedTest
		@ValueSource(strings = {"aaaaa", "bbbbb", "ccccc"})
		@DisplayName("Incorrect Values")
		void verifyWrongWordd(String words) {
			WordleBrain modelTest =  new WordleBrain();
			
			//Counts through the ValueSource and adds it to add letter (adds to boardstate)
			for(int i = 0; i <words.length();i++) {
				//Cheesy way of getting a single character
				String str = words.substring(4);
				modelTest.addLetter(str);
			}
			
			//Call the function to submit a word
			modelTest.submitWord();
			
			assertTrue(modelTest.updateString.equals("Enter a Valid Word"));
		}
	}
}

		
	
	

	

	

	
