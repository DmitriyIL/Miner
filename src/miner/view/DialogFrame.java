package miner.view;

import miner.MinerFrame;

import javax.swing.*;

/**
 * Отвечает за фрейм диалога
 */
public class DialogFrame extends JFrame {

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;

    /**
     * Coздает диалоговое окно
     * @param dialogName - название окна
     * @param text - текст в окне
     */
    public DialogFrame(String dialogName, String text, MinerFrame minerFrame) {
        setTitle(dialogName);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        Dialog dialog = new Dialog(DialogFrame.this, dialogName, text, minerFrame);
        dialog.setVisible(true);
    }
}
