package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class GeneratePresetTest {

    @Test
    void testGeneratePresetLimits() {
        GeneratePresetImpl generator = new GeneratePresetImpl();

        // Создаем дешевого юнита (стоит 10 очков)
        Unit weakUnit = new Unit("Peasant", "Peasant", 10, 1, 10, "Melee", null, null, 0, 0);
        List<Unit> unitList = Collections.singletonList(weakUnit);

        // Даем много очков (10000). По идее можно купить 1000 юнитов, но лимит типа - 11.
        Army army = generator.generate(unitList, 10000);

        // Проверяем
        assertNotNull(army.getUnits());
        assertEquals(11, army.getUnits().size(), "Должно быть ровно 11 юнитов из-за лимита на тип");
        assertTrue(army.getPoints() <= 10000, "Очки не должны превышать бюджет");
    }
}