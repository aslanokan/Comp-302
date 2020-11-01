package ui.panels;

import domain.Constants;
import ui.BrickingBadFrame;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {

    private JButton startButton;
    private JButton loadButton;
    private JButton logOutButton;
    private JButton helpButton;

    public MainMenuPanel(BrickingBadFrame frame) {
        setPreferredSize(new Dimension((int) Constants.gameSceneWidth, (int) Constants.gameSceneHeight));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));

        add(Box.createRigidArea(new Dimension(130, 230)));
        startButton = new JButton("Start New Game");
        startButton.addActionListener(e -> {
            frame.closePanel(this);
            frame.openPreBuildScenePanel();
        });
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(startButton);

        add(Box.createRigidArea(new Dimension(5, 30)));
        loadButton = new JButton("Load Game");
        loadButton.addActionListener(e -> {
            frame.closePanel(this);
            frame.openLoadGamePanel();
        });
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        add(loadButton);

        add(Box.createRigidArea(new Dimension(5, 30)));
        helpButton = new JButton("Help");
        helpButton.addActionListener(e -> {
            frame.closePanel(this);
            frame.openHelpScreenPanel();
        });
        helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        helpButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        add(helpButton);

        add(Box.createRigidArea(new Dimension(5, 30)));
        logOutButton = new JButton("Log Out");
        logOutButton.addActionListener(e -> {
            frame.closePanel(this);
            frame.openLoginPanel();
        });
        logOutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(logOutButton);

        setVisible(true);
    }
}
