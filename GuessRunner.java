import java.util.Scanner;

public class GuessRunner {

    static Result processGuess(int target, int guess) {
        String des = Integer.toString(target);
        String src = Integer.toString(guess);
        int hits = 0;
        int strikes = 0;

        // process strikes
        for (int i = 0; i < 4; i++) {
            if (src.charAt(i) == des.charAt(i)) {
                strikes++;
                des = des.replace(des.charAt(i), 'a');
                src = src.replace(src.charAt(i), 'a');
            }
        }
        // process hits
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (src.charAt(i) != 'a') {
                    if (src.charAt(i) == des.charAt(j)) {
                        hits++;
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

    /**
     * Get and validate a user's input
     * @return an integer between 1000 and 9999
     */
    static int getUserInput() {

        // Initialize the scanner class
        Scanner scanner = new Scanner(System.in);

        // Declare target variable
        int target;

        // Input Validation
        do {
            System.out.print("Please enter an integer between 1000 and 9999: ");

            // Loop to check whether the user input is a number
            while (!scanner.hasNextInt()) {
                System.out.println("The target number must be an integer between 1000 and 9999");
                System.out.print("Please enter an integer between 1000 and 9999: ");
                // Move to the next input
                scanner.next();
            }
            // Get user input
            target = scanner.nextInt();

        } while (target < 1000 || target > 9999);

        // Close scanner
        scanner.close();

        return target;
    }

    public static void main(String[] args) {
        int guess_cnt = 0;
        /* A dummy value, you need to code here
         * to get a target number from your opponent
         * should be a random number between [1000-9999]
         */
        int target = getUserInput();

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
