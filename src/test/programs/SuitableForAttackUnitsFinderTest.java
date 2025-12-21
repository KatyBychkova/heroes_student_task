package programs;

import com.battle.heroes.army.Unit;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class SuitableForAttackUnitsFinderTest {

    @Test
    void testGetSuitableUnits() {
        SuitableForAttackUnitsFinderImpl finder = new SuitableForAttackUnitsFinderImpl();

        // Создаем три юнита в ряд: Слева(0,0), Центр(1,0), Справа(2,0)
        Unit unitLeft = new Unit("Left", "T", 100, 10, 10, "M", null, null, 0, 0);
        Unit unitMid = new Unit("Mid", "T", 100, 10, 10, "M", null, null, 1, 0);
        Unit unitRight = new Unit("Right", "T", 100, 10, 10, "M", null, null, 2, 0);

        List<Unit> row = Arrays.asList(unitLeft, unitMid, unitRight);
        List<List<Unit>> unitsByRow = new ArrayList<>();
        unitsByRow.add(row);

        // Тест 1: Компьютер атакует (цель слева). Должен найтись только левый юнит.
        List<Unit> resultLeft = finder.getSuitableUnits(unitsByRow, true);
        assertEquals(1, resultLeft.size());
        assertEquals("Left", resultLeft.get(0).getName());

        // Тест 2: Игрок атакует (цель справа). Должен найтись только правый юнит.
        List<Unit> resultRight = finder.getSuitableUnits(unitsByRow, false);
        assertEquals(1, resultRight.size());
        assertEquals("Right", resultRight.get(0).getName());
    }
}