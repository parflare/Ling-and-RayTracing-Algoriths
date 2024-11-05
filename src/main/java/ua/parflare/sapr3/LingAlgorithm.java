package ua.parflare.sapr3;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

import static ua.parflare.sapr3.AlgorithmRunner.charArrayToString;

public class LingAlgorithm extends Algorithm {

    public LingAlgorithm(Schema schema, ArrayList<String> connections) {
        super(schema, connections);
    }

    protected LinkedList<Point> doSteps(Schema schema, String connectionName) {
        Point first = AlgorithmRunner.cellsHash.get(connectionName.charAt(0));
        Point second = AlgorithmRunner.cellsHash.get(connectionName.charAt(1));

        Schema tmpSchema = schema.getSchemaCopy();
        Cell[][] tmpCells1 = tmpSchema.getCells();

        int connectResponseCode = 0;
        boolean isFirstIteration = true;

        int stepNumber = 1;

        int sameCells = -1;

        while (sameCells != 0) {
            Cell[][] tmpCells = tmpSchema.getCells();

            if (isFirstIteration) {
                connectResponseCode = doLingStep(tmpCells, tmpCells1, first.x, first.y, String.valueOf(connectionName.charAt(1)), stepNumber);
            } else {
                String cellToFind = String.valueOf(stepNumber - 1);
                sameCells = 0;

                for (int i = 0; i < tmpCells.length; i++) {
                    for (int j = 0; j < tmpCells[i].length; j++) {
                        Cell cell = tmpCells[i][j];
                        if (Objects.equals(cell.getValue(), cellToFind)) {
                            sameCells++;
                        }
                    }
                }

                for (int i = 0; i < tmpCells.length && sameCells > 0 && connectResponseCode != 1; i++) {
                    for (int j = 0; j < tmpCells[i].length && sameCells > 0 && connectResponseCode != 1; j++) {
                        Cell cell = tmpCells[i][j];
                        if (Objects.equals(cell.getValue(), cellToFind)) {
                            connectResponseCode = doLingStep(tmpCells, tmpCells1, i, j, String.valueOf(connectionName.charAt(1)), stepNumber);
                            if (connectResponseCode == -1) {
                                sameCells--;
                            }
                        }
                    }
                }
            }

            if (connectResponseCode == 0 || sameCells > 0) {
                System.out.println("step " + (stepNumber));
                tmpSchema.setCells(tmpCells);
                tmpCells1 = tmpSchema.getCells();
                System.out.println(charArrayToString(tmpSchema.getSchemaInArray()));
            }
            // Якщо досягли кінцевої точки або нема варіантів для руху
            if (connectResponseCode == 1 || (sameCells == 0 && !isFirstIteration)) {
                break;
            }

            isFirstIteration = false;
            stepNumber++;
        }


        if (connectResponseCode == -1 && sameCells == 0) {
            return null;
        }

        LinkedList<Point> path = findBackWay(tmpSchema.getCells(), first, second, stepNumber);

        System.out.println();
        for (int i = 0; i < path.size(); i++) {
            System.out.println("\n" + path.get(i).toString());
        }

        return path;
    }

    private int doLingStep(Cell[][] schema, Cell[][] tmpSchema, int row, int col, String second, int stepNumber) {
        String currentChar;

        boolean notTriggered = true;

        if (row + 1 < tmpSchema.length) {
            currentChar = tmpSchema[row + 1][col].getValue();

            System.out.print(currentChar);

            if (currentChar.equals("0")) {
                notTriggered = false;

                schema[row + 1][col].setValue("" + stepNumber);
            } else if (currentChar.equals(second)) {
                return 1;
            }
        }
        if (row > 0) {
            currentChar = tmpSchema[row - 1][col].getValue();
            System.out.print(currentChar);

            if (currentChar.equals("0")) {
                notTriggered = false;

                schema[row - 1][col].setValue("" + stepNumber);
            } else if (currentChar.equals(second)) {
                return 1;
            }
        }

        if (col + 1 < tmpSchema[0].length) {
            currentChar = tmpSchema[row][col + 1].getValue();
            System.out.print(currentChar);

            if (currentChar.equals("0")) {
                notTriggered = false;

                schema[row][col + 1].setValue("" + stepNumber);
            } else if (currentChar.equals(second)) {
                return 1;
            }
        }

        if (col > 0) {
            currentChar = tmpSchema[row][col - 1].getValue();
            System.out.print(currentChar);

            if (currentChar.equals("0")) {
                notTriggered = false;

                schema[row][col - 1].setValue("" + stepNumber);
            } else if (currentChar.equals(second)) {
                return 1;
            }
        }
        System.out.println();

        if (notTriggered) {
            return -1;
        } else {
            return 0;
        }
    }


}
