package ua.parflare.sapr3.algorithms.impl;

import ua.parflare.sapr3.AlgorithmRunner;
import ua.parflare.sapr3.algorithms.Algorithm;
import ua.parflare.sapr3.models.Cell;
import ua.parflare.sapr3.models.Schema;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

import static ua.parflare.sapr3.AlgorithmRunner.charArrayToString;

public class RayTracingAlgorithm extends Algorithm {

    public RayTracingAlgorithm(Schema schema, ArrayList<String> connections) {
        super(schema, connections);
    }

    @Override
    protected LinkedList<Point> doSteps(Schema schema, String connectionName) {
        Point first = AlgorithmRunner.cellsHash.get(connectionName.charAt(0)); //AB1
        Point second = AlgorithmRunner.cellsHash.get(connectionName.charAt(1));

        Point tmp = new Point(first.x, first.y);
        Schema tmpSchema = schema.getSchemaCopy();
        Cell[][] tmpCells1 = tmpSchema.getCells();

        int connectResponseCode = 0;
        int stepNumber = 1;

        while (stepNumber < 30 && !tmp.equals(second) && connectResponseCode != -1) {
            Cell[][] tmpCells = tmpSchema.getCells();

            connectResponseCode = doRayStep(tmpCells, tmpCells1, tmp, second, stepNumber, String.valueOf(connectionName.charAt(1)));

            if (connectResponseCode == 0) {
                //System.out.println("step " + stepNumber);
                tmpSchema.setCells(tmpCells); // зберігаємо стан
                tmpCells1 = tmpSchema.getCells(); // оновлюємо стан
                //System.out.println(charArrayToString(tmpSchema.getSchemaInArray()));
            }

            if (connectResponseCode == 1) {
                break;
            }

            stepNumber++;
        }

        if (!tmp.equals(second) || stepNumber >= 30) {
            return null;
        }


        LinkedList<Point> path = findBackWay(tmpSchema.getCells(), first, second, stepNumber);

        //System.out.println();
        for (int i = 0; i < path.size(); i++) {
            //System.out.println(path.get(i).toString());
        }

        return path;
    }

    private int doRayStep(Cell[][] schema, Cell[][] tmpSchema, Point first, Point second, int stepNumber, String ch) {
        int firstYDirection = Integer.compare(second.y, first.y); // Рух по вертикалі
        int firstXDirection = Integer.compare(second.x, first.x); // Рух по горизонталі

        boolean canMoveY = false;
        boolean canMoveX = false;

        // Перевіряємо можливість руху по вертикалі
        if (first.y + firstYDirection >= 0 && first.y + firstYDirection < schema[0].length) {
            String nextYValue = tmpSchema[first.x][first.y + firstYDirection].getValue();
            canMoveY = Objects.equals(nextYValue, "0") || Objects.equals(nextYValue, ch);
            if (canMoveY) {
                first.y += firstYDirection; // Змінюємо позицію по вертикалі
            }
        }

        // Якщо по вертикалі не вдалося рухатись, перевіряємо рух по горизонталі
        if (!canMoveY) {
            if (first.x + firstXDirection >= 0 && first.x + firstXDirection < schema.length) {
                String nextXValue = tmpSchema[first.x + firstXDirection][first.y].getValue();
                canMoveX = Objects.equals(nextXValue, "0") || Objects.equals(nextXValue, ch);
                if (canMoveX) {
                    first.x += firstXDirection; // Змінюємо позицію по горизонталі
                }
            }
        }

        if (first.equals(second)) {
            return 1;
        }

        // Якщо не вдалося рухатися ні по вертикалі, ні по горизонталі – повертаємо -1
        if (!canMoveY && !canMoveX) {
            return -1;
        }

        // Оновлюємо значення на схемі, якщо не досягли кінцевої точки
        if (!first.equals(second)) {
            schema[first.x][first.y].setValue("" + stepNumber);
        }

        return 0;
    }

}
