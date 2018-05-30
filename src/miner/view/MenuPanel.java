package miner.view;


import javax.swing.*;
import java.awt.*;

/**
 * Панель Меню для настройки игры
 */
public class MenuPanel extends JPanel {

    private JTextField colsAmt, rowsAmt, bombsAmt;
    public JButton startGameButton;

    /**
     * Создает экземпляр MenuPanel
     */
    public MenuPanel() {
        setPreferredSize(new Dimension(400, 400));

        initMenu();
    }

    /**
     * @return кол-во введнных столбцов
     */
    public String getColsAmt() {
        return colsAmt.getText();
    }

    /**
     * @return кол-во введенных рядов
     */
    public String getRowsAmt() {
        return rowsAmt.getText();

    }

    /**
     * @return кол-во введенных бомб
     */
    public String getBombsAmt() {
        return bombsAmt.getText();
    }


    /**
     * Инициализирует меню и его внутренние компоненты
     */
    private void initMenu() {
        Container container = new Container();
        container.setSize(400, 400);
        container.setLayout(new GridLayout(4, 2, 20, 20));

        JLabel labelW = new JLabel("Width:");
        JLabel labelH = new JLabel("Height:");
        JLabel labelB = new JLabel("Bombs:");

        colsAmt = new JTextField("20", 2);
        rowsAmt = new JTextField("15", 2);
        bombsAmt = new JTextField("40", 4);
        startGameButton = new JButton("Создать игру");

        container.add(labelW);
        container.add(colsAmt);
        container.add(labelH);
        container.add(rowsAmt);
        container.add(labelB);
        container.add(bombsAmt);
        container.add(startGameButton);

        this.add(container, BorderLayout.CENTER);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon("res/img/texture.png").getImage(), 0, 0, 400, 400, this);
    }

}
