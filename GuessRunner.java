import java.util.Random;

public class GuessRunner {

    static Result processGuess(int target, int guess) {
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
        System.out.printf("\t");
        if (strikes == 4) { // game over
            System.out.printf("4 strikes - Game over\n");
            return new Result(hits, strikes);
        }
        if (hits == 0 && strikes == 0)
            System.out.printf("Miss\n");
        else if (hits > 0 && strikes == 0)
            System.out.printf("%d hits\n", hits);
        else if (hits == 0 && strikes > 0)
            System.out.printf("%d strikes\n", strikes);
        else if (hits > 0 && strikes > 0)
            System.out.printf("%d strikes and %d hits\n", strikes, hits);

        return new Result(hits, strikes);
    }


    public static void main(String[] args) {
        // Run in test mode
        if (args.length > 0 && args[0].equals("-t")){
           // Check if the user enters the number of tests
            if (args[1] != null){
                try {
                    // Attempt to parse
                    int tests = Math.abs(Integer.parseInt(args[1]));
                    runTests(tests);
                }
                catch (NumberFormatException e){
                    System.out.println(e.getMessage());
                }
            }
            else {
                // Default to 100 tests
                runTests(100);
            }

        }
        else {
            int guess_cnt = 0;
            /* A dummy value, you need to code here
             * to get a target number from your opponent
             * should be a random number between [1000-9999]
             */

            // High range for the target number to discourage brute forcing
            int target = getTarget(6000, 9999);

            Result res = new Result();

            System.out.println("Guess\tResponse\n");

            while (res.getStrikes() < 4) {
                /* take a guess from user provided class
                 * the user provided class must be a Guess.class file
                 * that has implemented a static function called make_guess()
                 */
                int guess = Guess.make_guess(res.getHits(), res.getStrikes());

                System.out.printf("%d\n", guess);

                if (guess == -1) {    // user quits
                    System.out.printf("you quit: %d\n", target);
                    return;
                }

                guess_cnt++;

                res = processGuess(target, guess);
            }
            System.out.printf("Target: %d - Number of guesses: %d\n", target, guess_cnt);
        }
    }

    /**
     * Generate a random number within a range (inclusive)
     *
     * @param min: min value
     * @param max: max value
     * @return: a random number
     */
    public static int getTarget(int min, int max) {
        Random random = new Random();
        int target = random.nextInt((max - min) + 1) + min;

        // Check if the number has distinct digits
        boolean distinct = hasDistinctDigits(target);

        // This will ensure the randomized number will always have duplicate digits
        // This will break program that does not accept numbers with repeating digits as inputs
        if (distinct) {
            char[] digits = Integer.toString(target).toCharArray();
            digits[random.nextInt(2) + 1] = digits[3];
            target = Integer.parseInt(new String(digits));
        }
        return target;

    }

    /**
     * Check if a number has distinct digits
     *
     * @param number: target number
     * @return true if that number has distinct digits, false if not
     */
    private static boolean hasDistinctDigits(int number) {
        int numMask = 0;
        int numDigits = (int) Math.ceil(Math.log10(number + 1));
        for (int digitIdx = 0; digitIdx < numDigits; digitIdx++) {
            int curDigit = (int) (number / Math.pow(10, digitIdx)) % 10;
            int digitMask = (int) Math.pow(2, curDigit);
            if ((numMask & digitMask) > 0) return false;
            numMask = numMask | digitMask;
        }
        return true;
    }

    /**
     * This method is responsible running autonomous tests generating data for the report
     */
    private static void runTests(int tests) {
        // Initialize necessary variables
        int totalGuessCounts = 0;
        long totalExecTime = 0;
        int maxGuesses = 0;

        for (int i = 0; i < tests; i++) {
            int guess_cnt = 0;
            int target = getTarget(1000, 9999);

            Result res = new Result();

            // Start counting exec time
            long startTime = System.nanoTime();
            while (res.getStrikes() < 4) {
                /* take a guess from user provided class
                 * the user provided class must be a Guess.class file
                 * that has implemented a static function called make_guess()
                 */
                int guess = Guess.make_guess(res.getHits(), res.getStrikes());

                System.out.printf("%d\n", guess);

                if (guess == -1) {    // user quits
                    System.out.printf("you quit: %d\n", target);
                    return;
                }

                guess_cnt++;

                res = processGuess(target, guess);
            }
            // Stop counting exec time
            long endTime = System.nanoTime();
            // Reset the Guess class every run
            Guess.reset();

            // Max Guess Count
            if (guess_cnt > maxGuesses){
                maxGuesses = guess_cnt;
            }
            // Add current guess counts to total guess counts
            totalGuessCounts += guess_cnt;
            // Add current exec time to total exec time
            totalExecTime += endTime - startTime;
        }

        System.out.printf("\nAverage Guess Counts: %f\n", (double) totalGuessCounts / tests);
        System.out.printf("Maximum # of Guesses: %d\n", maxGuesses);
        System.out.printf("Average Execution Time(s): %f\n", (double) totalExecTime / (tests * Math.pow(10, 9)));
    }
}
