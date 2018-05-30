package miner;

import miner.view.DialogFrame;
import miner.view.FieldPanel;
import miner.view.MenuPanel;
import miner.model.Game;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

/**
 * Контролирующий класс, из которого идет запуск приложения.
 * Инициализирует основной фрейм и его внутреннияе элементы.
 */

public class MinerFrame extends JFrame {

    private static Game game;
    private static JLabel statusLabel;
    private FieldPanel fieldPanel;
    private MenuPanel menuPanel;
    private JToolBar toolbar;
    private ActionListener restartListener, startListener, toMenuListener;
    private MouseListener fieldListener;
    private DialogFrame dialog; // диалоговое окно отключается методом steVisible(false), чтобы
                                // не накапливались невидимые фреймы - они переприсваиваются в эту переменную

    public static final int pixWidth = 48; // width in pixels
    public static final int pixHeight = 56; // height in pixels

    public static boolean firstStep = true;
    /**
     * Запускает приложение
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MinerFrame::new);
    }


    /**
     * Служит для связи пакетов view и model.
     * @return класс запущенной игры.
     */
    public static Game getGame() {
        return game;
    }


    /**
     * Инициализирует основной фрейм с меню настроек игры.
     */
    private MinerFrame() {
        initListeners();

        initMenu();

        initFrame();
    }

    /**
     * Инициализация всех слушателей
     */
    private void initListeners() {
        restartListener = e -> restartGame(); //кнопка рестарт в панели игры
        startListener = e -> {  //кнопка "Создать игру" в панели меню
            String colsAmt = menuPanel.getColsAmt();
            String rowsAmt = menuPanel.getRowsAmt();
            String bombsAmt = menuPanel.getBombsAmt();

            if (isValidValue(colsAmt, rowsAmt, bombsAmt)) {
                initFieldPanel(toInt(colsAmt), toInt(rowsAmt), toInt(bombsAmt));
            }
        };
        toMenuListener = e -> openMenu(); //кнопка возвращения в панель меню
        fieldListener = new MouseAdapter() { //ждет клики по ячейкам поля
            @Override
            public void mousePressed(MouseEvent e) {
                clickOnField(e);
            }
        };
    }


    /**
     * Инициализация панели меню
     */
    private void initMenu() {
        menuPanel = new MenuPanel();
        menuPanel.startGameButton.addActionListener(startListener);
        menuPanel.setLocation(100, 100);
        this.add(menuPanel);
    }


    /**
     * Инициализация основного фрейма и вывод его на экран.
     */
    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("miner");
        setResizable(false);

        setVisible(true);
        validateFrame();
        setIconImage(FieldPanel.getImage("ic_launcher"));
    }


    /**
     * Обработка основного фрейма для пользовательского удобства.
     */
    private void validateFrame() {
        pack();
        setLocationRelativeTo(null);
    }


    /**
     * Замена панели Меню на панель Поля игры.
     * Инициализация поля игры.
     * @param cols - кол-во столбцов в поле.
     * @param rows - кол-во рядов в поле.
     * @param mines - кол-во мин в поле.
     */
    private void initFieldPanel(int cols, int rows, int mines) {
        game = new Game(cols, rows, mines);

        initToolBar();
        initStatusLabel();

        fieldPanel = new FieldPanel(cols, rows, pixWidth, pixHeight);
        fieldPanel.addMouseListener(fieldListener);

        this.remove(menuPanel);
        add(fieldPanel);

        validateFrame();
    }


    /**
     * Инициализация панели инструментов вверху окна
     */
    private void initToolBar() {
        toolbar = new JToolBar();
        toolbar.setBorder(new BevelBorder(BevelBorder.RAISED));

        toolbar.addSeparator();
        JButton restart = new JButton(new ImageIcon("res/img/restart.png"));
        restart.addActionListener(restartListener);
        toolbar.add(restart);

        toolbar.addSeparator();
        JButton menuButton = new JButton(new ImageIcon("res/img/menu.png"));
        menuButton.addActionListener(toMenuListener);
        toolbar.add(menuButton);

        this.add(toolbar, BorderLayout.NORTH);
    }


    /**
     * Инициализация лэйбла-состояния внизу окна
     */
    private void initStatusLabel() {
        statusLabel = new JLabel("Найдите все бомбы");
        add(statusLabel, BorderLayout.SOUTH);
    }


    /**
     * Создает новую игру
     */
    public void restartGame() {
        firstStep = true;

        String fieldWidth = menuPanel.getColsAmt();
        String fieldHeight = menuPanel.getRowsAmt();
        String bombsAmt = menuPanel.getBombsAmt();

        game = new Game(toInt(fieldWidth), toInt(fieldHeight), toInt(bombsAmt));

        fieldPanel.repaint();
        statusLabel.setText("Найдите все бомбы");
    }


    /**
     * Возвращает игрока в Меню настроек игры.
     */
    private void openMenu() {
        firstStep = true;

        this.remove(fieldPanel);
        this.remove(toolbar);
        this.remove(statusLabel);

        initMenu();

        validateFrame();
    }


    /**
     * Обрабатывает нажатие на поле игры
     */
    private void clickOnField(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (e.getButton() == MouseEvent.BUTTON1)
            game.pressLeftButton(x, y);
        if (e.getButton() == MouseEvent.BUTTON3) {
            game.pressRightButton(x, y);
        }

        fieldPanel.repaint();

        checkGameStatus();
    }


    /**
     * Проверка статуса игры на победу или поражение.
     */
    private void checkGameStatus() {
        switch (game.getGameState()) {
            case LOSE:
                statusLabel.setText("Вы прогиграли");
                dialog = new DialogFrame("Поражение", "Упс.. Кажется вы взорвались.", this);
                return;
            case WIN:
                statusLabel.setText("Вы победили");
                dialog = new DialogFrame("Победа", "Поле отчищено от бомб!", this);
                return;
        }
    }


    /**
     * Проверяет валидность значений введенных в меню создания игры
     * @param colsAmt - кол-во столбцов, заданных в меню игры
     * @param rowsAmt - кол-во рядов, заданных в меню игры
     * @param bombsAmt - кол-во мин, заданных в меню игры
     */
    private boolean isValidValue(String colsAmt, String rowsAmt, String bombsAmt) {
        if (!isNumber(rowsAmt)) return false;
        if (!isNumber(colsAmt)) return false;
        if (!isNumber(bombsAmt)) return false;

        if (toInt(colsAmt) > 35) {
            dialog = new DialogFrame("Ошибка", "Длина поля не должна превышать 35", this);
            return false;
        }
        if (toInt(rowsAmt) > 20) {
            dialog = new DialogFrame("Ошибка", "Высота поля не должна превышать 20", this);
            return false;
        }
        if (toInt(rowsAmt) * toInt(colsAmt) <= toInt(bombsAmt)) {
            dialog = new DialogFrame("Ошибка", "Заданно слишком много бомб", this);
            return false;
        }
        return true;
    }


    /**
     * Проверяет записано ли в строке число.
     */
    private boolean isNumber(String numberInStr) {
        if (numberInStr.isEmpty()) {
            dialog = new DialogFrame("Ошибка", "Пустое значение", this);
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


    /**
     * Преобразовывает строку в число.
     */
    private int toInt(String number) {
        return Integer.parseInt(number);
    }
}