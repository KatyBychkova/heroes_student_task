package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        List<Unit> selectedUnits = new ArrayList<>();
        int totalPoints = 0;

        // Создаём список с информацией о каждом типе юнита
        List<UnitInfo> unitInfos = new ArrayList<>();
        for (Unit unit : unitList) {
            double efficiency = (double) unit.getBaseAttack() / unit.getCost();
            unitInfos.add(new UnitInfo(unit, efficiency));
        }

        // Сортируем по эффективности (от большего к меньшему)
        unitInfos.sort((a, b) -> Double.compare(b.efficiency, a.efficiency));

        // Счётчики для каждого типа юнита (максимум 11)
        Map<String, Integer> unitCounts = new HashMap<>();
        for (Unit unit : unitList) {
            unitCounts.put(unit.getUnitType(), 0);
        }

        // Жадно набираем юнитов
        boolean added = true;
        while (added && totalPoints < maxPoints) {
            added = false;

            for (UnitInfo info : unitInfos) {
                Unit originalUnit = info.unit;
                String unitType = originalUnit.getUnitType();
                int count = unitCounts.get(unitType);

                // Проверяем ограничения
                if (count < 11 && totalPoints + originalUnit.getCost() <= maxPoints) {
                    // Создаём копию юнита
                    Unit newUnit = createUnitCopy(originalUnit, count);
                    selectedUnits.add(newUnit);

                    totalPoints += originalUnit.getCost();
                    unitCounts.put(unitType, count + 1);
                    added = true;
                }
            }
        }

        // Создаём и возвращаем армию
        Army army = new Army();
        army.setUnits(selectedUnits);
        army.setPoints(totalPoints);

        return army;
    }

    // Вспомогательный метод для создания копии юнита
    private Unit createUnitCopy(Unit original, int index) {
        Unit copy = new Unit();
        copy.setName(original.getUnitType() + "_" + index);
        copy.setUnitType(original.getUnitType());
        copy.setHealth(original.getHealth());
        copy.setBaseAttack(original.getBaseAttack());
        copy.setCost(original.getCost());
        copy.setAttackType(original.getAttackType());
        copy.setAlive(true);
        return copy;
    }

    // Вспомогательный класс для хранения информации о юните
    private static class UnitInfo {
        Unit unit;
        double efficiency;

        UnitInfo(Unit unit, double efficiency) {
            this.unit = unit;
            this.efficiency = efficiency;
        }
    }
}