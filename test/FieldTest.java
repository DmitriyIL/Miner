import miner.model.*;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FieldTest {
    @Test
    public void bombTest() {
        Game game = new Game(20, 15, 40);
        Field field = game.getField();
        Cell[][] cellsMatr = field.getCellsMatrix();

        assertEquals(Game.actionGameState, game.getGameState());

        int bombsAmt = 0;
        for (Cell[] cellArr: cellsMatr) {
            for (Cell cell: cellArr) {
                if (cell.isMined()) bombsAmt++;
            }
        }
        assertEquals(40, bombsAmt);

        Position bombPos = findBomb(cellsMatr);
        Point bombCoords = game.findCellCenter(bombPos.col, bombPos.row);
        game.pressLeftButton(bombCoords.x, bombCoords.y); // иммитируем нажатие на бомбу
        assertEquals(Game.loseGameState, game.getGameState());
        assertTrue(cellsMatr[bombPos.col][bombPos.row].isBlasted()); //указано ли, что взорвана бомба
    }

    private Position findBomb(Cell[][] cellsMatr) {
        for (int heightPos = 0; heightPos < cellsMatr.length; heightPos++) {
            for (int widthPos = 0; widthPos < cellsMatr[0].length; widthPos++) {
                if (cellsMatr[heightPos][widthPos].isMined()) return new Position(heightPos, widthPos);
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
        Point bombCoords = game.findCellCenter(bombPos.col, bombPos.row);
        game.pressRightButton(bombCoords.x, bombCoords.y); // иммитируем флагирование бомбы

        assertTrue(cellsMatr[bombPos.col][bombPos.row].isFlaged()); //по пути проверяем флагирование

        Position emptyCellPos = (bombPos.col + 2 < 20)?  // отходим на 2 клетки от бомбы, чтобы нажатием
                new Position(bombPos.col + 2, bombPos.row) : //открыть все клетки кроме флажка
                new Position(bombPos.col - 2, bombPos.row);
        Point emptyCellCoords = game.findCellCenter(emptyCellPos.col, emptyCellPos.row);
        game.pressLeftButton(emptyCellCoords.x, emptyCellCoords.y); //тыкаем на путсую клетку

        assertEquals(Game.winGameState, game.getGameState());
    }

}
