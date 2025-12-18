package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        for (List<Unit> row : unitsByRow) {
            if (isLeftArmyTarget) {
                // Ищем самого левого (первого не-null) юнита в ряду
                for (int i = 0; i < row.size(); i++) {
                    Unit unit = row.get(i);
                    if (unit != null && unit.isAlive()) {
                        suitableUnits.add(unit);
                        break; // Как только нашли первого — этот ряд обработан
                    }
                }
            } else {
                // Ищем самого правого (последнего не-null) юнита в ряду
                for (int i = row.size() - 1; i >= 0; i--) {
                    Unit unit = row.get(i);
                    if (unit != null && unit.isAlive()) {
                        suitableUnits.add(unit);
                        break; // Как только нашли первого с края — выходим
                    }
                }
            }
        }

        return suitableUnits;
    }
}