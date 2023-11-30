import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton submitButton = new JButton("SUBMIT");
    JButton cancelButton = new JButton("CANCEL");
    JCheckBox showPassword = new JCheckBox("Show Password");

    LoginFrame() {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();

    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField.setBounds(150, 220, 150, 30);
        showPassword.setBounds(150, 250, 150, 30);
        submitButton.setBounds(50, 300, 100, 30);
        cancelButton.setBounds(200, 300, 100, 30);

    }

    public void addComponentsToContainer() {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(submitButton);
        container.add(cancelButton);
    }

    public void addActionEvent() {
        submitButton.addActionListener(this);
        cancelButton.addActionListener(this);
        showPassword.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String userText;
            String pwdText;
            userText = userTextField.getText();
            pwdText = passwordField.getText();
            if (userText.equalsIgnoreCase("sysintegra") && pwdText.equalsIgnoreCase("12345")) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                // Redirect to NotepadOpener class
                new TapOpener();

            } else {
                JOptionPane.showMessageDialog(this, " Invalid Username or Password");
            }

        }
        if (e.getSource() == cancelButton) {
            userTextField.setText("");
            passwordField.setText("");
        }
        if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }

        }
    }
}
