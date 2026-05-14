package GUI.participant;

import javax.swing.*;
import FeeSystem.Bill;
import java.awt.*;


public class BillSummaryDialog extends JDialog {
    
    public BillSummaryDialog(JFrame parent,int registration_id) {
        super(parent, "Bill Summary", true);
        setSize(650, 800);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        Bill bill = new Bill(registration_id);
        String eventName = bill.getEventName();
        int registrationId = registration_id;
        String catering = bill.getCateringService();
        double cateringFee = bill.getCateringFee();
        String transport = bill.getTransportService();
        double transportFee = bill.getTransportFee();
        String discount = bill.getDiscount_applied();
        double discountAmount = bill.getDiscountAmount();
        double baseFee = bill.getBase_fee();
        double totalFee = bill.calculateTotalFee();
        double netfee = bill.calculateNetTotal();
        String applicants = String.valueOf(bill.getApplicantamount());
        bill.generateBill();

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        JLabel title = new JLabel("Bill Summary of " + eventName, SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(title, BorderLayout.CENTER);

        // Top Summary Content
        JPanel topInfo = new JPanel();
        topInfo.setLayout(new GridLayout(5, 2, 10, 10));
        topInfo.setBackground(Color.WHITE);
        topInfo.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        topInfo.add(label("Registration ID:"));
        topInfo.add(value(String.valueOf(registrationId)));

        topInfo.add(label("Catering:"));
        topInfo.add(value(catering));

        topInfo.add(label("Transport:"));
        topInfo.add(value(transport));

        topInfo.add(label("Discount Applied:"));
        topInfo.add(value(discount));

        topInfo.add(label("Applicant amount:"));
        topInfo.add(value(applicants));

        // Bottom Summary Content
        JPanel bottomInfo = new JPanel();
        bottomInfo.setLayout(new GridLayout(6, 2, 10, 10));
        bottomInfo.setBackground(Color.WHITE);
        bottomInfo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            "Fee breakdown",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 15),
            Color.DARK_GRAY
        ));

        bottomInfo.add(label("Base Fee:"));
        bottomInfo.add(value("RM " + String.format("%.2f", baseFee)));

        bottomInfo.add(label("Catering Fee:"));
        bottomInfo.add(value("RM " + String.format("%.2f", cateringFee)));

        bottomInfo.add(label("Transport Fee:"));
        bottomInfo.add(value("RM " + String.format("%.2f", transportFee)));

        bottomInfo.add(label("Net Payment:"));
        bottomInfo.add(value("RM " + String.format("%.2f", netfee)));

        bottomInfo.add(label("Discount Amount:"));
        bottomInfo.add(value("- " + String.format("%.1f", discountAmount) + "%"));


        bottomInfo.add(label("Total Payment:"));
        JLabel totalVal = value("RM " + String.format("%.2f", totalFee));
        totalVal.setFont(new Font("Segoe UI", Font.BOLD, 15));
        totalVal.setForeground(new Color(50, 90, 160));
        bottomInfo.add(totalVal);

        // Button
        JPanel footer = new JPanel();
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton closeBtn = new JButton("Close");
        closeBtn.setPreferredSize(new Dimension(120, 35));
        closeBtn.setBackground(new Color(103, 80, 164));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());

        footer.add(closeBtn);

        // Layout
        JPanel combined = new JPanel();
        combined.setLayout(new BoxLayout(combined, BoxLayout.Y_AXIS));
        combined.setBackground(Color.WHITE);
        combined.add(topInfo);
        combined.add(bottomInfo);

        add(header, BorderLayout.NORTH);
        add(combined, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return l;
    }

    private JLabel value(String text) {
        JLabel v = new JLabel(text, SwingConstants.RIGHT);
        v.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return v;
    }

}
