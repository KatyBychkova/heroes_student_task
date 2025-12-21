package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    // Ограничение: максимум 11 юнитов одного типа
    private static final int MAX_UNITS_PER_TYPE = 11;
    // Размеры поля (ширина 27, высота 21)
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        // Создаем пустую армию для компьютера
        Army computerArmy = new Army();
        List<Unit> selectedUnits = new ArrayList<>();
        int currentPoints = 0;

        // Сортируем юнитов по эффективности:
        // (Атака + Здоровье) / Стоимость.
        // Самые выгодные будут в начале списка.
        unitList.sort(Comparator.comparingDouble(
                unit -> -((double) (unit.getBaseAttack() + unit.getHealth()) / unit.getCost())
        ));

        // Сет занятых клеток, чтобы юниты не накладывались друг на друга
        Set<String> occupiedCoords = new HashSet<>();
        Random random = new Random();

        // Проходим по каждому типу доступных юнитов
        for (Unit unit : unitList) {
            int unitsAdded = 0;

            // Добавляем юнитов, пока не достигнем лимита (11 шт) или не кончатся очки
            while (unitsAdded < MAX_UNITS_PER_TYPE && (currentPoints + unit.getCost()) <= maxPoints) {
                int x = -1, y = -1;
                boolean placeFound = false;

                // Пытаемся найти свободную клетку (делаем 100 попыток, чтобы не зависнуть)
                for (int i = 0; i < 100; i++) {
                    // ИСПРАВЛЕНИЕ ЗДЕСЬ:
                    // Компьютер должен быть СЛЕВА.
                    // random.nextInt(3) выдаст 0, 1 или 2.
                    // Мы убрали "+ 24", так как 24-26 — это ваша зона.
                    x = random.nextInt(3);
                    y = random.nextInt(HEIGHT); // Y от 0 до 20

                    // Проверяем, свободна ли клетка
                    if (!occupiedCoords.contains(x + "," + y)) {
                        occupiedCoords.add(x + "," + y);
                        placeFound = true;
                        break;
                    }
                }

                // Если место нашлось — создаем юнита и добавляем в список
                if (placeFound) {
                    Unit newUnit = new Unit(
                            unit.getUnitType() + " " + unitsAdded, // Имя (напр. Archer 0)
                            unit.getUnitType(),
                            unit.getHealth(),
                            unit.getBaseAttack(),
                            unit.getCost(),
                            unit.getAttackType(),
                            unit.getAttackBonuses(),
                            unit.getDefenceBonuses(),
                            x, y // Координаты
                    );
                    selectedUnits.add(newUnit);
                    currentPoints += unit.getCost();
                    unitsAdded++;
                } else {
                    // Если места на поле (в зоне 0-2) больше нет, прекращаем добавлять этот тип
                    break;
                }
            }
        }

        // Сохраняем и возвращаем готовую армию
        computerArmy.setUnits(selectedUnits);
        computerArmy.setPoints(currentPoints);
        return computerArmy;
    }
}