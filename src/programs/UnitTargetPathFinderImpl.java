package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    // Размеры поля
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    // Возможные ходы: по горизонтали, вертикали и диагонали
    private static final int[] DX = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] DY = {-1, 0, 1, -1, 1, -1, 0, 1};

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        // Координаты начала и конца
        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();

        // Создаем массив занятых клеток, чтобы быстро проверять препятствия
        boolean[][] isOccupied = new boolean[WIDTH][HEIGHT];
        for (Unit unit : existingUnitList) {
            // Клетка занята, если там живой юнит, и это не мы сами и не наша цель
            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                isOccupied[unit.getxCoordinate()][unit.getyCoordinate()] = true;
            }
        }

        // Очередь для BFS и массив посещенных клеток (храним там предков для восстановления пути)
        Queue<Edge> queue = new LinkedList<>();
        Edge[][] cameFrom = new Edge[WIDTH][HEIGHT]; // Хранит "откуда мы пришли" в эту клетку

        // Добавляем стартовую точку
        queue.add(new Edge(startX, startY));
        cameFrom[startX][startY] = new Edge(startX, startY); // Стартовая точка ссылается сама на себя

        while (!queue.isEmpty()) {
            Edge current = queue.poll();

            // Если пришли к цели — восстанавливаем путь
            if (current.getX() == targetX && current.getY() == targetY) {
                return reconstructPath(cameFrom, current);
            }

            // Проверяем всех соседей
            for (int i = 0; i < 8; i++) {
                int nextX = current.getX() + DX[i];
                int nextY = current.getY() + DY[i];

                // Проверяем границы поля, препятствия и то, что мы там еще не были
                if (isValid(nextX, nextY) && !isOccupied[nextX][nextY] && cameFrom[nextX][nextY] == null) {
                    queue.add(new Edge(nextX, nextY));
                    cameFrom[nextX][nextY] = current; // Запоминаем, откуда пришли
                }
            }
        }

        // Если путь не найден
        return new ArrayList<>();
    }

    // Проверка, что координаты внутри поля
    private boolean isValid(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    // Восстановление пути от цели к старту
    private List<Edge> reconstructPath(Edge[][] cameFrom, Edge end) {
        List<Edge> path = new ArrayList<>();
        Edge curr = end;

        // Идем назад по ссылкам, пока не дойдем до старта
        while (curr.getX() != cameFrom[curr.getX()][curr.getY()].getX() ||
                curr.getY() != cameFrom[curr.getX()][curr.getY()].getY()) {
            path.add(curr);
            curr = cameFrom[curr.getX()][curr.getY()];
        }
        // Добавляем старт
        path.add(curr);

        // Разворачиваем список, чтобы путь был от начала к концу
        Collections.reverse(path);
        return path;
    }
}