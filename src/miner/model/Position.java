package miner.model;

/**
 * Позиция ячейки в сетке
 * Существует ли клетка в данной позиции - проверяется
 * непосредственно в методах.
 */
public class Position {

    private int col;
    private int row;

    public Position(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
