package bankapp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Represents one customer's bank account and enforces transaction rules. */
public class BankAccount {
    private final String accountNumber;
    private final String holderName;
    private BigDecimal balance;
    private final List<Transaction> transactions = new ArrayList<>();

    public BankAccount(String accountNumber, String holderName, BigDecimal openingBalance) {
        if (accountNumber == null || accountNumber.isBlank()) throw new IllegalArgumentException("Account number is required.");
        if (holderName == null || holderName.isBlank()) throw new IllegalArgumentException("Account holder name is required.");
        if (openingBalance == null || openingBalance.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Opening balance cannot be negative.");
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = openingBalance.setScale(2, java.math.RoundingMode.HALF_UP);
        transactions.add(new Transaction("OPENING BALANCE", this.balance, this.balance, LocalDateTime.now(), "Account initialized"));
    }

    public synchronized void deposit(BigDecimal amount) {
        validateAmount(amount);
        balance = balance.add(amount).setScale(2, java.math.RoundingMode.HALF_UP);
        transactions.add(new Transaction("DEPOSIT", amount, balance, LocalDateTime.now(), "Cash deposit"));
    }

    public synchronized void withdraw(BigDecimal amount) {
        validateAmount(amount);
        if (amount.compareTo(balance) > 0) throw new IllegalStateException("Insufficient funds. Available balance: ₹ " + balance.toPlainString());
        balance = balance.subtract(amount).setScale(2, java.math.RoundingMode.HALF_UP);
        transactions.add(new Transaction("WITHDRAWAL", amount, balance, LocalDateTime.now(), "Cash withdrawal"));
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be greater than zero.");
        if (amount.scale() > 2) throw new IllegalArgumentException("Amount can have at most 2 decimal places.");
    }

    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public synchronized BigDecimal getBalance() { return balance; }
    public synchronized List<Transaction> getTransactions() {
        List<Transaction> copy = new ArrayList<>(transactions);
        Collections.reverse(copy);
        return Collections.unmodifiableList(copy);
    }
}
