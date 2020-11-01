package ui.panels;

import domain.Constants;
import domain.models.brick.Brick;
import ui.BrickingBadFrame;

import javax.swing.*;
import java.awt.*;

public class HelpScreenPanel extends JPanel {
    private BrickingBadFrame frame;

    private JButton backButton;
    private JTextArea textArea;
    private JLabel titleLabel;

    public HelpScreenPanel(BrickingBadFrame frame) {

        this.frame = frame;
        this.setPreferredSize(new Dimension((int) Constants.gameSceneWidth, (int) Constants.gameSceneHeight));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));

        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.closePanel(this);
            frame.openMenuPanel();
        });

        titleLabel = new JLabel("Help Screen");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));

        textArea = new JTextArea(
                        ("Welcome to Bricking Bad!\n" +
                        "Break all the bricks to win the game!\n\n" +
                        "Controls: \n" +
                        "Arrow Left & Right - move paddle \n" +
                        "A & D - rotate paddle\n" +
                        "T - taller paddle powerup\n" +
                        "M - magnet powerup\n" +
                        "C - chemical ball powerup\n" +
                        "Esc / Mouse Click - pause/resume game"));

        textArea.setSize(new Dimension((int) Constants.gameSceneWidth - 50, (int) Constants.gameSceneHeight - 100));
        textArea.setFont(new Font("Serif", Font.PLAIN, 20));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        this.add(backButton, gc);

        gc.anchor = GridBagConstraints.NORTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        this.add(titleLabel, gc);

        gc.anchor = GridBagConstraints.NORTH;
        gc.weightx = 1;
        gc.weighty = 20;
        gc.gridx = 0;
        gc.gridy = 1;
        this.add(textArea, gc);

        setVisible(true);
    }

}
