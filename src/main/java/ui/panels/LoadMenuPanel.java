package ui.panels;

import domain.Constants;
import ui.BrickingBadFrame;

import javax.swing.*;
import java.awt.*;

public class LoadMenuPanel extends JPanel {
    private JButton loadMapButton, loadGameButton, backButton;

    public LoadMenuPanel(BrickingBadFrame frame) {
        setPreferredSize(new Dimension((int) Constants.gameSceneWidth, (int) Constants.gameSceneHeight));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        GridBagConstraints gc = new GridBagConstraints();
        setLayout(new GridBagLayout());

        JLabel mapNameLabel = new JLabel("Map Name:");
        JTextField mapNameTextField = new JTextField(10);

        JLabel gameNameLabel = new JLabel("Game Name:");
        JTextField gameNameTextField = new JTextField(10);

        loadMapButton = new JButton("Load an existing Map");
        loadMapButton.addActionListener(e -> {
            String mapName = mapNameTextField.getText();
            if (frame.loadExistingMap(mapName)) {
                frame.closePanel(this);
                frame.openBuildScenePanel(mapName);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid map name");
            }
        });
        loadMapButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadMapButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        add(loadMapButton);

        loadGameButton = new JButton("Load an Existing Game");
        loadGameButton.addActionListener(e -> {
            String gameName = gameNameTextField.getText();
            if (frame.loadExistingGame(gameName)) {
                frame.closePanel(this);
                frame.openGameScenePanel();
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid game name");
            }
        });
        loadGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.closePanel(this);
            frame.openMenuPanel();
        });

        // FIRST COLUMN
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.weightx = 1;
        gc.weighty = 7;
        gc.gridx = 0;
        gc.gridy = 0;
        this.add(backButton, gc);

        gc.anchor = GridBagConstraints.SOUTH;
        gc.weightx = 0.5;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 1;
        this.add(mapNameLabel, gc);

        gc.anchor = GridBagConstraints.CENTER;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 2;
        this.add(mapNameTextField, gc);

        gc.anchor = GridBagConstraints.NORTH;
        gc.weightx = 0.5;
        gc.weighty = 10;
        gc.gridx = 0;
        gc.gridy = 3;
        this.add(loadMapButton, gc);

        // SECOND COLUMN
        gc.anchor = GridBagConstraints.SOUTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 1;
        gc.gridy = 1;
        this.add(gameNameLabel, gc);

        gc.anchor = GridBagConstraints.CENTER;
        gc.weightx = 1;
        gc.weighty = 0.5;
        gc.gridx = 1;
        gc.gridy = 2;
        this.add(gameNameTextField, gc);

        gc.anchor = GridBagConstraints.NORTH;
        gc.weightx = 0.5;
        gc.weighty = 10;
        gc.gridx = 1;
        gc.gridy = 3;
        this.add(loadGameButton, gc);

    }
}
