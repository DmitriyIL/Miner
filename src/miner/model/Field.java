package miner.model;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * Класс управляющий состоянием поля
 */
public class Field {

    public static int cols, rows;
    private int mines, flags;
    private Cell[][] cellsMatrix;
    private int restCells;


    /**
     * Конструктор создает поле с рандомным расположением бомб.
     *
     * @param colsAmt  - кол-во столбцов на поле.
     * @param rowsAmt  - кол-во рядов на поле.
     * @param minesAmt - кол-во мин на поле.
     */
    public Field(int colsAmt, int rowsAmt, int minesAmt) {
        cols = colsAmt;
        rows = rowsAmt;
        this.mines = minesAmt;

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
        return cellsMatrix[pos.getCol()][pos.getRow()];
    }


    /**
     * @return матрицу хранящую состояние поля.
     */
    public Cell[][] getCellsMatrix() {
        return cellsMatrix;
    }


    /**
     * Создает поле со всеми закрытыми ячейками.
     */
    private void createField() {
        cellsMatrix = new Cell[cols][rows];

        for (int rowPos = 0; rowPos < rows; rowPos++)
            for (int colPos = 0; colPos < cols; colPos++)
                cellsMatrix[colPos][rowPos] = new Cell(Cell.closedCell);
    }


    /**
     * Установка бомб
     *
     * Алгоритм:
     *
     * Создается масссив из всех позиций restPosition.
     *
     * Далее выбирает cлучайная позиция из restPosition,
     * в нее устанавливаетсся мина,
     * потом эта позиция удаляется из restPosition,
     * это повторяется пока не будет полученно нужное кол-во бомб.
     *
     * Далее для всех позиций в restPosition считается кол-во бомб вокруг.
     * @param pressedPos - нажатая клетка, в ней не может быть бомбы.
     */
    public void createBombs(Position pressedPos) {
        Random rand = new Random();
        List<Position> restPositions = new LinkedList<>();

        for (int rowPos = 0; rowPos < rows; rowPos++) {
            for (int colPos = 0; colPos < cols; colPos++) {
                restPositions.add(new Position(colPos, rowPos));
            }
        }

        restPositions.remove(pressedPos.getRow() * cols + pressedPos.getCol());

        // set mines
        for (int countMines = 0; countMines < mines; countMines++) {
            int index = rand.nextInt(restPositions.size());
            Position cellPos = restPositions.get(index);

            Cell cell = cellsMatrix[cellPos.getCol()][cellPos.getRow()];
            cell.setState(cell.getState() + Cell.minedCell);

            restPositions.remove(index);
        }

        // set other cells
        restPositions.add(pressedPos);
        for (Position cellPos : restPositions) {
            byte minesAround = countMinesAroundCell(cellPos);
            Cell cell = cellsMatrix[cellPos.getCol()][cellPos.getRow()];
            cell.setState(cell.getState() + minesAround);
        }
    }


    /**
     * Считает кол-во бомб вокруг клетки
     *
     * @param cellPos - позиция клетки
     */
    public byte countMinesAroundCell(Position cellPos) {
        byte result = 0;
        Position[] positionsAround = getPositionsAround(cellPos);

        for (Position posAround : positionsAround) {
            if (cellExist(posAround, cols, rows)
                    && cellsMatrix[posAround.getCol()][posAround.getRow()] != null
                    && cellsMatrix[posAround.getCol()][posAround.getRow()].isMined())
                result++;
        }

        return result;
    }


    /**
     * Проверка на существование клетки на позиции cellPos.
     *
     * @param cellPos - позиция клетки
     */
    public static boolean cellExist(Position cellPos, int colsAmt, int rowsAmt) {
        return cellPos.getCol() >= 0 && cellPos.getCol() < colsAmt &&
                cellPos.getRow() >= 0 && cellPos.getRow() < rowsAmt;
    }


    /**
     * Получает массив позиций вокруг позиции cellPos,
     * при этом несуществующие позиции не отсеиваются,
     * они отсеиваются в countMinesAroundCell().
     *
     * @param cellPos - позиция клетки
     */
    private Position[] getPositionsAround(Position cellPos) {

        Position positionsAround[];

        // в случае четного ряда
        if (cellPos.getRow() % 2 == 0) {
            positionsAround = new Position[]{
                    new Position(cellPos.getCol(), cellPos.getRow() - 1),
                    new Position(cellPos.getCol() - 1, cellPos.getRow() - 1),
                    new Position(cellPos.getCol() + 1, cellPos.getRow()),
                    new Position(cellPos.getCol() - 1, cellPos.getRow() + 1),
                    new Position(cellPos.getCol(), cellPos.getRow() + 1),
                    new Position(cellPos.getCol() - 1, cellPos.getRow())
            };
        }

        // в случае нечетного ряда
        else {
            positionsAround = new Position[]{
                    new Position(cellPos.getCol(), cellPos.getRow() - 1),
                    new Position(cellPos.getCol() + 1, cellPos.getRow() - 1),
                    new Position(cellPos.getCol() + 1, cellPos.getRow()),
                    new Position(cellPos.getCol() + 1, cellPos.getRow() + 1),
                    new Position(cellPos.getCol(), cellPos.getRow() + 1),
                    new Position(cellPos.getCol() - 1, cellPos.getRow())
            };
        }

        return positionsAround;
    }


    /**
     * Открывает клетку, при этом, если клетка пустая,
     * то рекурсией открываются соседние.
     *
     * @param cellPos - позиция клетки
     */
    public void openCell(Position cellPos) {
        if (!cellExist(cellPos, cols, rows)) return;

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
     *
     * @param cellPos - позиция клетки
     */
    public void markCell(Position cellPos) {
        if (!cellExist(cellPos, cols, rows)) return;

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
    public boolean checkWin() {
        return restCells == 0 && flags == mines;
    }
}
