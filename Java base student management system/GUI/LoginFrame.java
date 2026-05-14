package GUI;

import javax.swing.*;

import Users.User;

import java.awt.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login | Campus Event Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);

        JPanel container = new JPanel();
        container.setBackground(Color.WHITE);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel titleLabel = new JLabel("Campus Event Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Login to your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(120, 120, 120));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridLayout(50, 50, 5, 5));
        formPanel.setMaximumSize(new Dimension(400, 800));

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
;

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(103, 80, 164));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        formPanel.add(usernameField);
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(loginBtn);

        container.add(titleLabel);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        container.add(subtitleLabel);
        container.add(Box.createRigidArea(new Dimension(0, 25)));
        container.add(formPanel);
        
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                User user = new User(username, password);
                if (user.login()) {
                    String userRole = user.getUserRole();
                    switch (userRole) {
                        case "Staff" -> {
                            new ParticipantDashboard(user);
                            dispose();
                        }
                        case "Student" -> {
                           new ParticipantDashboard(user);
                            dispose();
                        }
                       case "Organizer" -> {
                            new OrganizerDashboard(user);
                            dispose();
                        }
                        default -> {
                            JOptionPane.showMessageDialog(this, "Unknown role: " + userRole, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        add(container);
        setVisible(true);
    }

}
