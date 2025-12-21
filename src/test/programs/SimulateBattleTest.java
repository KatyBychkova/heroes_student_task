package programs;

import com.battle.heroes.army.Army;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class SimulateBattleTest {

    @Test
    void testSimulateExecutes() {
        SimulateBattleImpl simulator = new SimulateBattleImpl();

        // Создаем две пустые армии. Бой должен сразу закончиться.
        Army army1 = new Army();
        army1.setUnits(new ArrayList<>());
        Army army2 = new Army();
        army2.setUnits(new ArrayList<>());

        // Проверяем, что вызов метода не вызывает ошибок (Exception)
        assertDoesNotThrow(() -> simulator.simulate(army1, army2));
    }
}