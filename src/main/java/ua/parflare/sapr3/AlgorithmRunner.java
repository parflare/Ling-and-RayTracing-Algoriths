package ua.parflare.sapr3;

import java.awt.*;
import java.util.*;

public class AlgorithmRunner {

    private static int[][] connectionParts = new int[][]{
          // B  C  D  E  F  H
            {2, 0, 1, 1, 0, 1}, //A
               {2, 0, 0, 1, 0}, //B
                  {0, 0, 1, 0}, //C
                     {1, 0, 0}, //D
                        {1, 0}, //E
                           {1}, //F
    };

    private static ArrayList<char[]> cells = new ArrayList<>() {{
        add(new char[]{4, 3, 'A'});
        add(new char[]{2, 5, 'B'});
        add(new char[]{1, 1, 'C'});
        add(new char[]{8, 6, 'D'});
        add(new char[]{9, 1, 'E'});
        add(new char[]{5, 7, 'F'});
        add(new char[]{6, 2, 'H'});
    }};

    private static Schema originalSchema;
    private static ArrayList<String> connections;

    public static HashMap<Character, Point> cellsHash;

    public static final int SCHEMA_COLS = 9;
    public static final int SCHEMA_ROWS = 11;

    public static final String[] vertices = {"A", "B", "C", "D", "E", "F", "H"};

    public static LingAlgorithm lingAlgorithm;
    public static RayTracingAlgorithm rayTracingAlgorithm;

//    public static void main(String[] args) {
//        init();
//
//        System.out.println(charArrayToString(originalSchema.getSchemaInArray()));
//
//        initLingAlg();
//        initRayAlg();
//    }


    public AlgorithmRunner(int[][] connectionParts, ArrayList<char[]> cells) {
        init(connectionParts, cells);
    }

    public AlgorithmRunner() {
        init(connectionParts, cells);
    }

    private static void initLingAlg() {
        lingAlgorithm = new LingAlgorithm(originalSchema, connections);
        lingAlgorithm.run();
        //var steps = lingAlgorithm.getSteps();
        //printSteps(steps);
        //System.out.println("Загальна довжина: " + lingAlgorithm.getWeights());
        //System.out.println("Всього шарів: " + lingAlgorithm.getSchemas());
    }

    private static void initRayAlg() {
        rayTracingAlgorithm = new RayTracingAlgorithm(originalSchema, connections);
        rayTracingAlgorithm.run();
        //var steps = rayTracingAlgorithm.getSteps();
        //printSteps(steps);
        //System.out.println("Загальна довжина: " + rayTracingAlgorithm.getWeights());
        //System.out.println("Всього шарів: " + rayTracingAlgorithm.getSchemas());
    }

    public static void printSteps(ArrayList<LinkedList<String[][]>> steps) {
        // Ітерація по кожному LinkedList у списку
        for (int i = 0; i < steps.size(); i++) {
            LinkedList<String[][]> stepList = steps.get(i);
            System.out.println("Schema " + (i + 1) + ":");

            // Ітерація по кожному масиву в LinkedList
            for (int j = 0; j < stepList.size(); j++) {
                String[][] array = stepList.get(j);
                System.out.println("  Step " + (j + 1) + ":");

                // Ітерація по рядках і стовпцях масиву
                for (int row = 0; row < array.length; row++) {
                    for (int col = 0; col < array[row].length; col++) {
                        System.out.print(array[row][col] + "\t");
                    }
                    System.out.println(); // Переходимо на новий рядок після виведення кожного рядка масиву
                }
            }
        }
    }


    public static void init(int[][] connectionParts, ArrayList<char[]> cells){
        connections = Controller.convertConnections(connectionParts);
        cellsHash = Controller.createCellHash(cells);
        originalSchema = new Schema(SCHEMA_ROWS, SCHEMA_COLS, cellsHash);

        initLingAlg();
        initRayAlg();

    }

    static String charArrayToString(String[][] schema) {
        String result = "\n";
        int rows = schema.length;
        int cols = schema[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result += (Objects.equals(schema[i][j], "0") ? "0" : getTextWithColor(schema[i][j])) + "\t";
            }
            result += "\n";
        }
        return result;
    }

    private static String getTextWithColor(String ch) {
        if (Arrays.toString(vertices).contains(ch)) {
            return "\u001B[33m" + ch + "\u001B[0m";

        }
        return ch;
    }


}
