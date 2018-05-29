package Miner.View;

import Miner.MinerFrame;

import javax.swing.*;

public class DialogFrame extends JFrame {

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;

    private Dialog dialog;

    public DialogFrame(String dialogName, String text, MinerFrame parentFrame) {

        setTitle(dialogName);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        dialog = new Dialog(DialogFrame.this, dialogName, text, parentFrame);
        dialog.setVisible(true); // отобразить диалог

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(event -> System.exit(0));
        fileMenu.add(exitItem);
    }
}
