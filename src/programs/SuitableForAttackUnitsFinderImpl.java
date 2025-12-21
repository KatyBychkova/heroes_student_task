package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        // Проходим по каждому ряду поля
        for (List<Unit> row : unitsByRow) {
            if (isLeftArmyTarget) {
                // Если атакуем левую армию (компьютер бьет нас)
                // Нужно найти самого левого живого юнита, так как остальные закрыты
                for (int i = 0; i < row.size(); i++) {
                    Unit unit = row.get(i);
                    // Проверяем, что юнит есть и он жив
                    if (unit != null && unit.isAlive()) {
                        suitableUnits.add(unit);
                        break; // Нашли первого — выходим, остальные за ним спрятаны
                    }
                }
            } else {
                // Если атакуем правую армию (мы бьем компьютер)
                // Идем справа налево, ищем крайнего
                for (int i = row.size() - 1; i >= 0; i--) {
                    Unit unit = row.get(i);
                    if (unit != null && unit.isAlive()) {
                        suitableUnits.add(unit);
                        break; // Нашли крайнего справа — добавляем и переходим к след. ряду
                    }
                }
            }
        }
        return suitableUnits;
    }
}