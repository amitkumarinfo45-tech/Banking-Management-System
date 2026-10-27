package bankapp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Immutable record of a single account event. */
public class Transaction {
    private final String type;
    private final BigDecimal amount;
    private final BigDecimal balanceAfter;
    private final LocalDateTime timestamp;
    private final String description;

    public Transaction(String type, BigDecimal amount, BigDecimal balanceAfter, LocalDateTime timestamp, String description) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = timestamp;
        this.description = description;
    }

    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
}
