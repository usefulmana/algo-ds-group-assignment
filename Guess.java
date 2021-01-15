import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * You need to implement an algorithm to make guesses
 * for a 4-digits number in the method make_guess below
 * that means the guess must be a number between [1000-9999]
 * PLEASE DO NOT CHANGE THE NAME OF THE CLASS AND THE METHOD
 */
public class Guess {
	private static List<Integer> possibleAnswers = generateAllPossibleNumbers();
	public static boolean first = true;

	public static int make_guess(int hits, int strikes) {

		int guess;

		if (first){
			// Shuffle the array
			Collections.shuffle(possibleAnswers);
			// Choose a random answer
			guess = possibleAnswers.get(0);
			first = false;
		}
		else {
			// Pruning possible answers
			for (int i = 1; i < possibleAnswers.size(); i++) {
				int[] scores = score(possibleAnswers.get(i), possibleAnswers.get(0));

				if (scores[0] != strikes || scores[1] != hits){
					int k = possibleAnswers.remove(i);
				}
			}
			// Shuffle again
			Collections.shuffle(possibleAnswers);
			// Choose a random number
			guess = possibleAnswers.get(0);
		}
		
		/*
		 * IMPLEMENT YOUR GUESS STRATEGY HERE
		 */
		 
		return guess;
	}

	public static int[] parseNum(int num){
		int[] result = new int[4];
		int count = 0;
		while (num > 0){
			result[count] = num % 10;
			num = num / 10;
			count++;
		}
		return result;
	}

	/**
	 * PROBLEM IS HERE? IS THIS EVEN LEGAL?
	 * @param target
	 * @param guess
	 * @return
	 */
	public static int[] score (int target, int guess){
		// IS THIS EVEN LEGAL IN THE COMPETITION?
		char des[] = Integer.toString(target).toCharArray();
		char src[] = Integer.toString(guess).toCharArray();
		int hits = 0;
		int strikes = 0;

		// process strikes
		for (int i = 0; i < 4; i++) {
			if (src[i] == des[i]) {
				strikes++;
				des[i] = 'a';
				src[i] = 'a';
			}
		}
		// process hits
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (src[i] != 'a') {
					if (src[i] == des[j]) {
						hits++;
						des[j] = 'a';
						break;
					}
				}
			}
		}
		int[] scores = new int[2];
		scores[0] = strikes;
		scores[1] = hits;
		return scores;
	}

	public static List<Integer> generateAllPossibleNumbers(){
		List<Integer> numbers = new ArrayList<>();
		for (int i = 1000; i < 10000; i++) {
			numbers.add(i);
		}
		return numbers;
	}

	/**
	 * Check if a number has distinct digits
	 * @param number: target number
	 * @return true if that number has distinct digits, false if not
	 */
	private static boolean isDistinct(int number) {
		int numMask = 0;
		int numDigits = (int) Math.ceil(Math.log10(number+1));
		for (int digitIdx = 0; digitIdx < numDigits; digitIdx++) {
			int curDigit = (int)(number / Math.pow(10,digitIdx)) % 10;
			int digitMask = (int)Math.pow(2, curDigit);
			if ((numMask & digitMask) > 0) return false;
			numMask = numMask | digitMask;
		}
		return true;
	}
}
