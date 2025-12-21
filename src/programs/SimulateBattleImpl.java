package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.*;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        // Получаем списки юнитов
        List<Unit> playerUnits = playerArmy.getUnits();
        List<Unit> computerUnits = computerArmy.getUnits();

        // Бой идет, пока в обеих армиях есть хотя бы один живой юнит
        while (hasAliveUnits(playerUnits) && hasAliveUnits(computerUnits)) {
            // Собираем всех живых в одну кучу, чтобы определить очередность хода
            List<Unit> allUnits = new ArrayList<>();
            allUnits.addAll(playerUnits);
            allUnits.addAll(computerUnits);

            // Сортируем по атаке: у кого больше, тот ходит первым (инициатива)
            allUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            // Раунд боя
            for (Unit unit : allUnits) {
                // Если юнит убит до своего хода — пропускаем
                if (!unit.isAlive()) continue;

                // Атакуем. (логика атаки внутри метода program.attack)
                Unit target = unit.getProgram().attack();

                // Если есть логгер и атака прошла успешно — пишем лог
                if (printBattleLog != null && target != null) {
                    printBattleLog.printBattleLog(unit, target);
                }

                // Делаем паузу, чтобы бой не пролетел мгновенно (для визуализации)
                Thread.sleep(10);
            }
        }
    }

    // Вспомогательный метод проверки живых
    private boolean hasAliveUnits(List<Unit> units) {
        for (Unit unit : units) {
            if (unit.isAlive()) return true;
        }
        return false;
    }

    public void setPrintBattleLog(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }
}