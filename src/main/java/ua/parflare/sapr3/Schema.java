package ua.parflare.sapr3;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Schema {

    private Cell[][] cells;
    private LinkedList<Connection> connections;

    private LinkedList<String[][]> schemaSteps;


    public LinkedList<String[][]> getSchemaSteps() {
        return schemaSteps;
    }


    public Schema(int rows, int cols, HashMap<Character, Point> cellsHash) {
        initCells(rows, cols, cellsHash);
        connections = new LinkedList<>();
        schemaSteps = new LinkedList<>();
    }

    public Schema(Cell[][] cells, LinkedList<Connection> connections) {
        this.cells = cells;
        this.connections = connections;
        schemaSteps = new LinkedList<>();
    }

    private void initCells(int rows, int cols, HashMap<Character, Point> cellsHash) {
        Cell[][] schema = new Cell[rows][cols];

        for (int i = 0; i < schema.length; i++) {
            for (int j = 0; j < schema[i].length; j++) {
                schema[i][j] = new Cell("0");
            }
        }

        for (var entry : cellsHash.entrySet()) {
            String value = "" + entry.getKey();
            int row = (int) entry.getValue().getX();
            int col = (int) entry.getValue().getY();

            schema[row][col] = new Cell(value);
        }

        cells = schema;
    }

    public Schema getSchemaCopy() {
        return new Schema(getCells(), getConnections());
    }

    public Cell[][] getCells() {
        if (cells == null) {
            return null;
        }

        int rows = cells.length;
        int cols = cells[0].length;
        Cell[][] schemaCopy = new Cell[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                schemaCopy[i][j] = new Cell(cells[i][j]);
            }
        }
        return schemaCopy;
    }

    public void setCells(Cell[][] cells) {
        if (cells == null) {
            this.cells = null;
            return;
        }

        int rows = cells.length;
        int cols = cells[0].length;
        this.cells = new Cell[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.cells[i][j] = new Cell(cells[i][j]);
            }
        }

        schemaSteps.add(getSchemaInArray());
    }

    public String[][] getSchemaInArray() {
        return Arrays.stream(getCells())
                .map(row -> Arrays.stream(row)
                        .map(Cell::getValue)
                        .toArray(String[]::new))
                .toArray(String[][]::new);
    }

    public LinkedList<Connection> getConnections() {
        if (connections == null) {
            return null;
        }

        LinkedList<Connection> connectionsCopy = new LinkedList<>();
        for (Connection connection : connections) {
            connectionsCopy.add(new Connection(connection));
        }
        return connectionsCopy;
    }

    public void setConnections(LinkedList<Connection> connections) {
        if (connections == null) {
            this.connections = null;
            return;
        }

        this.connections = new LinkedList<>();
        for (Connection connection : connections) {
            this.connections.add(new Connection(connection));
        }
    }

    public void addConnection(Connection connection) {
        this.connections.add(new Connection(connection));
    }
}
