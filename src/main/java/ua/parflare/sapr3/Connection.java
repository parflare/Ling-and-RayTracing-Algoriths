package ua.parflare.sapr3;

import java.awt.*;
import java.util.LinkedList;

public class Connection {

    private String name;
    private LinkedList<Point> cellsXY;

    public Connection(String name, LinkedList<Point> cellsXY) {
        this.name = name;
        setCellsXY(cellsXY);
    }

    public Connection(String name) {
        this(name, new LinkedList<>());
    }

    public Connection(Connection object) {
        this(object.getName(), object.getCellsXY());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLengths() {
        return cellsXY.size() - 1;
    }

    public LinkedList<Point> getCellsXY() {
        if (cellsXY == null) {
            return null;
        }

        LinkedList<Point> cellsXYCopy = new LinkedList<>();
        for (Point point : cellsXY) {
            cellsXYCopy.add(new Point(point));
        }
        return cellsXYCopy;
    }

    public void setCellsXY(LinkedList<Point> cellsXY) {
        if (cellsXY == null) {
            this.cellsXY = null;
            return;
        }

        LinkedList<Point> tmp = new LinkedList<>();
        for (Point point : cellsXY) {
            tmp.add(new Point(point));
        }
        this.cellsXY = tmp;
    }
}
