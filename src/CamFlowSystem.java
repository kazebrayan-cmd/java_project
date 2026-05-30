/**
 * CamFlowSystem.java
 * The core engine of CamFlow.  Holds all Account objects and the 2-D
 * transaction history array.  All business logic (deposits, withdrawals,
 * transfers, fees, interest, statistics) lives here.
 *
 * Author  : MAFOUO KAZE OSEZ DEF BRAYAN
 * Course  : CS 1301 – Programming and Problem Solving I Lab
 * Project : CamFlow – Campus Mobile Money & Savings Account Management System
 */
public class CamFlowSystem {

    // ─── Constants (Chapter 1 – named constants) ───────────────────────────────

    /** Maximum number of accounts the system can hold */
    public static final int    MAX_ACCOUNTS         = 50;

    /** Fee rate applied to every withdrawal (1.0 %) */
    public static final double WITHDRAWAL_FEE_RATE  = 0.01;

    /** Fee rate applied to every transfer (1.5 %) */
    public static final double TRANSFER_FEE_RATE    = 0.015;

    /** Minimum balance that must remain after any debit */
    public static final double MIN_BALANCE          = 500.0;

    /** Annual savings interest rate (5 %) */
    public static final double SAVINGS_INTEREST_RATE = 0.05;

    // ─── Fields ───────────────────────────────────────────────────────────────

    /** 1-D array of Account objects (Chapter 5 – 1-D array) */
    private Account[] accounts;

    /**
     * 2-D transaction history array (Chapter 5 – 2-D array).
     * history[i][j] = j-th Transaction for the account at index i.
     */
    private Transaction[][] history;

    /** Number of accounts currently stored */
    private int accountCount;

    // ─── Constructor ──────────────────────────────────────────────────────────

    /**
     * Initialises the system arrays.
     * Uses array creation with named constant MAX_ACCOUNTS (Chapter 5).
     */
    public CamFlowSystem() {
        accounts     = new Account[MAX_ACCOUNTS];              // 1-D array (Ch. 5)
        history      = new Transaction[MAX_ACCOUNTS][Account.MAX_TX]; // 2-D array (Ch. 5)
        accountCount = 0;
    }

    // =========================================================================
    // ACCOUNT MANAGEMENT
    // =========================================================================

    /**
     * Adds an Account to the system.
     *
     * @param a the Account object to add
     * @return  true if added successfully, false if the system is full
     */
    public boolean addAccount(Account a) {
        if (accountCount >= MAX_ACCOUNTS) {   // selection (Chapter 3)
            System.out.println("  [ERROR] System is full. Cannot add more accounts.");
            return false;
        }
        accounts[accountCount] = a;           // store in array (Chapter 5)
        accountCount++;
        return true;
    }

    /**
     * Searches for an account by its number string.
     * Uses a for loop to iterate the accounts array (Chapter 5).
     *
     * @param num the account number to find (e.g. "CF-0001")
     * @return    the index in accounts[], or -1 if not found
     */
    public int findAccountByNumber(String num) {
        for (int i = 0; i < accountCount; i++) {   // for loop (Chapter 3)
            if (accounts[i].getAccountNumber().equals(num)) {
                return i;
            }
        }
        return -1;  // not found
    }

    /**
     * Returns the Account object at a given index, or null if out of range.
     *
     * @param idx index in the accounts array
     * @return    the Account, or null
     */
    public Account getAccount(int idx) {
        if (idx < 0 || idx >= accountCount) return null;
        return accounts[idx];
    }

    /** @return the current number of accounts in the system */
    public int getAccountCount() { return accountCount; }

    // ─── closeAccount() ───────────────────────────────────────────────────────

    /**
     * Marks an account as inactive (closed).
     * Only allowed when the balance is 0 FCFA.
     * Decrements the static accountCounter (Chapter 6 – static field).
     *
     * @param num the account number string
     */
    public void closeAccount(String num) {
        int idx = findAccountByNumber(num);

        // Account not found
        if (idx == -1) {
            System.out.println("  [ERROR] Account " + num + " not found.");
            return;
        }

        Account acc = accounts[idx];

        // Account already closed
        if (!acc.isActive()) {
            System.out.println("  [ERROR] Account " + num + " is already closed.");
            return;
        }

        // Balance must be 0 before closing
        if (acc.getBalance() > 0) {
            System.out.printf("  [ERROR] Balance is %.2f FCFA. Withdraw all funds first.%n",
                              acc.getBalance());
            return;
        }

        acc.setActive(false);           // setter (Chapter 6)
        Account.decrementCounter();     // static counter (Chapter 6)
        System.out.println("  Account " + num + " has been closed successfully.");
    }

    // =========================================================================
    // FEE CALCULATION (Chapter 4 – value-returning method)
    // =========================================================================

    /**
     * Calculates the fee for a transaction.
     * Fee rules:
     *   DEPOSIT    → always 0
     *   WITHDRAWAL → amount × 1 %; waived if amount < 1 000 FCFA
     *   TRANSFER   → amount × 1.5 %
     *
     * @param amount the transaction principal
     * @param type   "DEPOSIT", "WITHDRAWAL", or "TRANSFER"
     * @return       the fee to charge
     */
    public double calculateFee(double amount, String type) {
        // switch on transaction type (Chapter 3 – switch statement)
        switch (type) {
            case "DEPOSIT":
                return 0.0;

            case "WITHDRAWAL":
                // Small-cash waiver: no fee under 1 000 FCFA (Chapter 3 – if/else)
                if (amount < 1000.0) {
                    return 0.0;
                }
                return amount * WITHDRAWAL_FEE_RATE;   // arithmetic (Chapter 2)

            case "TRANSFER":
                return amount * TRANSFER_FEE_RATE;

            default:
                return 0.0;
        }
    }

    // =========================================================================
    // RECORD TRANSACTION (Chapter 5 – passing arrays to methods)
    // =========================================================================

    /**
     * Stores a Transaction object in the 2-D history array for the given account.
     *
     * @param idx the account index in accounts[]
     * @param tx  the Transaction object to record
     */
    public void recordTransaction(int idx, Transaction tx) {
        if (idx < 0 || idx >= accountCount) return;

        Account acc = accounts[idx];

        // Check we have not exceeded MAX_TX (Chapter 3 – if/else)
        if (acc.getTransactionCount() >= Account.MAX_TX) {
            System.out.println("  [WARN] Transaction log full for " + acc.getAccountNumber());
            return;
        }

        // Store in 2-D array (Chapter 5)
        history[idx][acc.getTransactionCount()] = tx;
        acc.incrementTransactionCount();
    }

    // =========================================================================
    // DEPOSIT (Chapter 4)
    // =========================================================================

    /**
     * Processes a deposit for the specified account.
     *
     * @param num    the account number
     * @param amount the amount to deposit
     */
    public void performDeposit(String num, double amount) {
        int idx = findAccountByNumber(num);

        // Validation (Chapter 3 – if/else)
        if (idx == -1) { System.out.println("  [ERROR] Account not found."); return; }
        Account acc = accounts[idx];
        if (!acc.isActive()) { System.out.println("  [ERROR] Account is closed."); return; }

        // Perform the deposit
        acc.deposit(amount);

        // Build and record the transaction
        double fee = calculateFee(amount, "DEPOSIT");
        String desc = "Deposit to " + num;
        Transaction tx = new Transaction("DEPOSIT", amount, fee, acc.getBalance(), desc);
        recordTransaction(idx, tx);   // passing array index and object (Ch. 5)

        // Print receipt
        System.out.println("\n  == Transaction: Deposit ==");
        System.out.printf( "  Account : %s (%s)%n", acc.getAccountNumber(), acc.getOwnerName());
        System.out.printf( "  Amount  : %,.2f FCFA%n", amount);
        System.out.printf( "  Balance After: %,.2f FCFA%n", acc.getBalance());
        System.out.println("  Status  : SUCCESS");
    }

    // =========================================================================
    // WITHDRAWAL (Chapter 4)
    // =========================================================================

    /**
     * Processes a withdrawal, enforcing the minimum balance rule and fee.
     *
     * @param num    the account number
     * @param amount the amount to withdraw
     */
    public void performWithdrawal(String num, double amount) {
        int idx = findAccountByNumber(num);

        if (idx == -1) { System.out.println("  [ERROR] Account not found."); return; }
        Account acc = accounts[idx];
        if (!acc.isActive()) { System.out.println("  [ERROR] Account is closed."); return; }

        double fee   = calculateFee(amount, "WITHDRAWAL");
        double total = amount + fee;   // total debit = principal + fee

        // Check minimum balance (Chapter 3 – if/else)
        if (acc.getBalance() - total < MIN_BALANCE) {
            System.out.printf(
                "  [ERROR] Insufficient funds. Balance after would be %.2f FCFA " +
                "(minimum %.2f FCFA required).%n",
                acc.getBalance() - total, MIN_BALANCE);
            return;
        }

        acc.withdraw(total);  // deduct amount + fee in one call

        String desc = "Withdrawal from " + num;
        Transaction tx = new Transaction("WITHDRAWAL", amount, fee, acc.getBalance(), desc);
        recordTransaction(idx, tx);

        // Receipt (formatted output, Chapter 2)
        System.out.println("\n  == Transaction: Withdrawal ==");
        System.out.printf( "  Account      : %s (%s)%n", acc.getAccountNumber(), acc.getOwnerName());
        System.out.printf( "  Amount       : %,.2f FCFA%n", amount);
        if (fee > 0)
            System.out.printf("  Fee (1.0%%)   : %,.2f FCFA%n", fee);
        else
            System.out.println("  Fee          : WAIVED (< 1 000 FCFA)");
        System.out.printf( "  Total Debited: %,.2f FCFA%n", total);
        System.out.printf( "  Balance After: %,.2f FCFA%n", acc.getBalance());
        System.out.println("  Status       : SUCCESS");
    }

    // =========================================================================
    // TRANSFER (Chapter 4)
    // =========================================================================

    /**
     * Transfers money from one account to another.
     * Sender pays amount + fee; receiver gets only the amount.
     *
     * @param fromNum the sender's account number
     * @param toNum   the receiver's account number
     * @param amount  the principal to transfer
     */
    public void performTransfer(String fromNum, String toNum, double amount) {
        int fromIdx = findAccountByNumber(fromNum);
        int toIdx   = findAccountByNumber(toNum);

        // Validate both accounts (Chapter 3 – nested if/else)
        if (fromIdx == -1) { System.out.println("  [ERROR] Sender account not found.");   return; }
        if (toIdx   == -1) { System.out.println("  [ERROR] Receiver account not found."); return; }

        Account sender   = accounts[fromIdx];
        Account receiver = accounts[toIdx];

        if (!sender.isActive())   { System.out.println("  [ERROR] Sender account is closed.");   return; }
        if (!receiver.isActive()) { System.out.println("  [ERROR] Receiver account is closed."); return; }

        double fee   = calculateFee(amount, "TRANSFER");
        double total = amount + fee;

        // Sender must have enough funds
        if (sender.getBalance() - total < MIN_BALANCE) {
            System.out.printf(
                "  [ERROR] Sender has insufficient funds. " +
                "Required: %.2f FCFA, Available: %.2f FCFA%n",
                total + MIN_BALANCE, sender.getBalance());
            return;
        }

        // Execute both legs
        sender.withdraw(total);
        receiver.deposit(amount);

        // Record sender transaction
        String descFrom = "Transfer to " + toNum;
        Transaction txFrom = new Transaction("TRANSFER", amount, fee, sender.getBalance(), descFrom);
        recordTransaction(fromIdx, txFrom);

        // Record receiver transaction (no fee on the receive side)
        String descTo = "Transfer from " + fromNum;
        Transaction txTo = new Transaction("DEPOSIT", amount, 0, receiver.getBalance(), descTo);
        recordTransaction(toIdx, txTo);

        // Receipt
        System.out.println("\n  == Transaction: Transfer ==");
        System.out.printf( "  From         : %s (%s)%n", sender.getAccountNumber(),   sender.getOwnerName());
        System.out.printf( "  To           : %s (%s)%n", receiver.getAccountNumber(), receiver.getOwnerName());
        System.out.printf( "  Amount       : %,.2f FCFA%n", amount);
        System.out.printf( "  Fee (1.5%%)   : %,.2f FCFA%n", fee);
        System.out.printf( "  Total Debited: %,.2f FCFA%n", total);
        System.out.printf( "  Sender Balance After  : %,.2f FCFA%n", sender.getBalance());
        System.out.printf( "  Receiver Balance After: %,.2f FCFA%n", receiver.getBalance());
        System.out.println("  Status       : SUCCESS");
    }

    // =========================================================================
    // TRANSACTION HISTORY (Chapter 5 – iterating arrays)
    // =========================================================================

    /**
     * Displays the full transaction history for a given account.
     * Uses a for loop to iterate history[idx][0 .. transactionCount-1] (Chapter 5).
     *
     * @param num the account number string
     */
    public void printAccountHistory(String num) {
        int idx = findAccountByNumber(num);
        if (idx == -1) { System.out.println("  [ERROR] Account not found."); return; }

        Account acc = accounts[idx];
        System.out.println("\n  === Transaction History for " + num
                           + " (" + acc.getOwnerName() + ") ===");

        if (acc.getTransactionCount() == 0) {
            System.out.println("  No transactions recorded yet.");
            return;
        }

        // Accumulators for the summary (Chapter 2 – variables)
        double totalDeposited  = 0;
        double totalWithdrawn  = 0;
        double totalFees       = 0;

        // for loop over the 2-D history array (Chapter 3 & Chapter 5)
        for (int j = 0; j < acc.getTransactionCount(); j++) {
            Transaction tx = history[idx][j];
            tx.printReceipt();   // call void method (Chapter 4)

            // Accumulate statistics (Chapter 2 – arithmetic)
            if (tx.getType().equals("DEPOSIT")) {
                totalDeposited += tx.getAmount();
            } else {
                totalWithdrawn += tx.getAmount();
            }
            totalFees += tx.getFee();
        }

        // Summary (Chapter 2 – formatted output)
        System.out.println("  ─────────────────────────────────────────────────");
        System.out.printf( "  Total Transactions : %d%n",        acc.getTransactionCount());
        System.out.printf( "  Total Deposited    : %,.2f FCFA%n", totalDeposited);
        System.out.printf( "  Total Withdrawn    : %,.2f FCFA%n", totalWithdrawn);
        System.out.printf( "  Total Fees Paid    : %,.2f FCFA%n", totalFees);
    }

    // =========================================================================
    // MONTHLY INTEREST (Chapter 3 – for loop + if)
    // =========================================================================

    /**
     * Applies monthly interest to ALL active Savings accounts.
     * Current accounts are skipped inside the loop using an if condition (Chapter 3).
     */
    public void applyInterestToAll() {
        System.out.println("\n  Applying monthly interest to all Savings accounts...");
        int count = 0;

        // for loop iterating the accounts array (Chapter 3 & 5)
        for (int i = 0; i < accountCount; i++) {
            Account acc = accounts[i];

            // Skip inactive or Current accounts (Chapter 3 – if)
            if (!acc.isActive() || acc.getAccountType() != 'S') continue;

            double interestAmount = acc.getBalance() * SAVINGS_INTEREST_RATE / 100.0;
            acc.applyMonthlyInterest(SAVINGS_INTEREST_RATE);

            // Record as DEPOSIT transaction with special description
            String desc = "Monthly Interest";
            Transaction tx = new Transaction("DEPOSIT", interestAmount, 0, acc.getBalance(), desc);
            recordTransaction(i, tx);

            System.out.printf("  [OK] %s – Interest: %,.2f FCFA → New Balance: %,.2f FCFA%n",
                               acc.getAccountNumber(), interestAmount, acc.getBalance());
            count++;
        }

        if (count == 0) System.out.println("  No Savings accounts to apply interest to.");
        else System.out.println("  Interest applied to " + count + " account(s).");
    }

    // =========================================================================
    // REPORTS & STATISTICS (Chapter 5 – iterating arrays)
    // =========================================================================

    /**
     * Prints a listing of all active accounts with balances and transaction counts.
     * Uses a for loop and printf (Chapter 3, Chapter 2).
     */
    public void printAllAccounts() {
        System.out.println("\n  == All Active Accounts ==");
        System.out.printf( "  %-4s %-10s %-22s %-6s %-18s %-12s%n",
                           "#", "Account", "Owner", "Type", "Balance (FCFA)", "Transactions");
        System.out.println("  " + "-".repeat(76));

        int    displayCount  = 0;
        double totalFunds    = 0;

        // for loop (Chapter 3); array access (Chapter 5)
        for (int i = 0; i < accountCount; i++) {
            Account acc = accounts[i];
            if (!acc.isActive()) continue;    // skip closed accounts (Chapter 3 – if)

            displayCount++;
            totalFunds += acc.getBalance();
            System.out.printf("  %-4d %-10s %-22s %-6c %,18.2f %-12d%n",
                               displayCount,
                               acc.getAccountNumber(),
                               acc.getOwnerName(),
                               acc.getAccountType(),
                               acc.getBalance(),
                               acc.getTransactionCount());
        }

        System.out.println("  " + "-".repeat(76));
        System.out.printf( "  Total accounts: %d | Total funds: %,.2f FCFA%n",
                           displayCount, totalFunds);
    }

    /**
     * Prints system-wide statistics.
     * Finds highest/lowest balances using comparisons inside a for loop (Chapter 3 & 5).
     */
    public void printSystemStats() {
        System.out.println("\n  ===== System Statistics =====");

        int    totalActive = 0;
        int    totalTx     = 0;
        double totalFunds  = 0;
        double highBalance = Double.MIN_VALUE;  // start low so first value wins
        double lowBalance  = Double.MAX_VALUE;  // start high so first value wins
        String highAccNum  = "N/A";
        String lowAccNum   = "N/A";

        // for loop with accumulation (Chapter 3 & 2)
        for (int i = 0; i < accountCount; i++) {
            Account acc = accounts[i];
            if (!acc.isActive()) continue;

            totalActive++;
            totalFunds += acc.getBalance();
            totalTx    += acc.getTransactionCount();

            // Update highest balance (Chapter 3 – if)
            if (acc.getBalance() > highBalance) {
                highBalance = acc.getBalance();
                highAccNum  = acc.getAccountNumber();
            }
            // Update lowest balance
            if (acc.getBalance() < lowBalance) {
                lowBalance = acc.getBalance();
                lowAccNum  = acc.getAccountNumber();
            }
        }

        double avgBalance = (totalActive > 0) ? totalFunds / totalActive : 0;

        System.out.printf("  Total Active Accounts : %d%n",          totalActive);
        System.out.printf("  Total Funds in System : %,.2f FCFA%n",  totalFunds);
        System.out.printf("  Average Balance       : %,.2f FCFA%n",  avgBalance);
        System.out.printf("  Highest Balance       : %,.2f FCFA (%s)%n", highBalance, highAccNum);
        System.out.printf("  Lowest  Balance       : %,.2f FCFA (%s)%n", lowBalance,  lowAccNum);
        System.out.printf("  Total Transactions    : %d%n",          totalTx);
    }
}
