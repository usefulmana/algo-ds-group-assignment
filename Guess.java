/*
 * You need to implement an algorithm to make guesses
 * for a 4-digits number in the method make_guess below
 * that means the guess must be a number between [1000-9999]
 * PLEASE DO NOT CHANGE THE NAME OF THE CLASS AND THE METHOD
 */
public class Guess {
	
	public static int make_guess(int hits, int strikes) {
		// Initial guesses to evaluate
		int[] initialGuesses = {1234, 5678, 9900};
		
		/*
		 * IMPLEMENT YOUR GUESS STRATEGY HERE
		 */
		 
		return initialGuesses[0];
	}

	public static int[] breakNumberIntoDigits(int num){
		int[] result = new int[4];
		int count = 0;
		while (num > 0){
			result[count] = num % 10;
			num = num / 10;
			count++;
		}
		return result;
	}
}