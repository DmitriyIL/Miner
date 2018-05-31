import miner.model.*;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FieldTest {

    @Test
    public void cellsTest() {
        Game game = new Game(20, 15, 40);
        Field field = game.getField();
        Cell[][] cellsMatr = field.getCellsMatrix();

        for (int colPos = 0; colPos < cellsMatr.length; colPos++) {
            for (int rowPos = 0; rowPos < cellsMatr[0].length; rowPos++) {
                if (cellsMatr[colPos][rowPos].isMined()) continue;

                int expectedMinesAround = cellsMatr[colPos][rowPos].getState() % 10;
                int actualMinesAround = field.countMinesAroundCell(new Position(colPos, rowPos));

                assertEquals(expectedMinesAround, actualMinesAround);
            }
        }

    }


    @Test
    public void firstStepTest() {
        Game game = new Game(35, 20, 35 * 20 - 1);
        Point p = game.findCellCenter(0, 0);
        game.pressLeftButton(p.x, p.y);

        assertEquals(Game.GameStatus.ACTION, game.getGameState());

        Field field = game.getField();
        Cell[][] cellsMatr = field.getCellsMatrix();
        assertTrue(cellsMatr[0][0].isOpened());
    }


    @Test
    public void bombTest() {
        Game game = new Game(20, 15, 40);
        Field field = game.getField();
        Cell[][] cellsMatr = field.getCellsMatrix();

        assertEquals(Game.GameStatus.ACTION, game.getGameState());

        int bombsAmt = 0;
        for (Cell[] cellArr : cellsMatr) {
            for (Cell cell : cellArr) {
                if (cell.isMined()) bombsAmt++;
            }
        }
        assertEquals(40, bombsAmt);

        Position bombPos = findBomb(cellsMatr);
        Point bombCoords = game.findCellCenter(bombPos.getCol(), bombPos.getRow());
        game.pressLeftButton(bombCoords.x, bombCoords.y); // иммитируем нажатие на бомбу
        assertEquals(Game.GameStatus.LOSE, game.getGameState());
        assertTrue(cellsMatr[bombPos.getCol()][bombPos.getRow()].isBlasted()); //указано ли, что взорвана бомба
    }

    private Position findBomb(Cell[][] cellsMatr) {
        for (int colPos = 0; colPos < cellsMatr.length; colPos++) {
            for (int rowPos = 0; rowPos < cellsMatr[0].length; rowPos++) {
                if (cellsMatr[colPos][rowPos].isMined()) return new Position(colPos, rowPos);
            }
        }
        return null;
    }


    @Test
    public void winTest() {
        Game game = new Game(20, 15, 1); //создаем поле с 1 бомбой
        Field field = game.getField();
        Cell[][] cellsMatr = field.getCellsMatrix();

        Position bombPos = findBomb(cellsMatr);
        Point bombCoords = game.findCellCenter(bombPos.getCol(), bombPos.getRow());
        game.pressRightButton(bombCoords.x, bombCoords.y); // иммитируем флагирование бомбы

        assertTrue(cellsMatr[bombPos.getCol()][bombPos.getRow()].isFlaged()); //по пути проверяем флагирование

        Position emptyCellPos = (bombPos.getCol() + 2 < 20) ?       //отходим на 2 клетки от бомбы, чтобы нажатием
                new Position(bombPos.getCol() + 2, bombPos.getRow()) :  //открыть все клетки кроме флажка
                new Position(bombPos.getCol() - 2, bombPos.getRow());
        Point emptyCellCoords = game.findCellCenter(emptyCellPos.getCol(), emptyCellPos.getRow());
        game.pressLeftButton(emptyCellCoords.x, emptyCellCoords.y); //тыкаем на путсую клетку

        assertEquals(Game.GameStatus.WIN, game.getGameState());
    }


    @Test
    public void qustionedTest() {
        Game game = new Game(20, 15, 40);
        Field field = game.getField();
        Cell[][] cellsMatr = field.getCellsMatrix();

        Point cellCoords = game.findCellCenter(0, 0);

        game.pressRightButton(cellCoords.x, cellCoords.y);
        game.pressRightButton(cellCoords.x, cellCoords.y);

        assertTrue(cellsMatr[0][0].isOuestioned());

        game.pressLeftButton(cellCoords.x, cellCoords.y); // должна открыться

        assertTrue(cellsMatr[0][0].isOpened());
    }
}
