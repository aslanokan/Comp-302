package ui.panels;

import domain.Constants;
import domain.controllers.LoginHandler;
import ui.BrickingBadFrame;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JLabel user_label, password_label, message;
    private JTextField userName_text;
    private JPasswordField password_text;
    private JButton signIn, signUp;
    private LoginHandler loginHandler = LoginHandler.getInstance();

    private BrickingBadFrame brickingBadFrame;

    public LoginPanel(BrickingBadFrame frame) {
        brickingBadFrame = frame;

        this.setPreferredSize(new Dimension((int) Constants.gameSceneWidth, (int) Constants.gameSceneHeight));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));

        // Username Label
        user_label = new JLabel("Username:    ");
        userName_text = new JTextField(20);

        // Password Label
        password_label = new JLabel("Password:    ");
        password_text = new JPasswordField(20);

        // Submit
        signIn = new JButton("Sign In");
        signUp = new JButton("Sign Up");

        signIn.addActionListener(e -> {
            String userName = userName_text.getText();
            String password = String.valueOf(password_text.getPassword());
            signIn(userName, password);
        });

        signUp.addActionListener(e -> {
            String userName = userName_text.getText();
            String password = String.valueOf(password_text.getPassword());
            signUp(userName, password);
        });

        // FIRST COLUMN
        gc.anchor = GridBagConstraints.LAST_LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 7;
        gc.gridx = 0;
        gc.gridy = 0;
        this.add(user_label, gc);

        gc.anchor = GridBagConstraints.LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 1;
        this.add(password_label, gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 10;
        gc.gridx = 0;
        gc.gridy = 2;
        this.add(signIn, gc);

        // SECOND COLUMN
        gc.anchor = GridBagConstraints.LAST_LINE_START;
        gc.weightx = 1;
        gc.weighty = 7;
        gc.gridx = 1;
        gc.gridy = 0;
        this.add(userName_text, gc);

        gc.anchor = GridBagConstraints.LINE_START;
        gc.weightx = 1;
        gc.weighty = 0.5;
        gc.gridx = 1;
        gc.gridy = 1;
        this.add(password_text, gc);

        gc.anchor = GridBagConstraints.NORTH;
        gc.weightx = 0.5;
        gc.weighty = 10;
        gc.gridx = 1;
        gc.gridy = 2;
        this.add(signUp, gc);
    }

    public void signIn(String username, String password) {
        if (loginHandler.signIn(username, password)) {
            brickingBadFrame.closePanel(this);
            brickingBadFrame.openMenuPanel();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid username & password\nor Sign In");
        }
    }

    public void signUp(String username, String password) {
        if (loginHandler.signUp(username, password)) {
            JOptionPane.showMessageDialog(this, "You successfully signed in.");
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid username & password");
        }
    }
}
