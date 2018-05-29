package miner;

import miner.view.DialogFrame;
import miner.view.FieldPanel;
import miner.view.MenuPanel;
import miner.model.Game;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class MinerFrame extends JFrame {

    private static Game game;
    private static JLabel statusLabel;
    private FieldPanel fieldPanel;
    private MenuPanel menuPanel;
    private JToolBar toolbar;
    private ActionListener restartListener, startListener, toMenuListener;
    private DialogFrame dialog; // диалоговое окно отключается методом steVisible(false), чтобы
                                // не накапливались невидимые фреймы - они переприсваиваются в эту переменную

    public static final int pixWidth = 48; // width in pixels
    public static final int pixHeight = 56; // height in pixels


    public static void main(String[] args) {
        new MinerFrame();
    }

    private MinerFrame() {
        initListeners();

        initMenu();

        initFrame();
    }


    public static Game getGame() {
        return game;
    }


    public static JLabel getStatusLabel() {
        return statusLabel;
    }


    private void initFieldPanel(int cols, int rows, int mines) {
        game = new Game(cols, rows, mines);

        initToolBar();
        initStatusLabel();
        fieldPanel = new FieldPanel(cols, rows, pixWidth, pixHeight, this);

        this.remove(menuPanel);

        add(fieldPanel);

        validateFrame();
    }


    private void initToolBar() {
        toolbar = new JToolBar();
        toolbar.setBorder(new BevelBorder(BevelBorder.RAISED));

        toolbar.addSeparator();
        JButton restart = new JButton(new ImageIcon("res/img/restart.png"));
        restart.addActionListener(restartListener);
        toolbar.add(restart);

        toolbar.addSeparator();
        JButton menuButton = new JButton(new ImageIcon("res/img/menuPanel.png"));
        menuButton.addActionListener(toMenuListener);
        toolbar.add(menuButton);

        this.add(toolbar, BorderLayout.NORTH);
    }


    private void initMenu() {
        menuPanel = new MenuPanel();
        menuPanel.startGameButton.addActionListener(startListener);
        menuPanel.setLocation(100, 100);
        this.add(menuPanel);
    }


    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("miner");
        validateFrame();
        setIconImage(FieldPanel.getImage("ic_launcher"));
    }


    private void initListeners() {
        restartListener = e -> restartGame();
        startListener = e -> {
            String fieldWidth = menuPanel.getFieldWidth();
            String fieldHeight = menuPanel.getFieldHeight();
            String bombsAmt = menuPanel.getBombsAmt();

            if (isValidValue(fieldWidth, fieldHeight, bombsAmt)) {
                initFieldPanel(toInt(fieldWidth), toInt(fieldHeight), toInt(bombsAmt));
            }
        };
        toMenuListener = e -> openMenu();
    }


    private void initStatusLabel() {
        statusLabel = new JLabel("Найдите все бомбы");
        add(statusLabel, BorderLayout.SOUTH);
    }


    public void restartGame() {
        String fieldWidth = menuPanel.getFieldWidth();
        String fieldHeight = menuPanel.getFieldHeight();
        String bombsAmt = menuPanel.getBombsAmt();

        game = new Game(toInt(fieldWidth), toInt(fieldHeight), toInt(bombsAmt));

        fieldPanel.repaint();
        statusLabel.setText("Найдите все бомбы");
    }


    public void validateFrame() {
        setResizable(false);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }


    private void openMenu() {
        this.remove(fieldPanel);
        this.remove(toolbar);
        this.remove(statusLabel);
        initMenu();
        add(menuPanel);
        validateFrame();
    }


    private boolean isValidValue(String fieldWidth, String fieldHeight, String bombsAmt) {
        if (!validateNumber(fieldHeight)) return false;
        if (!validateNumber(fieldWidth)) return false;
        if (!validateNumber(bombsAmt)) return false;

        if (toInt(fieldWidth) > 35) {
            dialog = new DialogFrame("Ошибка", "Длина поля не должна превышать 35", this);
            return false;
        }
        if (toInt(fieldHeight) > 20) {
            dialog = new DialogFrame("Ошибка", "Высота поля не должна превышать 20", this);
            return false;
        }
        if (toInt(fieldHeight) * toInt(fieldWidth) <= toInt(bombsAmt)) {
            dialog = new DialogFrame("Ошибка", "Заданно слишком много бомб", this);
            return false;
        }
        return true;
    }


    private boolean validateNumber(String numberInStr) {
        if (numberInStr.isEmpty()) {
            dialog = new DialogFrame("Ошибка", "Одно из значиний пустое", this);
            return false;
        }
        if (numberInStr.charAt(0) == '0' && numberInStr.length() > 1) {
            dialog = new DialogFrame("Ошибка", "Задано некорректное число", this);
            return false;
        }
        if (!numberInStr.matches("[0-9]+")) {
            dialog = new DialogFrame("Ошибка", "Задано некорректное число", this);
            return false;
        }
        return true;
    }


    private int toInt(String number) {
        return Integer.parseInt(number);
    }

}