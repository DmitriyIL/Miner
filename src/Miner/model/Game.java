package Miner.model;


import Miner.MinerFrame;

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

        if (pressedPos.w < 0 || pressedPos.w >= width) return;
        if (pressedPos.h < 0 || pressedPos.h >= height) return;

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

        if (pressedPos.w < 0 || pressedPos.w >= width) return;
        if (pressedPos.h < 0 || pressedPos.h >= height) return;

        field.markCell(pressedPos);

        if (field.checkWin()) gameState = winGameState;
    }


    public Position fieldPointToFieldCell(int x, int y, int CellPixWidth, int CellPixHeight) {

        float height25 = CellPixHeight * 0.25f; //высота пустого треугольника вверху и внизу поля
        float height75 = CellPixHeight * 0.75f;

        int firstRow = (int) Math.floor((y - height25) / height75);
        int lastRow = (int) Math.floor(y / height75);

        float width50 = CellPixWidth * 0.50f;

        int colInEvenRow = (int) Math.floor(x / CellPixWidth);
        int colInOddRow = (int) Math.floor((x - width50) / CellPixWidth);

        Point pressPoint = new Point(x, y);
        int minDistanceToPressPoint = Integer.MAX_VALUE;
        Position pressedCellPos = new Position(-1, -1);

        for (int row = firstRow; row <= lastRow; row++) {
            int col = (row % 2 == 0) ? colInEvenRow : colInOddRow;

            int centerX = (int) ((col + 1) * CellPixWidth - ((row % 2 == 0) ? width50 : 0));
            int centerY = (int) ((row + 1) * height75 - height25);

            Point center = new Point(centerX, centerY);
            double distanceToPressPoint = center.distance(pressPoint);
            if (distanceToPressPoint < minDistanceToPressPoint) {
                minDistanceToPressPoint = (int) distanceToPressPoint;
                pressedCellPos = new Position(col, row);
            }
        }
        return pressedCellPos;
    }

}
