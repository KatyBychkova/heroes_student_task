package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[] DX = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] DY = {-1, 0, 1, -1, 1, -1, 0, 1};

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();

        // Быстрая проверка препятствий через массив
        boolean[][] isOccupied = new boolean[WIDTH][HEIGHT];
        for (Unit unit : existingUnitList) {
            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                int ux = unit.getxCoordinate();
                int uy = unit.getyCoordinate();
                if (isValid(ux, uy)) {
                    isOccupied[ux][uy] = true;
                }
            }
        }

        // Храним посещенные узлы в массиве для мгновенного доступа
        Node[][] visited = new Node[WIDTH][HEIGHT];
        Queue<Node> queue = new LinkedList<>();

        Node startNode = new Node(startX, startY, null);
        queue.add(startNode);
        visited[startX][startY] = startNode;

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.x == targetX && current.y == targetY) {
                return reconstructPath(current);
            }

            for (int i = 0; i < 8; i++) {
                int nextX = current.x + DX[i];
                int nextY = current.y + DY[i];

                if (isValid(nextX, nextY) && !isOccupied[nextX][nextY] && visited[nextX][nextY] == null) {
                    Node neighbor = new Node(nextX, nextY, current);
                    visited[nextX][nextY] = neighbor;
                    queue.add(neighbor);
                }
            }
        }

        return new ArrayList<>(); // Путь не найден
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    private List<Edge> reconstructPath(Node target) {
        List<Edge> path = new ArrayList<>();
        Node current = target;
        while (current != null) {
            path.add(new Edge(current.x, current.y));
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

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