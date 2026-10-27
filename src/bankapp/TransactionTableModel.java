package bankapp;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Adapts transaction objects for display in JTable. */
public class TransactionTableModel extends AbstractTableModel {
    private final String[] columns = {"Date & Time", "Type", "Amount (₹)", "Balance After (₹)", "Description"};
    private List<Transaction> transactions = new ArrayList<>();
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    public TransactionTableModel(List<Transaction> transactions) {
        setTransactions(transactions);
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = new ArrayList<>(transactions);
        fireTableDataChanged();
    }

    @Override public int getRowCount() { return transactions.size(); }
    @Override public int getColumnCount() { return columns.length; }
    @Override public String getColumnName(int column) { return columns[column]; }

    @Override
    public Object getValueAt(int row, int column) {
        Transaction t = transactions.get(row);
        switch (column) {
            case 0: return t.getTimestamp().format(FORMAT);
            case 1: return t.getType();
            case 2: return t.getAmount().toPlainString();
            case 3: return t.getBalanceAfter().toPlainString();
            case 4: return t.getDescription();
            default: return "";
        }
    }
}
