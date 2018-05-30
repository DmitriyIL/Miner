package miner.view;

import miner.MinerFrame;
import miner.model.Cell;
import miner.model.Field;

import javax.swing.*;
import java.awt.*;


/**
 * Класс отвечает за прорисовку поля и
 * обработку нажатие на поле.
 */
public class FieldPanel extends JPanel {

    private int cols, rows;
    private int pixWidth, pixHeight;


    /**
     * Создает экземпляр класса.
     * Устанавливает размер поля и инициализирует слушателей.
     * @param cols - кол-во столбцов поля.
     * @param rows - кол-во рядов поля.
     * @param pixWidth - ширина одно шестиугольника в пикселях.
     * @param pixHeight - длина одно шестиугольника в пикселях.
     */
    public FieldPanel(int cols, int rows, int pixWidth, int pixHeight) {
        this.cols = cols;
        this.rows = rows;
        this.pixHeight = pixHeight;
        this.pixWidth = pixWidth;

        this.setPreferredSize(new Dimension(cols * pixWidth + (int) ((rows == 1) ? 0 : 0.5 * pixWidth),
                (int) (rows * pixHeight * 0.75) + (int) (pixHeight * 0.25)));
    }


    private void drawClosedField(Graphics g) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int startX = j * pixWidth + ((i % 2 == 1) ? (int) (0.5 * pixWidth) : 0);
                int startY = i * (int) (pixHeight * 0.75);
                Point start = new Point(startX, startY);

                drawImg(g, 13, start); //13 - закрытая клетка
            }
        }
    }


    /**
     * Рисует поле игры в панели.
     * @param field - класс модели поля, хранящий всю информацию о состоянии поля.
     */
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

    /**
     * Рисует одну ячейку поля.
     * @param cell - класс Cell, хранящий состояние ячейки, которое надо отобразить.
     * @param start - класс Point, хранящий координаты начала отрисовки.
     */
    private void drawCell(Cell cell, Point start, Graphics g) {
        drawImg(g, 0, start); //0 - пустая клетка
        if (cell.isOpened()) {
            int imgId = cell.getState();
            drawImg(g, imgId, start); //цифра на клетке от 1 до 6
            if (cell.isBlasted())
                drawImg(g, 12, start); //12 - взорванная бомба
        } else if (MinerFrame.getGame().checkEndGame()) {
            if (!cell.isMined() && !cell.isFlaged()) {
                drawImg(g, 13, start); //13 - закрытая клетка
                return;
            }

            drawImg(g, 9, start); //9 - невзорванная бомба

            if (cell.isMined() && cell.isFlaged())
                drawImg(g, 10, start); //10 - галочка
            else if (cell.isFlaged())
                drawImg(g, 11, start); //11 - крестик
        } else {
            drawImg(g, 13, start); //13 - закрытая клетка
            if (cell.isFlaged())
                drawImg(g, 14, start); //14 - флаг
            if (cell.isOuestioned())
                drawImg(g, 15, start); //15 - вопросик
        }
    }


    /**
     * Рисует изображение из карты res/img/map_98x112 по индексу.
     * @param imgId - индекс изображения в карте.
     * @param start - класс Point, хранящий координаты начала отрисовки.
     */
    private void drawImg(Graphics g, int imgId, Point start) {
        g.drawImage(getImage("map_98x112"),
                start.x, start.y, start.x + pixWidth, start.y + pixHeight,
                imgId * 98, 0, (imgId + 1) * 98 - 1, 111, this);
    }


    /**
     * Получчает изображение из res/img/ по имени.
     * @param name - имя изображения.
     */
    public static Image getImage(String name) {
        String fileName = "res/img/" + name.toLowerCase() + ".png";
        ImageIcon icon = new ImageIcon(fileName);
        return icon.getImage();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (MinerFrame.firstStep) {
            drawClosedField(g);
            return;
        }
        drawField(MinerFrame.getGame().getField(), g);
    }
}
