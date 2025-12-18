package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {

    private PrintBattleLog printBattleLog;

    public SimulateBattleImpl() {}

    public SimulateBattleImpl(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }

    public void setPrintBattleLog(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        // Получаем списки юнитов один раз
        List<Unit> playerUnits = playerArmy.getUnits();
        List<Unit> computerUnits = computerArmy.getUnits();

        // Бой идет, пока в обеих армиях есть хоть кто-то живой
        while (hasAliveUnits(playerUnits) && hasAliveUnits(computerUnits)) {
            simulateRound(playerUnits, computerUnits);
        }
    }

    private void simulateRound(List<Unit> playerUnits, List<Unit> computerUnits) throws InterruptedException {
        List<Unit> allUnits = new ArrayList<>();

        // Добавляем только живых
        playerUnits.stream().filter(Unit::isAlive).forEach(allUnits::add);
        computerUnits.stream().filter(Unit::isAlive).forEach(allUnits::add);

        // Сортируем по атаке (от большего к меньшему)
        allUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

        for (Unit unit : allUnits) {
            if (!unit.isAlive()) continue;

            // Проверка: не победила ли одна из сторон прямо посреди раунда
            // Используем быстрые проверки
            if (!hasAliveUnits(playerUnits) || !hasAliveUnits(computerUnits)) {
                return;
            }

            // Юнит атакует
            Unit target = unit.getProgram().attack();

            // Важно: проверяем, что цель существует, прежде чем логировать
            if (printBattleLog != null && target != null) {
                printBattleLog.printBattleLog(unit, target);
            }

            Thread.sleep(50);
        }
    }

    // Оптимизированная проверка: используем List напрямую
    private boolean hasAliveUnits(List<Unit> units) {
        for (Unit unit : units) {
            if (unit.isAlive()) return true;
        }
        return false;
    }
}