# NovaBank — Java Banking Management System

A desktop GUI project built with **Java Swing** for an Object-Oriented Programming assignment.

## Features
- Banking dashboard with account details and current balance
- Deposit funds
- Withdraw funds with insufficient-balance validation
- Recent transaction history, including date/time, type, amount, and balance after transaction
- Input validation for invalid, zero, negative, and over-precision amounts
- Object-oriented separation of account, transaction, table model, and GUI

## Requirements
- JDK 11 or newer
- No third-party libraries required

## Run on Windows
1. Extract the ZIP.
2. Open Command Prompt in the extracted `JavaBankingManagementSystem` folder.
3. Compile:
   ```bat
   javac -d out src\bankapp\*.java
   ```
4. Run:
   ```bat
   java -cp out bankapp.Main
   ```

## Run on macOS / Linux
```bash
javac -d out src/bankapp/*.java
java -cp out bankapp.Main
```

## Demo account
- Account number: `1001001`
- Holder: `Demo Customer`
- Opening balance: `₹ 5000.00`

## Important note
This is an educational demo. Data is stored in memory and resets when the app closes. It is not connected to a real bank and does not provide real authentication or persistent storage.

## Project structure
```text
JavaBankingManagementSystem/
├── src/bankapp/
│   ├── Main.java
│   ├── BankAccount.java
│   ├── Transaction.java
│   └── TransactionTableModel.java
├── docs/
│   ├── class-diagram.md
│   └── design-documentation.md
└── README.md
```
