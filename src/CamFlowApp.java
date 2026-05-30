/**
 * CamFlowApp.java
 * Entry point for the CamFlow system.
 * Contains the main menu loop, all sub-menus, input validators,
 * and the "About" screen.
 *
 * Author  : MAFOUO KAZE OSEZ DEF BRAYAN
 * Course  : CS 1301 – Programming and Problem Solving I Lab
 * Project : CamFlow – Campus Mobile Money & Savings Account Management System
 */

import java.util.Scanner;

public class CamFlowApp {

    // ─── Shared Scanner (Chapter 2 – Scanner for input) ──────────────────────
    static Scanner sc = new Scanner(System.in);

    // ─── Shared system engine (Chapter 6 – object instantiation) ─────────────
    static CamFlowSystem system = new CamFlowSystem();

    // ─── Account-number counter (Chapter 2 – int variable) ───────────────────
    static int nextAccountId = 1;

    // =========================================================================
    // MAIN (Chapter 1 – main class entry point)
    // =========================================================================

    /**
     * Application entry point.
     * Displays the welcome banner and drives the main menu loop.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        printWelcomeBanner();   // void method (Chapter 4)

        boolean running = true;

        // while loop for main menu (Chapter 3 – while loop)
        while (running) {
            showMainMenu();
            int choice = getValidInt(sc, "Enter your choice", 0, 6);

            // switch on menu choice (Chapter 3 – switch)
            switch (choice) {
                case 1: handleAccountMenu();      break;
                case 2: handleTransactionMenu();  break;
                case 3: handleHistoryMenu();      break;
                case 4: handleReportMenu();       break;
                case 5: applyInterest();          break;
                case 6: showAbout();              break;
                case 0:
                    System.out.println("\n  Thank you for using CamFlow. Goodbye!\n");
                    running = false;   // break the while loop (Chapter 3 – break)
                    break;
                default:
                    System.out.println("  Invalid choice. Please try again.");
            }
        }

        sc.close();
    }

    // =========================================================================
    // WELCOME BANNER (Chapter 1 – welcome banner requirement)
    // =========================================================================

    /**
     * Prints the CamFlow welcome banner on startup.
     */
    public static void printWelcomeBanner() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║          CAMFLOW – Campus Mobile Money System        ║");
        System.out.println("  ║      PKFokam Institute of Excellence – CS 1301       ║");
        System.out.println("  ║         Author: MAFOUO KAZE OSEZ DEF BRAYAN         ║");
        System.out.println("  ╚══════════════════════════════════════════════════════╝");
        System.out.println();
    }

    // =========================================================================
    // MAIN MENU (Chapter 4 – displayMenu() void method)
    // =========================================================================

    /**
     * Displays the main menu options.
     */
    public static void showMainMenu() {
        System.out.println();
        System.out.println("  ============= CamFlow – Main Menu =============");
        System.out.println("  1. Account Management");
        System.out.println("  2. Transactions");
        System.out.println("  3. Transaction History");
        System.out.println("  4. Reports and Statistics");
        System.out.println("  5. Apply Monthly Interest");
        System.out.println("  6. About CamFlow");
        System.out.println("  0. Exit");
        System.out.println("  ===============================================");
    }

    // =========================================================================
    // ABOUT SCREEN (extra feature)
    // =========================================================================

    /**
     * Displays information about the CamFlow program.
     */
    public static void showAbout() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║                  ABOUT CAMFLOW                      ║");
        System.out.println("  ╠══════════════════════════════════════════════════════╣");
        System.out.println("  ║  CamFlow is a console-based simulation of mobile    ║");
        System.out.println("  ║  money services (like MTN MoMo and Orange Money)    ║");
        System.out.println("  ║  adapted for a university campus environment.        ║");
        System.out.println("  ║                                                      ║");
        System.out.println("  ║  Features:                                           ║");
        System.out.println("  ║   • Open Savings (S) or Current (C) accounts        ║");
        System.out.println("  ║   • Deposit, Withdraw, and Transfer funds            ║");
        System.out.println("  ║   • Automatic fee calculation (1% / 1.5%)           ║");
        System.out.println("  ║   • Monthly interest for Savings accounts (5%)      ║");
        System.out.println("  ║   • Full transaction history & system reports        ║");
        System.out.println("  ║   • Minimum balance protection (500 FCFA)           ║");
        System.out.println("  ║                                                      ║");
        System.out.println("  ║  Technical Details:                                  ║");
        System.out.println("  ║   • Language : Java (console application)            ║");
        System.out.println("  ║   • Classes  : Account, Transaction,                 ║");
        System.out.println("  ║                CamFlowSystem, CamFlowApp             ║");
        System.out.println("  ║   • Storage  : Array-based (no database)             ║");
        System.out.println("  ║                                                      ║");
        System.out.println("  ║  Author  : MAFOUO KAZE OSEZ DEF BRAYAN              ║");
        System.out.println("  ║  Course  : CS 1301 – Programming & Problem Solving  ║");
        System.out.println("  ║  School  : PKFokam Institute of Excellence           ║");
        System.out.println("  ║  Version : 1.0                                       ║");
        System.out.println("  ╚══════════════════════════════════════════════════════╝");
    }

    // =========================================================================
    // SUB-MENU 1 – ACCOUNT MANAGEMENT
    // =========================================================================

    /**
     * Handles the Account Management sub-menu.
     * Uses a do-while loop to keep showing the menu until the user goes back
     * (Chapter 3 – do-while loop).
     */
    public static void handleAccountMenu() {
        boolean back = false;

        // do-while loop (Chapter 3)
        do {
            System.out.println();
            System.out.println("  ─── Account Management ───────────────────────────");
            System.out.println("  1. Open Account");
            System.out.println("  2. List All Accounts");
            System.out.println("  3. Search Account");
            System.out.println("  4. Close Account");
            System.out.println("  0. Back");
            System.out.println("  ──────────────────────────────────────────────────");

            int choice = getValidInt(sc, "Enter choice", 0, 4);

            switch (choice) {
                case 1: openAccount();       break;
                case 2: system.printAllAccounts(); break;
                case 3: searchAccount();     break;
                case 4: closeAccount();      break;
                case 0: back = true;         break;  // break out (Chapter 3 – break)
            }
        } while (!back);
    }

    // ─── Open Account ─────────────────────────────────────────────────────────

    /**
     * Collects input and creates a new Account.
     * Generates account number in format CF-XXXX (Chapter 1 – named format).
     */
    public static void openAccount() {
        System.out.println("\n  --- Open New Account ---");

        // Collect owner name (Chapter 2 – String input)
        System.out.print("  Enter owner full name: ");
        sc.nextLine();           // consume leftover newline
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("  [ERROR] Name cannot be empty.");
            return;
        }

        // Choose account type with input validation (Chapter 3 – do-while)
        char type;
        do {
            System.out.print("  Account type – S (Savings) or C (Current): ");
            String typeInput = sc.nextLine().trim().toUpperCase();
            if (typeInput.equals("S") || typeInput.equals("C")) {
                type = typeInput.charAt(0);
                break;
            }
            System.out.println("  [ERROR] Please enter S or C.");
            type = ' ';
        } while (true);

        // Opening deposit – must be >= 500 FCFA (Chapter 2 – double)
        double opening = getValidDouble(sc, "Opening deposit (min 500 FCFA)", 500.0, 10_000_000.0);

        // Generate account number: CF-XXXX (Chapter 2 – String formatting)
        String accountNum = String.format("CF-%04d", nextAccountId);
        nextAccountId++;

        // Create Account object and add to system (Chapter 6 – object instantiation)
        Account acc = new Account(accountNum, name, type, opening);
        if (system.addAccount(acc)) {
            // Record the opening deposit as first transaction
            int idx = system.findAccountByNumber(accountNum);
            Transaction tx = new Transaction("DEPOSIT", opening, 0, opening, "Account Opening");
            system.recordTransaction(idx, tx);

            System.out.printf("%n  [OK] Account %s opened for %s. Balance: %,.2f FCFA%n",
                               accountNum, name, opening);
        }
    }

    // ─── Search Account ───────────────────────────────────────────────────────

    /**
     * Searches for an account by number and prints its summary.
     */
    public static void searchAccount() {
        System.out.print("\n  Enter account number (e.g. CF-0001): ");
        sc.nextLine();
        String num = sc.nextLine().trim().toUpperCase();

        int idx = system.findAccountByNumber(num);
        if (idx == -1) {
            System.out.println("  [ERROR] Account " + num + " not found.");
        } else {
            system.getAccount(idx).displaySummary();   // method call (Chapter 4)
        }
    }

    // ─── Close Account ────────────────────────────────────────────────────────

    /**
     * Closes an account after confirming the balance is zero.
     */
    public static void closeAccount() {
        System.out.print("\n  Enter account number to close: ");
        sc.nextLine();
        String num = sc.nextLine().trim().toUpperCase();
        system.closeAccount(num);
    }

    // =========================================================================
    // SUB-MENU 2 – TRANSACTIONS
    // =========================================================================

    /**
     * Handles the Transactions sub-menu.
     */
    public static void handleTransactionMenu() {
        boolean back = false;

        do {
            System.out.println();
            System.out.println("  ─── Transactions ─────────────────────────────────");
            System.out.println("  1. Deposit");
            System.out.println("  2. Withdrawal");
            System.out.println("  3. Transfer");
            System.out.println("  0. Back");
            System.out.println("  ──────────────────────────────────────────────────");

            int choice = getValidInt(sc, "Enter choice", 0, 3);

            switch (choice) {
                case 1: doDeposit();    break;
                case 2: doWithdraw();   break;
                case 3: doTransfer();   break;
                case 0: back = true;    break;
            }
        } while (!back);
    }

    // ─── Deposit helper ───────────────────────────────────────────────────────

    /** Collects input and calls performDeposit(). */
    public static void doDeposit() {
        System.out.print("\n  Enter account number: ");
        sc.nextLine();
        String num = sc.nextLine().trim().toUpperCase();
        double amt = getValidDouble(sc, "Deposit amount (FCFA)", 1.0, 10_000_000.0);
        system.performDeposit(num, amt);
    }

    // ─── Withdrawal helper ────────────────────────────────────────────────────

    /** Collects input and calls performWithdrawal(). */
    public static void doWithdraw() {
        System.out.print("\n  Enter account number: ");
        sc.nextLine();
        String num = sc.nextLine().trim().toUpperCase();
        double amt = getValidDouble(sc, "Withdrawal amount (FCFA)", 1.0, 10_000_000.0);
        system.performWithdrawal(num, amt);
    }

    // ─── Transfer helper ──────────────────────────────────────────────────────

    /** Collects input for sender, receiver, and amount, then calls performTransfer(). */
    public static void doTransfer() {
        System.out.print("\n  Enter sender account number: ");
        sc.nextLine();
        String from = sc.nextLine().trim().toUpperCase();
        System.out.print("  Enter receiver account number: ");
        String to   = sc.nextLine().trim().toUpperCase();
        double amt  = getValidDouble(sc, "Transfer amount (FCFA)", 1.0, 10_000_000.0);
        system.performTransfer(from, to, amt);
    }

    // =========================================================================
    // SUB-MENU 3 – TRANSACTION HISTORY
    // =========================================================================

    /**
     * Handles the Transaction History sub-menu.
     */
    public static void handleHistoryMenu() {
        boolean back = false;

        do {
            System.out.println();
            System.out.println("  ─── Transaction History ──────────────────────────");
            System.out.println("  1. View History for an Account");
            System.out.println("  2. Print Full Statement");
            System.out.println("  0. Back");
            System.out.println("  ──────────────────────────────────────────────────");

            int choice = getValidInt(sc, "Enter choice", 0, 2);

            switch (choice) {
                case 1:
                case 2:
                    // Both options show history (full statement = same as history here)
                    System.out.print("\n  Enter account number: ");
                    sc.nextLine();
                    String num = sc.nextLine().trim().toUpperCase();
                    system.printAccountHistory(num);
                    break;
                case 0:
                    back = true;
                    break;
            }
        } while (!back);
    }

    // =========================================================================
    // SUB-MENU 4 – REPORTS AND STATISTICS
    // =========================================================================

    /**
     * Handles the Reports and Statistics sub-menu.
     */
    public static void handleReportMenu() {
        boolean back = false;

        do {
            System.out.println();
            System.out.println("  ─── Reports and Statistics ───────────────────────");
            System.out.println("  1. System Statistics");
            System.out.println("  2. Full Statement (single account)");
            System.out.println("  0. Back");
            System.out.println("  ──────────────────────────────────────────────────");

            int choice = getValidInt(sc, "Enter choice", 0, 2);

            switch (choice) {
                case 1:
                    system.printAllAccounts();
                    system.printSystemStats();
                    break;
                case 2:
                    System.out.print("\n  Enter account number: ");
                    sc.nextLine();
                    String num = sc.nextLine().trim().toUpperCase();
                    system.printAccountHistory(num);
                    break;
                case 0:
                    back = true;
                    break;
            }
        } while (!back);
    }

    // =========================================================================
    // APPLY INTEREST (menu option 5)
    // =========================================================================

    /**
     * Triggers monthly interest application for all Savings accounts.
     */
    public static void applyInterest() {
        system.applyInterestToAll();
    }

    // =========================================================================
    // INPUT VALIDATORS (Chapter 4 – reusable methods; Chapter 3 – do-while)
    // =========================================================================

    /**
     * Reads and validates a double within [min, max].
     * Loops until the user provides a valid value (Chapter 3 – do-while).
     *
     * @param sc     the Scanner to read from
     * @param prompt the message shown to the user
     * @param min    minimum acceptable value (inclusive)
     * @param max    maximum acceptable value (inclusive)
     * @return       a valid double in [min, max]
     */
    public static double getValidDouble(Scanner sc, String prompt, double min, double max) {
        double value;
        // do-while for input validation (Chapter 3)
        do {
            System.out.printf("  %s [%.2f – %,.2f]: ", prompt, min, max);
            while (!sc.hasNextDouble()) {             // loop until numeric input
                System.out.print("  [ERROR] Please enter a number: ");
                sc.next();
            }
            value = sc.nextDouble();
            if (value < min || value > max) {
                System.out.printf("  [ERROR] Value must be between %.2f and %,.2f.%n", min, max);
            }
        } while (value < min || value > max);
        return value;
    }

    /**
     * Reads and validates an int within [min, max].
     *
     * @param sc     the Scanner to read from
     * @param prompt the message shown to the user
     * @param min    minimum acceptable value (inclusive)
     * @param max    maximum acceptable value (inclusive)
     * @return       a valid int in [min, max]
     */
    public static int getValidInt(Scanner sc, String prompt, int min, int max) {
        int value;
        do {
            System.out.printf("  %s [%d–%d]: ", prompt, min, max);
            while (!sc.hasNextInt()) {
                System.out.print("  [ERROR] Please enter a whole number: ");
                sc.next();
            }
            value = sc.nextInt();
            if (value < min || value > max) {
                System.out.printf("  [ERROR] Choice must be between %d and %d.%n", min, max);
            }
        } while (value < min || value > max);
        return value;
    }
}
