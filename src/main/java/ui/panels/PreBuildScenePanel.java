package ui.panels;

import domain.Constants;
import domain.controllers.BuildGameHandler;
import ui.BrickingBadFrame;

import javax.swing.*;
import java.awt.*;

public class PreBuildScenePanel extends JPanel {
    private JLabel simpleBrick_label, mineBrick_label, wrapperBrick_label, halfMetalBrick_label, gameName_label;
    private JTextField simpleBrick_text, mineBrick_text, wrapperBrick_text, halfMetalBrick_text, gameName_text;
    private JButton createButton, backButton;
    private BuildGameHandler buildGameHandler = BuildGameHandler.getInstance();

    public PreBuildScenePanel(BrickingBadFrame frame) {
        setPreferredSize(new Dimension((int) Constants.gameSceneWidth, (int) Constants.gameSceneHeight));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));

        gameName_label = new JLabel("Game Name:\t");
        gameName_text = new JTextField(10);

        // Simple Brick Label
        simpleBrick_label = new JLabel("Simple Brick:\t");
        simpleBrick_text = new JTextField(10);
        simpleBrick_text.setText("75");

        // Mine Brick Label
        mineBrick_label = new JLabel("Mine Brick:\t");
        mineBrick_text = new JTextField(10);
        mineBrick_text.setText("5");

        // Wrapper Brick Label
        wrapperBrick_label = new JLabel("Wrapper Brick:\t");
        wrapperBrick_text = new JTextField(10);
        wrapperBrick_text.setText("10");

        // Half Metal Brick Label
        halfMetalBrick_label = new JLabel("Half Metal Brick:\t");
        halfMetalBrick_text = new JTextField(10);
        halfMetalBrick_text.setText("10");

        // Submit
        createButton = new JButton("Generate Map");
        createButton.addActionListener(e -> {
            try {
                String gameName = gameName_text.getText();
                int simpleBrick = Integer.parseInt(simpleBrick_text.getText());
                int mineBrick = Integer.parseInt(mineBrick_text.getText());
                int wrapperBrick = Integer.parseInt(wrapperBrick_text.getText());
                int halfMetalBrick = Integer.parseInt(halfMetalBrick_text.getText());
                if (buildGameHandler.verifyBrickNumbers(simpleBrick, mineBrick, wrapperBrick, halfMetalBrick)) {
                    frame.closePanel(this);
                    frame.openBuildScenePanel(gameName, simpleBrick, mineBrick, wrapperBrick, halfMetalBrick);
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a number of bricks." +
                            "\nThere should be at least" +
                            "\n\t- 75 Simple Bricks" +
                            "\n\t-  5 Mine Bricks" +
                            "\n\t- 10 Wrapper Bricks" +
                            "\n\t- 10 Half-Metal Bricks");
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(this, "Please valid numbers");
            }
        });

        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.closePanel(this);
            frame.openMenuPanel();
        });

        // FIRST COLUMN
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.weightx = 0.5;
        gc.weighty = 7;
        gc.gridx = 0;
        gc.gridy = 0;
        this.add(backButton, gc);

        gc.anchor = GridBagConstraints.LAST_LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 1;
        this.add(gameName_label, gc);

        gc.anchor = GridBagConstraints.LAST_LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 2;
        this.add(simpleBrick_label, gc);

        gc.anchor = GridBagConstraints.LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 3;
        this.add(mineBrick_label, gc);

        gc.anchor = GridBagConstraints.LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 4;
        this.add(wrapperBrick_label, gc);

        gc.anchor = GridBagConstraints.LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 5;
        this.add(halfMetalBrick_label, gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 7;
        gc.gridx = 0;
        gc.gridy = 6;
        this.add(createButton, gc);

        // SECOND COLUMN
        gc.anchor = GridBagConstraints.LAST_LINE_START;
        gc.weightx = 0.5;
        gc.weighty = 1;
        gc.gridx = 1;
        gc.gridy = 1;
        this.add(gameName_text, gc);

        gc.anchor = GridBagConstraints.LAST_LINE_START;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 1;
        gc.gridy = 2;
        this.add(simpleBrick_text, gc);

        gc.anchor = GridBagConstraints.LINE_START;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 1;
        gc.gridy = 3;
        this.add(mineBrick_text, gc);

        gc.anchor = GridBagConstraints.LINE_START;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 1;
        gc.gridy = 4;
        this.add(wrapperBrick_text, gc);

        gc.anchor = GridBagConstraints.LINE_START;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 1;
        gc.gridy = 5;
        this.add(halfMetalBrick_text, gc);
    }
}
