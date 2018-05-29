package miner.view;

import miner.MinerFrame;
import miner.model.Cell;
import miner.model.Field;
import miner.model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FieldPanel extends JPanel {

    private int cols, rows;
    private int pixWidth, pixHeight;

    private MinerFrame ownerFrame;
    private DialogFrame dialog;

    public FieldPanel(int cols, int rows, int pixWidth, int pixHeight, MinerFrame ownerFrame) {
        this.ownerFrame = ownerFrame;
        this.cols = cols;
        this.rows = rows;
        this.pixHeight = pixHeight;
        this.pixWidth = pixWidth;

        this.setPreferredSize(new Dimension(cols * pixWidth + (int) ((rows == 1) ? 0 : 0.5 * pixWidth),
                (int) (rows * pixHeight * 0.75) + (int) (pixHeight * 0.25)));

        initListener();
    }

    private void drawField(Field field, Graphics g) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int startX = j * pixWidth + ((i % 2 == 1) ? (int) (0.5 * pixWidth) : 0);
                int startY = i * (int) (pixHeight * 0.75);
                Point start = new Point(startX, startY);

                Cell cell = field.getCell(j, i);

                drawCell(cell, start, g);
            }
        }
    }

    private void drawCell(Cell cell, Point start, Graphics g) {
        drawImg(g, 0, start);
        if (cell.isOpened()) {
            int imgId = cell.getState();
            drawImg(g, imgId, start);
            if (cell.isBlasted())
                drawImg(g, 12, start);
        } else if (MinerFrame.getGame().checkEndGame()) {
            if (!cell.isMined() && !cell.isFlaged()) {
                drawImg(g, 13, start);
                return;
            }

            drawImg(g, 9, start);

            if (cell.isMined() && cell.isFlaged())
                drawImg(g, 10, start);
            else if (cell.isFlaged())
                drawImg(g, 11, start);
        } else {
            drawImg(g, 13, start);
            if (cell.isFlaged())
                drawImg(g, 14, start);
            if (cell.isOuestioned())
                drawImg(g, 15, start);
        }
    }


    public static Image getImage(String name) {
        String fileName = "res/img/" + name.toLowerCase() + ".png";
        ImageIcon icon = new ImageIcon(fileName);
        return icon.getImage();
    }

    private void drawImg(Graphics g, int imgId, Point start) {
        g.drawImage(getImage("map_98x112"),
                start.x, start.y, start.x + pixWidth, start.y + pixHeight,
                imgId * 98, 0, (imgId + 1) * 98 - 1, 111, this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawField(MinerFrame.getGame().getField(), g);
    }


    private void initListener() {
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onPress(e);
            }
        };
        addMouseListener(mouseListener);
    }

    private void onPress(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        Game game = MinerFrame.getGame();

        if (e.getButton() == MouseEvent.BUTTON1)
            game.pressLeftButton(x, y);
        if (e.getButton() == MouseEvent.BUTTON3)
            game.pressRightButton(x, y);

        this.repaint(); // после каждого действия мыши перерисовываем панель игры

        checkGameStatus();
    }

    private void checkGameStatus() {
        switch (MinerFrame.getGame().getGameState()) {
            case 1:
                MinerFrame.getStatusLabel().setText("Вы прогиграли");
                dialog = new DialogFrame("Поражение", "Упс.. Кажется вы взорвались.", ownerFrame);
                return;
            case 3:
                MinerFrame.getStatusLabel().setText("Вы победили");
                dialog = new DialogFrame("Победа", "Поздравляем вас с победой!", ownerFrame);
                return;
        }
    }
}
