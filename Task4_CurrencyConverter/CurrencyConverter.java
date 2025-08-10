import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class CurrencyConverter extends JFrame {
    private JComboBox<String> fromCurrency;
    private JComboBox<String> toCurrency;
    private JTextField amountField;
    private JLabel resultLabel;

    private final HashMap<String, Double> currencyRates = new HashMap<>();

    public CurrencyConverter() {
        setTitle("Global Currency Converter");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10));

        // Supported currencies (relative to INR)
        currencyRates.put("INR - Indian Rupee", 1.0);
        currencyRates.put("USD - US Dollar", 0.012);
        currencyRates.put("EUR - Euro", 0.011);
        currencyRates.put("GBP - British Pound", 0.0095);
        currencyRates.put("AUD - Australian Dollar", 0.019);
        currencyRates.put("CAD - Canadian Dollar", 0.016);
        currencyRates.put("JPY - Japanese Yen", 1.75);
        currencyRates.put("CNY - Chinese Yuan", 0.088);
        currencyRates.put("RUB - Russian Ruble", 1.09);
        currencyRates.put("BRL - Brazilian Real", 0.064);
        currencyRates.put("ZAR - South African Rand", 0.22);
        currencyRates.put("SGD - Singapore Dollar", 0.016);
        currencyRates.put("NZD - New Zealand Dollar", 0.020);
        currencyRates.put("MXN - Mexican Peso", 0.20);
        currencyRates.put("CHF - Swiss Franc", 0.011);
        currencyRates.put("AED - UAE Dirham", 0.044);
        currencyRates.put("HKD - Hong Kong Dollar", 0.095);
        currencyRates.put("KRW - South Korean Won", 16.5);
        currencyRates.put("MYR - Malaysian Ringgit", 0.056);
        currencyRates.put("SEK - Swedish Krona", 0.13);
        currencyRates.put("THB - Thai Baht", 0.43);
        currencyRates.put("IDR - Indonesian Rupiah", 181.0);

        // UI Elements
        JLabel fromLabel = new JLabel("From Currency:");
        JLabel toLabel = new JLabel("To Currency:");
        JLabel amountLabel = new JLabel("Amount:");
        resultLabel = new JLabel("Converted Amount: ", SwingConstants.CENTER);
        amountField = new JTextField();

        fromCurrency = new JComboBox<>(currencyRates.keySet().toArray(new String[0]));
        toCurrency = new JComboBox<>(currencyRates.keySet().toArray(new String[0]));

        JButton convertButton = new JButton("Convert");
        JButton resetButton = new JButton("Reset");
        JButton exitButton = new JButton("Exit");

        add(fromLabel); add(fromCurrency);
        add(toLabel); add(toCurrency);
        add(amountLabel); add(amountField);
        add(convertButton); add(resetButton);
        add(resultLabel); add(exitButton);

        // Logic
        convertButton.addActionListener(e -> performConversion());
        resetButton.addActionListener(e -> resetFields());
        exitButton.addActionListener(e -> System.exit(0));
    }

    private void performConversion() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String from = (String) fromCurrency.getSelectedItem();
            String to = (String) toCurrency.getSelectedItem();

            if (from.equals(to)) {
                resultLabel.setText("Converted Amount: " + amount);
                return;
            }

            double fromRate = currencyRates.get(from);
            double toRate = currencyRates.get(to);
            double converted = amount * (toRate / fromRate);

            resultLabel.setText(String.format("Converted Amount: %.2f", converted));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.");
        }
    }

    private void resetFields() {
        amountField.setText("");
        fromCurrency.setSelectedIndex(0);
        toCurrency.setSelectedIndex(1);
        resultLabel.setText("Converted Amount: ");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CurrencyConverter app = new CurrencyConverter();
            app.setVisible(true);
        });
    }
}
