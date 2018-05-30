package miner.model;


/**
 * Класс, хранящий состояние клетки.
 * У состояния 2 слоя: нижний и верхний.
 *
 * В нижнем хранится то что скрыто в начале игры,
 * а именно - начилие мины и кол-во бомб вокруг. А также
 * состояние, когда бомба взорвана.
 *
 * В верхнем слое хранится - помечена ли бомба флагом или вопросиком, или
 * просто закрыта.
 */
public class Cell {
    public static final byte closedCell = 10;
    public static final byte questionedCell = 20;
    public static final byte flagedCell = 30;

    public static final byte blastedCell = 8;
    public static final byte minedCell = 9;

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
