package miner.model;

public class Cell {
    static final int closedCell = 10;
    static final int questionedCell = 20;
    static final int flagedCell = 30;

    static final int blastedCell = 8;
    static final int minedCell = 9;

    private byte state;


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
        return state == 0;
    }

    public boolean isMined() {
        return state % 10 == minedCell;
    }


    public boolean isOpened() {
        return state < closedCell;
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
