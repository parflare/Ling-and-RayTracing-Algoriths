package ua.parflare.sapr3;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

import static ua.parflare.sapr3.AlgorithmRunner.charArrayToString;

public abstract class Algorithm {

    protected ArrayList<String> connectionsCopy;
    protected Schema originalSchema;
    protected LinkedList<Schema> schemas;
    protected ArrayList<String> connections;

    public Algorithm(Schema schema, ArrayList<String> connections) {
        originalSchema = schema.getSchemaCopy();
        schemas = new LinkedList<>();
        schemas.add(schema.getSchemaCopy());

        this.connections = new ArrayList<>();
        this.connections.addAll(connections);

        connectionsCopy = new ArrayList<>();
        this.connectionsCopy.addAll(connections);
    }

    public ArrayList<String> getConnectionsCopy() {
        return connectionsCopy;
    }

    public int getSchemas() {
        return schemas.size();
    }

    public ArrayList<LinkedList<String[][]>> getSteps() {
        ArrayList<LinkedList<String[][]>> arrayLists = new ArrayList<>();
        for (int i = 0; i < schemas.size(); i++) {
            Schema schema = schemas.get(i);
            arrayLists.add(schema.getSchemaSteps());
        }
        return arrayLists;
    }

    public int getWeights() {
        int weight = 0;
        for (int i = 0; i < schemas.size(); i++) {
            Schema schema = schemas.get(i);
            LinkedList<Connection> connection = schema.getConnections();
            for (int j = 0; j < connection.size(); j++) {
                weight += connection.get(j).getLengths();
            }
        }
        return weight;
    }

    protected void run() {

        while (connections.size() >= 1) {
            for (int i = 0; i < schemas.size(); i++) {
                Schema schema = schemas.get(i);
                String connectionName = connections.getFirst();
                LinkedList<Point> path = doSteps(schema, connectionName);

                if (path == null && i == schemas.size() - 1) {
                    schemas.add(originalSchema.getSchemaCopy());
                    System.out.println("created new schema");
                    break;
                }
                if (path != null) {
                    Cell[][] tmpCells = schema.getCells();
                    for (int k = 0; k < path.size(); k++) {
                        Point cell = path.get(k);
                        if (Objects.equals(tmpCells[cell.x][cell.y].getValue(), "0")) {
                            tmpCells[cell.x][cell.y].setValue(connectionName);
                        }
                    }
                    Connection connection = new Connection(connectionName, path);
                    schema.addConnection(connection);
                    schema.setCells(tmpCells);

                    System.out.println(charArrayToString(schema.getSchemaInArray()));

                    connections.remove(connectionName);
                    break;
                } else {
                    System.out.println("trying to find way in next schema..");
                }
            }

        }

    }

    protected abstract LinkedList<Point> doSteps(Schema schema, String connectionName);

    protected LinkedList<Point> findBackWay(Cell[][] tmpSchema, Point first, Point second, int stepNumber) {
        LinkedList<Point> path = new LinkedList<>();
        boolean isAdded = false;
        int row = second.x;
        int col = second.y;

        path.add(new Point(row, col));

        while (stepNumber > 0) {

            String currentChar;

            if (row + 1 < tmpSchema.length) {
                currentChar = tmpSchema[row + 1][col].getValue();
                if (Objects.equals(currentChar, Integer.toString(stepNumber))) {
                    isAdded = true;
                    path.add(new Point(row + 1, col));
                }
            }
            if (row > 0 && !isAdded) {
                currentChar = tmpSchema[row - 1][col].getValue();
                if (Objects.equals(currentChar, Integer.toString(stepNumber))) {
                    isAdded = true;
                    path.add(new Point(row - 1, col));
                }
            }

            if (col + 1 < tmpSchema[0].length && !isAdded) {
                currentChar = tmpSchema[row][col + 1].getValue();
                if (Objects.equals(currentChar, Integer.toString(stepNumber))) {
                    isAdded = true;
                    path.add(new Point(row, col + 1));
                }
            }

            if (col > 0 && !isAdded) {
                currentChar = tmpSchema[row][col - 1].getValue();
                if (Objects.equals(currentChar, Integer.toString(stepNumber))) {
                    isAdded = true;
                    path.add(new Point(row, col - 1));
                }

            }

            stepNumber--;
            isAdded = false;
            row = path.getLast().x;
            col = path.getLast().y;
        }

        path.add(new Point(first));

        return path;

    }


}
