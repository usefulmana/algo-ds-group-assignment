public class GuessRunner {

	static boolean processGuess(int target, int guess) {
		int hits=0;
		int strikes=0;
		
		/* Provide your implementation to process 
		 * the guess by your oponent.
		 * Your code need to properly count the number of strikes and hits
		 * Code to print out the output of the guess is provided below
		 */
		 
		System.out.printf("\t");
		if (strikes==4)	{ // game over
			System.out.printf("4 strikes - Game over\n");
			return true;
		}
		if (hits==0 && strikes==0)
			System.out.printf("Miss\n");
		else if(hits>0 && strikes==0)
			System.out.printf("%d hits\n", hits);
		else if(hits==0 && strikes>0)
			System.out.printf("%d strikes\n", strikes);
		else if(hits>0 && strikes>0)
			System.out.printf("%d strikes and %d hits\n", strikes, hits);
		return false;
	}

	public static void main(String[] args) {
		boolean correct=false;
		int guess_cnt = 0;
		
		 /* A dummy value, you need to code here
		  * to get a target number for your oponent
		  * should be a random number between [1000-9999]
		  */
		int target = 1234;
		
		System.out.println("Guess\tResponse\n");
		while(!correct) {
			
			/* take a guess from user provided class
			 * the user provided class must be a Guess.class file
			 * that has implemented a static function called make_guess()
			 */
			int guess = Guess.make_guess();
			System.out.printf("%d\n", guess);
			
			if (guess == -1) {	// user quits
				System.out.printf("you quit: %d\n", target);
				return;
			}
			guess_cnt++;
			
			/* You need to code this method to process a guess
			 * provided by your oponent
			 */
			correct = processGuess(target, guess);
		}
		System.out.printf("Target: %d - Number of guesses: %d\n", target, guess_cnt);
	}
}
