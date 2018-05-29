package Miner.model;

public class Cell {
    public static final int openedCell = 0;
    public static final int closedCell = 10;
    public static final int questionedCell = 20;
    public static final int flagedCell = 30;

    public static final int emptyCell = 0;
    public static final int blastedCell = 8;
    public static final int minedCell = 9;

    private byte state;

    public Cell(byte state) {
        this.state = state;
    }

    public Cell(int state) {
        this.state = (byte) state;
    }

    public byte getState() {
        return state;
    }

    public void setState(int state) {
        this.state = (byte) state;
    }

    public boolean isEmpty() {
        return state == emptyCell;
    }

    public boolean isMined() {
        return state % 10 == minedCell;
    }


    public boolean isOpened() {
        return state / 10 == openedCell;
    }

    public boolean isOuestioned() {
        return state / 10 == questionedCell / 10;
    }

    public boolean isFlaged() {
        return state / 10 == flagedCell / 10;
    }

    public boolean isBlasted() {
        return state == blastedCell;
    }

}
