package miner.model;


import miner.MinerFrame;

import java.awt.Point;

public class Game {

    private Field field;

    public static final int loseGameState = 1;
    public static final int actionGameState = 2;
    public static final int winGameState = 3;

    private int gameState;
    private int width, height;


    public Game(int width, int height, int mines) {
        this.width = width;
        this.height = height;

        gameState = actionGameState;
        field = new Field(width, height, mines);
    }

    public Field getField() {
        return field;
    }

    public int getGameState() {
        return gameState;
    }

    public boolean checkEndGame() {
        return gameState != actionGameState;
    }


    public void pressLeftButton(int x, int y) {
        if (gameState != actionGameState) return;

        Position pressedPos = fieldPointToFieldCell(x, y, MinerFrame.pixWidth, MinerFrame.pixHeight);

        if (pressedPos.col < 0 || pressedPos.col >= width) return;
        if (pressedPos.row < 0 || pressedPos.row >= height) return;

        Cell pressedCell = field.getCell(pressedPos);

        if (pressedCell.isMined()) {
            pressedCell.setState(Cell.blastedCell);
            gameState = loseGameState;
            return;
        }

        field.openCell(pressedPos);

        if (field.checkWin()) gameState = winGameState;
    }


    public void pressRightButton(int x, int y) {
        if (gameState != actionGameState) return;

        Position pressedPos = fieldPointToFieldCell(x, y, MinerFrame.pixWidth, MinerFrame.pixHeight);

        if (pressedPos.col < 0 || pressedPos.col >= width) return;
        if (pressedPos.row < 0 || pressedPos.row >= height) return;

        field.markCell(pressedPos);

        if (field.checkWin()) gameState = winGameState;
    }


    private Position fieldPointToFieldCell(int x, int y, int CellWidth, int CellHeight) {

        float height25 = CellHeight * 0.25f; //высота пустого треугольника вверху и внизу поля
        float height75 = CellHeight * 0.75f;
        float width50 = CellWidth * 0.50f;

        int firstRow = (int) Math.floor((y - height25) / height75); //на одном Y могут лежать 2 ряда
        int lastRow = (int) Math.floor(y / height75);

        int colInEvenRow = (int) Math.floor(x / CellWidth);  //на одном X могут лежать 2 колонны
        int colInOddRow = (int) Math.floor((x - width50) / CellWidth);

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


    public Point findCellCenter(int col, int row) {
        int cellWidth = MinerFrame.pixWidth;
        int cellHeight = MinerFrame.pixHeight;

        int centerX = (int) ((col + 1) * cellWidth - ((row % 2 == 0) ? cellWidth * 0.50f : 0));
        int centerY = (int) ((row + 1) * cellHeight * 0.75f - cellHeight * 0.25f);

        return new Point(centerX, centerY);
    }

}
