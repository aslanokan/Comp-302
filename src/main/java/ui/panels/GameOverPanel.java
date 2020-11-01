package ui.panels;

import domain.BrickingBadApplication;
import domain.Constants;
import domain.models.Grid;
import ui.BrickingBadFrame;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GameOverPanel extends JPanel {

    private BrickingBadFrame frame;
    private GridBagConstraints gc = new GridBagConstraints();

    private JPanel gameOverPanel;
    private JButton quitButton;
    private JLabel gameOverLabel;
    private JLabel messageLabel;

    private JPanel tablePanel;
    private JTable dataTable;

    public GameOverPanel(BrickingBadFrame frame, Object[][] scoreTable, double score, String message) {
        setPreferredSize(new Dimension((int) Constants.gameSceneWidth, (int) Constants.gameSceneHeight));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        this.setLayout(new BorderLayout());
        this.frame = frame;

        gameOverPanel = new JPanel();
        gameOverPanel.setSize(new Dimension((int) Constants.gameSceneWidth,(int) Constants.controlBarHeight));
        gameOverPanel.setLayout(new GridBagLayout());

        tablePanel = new JPanel();
        tablePanel.setSize(new Dimension((int) Constants.gameSceneWidth,(int) (Constants.gameSceneHeight - Constants.controlBarHeight)));
        tablePanel.setLayout(new GridBagLayout());

        quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> {
            frame.quitGame();
        });
        gameOverLabel = new JLabel();
        if(message.equals("win")){
            gameOverLabel.setText("CONGRATULATIONS");
        }else {
            gameOverLabel.setText("GAME OVER :(");
        }
        messageLabel = new JLabel("\n"+ BrickingBadApplication.getUser().getUsername() + " has scored "+((int) score)+" points!");

        // Game Over Panel
        gc.anchor = GridBagConstraints.CENTER;
        gc.weightx = 0.5;
        gc.weighty = 2;
        gc.gridx = 0;
        gc.gridy = 0;
        gameOverPanel.add(quitButton, gc);

        gc.anchor = GridBagConstraints.CENTER;
        gc.weightx = 0.5;
        gc.weighty = 2;
        gc.gridx = 0;
        gc.gridy = 1;
        gameOverPanel.add(gameOverLabel, gc);

        gc.anchor = GridBagConstraints.CENTER;
        gc.weightx = 0.5;
        gc.weighty = 2;
        gc.gridx = 0;
        gc.gridy = 2;
        gameOverPanel.add(messageLabel, gc);

        // Table Data Panel
        String[] columnNames = {"Ranking",
                "Username",
                "Score"};

        dataTable = new JTable(scoreTable, columnNames);

        gc.anchor = GridBagConstraints.CENTER;
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridx = 0;
        gc.gridy = 0;
        tablePanel.add(dataTable, gc);

        this.add(gameOverPanel, BorderLayout.NORTH);
        this.add(tablePanel, BorderLayout.CENTER);
        setVisible(true);
    }


}
