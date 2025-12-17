package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        // Проходим по каждому ряду (их 3)
        for (List<Unit> row : unitsByRow) {
            // Если атакуем левую армию (компьютер)
            if (isLeftArmyTarget) {
                // Проверяем юнитов слева направо
                for (int i = 0; i < row.size(); i++) {
                    Unit unit = row.get(i);
                    if (unit == null) continue;

                    // Проверяем, есть ли кто-то СЛЕВА от этого юнита
                    boolean hasUnitOnLeft = false;
                    for (int j = 0; j < i; j++) {
                        if (row.get(j) != null) {
                            hasUnitOnLeft = true;
                            break;
                        }
                    }

                    // Если СЛЕВА никого нет - можно атаковать
                    if (!hasUnitOnLeft) {
                        suitableUnits.add(unit);
                    }
                }
            } else {
                // Атакуем правую армию (игрока)
                // Проверяем юнитов справа налево
                for (int i = 0; i < row.size(); i++) {
                    Unit unit = row.get(i);
                    if (unit == null) continue;

                    // Проверяем, есть ли кто-то СПРАВА от этого юнита
                    boolean hasUnitOnRight = false;
                    for (int j = i + 1; j < row.size(); j++) {
                        if (row.get(j) != null) {
                            hasUnitOnRight = true;
                            break;
                        }
                    }

                    // Если СПРАВА никого нет - можно атаковать
                    if (!hasUnitOnRight) {
                        suitableUnits.add(unit);
                    }
                }
            }
        }

        return suitableUnits;
    }
}