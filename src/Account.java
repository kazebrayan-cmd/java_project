/**
 * Account.java
 * Represents a bank account (Savings or Current) held by a student or staff member.
 * Encapsulates balance and account details; exposes operations through methods.
 *
 * Author  : MAFOUO KAZE OSEZ DEF BRAYAN
 * Course  : CS 1301 – Programming and Problem Solving I Lab
 * Project : CamFlow – Campus Mobile Money & Savings Account Management System
 */
public class Account {

    // ─── Constants (Chapter 1 – named constants) ───────────────────────────────

    /** Maximum number of transactions an account can record */
    public static final int MAX_TX = 50;

    // ─── Static counter (Chapter 6 – static field) ────────────────────────────

    /** Counts all accounts ever created; used to generate account numbers */
    private static int accountCounter = 0;

    // ─── Private fields (Chapter 6 – encapsulation) ───────────────────────────

    private String  accountNumber;   // e.g. "CF-0001"
    private String  ownerName;       // full name of the account holder
    private double  balance;         // MUST be private (project requirement)
    private char    accountType;     // 'S' = Savings, 'C' = Current
    private int     transactionCount;// how many transactions recorded so far
    private boolean isActive;        // false when account is closed

    // ─── Constructor (Chapter 6 – parameterized constructor) ──────────────────

    /**
     * Opens a new account.
     *
     * @param number         the generated account number (e.g. "CF-0001")
     * @param name           the owner's full name
     * @param type           'S' for Savings, 'C' for Current
     * @param initialBalance the opening deposit amount
     */
    public Account(String number, String name, char type, double initialBalance) {
        accountCounter++;                    // static counter incremented here (Ch. 6)
        this.accountNumber    = number;      // this keyword (Chapter 6)
        this.ownerName        = name;
        this.accountType      = type;
        this.balance          = initialBalance;
        this.transactionCount = 0;
        this.isActive         = true;
    }

    // ─── Getters (Chapter 6) ──────────────────────────────────────────────────

    /** @return the account number string */
    public String getAccountNumber()  { return accountNumber; }

    /** @return the owner's name */
    public String getOwnerName()      { return ownerName; }

    /** @return the current balance */
    public double getBalance()        { return balance; }

    /** @return 'S' or 'C' */
    public char getAccountType()      { return accountType; }

    /** @return how many transactions have been recorded */
    public int getTransactionCount()  { return transactionCount; }

    /** @return true if the account is still open */
    public boolean isActive()         { return isActive; }

    // ─── Setter ───────────────────────────────────────────────────────────────

    /** @param active pass false to deactivate (close) the account */
    public void setActive(boolean active) { this.isActive = active; }

    /** @return the total number of accounts ever opened */
    public static int getAccountCounter() { return accountCounter; }

    /**
     * Decrements the static counter when an account is permanently closed.
     * Called by CamFlowSystem.closeAccount().
     */
    public static void decrementCounter() { accountCounter--; }

    // ─── increment transaction counter (used by CamFlowSystem) ───────────────

    /** Increments the transaction count for this account by one. */
    public void incrementTransactionCount() { transactionCount++; }

    // ─── deposit() – value-returning method (Chapter 4) ───────────────────────

    /**
     * Adds the given amount to the account balance.
     * Returns true on success, false if amount is not positive.
     *
     * @param amount the amount to deposit (must be > 0)
     * @return true if deposit was successful
     */
    public boolean deposit(double amount) {
        // Input check – selection statement (Chapter 3)
        if (amount <= 0) {
            System.out.println("  [ERROR] Deposit amount must be greater than 0.");
            return false;
        }
        balance += amount;   // arithmetic (Chapter 2)
        return true;
    }

    // ─── withdraw() – value-returning method (Chapter 4) ──────────────────────

    /**
     * Deducts the given amount from the account balance.
     * Does NOT enforce the minimum balance or fee here; that is handled by
     * CamFlowSystem.performWithdrawal() before calling this method.
     *
     * @param amount the amount to withdraw (must be > 0)
     * @return true if successful
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("  [ERROR] Withdrawal amount must be greater than 0.");
            return false;
        }
        balance -= amount;
        return true;
    }

    // ─── applyMonthlyInterest() (Chapter 4 – void method) ────────────────────

    /**
     * Applies monthly interest to Savings accounts ONLY.
     * Interest rate is defined in CamFlowSystem.SAVINGS_INTEREST_RATE.
     * This method simply adds the interest; recording the transaction is done
     * by CamFlowSystem.applyInterestToAll().
     *
     * @param rate the annual interest rate (e.g. 0.05 for 5 %)
     */
    public void applyMonthlyInterest(double rate) {
        // Only Savings accounts earn interest (Chapter 3 – if/else)
        if (accountType == 'S') {
            double interest = balance * rate / 100.0;  // arithmetic (Chapter 2)
            balance += interest;
        }
        // Current accounts ('C') are silently skipped
    }

    // ─── displaySummary() (Chapter 4 – void method) ───────────────────────────

    /**
     * Prints a formatted summary of the account to the console.
     */
    public void displaySummary() {
        String typeLabel = (accountType == 'S') ? "Savings" : "Current"; // conditional (Ch. 3)
        String status    = isActive ? "ACTIVE" : "CLOSED";

        System.out.println("  +--------------------------------------------------+");
        System.out.printf( "  | Account Number : %-31s|\n", accountNumber);
        System.out.printf( "  | Owner          : %-31s|\n", ownerName);
        System.out.printf( "  | Type           : %-31s|\n", typeLabel);
        System.out.printf( "  | Balance        : %,28.2f FCFA |\n", balance);
        System.out.printf( "  | Transactions   : %-31d|\n", transactionCount);
        System.out.printf( "  | Status         : %-31s|\n", status);
        System.out.println("  +--------------------------------------------------+");
    }
}
