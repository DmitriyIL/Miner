package miner.model;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Field {

    private int width, height, mines, flags;
    private Cell[][] cellsMatrix;
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
        return cellsMatrix[width][height];
    }

    public Cell getCell(Position pos) {
        return cellsMatrix[pos.col][pos.row];
    }

    public Cell[][] getCellsMatrix() {
        return cellsMatrix;
    }

    private void createField() {
        cellsMatrix = new Cell[width][height];

        Random rand = new Random();
        byte mined = (byte) (Cell.closedCell + Cell.minedCell);
        List<Position> restPositions = new LinkedList<>();

        for (int rowPos = 0; rowPos < height; rowPos++)
            for (int colPos = 0; colPos < width; colPos++)
                restPositions.add(new Position(colPos, rowPos));

        // set mines
        for (int countMines = 0; countMines < mines; countMines++) {
            int index = rand.nextInt(restPositions.size());
            Position cellPos = restPositions.get(index);
            cellsMatrix[cellPos.col][cellPos.row] = new Cell(mined);
            restPositions.remove(index);
        }

        // set other cells
        for (Position cellPos : restPositions) {
            byte minesAround = countMinesAroundCell(cellPos);
            cellsMatrix[cellPos.col][cellPos.row] = new Cell(Cell.closedCell + minesAround);
        }
    }


    public byte countMinesAroundCell(Position cellPos) {
        byte result = 0;
        Position[] positionsAround = getPositionsAround(cellPos);

        for (Position posAround : positionsAround) {
            if (cellExist(posAround)
                    && cellsMatrix[posAround.col][posAround.row] != null
                    && cellsMatrix[posAround.col][posAround.row].isMined())
                result++;
        }

        return result;
    }


    private boolean cellExist(Position cellPos) {
        return cellPos.col >= 0 && cellPos.col < width && cellPos.row >= 0 && cellPos.row < height;
    }


    private Position[] getPositionsAround(Position cellPos) {

        Position positionsAround[];

        // even rows
        if (cellPos.row % 2 == 0) {
            positionsAround = new Position[]{
                    new Position(cellPos.col, cellPos.row - 1), new Position(cellPos.col - 1, cellPos.row - 1),
                    new Position(cellPos.col + 1, cellPos.row), new Position(cellPos.col - 1, cellPos.row + 1),
                    new Position(cellPos.col, cellPos.row + 1), new Position(cellPos.col - 1, cellPos.row)
            };
        }

        // odd rows
        else {
            positionsAround = new Position[]{
                    new Position(cellPos.col, cellPos.row - 1), new Position(cellPos.col + 1, cellPos.row - 1),
                    new Position(cellPos.col + 1, cellPos.row), new Position(cellPos.col + 1, cellPos.row + 1),
                    new Position(cellPos.col, cellPos.row + 1), new Position(cellPos.col - 1, cellPos.row)
            };
        }

        return positionsAround;
    }


    void openCell(Position cellPos) {
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


    void markCell(Position cellPos) {
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


    boolean checkWin() {
        return restCells == 0 && flags == mines;
    }
}
