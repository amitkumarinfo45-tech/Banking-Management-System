package bankapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Java Banking Management System - Swing GUI entry point.
 * Demo app: data is held in memory and resets when the application closes.
 */
public class Main {
    private static final Color NAVY = new Color(18, 28, 48);
    private static final Color BLUE = new Color(37, 99, 235);
    private static final Color BG = new Color(245, 247, 251);
    private static final Color MUTED = new Color(100, 116, 139);
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm:ss a");

    private final BankAccount account = new BankAccount("1001001", "Demo Customer", new BigDecimal("5000.00"));
    private JLabel balanceValue;
    private JLabel accountValue;
    private JLabel customerValue;
    private JLabel statusLabel;
    private JTable transactionTable;
    private TransactionTableModel tableModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) { }
            new Main().showWindow();
        });
    }

    private void showWindow() {
        JFrame frame = new JFrame("NovaBank | Banking Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(900, 620));
        frame.setSize(1080, 720);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(buildContent());
        frame.setVisible(true);
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        JPanel sidebar = new JPanel();
        sidebar.setBackground(NAVY);
        sidebar.setPreferredSize(new Dimension(225, 0));
        sidebar.setBorder(new EmptyBorder(28, 20, 24, 20));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel brand = new JLabel("✦  NovaBank");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("SansSerif", Font.BOLD, 24));
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(brand);
        sidebar.add(Box.createVerticalStrut(10));
        JLabel subtitle = new JLabel("BANKING DASHBOARD");
        subtitle.setForeground(new Color(161, 174, 196));
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(42));
        sidebar.add(sidebarItem("▦   Overview", true));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(sidebarItem("↗   Deposit / Withdraw", false));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(sidebarItem("≡   Transactions", false));
        sidebar.add(Box.createVerticalGlue());

        JLabel demo = new JLabel("<html><div style='width:155px;color:#a1aec4'>DEMO ACCOUNT<br><br>Funds and transactions are stored only while this app is running.</div></html>");
        demo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sidebar.add(demo);

        JPanel main = new JPanel(new BorderLayout(0, 20));
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(26, 30, 28, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Banking Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 27));
        title.setForeground(new Color(15, 23, 42));
        JLabel desc = new JLabel("Manage your account, move funds, and review activity.");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        desc.setForeground(MUTED);
        heading.add(title);
        heading.add(Box.createVerticalStrut(6));
        heading.add(desc);
        header.add(heading, BorderLayout.WEST);

        JLabel avatar = new JLabel("  DC  ");
        avatar.setOpaque(true);
        avatar.setBackground(new Color(219, 234, 254));
        avatar.setForeground(new Color(29, 78, 216));
        avatar.setFont(new Font("SansSerif", Font.BOLD, 13));
        avatar.setBorder(new EmptyBorder(12, 8, 12, 8));
        header.add(avatar, BorderLayout.EAST);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel summary = new JPanel(new GridLayout(1, 3, 16, 0));
        summary.setOpaque(false);
        summary.setMaximumSize(new Dimension(Integer.MAX_VALUE, 126));
        summary.add(summaryCard("AVAILABLE BALANCE", "₹ " + money(account.getBalance()), "Current account balance", true));
        summary.add(summaryCard("ACCOUNT NUMBER", account.getAccountNumber(), "Savings account", false));
        summary.add(summaryCard("ACCOUNT HOLDER", account.getHolderName(), "Verified demo profile", false));

        JPanel actions = new JPanel(new GridLayout(1, 2, 16, 0));
        actions.setOpaque(false);
        actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 172));
        actions.add(actionCard("＋", "Deposit funds", "Add money to your account.", BLUE, this::deposit));
        actions.add(actionCard("−", "Withdraw funds", "Withdraw from available balance.", new Color(217, 119, 6), this::withdraw));

        JPanel tableCard = new JPanel(new BorderLayout(0, 14));
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(18, 18, 18, 18)));
        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setOpaque(false);
        JLabel transactionsTitle = new JLabel("Recent transactions");
        transactionsTitle.setFont(new Font("SansSerif", Font.BOLD, 17));
        transactionsTitle.setForeground(new Color(15, 23, 42));
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> refreshTransactions());
        tableHeader.add(transactionsTitle, BorderLayout.WEST);
        tableHeader.add(refresh, BorderLayout.EAST);

        tableModel = new TransactionTableModel(account.getTransactions());
        transactionTable = new JTable(tableModel);
        transactionTable.setRowHeight(30);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        transactionTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        transactionTable.getTableHeader().setBackground(new Color(248, 250, 252));
        transactionTable.setGridColor(new Color(241, 245, 249));
        JScrollPane scroll = new JScrollPane(transactionTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        tableCard.add(tableHeader, BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);

        content.add(summary);
        content.add(Box.createVerticalStrut(18));
        content.add(actions);
        content.add(Box.createVerticalStrut(18));
        content.add(tableCard);

        statusLabel = new JLabel("Ready. This is a demo banking application.");
        statusLabel.setForeground(MUTED);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        main.add(header, BorderLayout.NORTH);
        main.add(content, BorderLayout.CENTER);
        main.add(statusLabel, BorderLayout.SOUTH);

        root.add(sidebar, BorderLayout.WEST);
        root.add(main, BorderLayout.CENTER);
        return root;
    }

    private JLabel sidebarItem(String text, boolean selected) {
        JLabel item = new JLabel(text);
        item.setOpaque(true);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        item.setBorder(new EmptyBorder(12, 12, 12, 8));
        item.setFont(new Font("SansSerif", selected ? Font.BOLD : Font.PLAIN, 13));
        item.setForeground(selected ? Color.WHITE : new Color(190, 201, 219));
        item.setBackground(selected ? new Color(42, 56, 82) : NAVY);
        return item;
    }

    private JPanel summaryCard(String label, String value, String note, boolean primary) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(primary ? new Color(239, 246, 255) : Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primary ? new Color(191, 219, 254) : new Color(226, 232, 240)),
                new EmptyBorder(16, 16, 14, 16)));
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        l.setForeground(MUTED);
        JLabel v = new JLabel(value);
        v.setFont(new Font("SansSerif", Font.BOLD, 20));
        v.setForeground(primary ? new Color(29, 78, 216) : new Color(15, 23, 42));
        JLabel n = new JLabel(note);
        n.setFont(new Font("SansSerif", Font.PLAIN, 11));
        n.setForeground(MUTED);
        card.add(l);
        card.add(Box.createVerticalStrut(12));
        card.add(v);
        card.add(Box.createVerticalStrut(7));
        card.add(n);
        return card;
    }

    private JPanel actionCard(String icon, String title, String desc, Color accent, Runnable action) {
        JPanel card = new JPanel(new BorderLayout(14, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(18, 18, 18, 18)));
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 24));
        iconLabel.setForeground(accent);
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        iconLabel.setPreferredSize(new Dimension(54, 54));
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 16));
        t.setForeground(new Color(15, 23, 42));
        JLabel d = new JLabel("<html><div style='width:190px'>" + desc + "</div></html>");
        d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        d.setForeground(MUTED);
        JButton button = new JButton(title);
        button.setFocusPainted(false);
        button.setBackground(accent);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBorder(new EmptyBorder(9, 14, 9, 14));
        button.addActionListener(e -> action.run());
        textPanel.add(t);
        textPanel.add(Box.createVerticalStrut(7));
        textPanel.add(d);
        textPanel.add(Box.createVerticalStrut(12));
        textPanel.add(button);
        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    private void deposit() {
        processTransaction(true);
    }

    private void withdraw() {
        processTransaction(false);
    }

    private void processTransaction(boolean isDeposit) {
        String prompt = isDeposit ? "Enter amount to deposit (₹):" : "Enter amount to withdraw (₹):";
        String raw = JOptionPane.showInputDialog(null, prompt, isDeposit ? "Deposit Funds" : "Withdraw Funds", JOptionPane.QUESTION_MESSAGE);
        if (raw == null) return;
        try {
            BigDecimal amount = new BigDecimal(raw.trim());
            if (amount.scale() > 2) throw new IllegalArgumentException("Please enter at most 2 decimal places.");
            if (isDeposit) account.deposit(amount);
            else account.withdraw(amount);
            refreshTransactions();
            statusLabel.setText((isDeposit ? "Deposit" : "Withdrawal") + " successful at " + LocalDateTime.now().format(TIME_FORMAT));
            JOptionPane.showMessageDialog(null, (isDeposit ? "Deposit" : "Withdrawal") + " successful.\nNew balance: ₹ " + money(account.getBalance()),
                    "Transaction Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            showError("Enter a valid numeric amount, for example 250 or 125.50.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            showError(ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Unable to process transaction", JOptionPane.ERROR_MESSAGE);
    }

    private void refreshTransactions() {
        tableModel.setTransactions(account.getTransactions());
        balanceValue = null; // summary cards are refreshed by rebuilding the content.
        // Update the visible balance label by replacing the center panel.
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(transactionTable);
        frame.setContentPane(buildContent());
        frame.revalidate();
        frame.repaint();
    }

    private static String money(BigDecimal amount) {
        return amount.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }
}
