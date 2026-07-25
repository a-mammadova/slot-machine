import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final String RESET = "\u001B[0m";private static final String BOLD = "\u001B[1m";

    // text color
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String ORANGE = "\u001B[38;5;208m";

    // bg color
    private static final String RED_BG = "\u001B[41m";
    private static final String GREEN_BG = "\u001B[42m";

    // messages
    private static final ArrayList<String> lossMessages = new ArrayList<>();
    private static final ArrayList<String> winMessages = new ArrayList<>();

    private static final Random random = new Random();

    // achievements
    private static boolean firstWin = false;
    private static boolean firstLoss = false;
    private static boolean survived50Rounds = false;
    private static boolean allOrNothing = false;
    private static boolean playingItSafe = false;
    private static boolean welcomeToTheStreets = false;
    private static boolean houseHasConcerns = false;
    // private static final boolean recoveredFromAshes = false;
    private static boolean investor = false;
    private static boolean certifiedLuckyBastard = false;
    private static boolean weNeedToTalk = false;
    private static int jackpot = 0;
    private static boolean crack = false;

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        Scanner scanner = new Scanner(System.in);

        boolean repeat = true;

        double balance = 100;
        double highestBalance = 100;
        double lowestBalance = balance;
        double startingBalance = 100;
        double totalBet = 0;
        double totalWon = 0;
        double totalLoss = 0;
        double[] stat;

        int wins = 0;
        int losses = 0;
        int rounds = 0;
        int winStreak = 0;
        int lossStreak = 0;

        String[] result = new String[0];
        String[][] megaResult = new String[3][3];
        String answer = "";
        String[] symbols = new String[15];


        lossMessages.addAll(loadMessages("messages/loss.txt"));
        winMessages.addAll(loadMessages("messages/wins.txt"));
        ArrayList<String> streakMessages = loadMessages("messages/streak.txt");
        System.out.println(new File(".").getAbsolutePath());
        welcomeMessage();

        int reels = chooseGameMode(scanner);

        if (reels == 1) {result = new String[3]; }
        else if (reels == 2) {result = new String[5]; }
        else if (reels == 3) {megaResult = new String[3][3]; }

        int theme = chooseTheme(scanner);

        switch (theme) {
            case 1 -> symbols  = new String[]{ "🍒", "🍒", "🍒", "🍒", "🍒",
                    "🍋", "🍋", "🍋", "🔔", "🔔", "⭐", "7" };
                    // symbols = new String[]{"7" };

            case 2 -> symbols = new String[]{"🚀", "🚀", "🚀", "🚀", "🚀",
                    "🪐", "🪐", "🪐", "🛸", "🛸", "🌠", "🌜"};
                    // symbols = new String[]{"🌜"};

            case 3 -> symbols = new String[]{"🎸", "🎸", "🎸", "🎸", "🎸",
                    "🎼", "🎼", "🎼", "🎶", "🎶", "🎺", "🥁"};
                    // symbols = new String[]{"🥁"};
        }

        while (balance > 0 && repeat) {

            double bet = getBet(scanner, balance);
            balance -=bet;
            totalBet += bet;

            System.out.println(YELLOW + "\nSpinning..." + RESET);

            if (reels == 1) {
                result = spinClassic(result, symbols);
                stat = calculateClassicReward(bet, result);
            }
            else if (reels == 2) {
                result = spinDeluxe(result, symbols);
                stat = calculateDeluxeReward(bet, result);
            }
            else  {
                megaResult = spinMega(megaResult, symbols);
                stat = calculateMegaReward(bet, megaResult);
            }

            balance += stat[0];

            rounds++;

            if (stat[1] == 1) {
                wins++;
                totalWon += stat[0];
                winStreak++;
                lossStreak = 0;
            }
            else {
                losses++;
                totalLoss += stat[2];
                lossStreak++;
                winStreak = 0;
            }

            // streaks
            if (winStreak == 3) {
                System.out.println(GREEN + "🔥 Win Streak x3!" + RESET);
                System.out.println(GREEN + random("Three wins in a row? Who approved this?",
                        "Please stop winning.") + RESET);
            } if (winStreak == 5) {
                System.out.println( GREEN + random("🔥 Win Streak x5!\nThis is getting expensive. Please stop.",
                        "Leslie Casino would like to inspect your pockets.") + RESET);
            } if (winStreak == 10) {
                System.out.println(GREEN + "👑 LEGENDARY WIN STREAK!\nWe officially don't like you anymore." + RESET);
            } if (winStreak >= 10) {
                System.out.println(GREEN +
                        "☢ This shouldn't be statistically possible.\nWe're checking your sleeves." + RESET);
            }

            if (lossStreak == 3) {
                System.out.println(ORANGE + "🔥 Loss Streak x3! Luck issue" + RESET);
            } if (lossStreak == 5) {
                System.out.println(ORANGE + random( "🔥 Loss Streak x5!\nDeveloper Note: Please lose a little slower.",
                        "🔥 Loss Streak x5!\nTold you, the house always wins.") + RESET);
            } if (lossStreak == 10) {
                System.out.println(ORANGE + random("🔥 Loss Streak x10! Told you, the house always wins.",
                        "🔥 Loss Streak x10!\nLeslie Casino has updated your employee status from 'Customer' to 'Revenue Source'.")
                + RESET);
            } if (lossStreak >= 10) {
                System.out.println( ORANGE + random("Emergency meeting:\nShould we give this player directions home?",
                        "LEGENDARY LOSS STREAK!") + RESET);
            }

            if (!survived50Rounds && rounds >= 50) {
                survived50Rounds = unlock(survived50Rounds,"Endurance Test", "Touch some grass, bro.");
            }

            if (!welcomeToTheStreets && Math.floor(balance) == 1 || Math.ceil(balance) == 1) {
                welcomeToTheStreets = unlock(welcomeToTheStreets, "Hit the Bottom", "Welcome to the Streets.");
            }
            if (!houseHasConcerns && Math.floor(balance) == 1000 || Math.ceil(balance) == 1000) {
                houseHasConcerns = unlock(houseHasConcerns, "Hit 1k", "The House has concerns.");
            }

            if (!firstWin && wins == 1) {
                firstWin = unlock(firstWin, "Beginner's Luck", "Enjoy it while it lasts.");
            }

            if (!firstLoss && losses == 1) {
                firstLoss = unlock(firstLoss, "First Loss", "Welcome to Gambling");
            }

            if (!certifiedLuckyBastard && jackpot == 1) {
                certifiedLuckyBastard = unlock(certifiedLuckyBastard, "Certified Lucky Bastart",
                        "Hit your first Jackpot");
            } if (!weNeedToTalk && jackpot == 2) {
                weNeedToTalk = unlock(weNeedToTalk, "Second Jackpot?", "We need to talk.");
            } if (!crack && jackpot == 3) {
                crack = unlock(crack, "-_-", "3 Jackpot is impossible. You are definitely playing crack.");
            }


            if ( balance == 0) {
                System.out.println(BOLD + GREEN +
                        "Since your attempts kept Leslie Boss entertained, she spared you a $1 sədəqə."
                        + RESET);
                balance++;
            }
            if (balance > highestBalance) { highestBalance = balance; }
            if (balance < lowestBalance) { lowestBalance = balance; }

            repeat = playAgain(scanner);
        }

        showStats(startingBalance, rounds, wins, losses, totalWon, totalLoss, totalBet, highestBalance, lowestBalance, balance);
        gameOver();

        scanner.close();
    }

    static ArrayList<String> loadMessages(String file) throws FileNotFoundException {
        ArrayList<String> messages = new ArrayList<>();

        try (Scanner fileScanner = new Scanner(new File(file))) {
            while (fileScanner.hasNextLine()) {
                messages.add(fileScanner.nextLine());
            }
        }
        return messages;
    }

    static int chooseTheme(Scanner scanner) {
        int theme;

        do {
            System.out.print("""
                    
                    1. Default: 🍒 🔔 🍋 
                    2. Space: 🚀 🪐 🛸
                    3. Music: 🎸 🎼 🎹
                    
                    """);
            System.out.print("Choose a theme: ");
            theme = scanner.nextInt();
        } while (theme != 1 && theme != 2 && theme != 3);

        return theme;
    }
    static void welcomeMessage() {
        System.out.println(PURPLE + """
                             
                             ▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌
                             ▐                                        ▌
                             ▐             Leslie Casino              ▌
                             ▐                                        ▌
                             ▐▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌   
                        
                              """ + RESET);
    }

    static int chooseGameMode(Scanner scanner) {
        int mode;

        do {
            System.out.print("""
                1. Classic (3 Reels)
                2. Deluxe (5 Reels)
                3. Mega Grid (3×3)
                    
                    """);
            System.out.print("Choose a machine: ");
            mode = scanner.nextInt();
        } while (mode != 1 && mode != 2 && mode != 3);

        return mode;
    }

    static String center(String text, int width) {
        if (text.length() >= width) {
            return text;
        }

        int left = (width - text.length()) / 2;
        int right = width - text.length() - left;

        return " ".repeat(left) + text + " ".repeat(right);
    }

    static boolean unlock(boolean unlocked, String title, String description) {
        if (unlocked) {
            return true;
        }

        System.out.println(YELLOW + BOLD +
                "╔══════════════════════════════════════════════════════╗");
        System.out.printf ("║%s║%n", center("🏆 ACHIEVEMENT UNLOCKED 🏆", 54));
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.printf ("║%s║%n", center(title, 54));
        System.out.printf ("║%s║%n", center(description, 54));
        System.out.println("╚══════════════════════════════════════════════════════╝"
                + RESET);

        return true;
    }


    static double getBet(Scanner scanner, double balance) {
        System.out.println("───────────────────────");
        System.out.printf(BLUE + "Your balance is $%.2f\n" + RESET, balance);

        if (!houseHasConcerns && balance == 1000) {
            houseHasConcerns = unlock(houseHasConcerns, "Just Reached 1k", "The House Has Concerns.");
        }

        System.out.print("Enter bet amount: ");
        double bet = scanner.nextDouble();

        boolean allIn = bet == balance;
        while ( bet <= 0 || bet > balance){
            System.out.printf("\nYour balance is %.2f\n", balance);

            System.out.print("Enter a proper bet amount: ");
            bet = scanner.nextDouble();
        }

        if (!allOrNothing && allIn) {
            allOrNothing = unlock(allOrNothing, "All or Nothing", "Mostly Nothing.");
        } if (!investor && bet == 100) {
            investor = unlock(investor, "Investor", "Returns Not Guaranteed.");
        } if (!playingItSafe && bet == 1) {
            playingItSafe = unlock(playingItSafe, "Coward", "Playing It Safe.");
        } if (!welcomeToTheStreets && balance == 1) {
            welcomeToTheStreets = unlock(welcomeToTheStreets, "Lost Everything", "Welcome to the Streets.");
        }

        return bet;
    }

    static String[] spinClassic(String[] result, String[] symbols) throws InterruptedException{
        Thread.sleep(1000);
        for (int i = 0; i < result.length; i++) { result[i] = symbols[random.nextInt(symbols.length)]; }

        System.out.println(PURPLE + "┌────────────────┐");
        System.out.print("\r|  ?  | ?  | ? |\r");
        Thread.sleep(600);
        System.out.print("\r                    \r");

        System.out.printf("\r|  %s | ?  | ? |\r", result[0]);
        Thread.sleep(600);
        System.out.print("\r                    \r");

        System.out.printf("\r|  %s | %s | ? |\r", result[0], result[1]);
        Thread.sleep(600);
        System.out.print("\r                    \r");

        System.out.printf("\r|  %s | %s | %s |    \n", result[0], result[1], result[2]);
        System.out.println("└────────────────┘" + RESET);

        return result;
    }

    static String[][] spinMega(String[][] megaResult, String[] symbols) throws InterruptedException{
        Thread.sleep(1000);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {megaResult[i][j] = symbols[random.nextInt(symbols.length)];}
        }

        System.out.println(PURPLE + "┌────────────────┐");
        System.out.print("\r|  ?  | ?  | ? |");
        Thread.sleep(300);
        System.out.print("\r                    \r");

        System.out.printf("\r|  %s | ?  | ? |", megaResult[0][0]);
        Thread.sleep(300);
        System.out.print("\r                    \r");

        System.out.printf("\r|  %s | %s | ? |", megaResult[0][0], megaResult[0][1]);
        Thread.sleep(300);
        System.out.print("\r                    \r");

        System.out.printf("\r|  %s | %s | %s |", megaResult[0][0], megaResult[0][1], megaResult[0][2]);
        Thread.sleep(300);

        System.out.println();
        System.out.printf("\r|  %s | ? | ? |", megaResult[1][0]);
        Thread.sleep(300);
        System.out.print("\r                    \r");

        System.out.printf("\r|  %s | %s | ? |", megaResult[1][0], megaResult[1][1]);
        Thread.sleep(300);
        System.out.print("\r                    \r");

        System.out.printf("\r|  %s | %s | %s |\n", megaResult[1][0], megaResult[1][1], megaResult[1][2]);
        Thread.sleep(300);

        System.out.printf("\r|  %s | ? | ? |", megaResult[2][0]);
        Thread.sleep(300);
        System.out.print("\r                    \r");

        System.out.printf("\r|  %s | %s | ? |", megaResult[2][0], megaResult[2][1]);
        Thread.sleep(300);
        System.out.print("\r                    \r");

        System.out.printf("\r|  %s | %s | %s |", megaResult[2][0], megaResult[2][1], megaResult[2][2]);
        Thread.sleep(300);

        System.out.println("\n└────────────────┘" + RESET);

        return megaResult;
    }

    static String[] spinDeluxe(String[] result, String[] symbols) throws InterruptedException {
        Thread.sleep(1000);
        for (int i = 0; i < result.length; i++) { result[i] = symbols[random.nextInt(symbols.length)]; }

        System.out.println(PURPLE + "┌───────────────────────┐");
        System.out.print("\r|  ?  | ?  | ? | ? | ? |\r");
        Thread.sleep(800);
        System.out.print("\r                    \r");

        System.out.printf("\r|  %s | ?  | ? | ? | ? |\r", result[0]);
        Thread.sleep(800);
        System.out.print("\r                    \r");

        System.out.printf("\r|  %s | %s | ? | ? | ? |\r", result[0], result[1]);
        Thread.sleep(800);
        System.out.print("\r                    \r");

        System.out.printf("\r|  %s | %s | %s | ? | ? |\r", result[0], result[1], result[2]);
        Thread.sleep(800);
        System.out.print("\r                    \r");

        System.out.printf("\r|  %s | %s | %s | %s | ? |\r", result[0], result[1], result[2],
                result[3]);
        Thread.sleep(800);
        System.out.print("\r                    \r");

        System.out.printf("\r|  %s | %s | %s | %s | %s |  \n", result[0], result[1], result[2],
                result[3], result[4]);
        System.out.println("└───────────────────────┘" + RESET);

        return result;

    }
    static double[] calculateClassicReward(double bet, String[] result) {
        double[] stat = new double[3];
        double loss = 0;
        if (threeEqual(result[0], result[1], result[2])) {
            bet = reward(bet, result, 3);
            System.out.printf(GREEN + "\nYou won $%.2f in this round!\n" + RESET, bet);
            stat[0] = bet;
            stat[1] = 1;
        } else {
        randomLossMessage();
        loss = bet;
        stat[2] = loss;
    }
        return stat;
    }

    static double[] calculateDeluxeReward(double bet, String[] result) {
        double[] stat = new double[3];
        double loss = 0;

        if (result[0].equals(result[1]) && result[0].equals(result[2]) &&
                result[0].equals(result[3]) && result[0].equals(result[4])) {
            bet = reward(bet, result, 5);
            System.out.printf(GREEN + "\nYou won $%.2f in this round!\n" + RESET, bet);
            stat[0] = bet;
            stat[1] = 1;

        }
        else if (result[0].equals(result[1]) && result[0].equals(result[2]) && result[0].equals(result[3])) {
            bet = reward(bet, result, 4);
            System.out.printf(GREEN + "\nYou won $%.2f in this round!\n" + RESET, bet);
            stat[0] = bet;
            stat[1] = 1;
        }
        else if (result[0].equals(result[1]) && result[0].equals(result[2])) {
            bet = reward(bet, result, 3);
            System.out.printf(GREEN + "\nYou won $%.2f in this round!\n" + RESET, bet);
            stat[0] = bet;
            stat[1] = 1;
        }
        else {
            randomLossMessage();
            loss = bet;
            stat[2] = loss;
        }
        return stat;
    }

    static double[] calculateMegaReward(double bet, String[][] megaResult) {
        double[] stat = new double[3];
        double loss = 0;
        double totalGain = 0;
        int jackpotCheck = 0;
        boolean won = false;

        if (threeEqual(megaResult[0][0], megaResult[0][1], megaResult[0][2])) {
            bet = rewardMega(bet, megaResult,0, 0);
            won = true;
            jackpotCheck ++;
            totalGain += bet;
            stat[0] = totalGain;
            stat[1] = 1;
        } if (threeEqual(megaResult[1][0], megaResult[1][1], megaResult[1][2])) {
            bet = rewardMega(bet, megaResult,1, 0);
            won = true;
            jackpotCheck++;
            totalGain += bet;
            stat[0] = totalGain;
            stat[1] = 1;
        } if (threeEqual(megaResult[2][0],megaResult[2][1],megaResult[2][2])) {
            bet = rewardMega(bet, megaResult, 2, 0);
            won = true;
            jackpotCheck++;
            totalGain += bet;
            stat[0] = totalGain;
            stat[1] = 1;
        } if (threeEqual(megaResult[0][0], megaResult[1][1], megaResult[2][2])) {
            bet = rewardMega(bet, megaResult, 0, 0);
            won = true;
            jackpotCheck++;
            totalGain += bet;
            stat[0] = totalGain;
            stat[1] = 1;
        } if (threeEqual(megaResult[0][2], megaResult[1][1], megaResult[2][0])) {
            bet = rewardMega(bet, megaResult, 0, 2);
            won = true;
            jackpotCheck++;
            totalGain += bet;
            stat[0] = totalGain;
            stat[1] = 1;
        } if (threeEqual(megaResult[0][0], megaResult[1][0], megaResult[2][0])) {
            bet = rewardMega(bet, megaResult, 0, 0);
            won = true;
            jackpotCheck++;
            totalGain += bet;
            stat[0] = totalGain;
            stat[1] = 1;
        } if (threeEqual(megaResult[0][1], megaResult[1][1], megaResult[2][1])) {
            bet = rewardMega(bet, megaResult, 0, 1);
            won = true;
            jackpotCheck++;
            totalGain += bet;
            stat[0] = totalGain;
            stat[1] = 1;
        } if (threeEqual(megaResult[0][2], megaResult[1][2], megaResult[2][2])) {
            bet = rewardMega(bet, megaResult, 0, 2);
            won = true;
            jackpotCheck++;
            totalGain += bet;
            stat[0] = totalGain;
            stat[1] = 1;
        } if (!won) {
            randomLossMessage();
            loss = bet;
            stat[2] = loss;
        }

        if (jackpotCheck == 8) {
            System.out.printf(YELLOW + BOLD + """
                    ╔═══════════════════════════════════════╗
                    ║         💎💎 JACKPOT! 💎💎            ║
                    ║                                        ║
                    ║           %s │ %s │ %s                 ║
                    ║          ────┼────┼────                ║
                    ║           %s │ %s │ %s                 ║
                    ║          ────┼────┼────                ║
                    ║           %s │ %s │ %s                 ║
                    ║                                        ║
                    ║   💰 CONGRATULATIONS, MILLIONAIRE! 💰   ║
                    ╚═════════════════════════════════════════╝
                    """ + RESET, megaResult[0][0], megaResult[0][0], megaResult[0][0],
            megaResult[0][0], megaResult[0][0], megaResult[0][0],
                    megaResult[0][0], megaResult[0][0], megaResult[0][0]) ;
            System.out.printf(GREEN + "\nYou won $%.2f in this round!\n" + RESET, bet);
            jackpot++;
            randomWinMessage();
        }
        return stat;
    }
    static boolean playAgain(Scanner scanner) {
        scanner.nextLine();
        String answer;
        do {
            System.out.print("Would you like to play one more round? (y/n): ");
            answer = scanner.nextLine().trim().toLowerCase();
        } while (!answer.equals("y") && !answer.equals("n"));

        return answer.equals("y");
    }

    static void showStats(double startingBalance, int rounds, int wins, int losses, double totalWon,
                          double totalLoss, double totalBet, double highestBalance, double lowestBalance,
                          double balance) {
        System.out.println(ORANGE);
        System.out.println("\n╔───────────────────────────────╗");
        System.out.println("|           Statistics          |");
        System.out.printf("│ %-17s : $%8.2f │%n", "Starting Balance", startingBalance);
        System.out.printf("│ %-17s : %8d  │%n", "Played Rounds", rounds);
        System.out.printf("│ %-17s : %8d  │%n", "Wins", wins);
        System.out.printf("│ %-17s : %8d  │%n", "Loss", losses);
        System.out.printf("│ %-17s : $%8.2f │%n", "Total Won", totalWon);
        System.out.printf("│ %-17s : $%8.2f │%n", "Total Loss", totalLoss);
        System.out.printf("│ %-17s : $%8.2f │%n", "Total Bet", totalBet);
        System.out.printf("│ %-17s : $%8.2f │%n", "Highest Balance", highestBalance);
        System.out.printf("│ %-17s : $%8.2f │%n", "Lowest Balance", lowestBalance);
        System.out.printf("│ %-17s : $%8.2f │%n", "Final Balance", balance);
        System.out.println("╚───────────────────────────────╝");
        System.out.println(RESET);
    }
    static void gameOver() {
        System.out.println(RED + """
                ╔════════════════════════════╗
                ║         GAME OVER          ║
                ║                            ║
                ║    The house always wins.  ║
                ╚════════════════════════════╝
                """ + RESET);
    }
    static boolean threeEqual(String a, String b, String c) {return a.equals(b) && a.equals(c);}

    static double reward(double bet,String[] result, int matches) {
        int multiplier = 0;
        if (matches == 3) { multiplier = 5;}
        if (matches == 4) {  multiplier = 10; }
        if (matches == 5) { multiplier = 20;}
        switch (result[0]) {
                case "🍒",  "🚀", "🎸" : bet *= multiplier; randomWinMessage();break;
                case "🍋", "🪐", "🎼": bet *= multiplier*2; randomWinMessage();break;
                case "🔔", "🛸", "🎶": bet *= multiplier*4; randomWinMessage();break;
                case "⭐", "🌠", "🎺" : bet *= multiplier*10; randomWinMessage();break;
                case "7", "🌜", "🥁" : bet *= multiplier*20;
                    if (matches == 3) {
                        System.out.printf(YELLOW + BOLD + """
                                 ╔══════════════════════════════════════╗
                                 ║              JACKPOT!                ║
                                 ║                                      ║
                                 ║          %s  |  %s  |  %s               ║
                                 ║                                      ║
                                 ║       💰  YOU HIT THE JACKPOT  💰    ║
                                 ╚══════════════════════════════════════╝
                                """ + RESET, result[0], result[0], result[0]);
                        jackpot++;
                        randomWinMessage();
                        break;
                    } else if (matches == 4) {
                        System.out.printf(YELLOW + BOLD + """
                            ╔══════════════════════════════════════╗
                            ║              JACKPOT!                ║
                            ║                                      ║
                            ║          %s | %s | %s | %s               ║
                            ║                                      ║
                            ║       💰  YOU HIT THE JACKPOT  💰    ║
                            ╚══════════════════════════════════════╝
                           """ + RESET, result[0], result[0], result[0], result[0]);
                    jackpot++;
                    randomWinMessage();break;
                    } else if (matches == 5) {
                        System.out.printf(YELLOW + BOLD + """
                         ╔══════════════════════════════════════╗
                         ║              JACKPOT!                ║
                         ║                                      ║
                         ║          %s | %s | %s | %s | %s           ║
                         ║                                      ║
                         ║       💰  YOU HIT THE JACKPOT  💰    ║
                         ╚══════════════════════════════════════╝
                        """ + RESET, result[0], result[0], result[0], result[0], result[0]);
                jackpot++;
                randomWinMessage();break;
                    }
        }
        return bet;
    }
    static double rewardMega(double bet,String[][] megaResult, int i, int j) {
        switch (megaResult[i][j]) {
                case "🍒",  "🚀", "🎸" : bet *= 5; break;
                case "🍋", "🪐", "🎼": bet *= 10; break;
                case "🔔", "🛸", "🎶": bet *= 20; break;
                case "⭐", "🌠", "🎺" : bet *=50; break;
                case "7", "🌜", "🥁" : bet *= 100; randomWinMessage();break;
        }
        return bet;
    }
    static void randomWinMessage() {
        System.out.println(GREEN + winMessages.get(random.nextInt(winMessages.size())) + RESET);
    }
    static void randomLossMessage() {
        System.out.println(RED + lossMessages.get(random.nextInt(lossMessages.size())) + RESET);
    }
    static String random(String... options) {return options[random.nextInt(options.length)];}
}