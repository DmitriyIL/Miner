package miner.model;

/**
 * Позиция ячейки в сетке
 * Существует ли клетка в данной позиции - проверяется
 * непосредственно в методах.
 */
public class Position {

    public int col;
    public int row;

    public Position(int col, int row) {
        this.col = col;
        this.row = row;
    }
}
