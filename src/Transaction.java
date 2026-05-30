/**
 * Transaction.java
 * Represents a single financial transaction (deposit, withdrawal, or transfer).
 * Each transaction stores its type, amount, fee, balance after, and a description.
 *
 * Author  : MAFOUO KAZE OSEZ DEF BRAYAN
 * Course  : CS 1301 – Programming and Problem Solving I Lab
 * Project : CamFlow – Campus Mobile Money & Savings Account Management System
 */
public class Transaction {

    // ─── Fields (all private – encapsulation, Chapter 6) ──────────────────────

    /** Unique ID for this transaction */
    private int transactionId;

    /** Type of transaction: "DEPOSIT", "WITHDRAWAL", or "TRANSFER" */
    private String type;

    /** Amount involved in the transaction */
    private double amount;

    /** Fee charged for this transaction (0 for deposits) */
    private double fee;

    /** Account balance immediately after this transaction */
    private double balanceAfter;

    /** Short human-readable description of the transaction */
    private String description;

    /** Static counter shared by all Transaction objects (Chapter 6 – static field) */
    private static int txCounter = 0;

    // ─── Constructor (Chapter 6 – parameterized constructor) ──────────────────

    /**
     * Creates a new Transaction and automatically assigns the next ID.
     *
     * @param type        "DEPOSIT", "WITHDRAWAL", or "TRANSFER"
     * @param amount      the principal amount of the transaction
     * @param fee         the fee charged (0 for deposits)
     * @param balanceAfter the account balance after this transaction
     * @param description a short description string
     */
    public Transaction(String type, double amount, double fee,
                       double balanceAfter, String description) {
        txCounter++;                        // increment static counter
        this.transactionId = txCounter;     // assign unique ID (this keyword, Ch. 6)
        this.type          = type;
        this.amount        = amount;
        this.fee           = fee;
        this.balanceAfter  = balanceAfter;
        this.description   = description;
    }

    // ─── Getters (Chapter 6 – getters) ────────────────────────────────────────

    /** @return the unique transaction ID */
    public int getId()           { return transactionId; }

    /** @return the transaction type string */
    public String getType()      { return type; }

    /** @return the principal amount */
    public double getAmount()    { return amount; }

    /** @return the fee charged */
    public double getFee()       { return fee; }

    /** @return the balance after the transaction */
    public double getBalanceAfter() { return balanceAfter; }

    /** @return the description string */
    public String getDescription()  { return description; }

    // ─── printReceipt() – void method (Chapter 4) ─────────────────────────────

    /**
     * Prints a formatted receipt for this transaction to the console.
     * Uses System.out.printf for aligned columns (Chapter 2 – formatted output).
     */
    public void printReceipt() {
        System.out.println("  +-------------------------------------------------+");
        System.out.printf( "  | TX #%-5d  %-33s|\n", transactionId, description);
        System.out.println("  +-------------------------------------------------+");
        System.out.printf( "  | Type        : %-33s|\n", type);
        System.out.printf( "  | Amount      : %,30.2f FCFA |\n", amount);

        // Only show fee line when a fee was actually charged
        if (fee > 0) {
            System.out.printf("  | Fee         : %,30.2f FCFA |\n", fee);
            System.out.printf("  | Total       : %,30.2f FCFA |\n", amount + fee);
        }

        System.out.printf( "  | Balance After: %,29.2f FCFA |\n", balanceAfter);
        System.out.println("  +-------------------------------------------------+");
    }
}
