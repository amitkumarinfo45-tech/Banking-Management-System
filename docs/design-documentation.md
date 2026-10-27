# Design Documentation

## 1. Objective
Create a Java desktop application that demonstrates object-oriented programming through a basic banking workflow.

## 2. Requirements covered
1. View account holder, account number, and available balance.
2. Deposit a positive monetary amount.
3. Withdraw a positive amount if sufficient funds are available.
4. Review a timestamped transaction history.
5. Validate user input and communicate errors clearly.

## 3. OOP principles
- **Encapsulation:** account fields are private; balance changes only through validated methods.
- **Abstraction:** the GUI calls `deposit` and `withdraw` without managing balance arithmetic itself.
- **Single responsibility:** `BankAccount` owns account rules, `Transaction` stores an immutable event, `TransactionTableModel` adapts data for Swing, and `Main` handles the interface.
- **Composition:** a bank account owns a collection of transaction records.

## 4. Main workflow
- App starts with a demo account and ₹5000.00 opening balance.
- User selects Deposit or Withdraw.
- App asks for an amount and validates it.
- `BankAccount` updates the balance and adds a transaction.
- The dashboard refreshes and shows the latest balance and history.
- Invalid amounts and insufficient funds produce an error dialog.

## 5. Validation
- Amount must be numeric and greater than zero.
- Amount may have at most two decimal places.
- Withdrawals cannot exceed the available balance.
- Account number, holder name, and opening balance are validated at construction.

## 6. Limitations and future enhancements
- Current data is in memory only and resets after closing.
- Demo profile is not authenticated.
- Possible future work: multiple accounts, persistent database storage, login, transfer between accounts, CSV export, and automated unit tests.

## 7. Test cases
| Scenario | Expected result |
|---|---|
| Deposit 250 | Balance increases by ₹250.00; deposit appears in history |
| Withdraw 100 | Balance decreases by ₹100.00; withdrawal appears in history |
| Withdraw more than balance | Error shown; balance unchanged |
| Deposit 0 or negative value | Validation error; balance unchanged |
| Enter non-numeric text | Validation error |
| Cancel amount dialog | No transaction is made |
