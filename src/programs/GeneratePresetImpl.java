package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    private static final int MAX_UNITS_PER_TYPE = 11;
    private static final int FIELD_WIDTH = 27;
    private static final int FIELD_HEIGHT = 21;

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        Army computerArmy = new Army();
        List<Unit> selectedUnits = new ArrayList<>();
        int currentPoints = 0;

        // Сортируем по эффективности (атака + хп) / цена
        unitList.sort(Comparator.comparingDouble(
                u -> -((double) (u.getBaseAttack() + u.getHealth()) / u.getCost())
        ));

        Set<String> occupiedCoords = new HashSet<>();
        Random random = new Random();

        for (Unit unit : unitList) {
            int unitsAdded = 0;
            while (unitsAdded < MAX_UNITS_PER_TYPE && (currentPoints + unit.getCost()) <= maxPoints) {
                int x, y;
                String key;
                int attempts = 0;

                // Ищем свободное место в зоне компьютера (X: 24-26)
                do {
                    x = random.nextInt(3) + 24;
                    y = random.nextInt(FIELD_HEIGHT);
                    key = x + "," + y;
                    attempts++;
                } while (occupiedCoords.contains(key) && attempts < 100);

                if (attempts >= 100) break;

                occupiedCoords.add(key);
                selectedUnits.add(createUnitCopy(unit, unitsAdded, x, y));
                currentPoints += unit.getCost();
                unitsAdded++;
            }
        }

        computerArmy.setUnits(selectedUnits);
        computerArmy.setPoints(currentPoints);
        return computerArmy;
    }

    private Unit createUnitCopy(Unit original, int index, int x, int y) {
        return new Unit(
                original.getUnitType() + " " + index,
                original.getUnitType(),
                original.getHealth(),
                original.getBaseAttack(),
                original.getCost(),
                original.getAttackType(),
                original.getAttackBonuses(),
                original.getDefenceBonuses(),
                x, y
        );
    }
}