package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.UnitTargetPathFinder;
import com.battle.heroes.army.model.Edge;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    // 8 направлений движения (включая диагонали)
    private static final int[] DX = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] DY = {-1, 0, 1, -1, 1, -1, 0, 1};

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        int startX = attackUnit.getX();
        int startY = attackUnit.getY();
        int targetX = targetUnit.getX();
        int targetY = targetUnit.getY();

        // Создаём множество занятых клеток (препятствия)
        Set<String> obstacles = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                obstacles.add(unit.getX() + "," + unit.getY());
            }
        }

        // BFS для поиска пути
        Queue<Node> queue = new LinkedList<>();
        Map<String, Node> visited = new HashMap<>();

        Node start = new Node(startX, startY, null);
        queue.add(start);
        visited.put(startX + "," + startY, start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Если достигли цели
            if (current.x == targetX && current.y == targetY) {
                return reconstructPath(current);
            }

            // Проверяем всех соседей (8 направлений)
            for (int i = 0; i < 8; i++) {
                int newX = current.x + DX[i];
                int newY = current.y + DY[i];
                String key = newX + "," + newY;

                // Проверяем, что клетка валидна
                if (isValid(newX, newY) && !obstacles.contains(key) && !visited.containsKey(key)) {
                    Node neighbor = new Node(newX, newY, current);
                    queue.add(neighbor);
                    visited.put(key, neighbor);
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
    private List<Edge> reconstructPath(Node target) {
        List<Edge> path = new ArrayList<>();
        Node current = target;

        while (current != null) {
            path.add(new Edge(current.x, current.y));
            current = current.parent;
        }

        // Разворачиваем путь (от старта к цели)
        Collections.reverse(path);
        return path;
    }

    // Вспомогательный класс для узла в BFS
    private static class Node {
        int x, y;
        Node parent;

        Node(int x, int y, Node parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }
    }
}