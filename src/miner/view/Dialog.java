package miner.view;

import miner.MinerFrame;

import javax.swing.*;
import java.awt.*;

/**
 * отвечает за диалоги
 */
class Dialog extends JDialog {

    /**
     * Инициализирует диалог
     * @param owner - класс DialogFrame, который вызвал диалог
     * @param dialogName - название окна
     * @param textDialog - - текст в окне диалога
     */
    public Dialog(JFrame owner, String dialogName, String textDialog, MinerFrame minerFrame) {
        super(owner, dialogName, true);

        add(new JLabel(textDialog), BorderLayout.CENTER);

        JPanel panel = new JPanel();

        JButton ok = new JButton("Ok");
        ok.addActionListener(event -> setVisible(false));
        panel.add(ok);

        if (!dialogName.equals("Ошибка")) {
            JButton restart = new JButton("New Game");
            restart.addActionListener(event -> {
                minerFrame.restartGame();
                setVisible(false);
            });
            panel.add(restart);
        }

        add(panel, BorderLayout.SOUTH);
        setSize(300, 200);
        setLocationRelativeTo(null);
    }
}
