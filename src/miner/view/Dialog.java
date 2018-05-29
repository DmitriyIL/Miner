package miner.view;

import miner.MinerFrame;

import javax.swing.*;
import java.awt.*;

public class Dialog extends JDialog {

    public Dialog(JFrame owner, String dialogName, String textError, MinerFrame parentFrame) {
        super(owner, dialogName, true);

        add(new JLabel(textError), BorderLayout.CENTER);

        JPanel panel = new JPanel();

        JButton ok = new JButton("Ok");
        ok.addActionListener(event -> setVisible(false));
        panel.add(ok);

        if (!dialogName.equals("Ошибка")) {
            JButton restart = new JButton("New Game");
            restart.addActionListener(event -> {
                parentFrame.restartGame();
                setVisible(false);
            });
            panel.add(restart);
        }

        add(panel, BorderLayout.SOUTH);
        setSize(300, 200);
        setLocationRelativeTo(null);
    }
}
