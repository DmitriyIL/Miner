package miner.view;


import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {

    private JTextField fieldWidth, fieldHeight, bombs;
    public JButton startGameButton;


    public MenuPanel() {
        setPreferredSize(new Dimension(400, 400));

        initMenu();
    }


    public String getFieldWidth() {
        return fieldWidth.getText();
    }


    public String getFieldHeight() {
        return fieldHeight.getText();

    }


    public String getBombsAmt() {
        return bombs.getText();
    }


    private void initMenu() {
        Container container = new Container();
        container.setSize(400, 400);
        container.setLayout(new GridLayout(4, 2, 20, 20));

        JLabel labelW = new JLabel("Width:");
        JLabel labelH = new JLabel("Height:");
        JLabel labelB = new JLabel("Bombs:");

        fieldWidth = new JTextField("20", 2);
        fieldHeight = new JTextField("15", 2);
        bombs = new JTextField("40", 4);
        startGameButton = new JButton("Начать");

        container.add(labelW);
        container.add(fieldWidth);
        container.add(labelH);
        container.add(fieldHeight);
        container.add(labelB);
        container.add(bombs);
        container.add(startGameButton);

        this.add(container);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon("res/img/texture.png").getImage(), 0, 0, 400, 400, this);
    }

}
