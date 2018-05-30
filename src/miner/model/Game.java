package miner.model;


import miner.MinerFrame;

import java.awt.Point;
import java.util.LinkedList;


/**
 * Класс отвечает за игровой процесс, состояние игры,
 * также отвечает за обработка нажатий.
 * Соединяет MinerFrame и Field.
 */
public class Game {

    private Field field;

    public enum GameStatus {
        ACTION,
        LOSE,
        WIN
    }

    private GameStatus gameState;
    private int colsAmt, rowsAmt;

    /**
     * Конструктор игрового процесса
     * @param colsAmt - кол-во столбцов на поле.
     * @param rowsAmt - кол-во рядов на поле.
     * @param minesAmt - кол-во мин на поле.
     */
    public Game(int colsAmt, int rowsAmt, int minesAmt) {
        this.colsAmt = colsAmt;
        this.rowsAmt = rowsAmt;

        gameState = GameStatus.ACTION;

        field = new Field(colsAmt, rowsAmt, minesAmt);
    }


    /**
     * @return класс field.
     */
    public Field getField() {
        return field;
    }


    /**
     * @return состояние игры.
     */
    public GameStatus getGameState() {
        return gameState;
    }


    /**
     * Обрабатывает нажатие на поле левой кнопкой мыши.
     * @param x - координата нажатия.
     * @param y - координата нажатия.
     */
    public void pressLeftButton(int x, int y) {
        if (gameState != GameStatus.ACTION) return;

        Position pressedPos = fieldPointToFieldCell(x, y, MinerFrame.pixWidth, MinerFrame.pixHeight);

        if (!Field.cellExist(pressedPos, colsAmt, rowsAmt)) return;

        Cell pressedCell = field.getCell(pressedPos);

        if (MinerFrame.firstStep && !pressedCell.isFlaged()) {
            field.createBombs(pressedPos);
            MinerFrame.firstStep = false;
        }

        if (pressedCell.isMined()) {
            pressedCell.setState(Cell.blastedCell);
            gameState = GameStatus.LOSE;
            return;
        }

        field.openCell(pressedPos);

        if (field.checkWin()) gameState = GameStatus.WIN;
    }



    /**
     * Обрабатывает нажатие на поле правой кнопкой мыши.
     * @param x - координата нажатия.
     * @param y - координата нажатия.
     */
    public void pressRightButton(int x, int y) {
        if (gameState != GameStatus.ACTION) return;

        Position pressedPos = fieldPointToFieldCell(x, y, MinerFrame.pixWidth, MinerFrame.pixHeight);

        if (!Field.cellExist(pressedPos, colsAmt, rowsAmt)) return;

        field.markCell(pressedPos);

        if (field.checkWin()) gameState = GameStatus.WIN;
    }


    /**
     * Высчитывает позицию клетки, на которую нажали мышкой.
     *
     * Алгоритм:
     * Находит предполагаемые клетки на которые нажали,
     * потом путем перебора ищет минимальное расстояние
     * от точки нажатия до центра клеток.
     *
     * @param x - координата нажатия мышкой.
     * @param y - координата нажатия мышкой.
     * @param CellWidth - ширина одного шестиугольника в пикселях.
     * @param CellHeight - высота одного шестиугольника в пикселях.
     * @return поцицию клетки на которую нажали
     */
    private Position fieldPointToFieldCell(int x, int y, int CellWidth, int CellHeight) {

        float height25 = CellHeight * 0.25f; //высота пустого треугольника вверху и внизу поля
        float height75 = CellHeight * 0.75f;
        float width50 = CellWidth * 0.50f;

        int firstRow = (int) Math.floor((y - height25) / height75); //на одном Y могут лежать 2 ряда
        int lastRow = (int) Math.floor(y / height75);

        int colInEvenRow = (int) Math.floor(x / CellWidth);  //на одном X могут лежать 2 колонны
        int colInOddRow = (int) Math.floor((x - width50) / CellWidth);

        //поиск клетки с ближайшим центром
        Point pressPoint = new Point(x, y);
        int minDistanceToPressPoint = Integer.MAX_VALUE;
        Position pressedCellPos = new Position(-1, -1);

        for (int row = firstRow; row <= lastRow; row++) {
            int col = (row % 2 == 0) ? colInEvenRow : colInOddRow;

            Point center = findCellCenter(col, row);
            double distanceToPressPoint = center.distance(pressPoint);
            if (distanceToPressPoint < minDistanceToPressPoint) {
                minDistanceToPressPoint = (int) distanceToPressPoint;
                pressedCellPos = new Position(col, row);
            }
        }
        return pressedCellPos;
    }


    /**
     * Ищет центр клетки.
     * @param col - столбец в котором расположена клетка.
     * @param row - ряд в котором расположена клетка.
     * @return класс Point, хранящий координаты центра.
     */
    public Point findCellCenter(int col, int row) {
        int cellWidth = MinerFrame.pixWidth;
        int cellHeight = MinerFrame.pixHeight;

        int centerX = (int) ((col + 1) * cellWidth - ((row % 2 == 0) ? cellWidth * 0.50f : 0));
        int centerY = (int) ((row + 1) * cellHeight * 0.75f - cellHeight * 0.25f);

        return new Point(centerX, centerY);
    }


    /**
     * Проверяет окончена ли игра.
     */
    public boolean checkEndGame() {
        return gameState != GameStatus.ACTION;
    }
}
