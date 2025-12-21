package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class UnitTargetPathFinderTest {

    @Test
    void testPathFinding() {
        UnitTargetPathFinderImpl finder = new UnitTargetPathFinderImpl();

        // Юнит стоит в (0,0), хочет попасть в (0,2)
        Unit attacker = new Unit("Attacker", "T", 100, 10, 10, "M", null, null, 0, 0);
        Unit target = new Unit("Target", "T", 100, 10, 10, "M", null, null, 0, 2);

        List<Unit> obstacles = new ArrayList<>(); // Без препятствий

        List<Edge> path = finder.getTargetPath(attacker, target, obstacles);

        assertNotNull(path);
        assertFalse(path.isEmpty(), "Путь должен быть найден");
        // Путь начинается с координаты атакующего
        assertEquals(0, path.get(0).getX());
        assertEquals(0, path.get(0).getY());
    }
}