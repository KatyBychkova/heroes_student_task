package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {

    private PrintBattleLog printBattleLog;

    public SimulateBattleImpl(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        // Бой идёт, пока в обеих армиях есть живые юниты
        while (hasAliveUnits(playerArmy) && hasAliveUnits(computerArmy)) {
            // Начинаем новый раунд
            simulateRound(playerArmy, computerArmy);
        }
    }

    // Симуляция одного раунда боя
    private void simulateRound(Army playerArmy, Army computerArmy) throws InterruptedException {
        // Собираем всех живых юнитов из обеих армий
        List<Unit> allUnits = new ArrayList<>();

        for (Unit unit : playerArmy.getUnits()) {
            if (unit.isAlive()) {
                allUnits.add(unit);
            }
        }

        for (Unit unit : computerArmy.getUnits()) {
            if (unit.isAlive()) {
                allUnits.add(unit);
            }
        }

        // Сортируем по убыванию атаки (сильнейшие ходят первыми)
        allUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

        // Каждый юнит делает ход
        for (Unit unit : allUnits) {
            // Проверяем, что юнит всё ещё жив
            if (!unit.isAlive()) {
                continue;
            }

            // Проверяем, что противник ещё жив
            if (!hasAliveUnits(playerArmy) || !hasAliveUnits(computerArmy)) {
                break;
            }

            // Юнит атакует (вызываем его программу)
            Unit target = unit.getProgram().attack();

            // Логируем атаку
            if (printBattleLog != null) {
                printBattleLog.printBattleLog(unit, target);
            }

            // Небольшая задержка для визуализации
            Thread.sleep(50);
        }
    }

    // Проверка, есть ли в армии живые юниты
    private boolean hasAliveUnits(Army army) {
        for (Unit unit : army.getUnits()) {
            if (unit.isAlive()) {
                return true;
            }
        }
        return false;
    }
}