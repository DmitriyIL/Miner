package miner.model;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * Класс управляющий состоянием поля
 */
public class Field {

    private int cols, rows, mines, flags;
    private Cell[][] cellsMatrix;
    private int restCells;

    private Position pressedPos;

    /**
     * Конструктор создает поле с рандомным расположением бомб.
     * @param colsAmt - кол-во столбцов на поле.
     * @param rowsAmt - кол-во рядов на поле.
     * @param minesAmt - кол-во мин на поле.
     */
    public Field(int colsAmt, int rowsAmt, int minesAmt, Position pressedPos) {
        this.cols = colsAmt;
        this.rows = rowsAmt;
        this.mines = minesAmt;
        this.pressedPos = pressedPos;

        flags = 0;
        restCells = (colsAmt * rowsAmt) - minesAmt;
        createField();
    }


    /**
     * @return класс Cell по двум координатам.
     */
    public Cell getCell(int col, int row) {
        return cellsMatrix[col][row];
    }


    /**
     * @return класс Cell по классу Position.
     */
    public Cell getCell(Position pos) {
        return cellsMatrix[pos.col][pos.row];
    }


    /**
     * @return матрицу хранящую состояние поля.
     */
    public Cell[][] getCellsMatrix() {
        return cellsMatrix;
    }


    /**
     * Создает поле с рандомным расположением мин.
     *
     * Алгоритм:
     *
     * Создается масссив из всех позиций restPosition.
     *
     * Далее выбирает cлучайная позиция из restPosition,
     * в нее устанавливаетсся мина,
     * потом эта позиция удаляется из restPosition,
     * это повторяется пока не буде полученно нужное кол-во бомб.
     *
     * Далее для всех позиций в restPosition считается кол-во бомб вокруг.
     */
    private void createField() {
        cellsMatrix = new Cell[cols][rows];

        Random rand = new Random();
        byte mined = (byte) (Cell.closedCell + Cell.minedCell);
        List<Position> restPositions = new LinkedList<>();

        for (int rowPos = 0; rowPos < rows; rowPos++){
            for (int colPos = 0; colPos < cols; colPos++) {
                if (pressedPos.row == rowPos && pressedPos.col == colPos) continue;

                restPositions.add(new Position(colPos, rowPos));
            }
        }

        // set mines
        for (int countMines = 0; countMines < mines; countMines++) {
            int index = rand.nextInt(restPositions.size());
            Position cellPos = restPositions.get(index);
            cellsMatrix[cellPos.col][cellPos.row] = new Cell(mined);
            restPositions.remove(index);
        }

        // set other cells
        restPositions.add(pressedPos);
        for (Position cellPos : restPositions) {
            byte minesAround = countMinesAroundCell(cellPos);
            cellsMatrix[cellPos.col][cellPos.row] = new Cell(Cell.closedCell + minesAround);
        }
    }


    /**
     * Считает кол-во бомб вокруг клетки
     * @param cellPos - позиция клетки
     */
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


    /**
     * Проверка на существование клетки на позиции cellPos.
     * @param cellPos - позиция клетки
     */
    private boolean cellExist(Position cellPos) {
        return cellPos.col >= 0 && cellPos.col < cols && cellPos.row >= 0 && cellPos.row < rows;
    }


    /**
     * Получает массив позиций вокруг позиции cellPos,
     * при этом несуществующие позиции не отсеиваются,
     * они отсеиваются в countMinesAroundCell().
     * @param cellPos - позиция клетки
     */
    private Position[] getPositionsAround(Position cellPos) {

        Position positionsAround[];

        // в случае четного ряда
        if (cellPos.row % 2 == 0) {
            positionsAround = new Position[]{
                    new Position(cellPos.col, cellPos.row - 1), new Position(cellPos.col - 1, cellPos.row - 1),
                    new Position(cellPos.col + 1, cellPos.row), new Position(cellPos.col - 1, cellPos.row + 1),
                    new Position(cellPos.col, cellPos.row + 1), new Position(cellPos.col - 1, cellPos.row)
            };
        }

        // в случае нечетного ряда
        else {
            positionsAround = new Position[]{
                    new Position(cellPos.col, cellPos.row - 1), new Position(cellPos.col + 1, cellPos.row - 1),
                    new Position(cellPos.col + 1, cellPos.row), new Position(cellPos.col + 1, cellPos.row + 1),
                    new Position(cellPos.col, cellPos.row + 1), new Position(cellPos.col - 1, cellPos.row)
            };
        }

        return positionsAround;
    }


    /**
     * Открывает клетку, при этом, если клетка пустая,
     * то рекурсией открываются соседние.
     * @param cellPos - позиция клетки
     */
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


    /**
     * Помечает клетку флагом или вопросиком.
     * @param cellPos - позиция клетки
     */
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


    /**
     * Проверяет достигнуты ли условия для победы.
     */
    boolean checkWin() {
        return restCells == 0 && flags == mines;
    }
}
