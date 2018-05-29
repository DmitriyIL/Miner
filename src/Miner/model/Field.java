package Miner.model;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Field {

    private int width, height, mines, flags;
    private Cell[][] field;
    private int restCells;


    public Field(int width, int height, int mines) {
        this.width = width;
        this.height = height;
        this.mines = mines;

        flags = 0;
        restCells = (width * height) - mines;
        createField();
    }


    public Cell getCell(int width, int height) {
        return field[width][height];
    }


    public Cell getCell(Position pos) {
        return field[pos.w][pos.h];
    }


    private void createField() {
        field = new Cell[width][height];

        Random rand = new Random();
        byte mined = (byte) (Cell.closedCell + Cell.minedCell);
        List<Position> restPositions = new LinkedList<>();

        for (int heightPos = 0; heightPos < height; heightPos++)
            for (int widthPos = 0; widthPos < width; widthPos++)
                restPositions.add(new Position(widthPos, heightPos));

        // set mines
        for (int countMines = 0; countMines < mines; countMines++) {
            int index = rand.nextInt(restPositions.size());
            Position cellPos = restPositions.get(index);
            field[cellPos.w][cellPos.h] = new Cell(mined);
            restPositions.remove(index);
        }

        // set other cells
        for (Position cellPos : restPositions) {
            byte minesAround = countMinesAroundCell(cellPos);
            field[cellPos.w][cellPos.h] = new Cell(Cell.closedCell + minesAround);
        }
    }


    private byte countMinesAroundCell(Position cellPos) {
        byte result = 0;
        Position[] positionsAround = getPositionsAround(cellPos);

        for (Position posAround : positionsAround) {
            if (cellExist(posAround)
                    && field[posAround.w][posAround.h] != null
                    && field[posAround.w][posAround.h].isMined())
                result++;
        }

        return result;
    }


    public boolean cellExist(Position cellPos) {
        return cellPos.w >= 0 && cellPos.w < width && cellPos.h >= 0 && cellPos.h < height;
    }


    public Position[] getPositionsAround(Position cellPos) {

        Position positionsAround[];

        // even rows
        if (cellPos.h % 2 == 0) {
            positionsAround = new Position[]{
                    new Position(cellPos.w, cellPos.h - 1), new Position(cellPos.w - 1, cellPos.h - 1),
                    new Position(cellPos.w + 1, cellPos.h), new Position(cellPos.w - 1, cellPos.h + 1),
                    new Position(cellPos.w, cellPos.h + 1), new Position(cellPos.w - 1, cellPos.h)
            };
        }

        // odd rows
        else {
            positionsAround = new Position[]{
                    new Position(cellPos.w, cellPos.h - 1), new Position(cellPos.w + 1, cellPos.h - 1),
                    new Position(cellPos.w + 1, cellPos.h), new Position(cellPos.w + 1, cellPos.h + 1),
                    new Position(cellPos.w, cellPos.h + 1), new Position(cellPos.w - 1, cellPos.h)
            };
        }

        return positionsAround;
    }


    public void openCell(Position cellPos) {
        if (!cellExist(cellPos)) return;

        Cell cell = getCell(cellPos);

        if (cell.isOpened() || cell.isFlaged()) return;

        if (cell.isOuestioned())
            cell.setState(cell.getState() - Cell.questionedCell);
        else
            cell.setState(cell.getState() - Cell.closedCell);

        restCells--;

        if (!cell.isEmpty()) return;

        Position[] cellsArround = getPositionsAround(cellPos);

        for (Position posAround : cellsArround) {
            openCell(posAround);
        }
    }


    public void markCell(Position cellPos) {
        if (!cellExist(cellPos)) return;

        Cell cell = getCell(cellPos);

        if (cell.isFlaged()) {
            cell.setState(cell.getState() - 10); // to closed questioned cell
            flags--;
        } else if (cell.isOuestioned()) {
            cell.setState(cell.getState() - 10); // to closed unmarked cell
        } else if (!cell.isOpened()) {
            cell.setState(cell.getState() + 20); // to closed flaged cell
            flags++;
        }
    }


    public boolean checkWin() {
        return restCells == 0 && flags == mines;
    }
}
