package ua.parflare.sapr3.models;

import java.awt.*;

public class Cell {
    private String value;
    private Color color;

    public Cell(String value, Color color) {
        this.value = value;
        this.color = color;
    }

    public Cell(String value) {
        this.value = value;
        this.color = null;
    }

    public Cell(Cell value) {
        this(value.getValue(), value.getColor());
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "value='" + value + '\'' +
                ", color=" + color +
                '}';
    }
}
