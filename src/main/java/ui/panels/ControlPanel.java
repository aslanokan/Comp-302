package ui.panels;

import domain.Constants;
import ui.BrickingBadFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {

    private GridBagConstraints gc = new GridBagConstraints();
    private BrickingBadFrame frame;

    private JPanel controlPanel;
    private JButton startButton;
    private JButton loadButton;
    private JButton saveButton;
    private JButton quitButton;

    private JPanel buildPanel;
    private JButton simpleBrick;
    private JButton mineBrick;
    private JButton wrapperBrick;
    private JButton halfMetalBrick;

    private JPanel infoPanel;
    private JLabel score_label;
    private JLabel score_text;
    private JLabel lives_label;
    private JLabel lives_text;
    private JLabel powerups_label;
    private JLabel powerups_text;

    public ControlPanel(BrickingBadFrame frame) {
        setPreferredSize(new Dimension((int) Constants.gameSceneWidth, (int) Constants.controlBarHeight));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        this.frame = frame;

        initControlPanel();
        initBuildPanel();

        setVisible(true);
    }

    private void initBuildPanel() {
        //CREATING THE BUILD PANEL
        buildPanel = new JPanel();
        buildPanel.setPreferredSize(new Dimension(150, 100));
        buildPanel.setLayout(new GridBagLayout());

        simpleBrick = new JButton("S");
        simpleBrick.setBackground(Color.BLUE);
        halfMetalBrick = new JButton("HM");
        halfMetalBrick.setBackground(Color.RED);
        mineBrick = new JButton("M");
        mineBrick.setBackground(Color.GREEN);
        wrapperBrick = new JButton(" W ");
        wrapperBrick.setBackground(Color.YELLOW);

        simpleBrick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.addBrickOfType("SimpleBrick");
                frame.requestFocusInBuildScenePanel();
            }
        });

        mineBrick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.addBrickOfType("MineBrick");
                frame.requestFocusInBuildScenePanel();
            }
        });

        halfMetalBrick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.addBrickOfType("HalfMetalBrick");
                frame.requestFocusInBuildScenePanel();
            }
        });

        wrapperBrick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.addBrickOfType("WrapperBrick");
                frame.requestFocusInBuildScenePanel();
            }
        });

        // FIRST COLUMN
        gc.anchor = GridBagConstraints.LAST_LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 0;
        buildPanel.add(simpleBrick, gc);

        gc.anchor = GridBagConstraints.LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 1;
        buildPanel.add(mineBrick, gc);

        // SECOND COLUMN
        gc.anchor = GridBagConstraints.LAST_LINE_START;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 1;
        gc.gridy = 0;
        buildPanel.add(halfMetalBrick, gc);

        gc.anchor = GridBagConstraints.LINE_START;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 1;
        gc.gridy = 1;
        buildPanel.add(wrapperBrick, gc);

        add(buildPanel, BorderLayout.EAST);
    }

    public void initInfoPanel() {
        //CREATING THE INFO PANEL
        infoPanel = new JPanel();

        infoPanel.setPreferredSize(new Dimension(150, 100));
        infoPanel.setLayout(new GridBagLayout());

        // Score Label
        score_label = new JLabel("Score: ");
        score_text = new JLabel("0");
        // Lives Label
        lives_label = new JLabel("Lives: ");
        lives_text = new JLabel("3");
        // powerups label
        powerups_label = new JLabel("Powerups: ");
        powerups_text = new JLabel("");

        // FIRST COLUMN
        gc.anchor = GridBagConstraints.LAST_LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 0;
        infoPanel.add(score_label, gc);

        gc.anchor = GridBagConstraints.LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 1;
        infoPanel.add(lives_label, gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 2;
        infoPanel.add(powerups_label, gc);

        // SECOND COLUMN
        gc.anchor = GridBagConstraints.LAST_LINE_START;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 1;
        gc.gridy = 0;
        infoPanel.add(score_text, gc);

        gc.anchor = GridBagConstraints.LINE_START;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 1;
        gc.gridy = 1;
        infoPanel.add(lives_text, gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 1;
        gc.gridy = 2;
        infoPanel.add(powerups_text, gc);

        add(infoPanel, BorderLayout.EAST);
    }

    public void initControlPanel() {
        //CREATING THE CONTROL PANEL
        controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension((int) Constants.controlBarWidth, (int) Constants.controlBarHeight));
        controlPanel.setLayout(new GridBagLayout());

        startButton = new JButton("Start Game");
        startButton.addActionListener(e -> {
            frame.activatePlayMode();
            frame.requestFocusInGameScenePanel();
        });

        saveButton = new JButton("Save Game");
        saveButton.addActionListener(e -> {
            frame.save();
            frame.requestFocusInGameScenePanel();
        });

        // TODO: stop game and hide gameScene.
        quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> {
//            frame.closePanel(this);
//            frame.closeCurrentGamePanel();
//            frame.openMenuPanel();
            frame.quitGame();
        });

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 0;
        controlPanel.add(startButton, gc);

//        gc.anchor = GridBagConstraints.LINE_START;
//        gc.weightx = 0.5;
//        gc.weighty = 3;
//        gc.gridx = 1;
//        gc.gridy = 0;
//        controlPanel.add(loadButton, gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 1;
        gc.gridy = 0;
        controlPanel.add(saveButton, gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 2;
        gc.gridy = 0;
        controlPanel.add(quitButton, gc);

        add(controlPanel, BorderLayout.WEST);
    }

    public void updateScore(double score) {
        score = Math.ceil(score);
        score_text.setText("" + score);
    }

    public void updateLife(int life) {
        lives_text.setText("" + life);
    }

    public void changeToPlayMode() {
        initInfoPanel();
        buildPanel.setVisible(false);
    }

    public void stackedPowerupsUpdated(String stackedPowerups) {
        powerups_text.setText("" + stackedPowerups);
    }
}
