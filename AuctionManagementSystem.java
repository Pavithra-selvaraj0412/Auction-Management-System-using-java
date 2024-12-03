import javax.swing.*;
import java.lang.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AuctionManagementSystem {
    private JFrame frame;
    private JTextField sellerNameField, itemNameField, basePriceField, bidderNameField, bidAmountField;
    private JTextArea itemsArea;
    private JComboBox<String> itemDropdown;
    private String sellerName;
    private ArrayList<AuctionItem> auctionItems = new ArrayList<>();

    public AuctionManagementSystem() {
        // Step 1: Ask for seller name, item name, and base price
        frame = new JFrame("Auction Management System");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel sellerNameLabel = new JLabel("Seller Name:");
        sellerNameLabel.setBounds(20, 20, 100, 25);
        sellerNameField = new JTextField();
        sellerNameField.setBounds(140, 20, 200, 25);

        JLabel itemNameLabel = new JLabel("Item Name:");
        itemNameLabel.setBounds(20, 60, 100, 25);
        itemNameField = new JTextField();
        itemNameField.setBounds(140, 60, 200, 25);

        JLabel basePriceLabel = new JLabel("Base Price:");
        basePriceLabel.setBounds(20, 100, 100, 25);
        basePriceField = new JTextField();
        basePriceField.setBounds(140, 100, 200, 25);

        JButton addItemButton = new JButton("Add Item");
        addItemButton.setBounds(140, 140, 100, 30);

        JButton finishButton = new JButton("Finish");
        finishButton.setBounds(250, 140, 100, 30);

        frame.add(sellerNameLabel);
        frame.add(sellerNameField);
        frame.add(itemNameLabel);
        frame.add(itemNameField);
        frame.add(basePriceLabel);
        frame.add(basePriceField);
        frame.add(addItemButton);
        frame.add(finishButton);

        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem();
            }
        });

        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (auctionItems.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please add at least one item before finishing.");
                } else {
                    showAuctionScreen();
                }
            }
        });

        frame.setVisible(true);
    }

    private void addItem() {
        try {
            if (sellerName == null) {
                sellerName = sellerNameField.getText();
                if (sellerName.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter the seller's name.");
                    return;
                }
            }

            String itemName = itemNameField.getText();
            double basePrice = Double.parseDouble(basePriceField.getText());

            if (itemName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Item name cannot be empty.");
                return;
            }

            // Add item to the list
            AuctionItem newItem = new AuctionItem(itemName, basePrice);
            auctionItems.add(newItem);

            // Clear input fields for the next item
            itemNameField.setText("");
            basePriceField.setText("");

            JOptionPane.showMessageDialog(frame, "Item added successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid base price. Please enter a numeric value.");
        }
    }

    private void showAuctionScreen() {
        frame.getContentPane().removeAll(); // Clear previous components
        frame.setSize(500, 700);
        frame.setLayout(null);

        JLabel itemsLabel = new JLabel("Auction Items:");
        itemsLabel.setBounds(20, 20, 100, 25);
        itemsArea = new JTextArea(10, 30);
        itemsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(itemsArea);
        scrollPane.setBounds(20, 50, 440, 150);

        // Display all added items
        for (AuctionItem item : auctionItems) {
            itemsArea.append("Item: " + item.getName() + ", Base Price: $" + item.getBasePrice() + "\n");
        }

        frame.add(itemsLabel);
        frame.add(scrollPane);

        // Dropdown to select an item
        JLabel selectItemLabel = new JLabel("Select Item:");
        selectItemLabel.setBounds(20, 220, 100, 25);
        itemDropdown = new JComboBox<>();
        for (AuctionItem item : auctionItems) {
            itemDropdown.addItem(item.getName());
        }
        itemDropdown.setBounds(140, 220, 200, 25);

        JLabel bidderNameLabel = new JLabel("Bidder Name:");
        bidderNameLabel.setBounds(20, 260, 100, 25);
        bidderNameField = new JTextField();
        bidderNameField.setBounds(140, 260, 200, 25);

        JLabel bidAmountLabel = new JLabel("Bid Amount:");
        bidAmountLabel.setBounds(20, 300, 100, 25);
        bidAmountField = new JTextField();
        bidAmountField.setBounds(140, 300, 200, 25);

        JButton placeBidButton = new JButton("Place Bid");
        placeBidButton.setBounds(140, 340, 100, 30);

        JButton checkWinnerButton = new JButton("Check Winner");
        checkWinnerButton.setBounds(250, 340, 140, 30);

        frame.add(selectItemLabel);
        frame.add(itemDropdown);
        frame.add(bidderNameLabel);
        frame.add(bidderNameField);
        frame.add(bidAmountLabel);
        frame.add(bidAmountField);
        frame.add(placeBidButton);
        frame.add(checkWinnerButton);

        placeBidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeBid();
            }
        });

        checkWinnerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkWinner();
            }
        });

        frame.revalidate();
        frame.repaint();
    }

    private void placeBid() {
        try {
            String selectedItemName = (String) itemDropdown.getSelectedItem();
            String bidderName = bidderNameField.getText();
            double bidAmount = Double.parseDouble(bidAmountField.getText());

            if (bidderName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Bidder name cannot be empty.");
                return;
            }

            // Find the selected item and update its highest bid
            for (AuctionItem item : auctionItems) {
                if (item.getName().equals(selectedItemName)) {
                    if (bidAmount > item.getHighestBid()) {
                        item.setHighestBid(bidAmount);
                        item.setHighestBidder(bidderName);
                        JOptionPane.showMessageDialog(frame, "Bid placed successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Bid amount must be higher than the current highest bid.");
                    }
                    break;
                }
            }

            // Update the display
            itemsArea.setText("");
            for (AuctionItem item : auctionItems) {
                itemsArea.append("Item: " + item.getName() + ", Base Price: $" + item.getBasePrice() + ", Highest Bid: $" + item.getHighestBid() + ", Highest Bidder: " + item.getHighestBidder() + "\n");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid bid amount. Please enter a numeric value.");
        }
    }

    private void checkWinner() {
        StringBuilder result = new StringBuilder();
        for (AuctionItem item : auctionItems) {
            result.append("Item: ").append(item.getName()).append(", Highest Bidder: ").append(item.getHighestBidder()).append(", Bid: $").append(item.getHighestBid()).append("\n");
        }

        JOptionPane.showMessageDialog(frame, result.toString(), "Auction Results", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new AuctionManagementSystem();
    }
}

class AuctionItem {
    private String name;
    private double basePrice;
    private double highestBid;
    private String highestBidder;

    public AuctionItem(String name, double basePrice) {
        this.name = name;
        this.basePrice = basePrice;
        this.highestBid = basePrice;
        this.highestBidder = "No bids yet";
    }

    public String getName() {
        return name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public double getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(double highestBid) {
        this.highestBid = highestBid;
    }

    public String getHighestBidder() {
        return highestBidder;
    }

    public void setHighestBidder(String highestBidder) {
        this.highestBidder = highestBidder;
    }
}
