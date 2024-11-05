package ua.parflare.sapr3;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import static ua.parflare.sapr3.AlgorithmRunner.*;

public class Controller {

    @FXML
    public GridPane gridSchema, gridColumns, gridConnections;
    @FXML
    public Tab valuesTab, algorithm1Tab, algorithm2Tab;
    @FXML
    public TabPane mainTabPane, algorithm1TabPane, algorithm2TabPane;

    public Button calculateButton;
    AlgorithmRunner algorithmRunner;
    private TextField[][] textFields;
    private Spinner<Integer>[][] spinners;

    public static ArrayList<String> convertConnections(int[][] connectionParts) {
        ArrayList<String> connects = new ArrayList<>();

        for (int i = 0; i < connectionParts.length; i++) {
            for (int j = 0; j < connectionParts[i].length; j++) {
                int connections = connectionParts[i][j];

                for (int k = 1; k <= connections; k++) {
                    connects.add("" + vertices[i] + vertices[i + j + 1] + k);
                }
            }
        }
        return connects;
    }

    public static HashMap<Character, Point> createCellHash(ArrayList<char[]> cells) {
        HashMap<Character, Point> elements = new HashMap<>(cells.size());

        for (int i = 0; i < cells.size(); i++) {
            char[] cell = cells.get(i);
            int row = cell[0]; // y
            int col = cell[1]; // x
            elements.put(cell[2], new Point(row, col));
        }

        return elements;
    }

    private void addStepsToTabs(TabPane algorithm1TabPane, ArrayList<LinkedList<String[][]>> steps, ArrayList<String> connectionsCopy, String text) {
        for (int i = 0; i < steps.size(); i++) {
            LinkedList<String[][]> schemas = steps.get(i);

            Tab newTab = new Tab("Шар " + (i + 1));

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);

            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            vbox.setSpacing(10);

            for (int j = 0; j < schemas.size(); j++) {
                String[][] schema = schemas.get(j);
                GridPane schemaGrid = createGridFromSchema(schema, connectionsCopy);

                Label stepLabel = new Label("Крок " + (j + 1));
                stepLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                Separator separator = new Separator();

                vbox.getChildren().addAll(stepLabel, schemaGrid, separator);
            }
            vbox.getChildren().add(new Label(text));
            scrollPane.setContent(vbox);
            newTab.setContent(scrollPane);
            algorithm1TabPane.getTabs().add(newTab);
        }
    }

    private GridPane createGridFromSchema(String[][] schema, ArrayList<String> connectionsCopy) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        int boxSize = 35;
        for (int col = 0; col < schema[0].length; col++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPrefWidth(boxSize);
            colConst.setMinWidth(boxSize);
            colConst.setMaxWidth(boxSize);
            gridPane.getColumnConstraints().add(colConst);
        }

        for (int row = 0; row < schema.length; row++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPrefHeight(boxSize);
            rowConst.setMinHeight(boxSize);
            rowConst.setMaxHeight(boxSize);
            gridPane.getRowConstraints().add(rowConst);
        }

        for (int row = 0; row < schema.length; row++) {
            for (int col = 0; col < schema[row].length; col++) {
                String value = schema[row][col];

                if ("0".equals(value)) {
                    TextField textField = new TextField();
                    textField.setEditable(false);
                    textField.setPrefWidth(boxSize);
                    textField.setPrefHeight(boxSize);
                    textField.setMaxSize(boxSize, boxSize);
                    textField.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1px;");
                    gridPane.add(textField, col, row);
                    continue;
                }

                TextField textField = new TextField(value);
                textField.setEditable(false);
                textField.setPrefWidth(boxSize);
                textField.setPrefHeight(boxSize);
                textField.setMaxSize(boxSize, boxSize);
                textField.setAlignment(Pos.CENTER);
                textField.setFont(Font.font("Arial", 11));
                textField.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

                if (value.matches("[A-Z]{2}\\d+")) {
                    int connectionIndex = connectionsCopy.indexOf(value);
                    java.awt.Color awtColor = ColorList.getColorByIndex(connectionIndex);
                    String fxColor = String.format("#%02x%02x%02x", awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
                    textField.setStyle("-fx-background-color: " + fxColor + "; -fx-border-color: black; -fx-border-width: 1px;");
                } else if (value.matches("[A-Z]")) {
                    textField.setStyle("-fx-background-color: lightgray; -fx-border-color: black; -fx-border-width: 1px;");
                }

                gridPane.add(textField, col, row);
            }
        }

        return gridPane;
    }

    public void initialize() {
        initCellGrid();
        initConnectionGrid();
        initListeners();
        //algorithmRunner = new AlgorithmRunner();
    }

    private void initListeners() {
        calculateButton.setOnAction(event -> {

            var k = getConnectionParts();
            for (int i = 0; i < k.length; i++) {
                System.out.println(Arrays.toString(k[i]));
            }

            System.out.println();

            var c = getCellsData();
            for (int i = 0; i < c.size(); i++) {
                System.out.println(Arrays.toString(c.get(i)));
            }

            algorithmRunner = new AlgorithmRunner(getConnectionParts(), getCellsData());

            algorithm1TabPane.getTabs().clear();
            var steps1 = lingAlgorithm.getSteps();
            var connection1 = lingAlgorithm.getConnectionsCopy();
            var text1 = "Загальна довжина: " + lingAlgorithm.getWeights() + " "
                    + "Всього шарів: " + lingAlgorithm.getSchemas();
            addStepsToTabs(algorithm1TabPane, steps1, connection1, text1);

            algorithm2TabPane.getTabs().clear();
            var steps2 = rayTracingAlgorithm.getSteps();
            var connection2 = rayTracingAlgorithm.getConnectionsCopy();
            var text2 = "Загальна довжина: " + rayTracingAlgorithm.getWeights() + " "
                    + "Всього шарів: " + rayTracingAlgorithm.getSchemas();
            addStepsToTabs(algorithm2TabPane, steps2, connection2, text2);

            mainTabPane.getSelectionModel().select(1);
        });

        for (TextField[] textField : textFields) {
            for (int j = 0; j < textField.length; j++) {
                TextField field = textField[j];
                field.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.isEmpty()) {
                        field.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, null, null)));
                    } else {
                        field.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
                    }

                    if (newValue.length() > 2) {
                        field.setText(oldValue);
                    }
                });
            }
        }
    }

    private void initConnectionGrid() {
        for (var column : gridConnections.getColumnConstraints()) {
            column.setPrefWidth(50);
            column.setHalignment(HPos.CENTER);
        }

        for (var column : gridColumns.getColumnConstraints()) {
            column.setPrefWidth(50);
            column.setHalignment(HPos.CENTER);
        }

        int rows = 6;
        int columns = 7;
        spinners = new Spinner[rows][columns];

        for (int i = 0; i < 1; i++) {
            for (int j = 1; j < columns; j++) {
                javafx.scene.control.Label label = new javafx.scene.control.Label(vertices[j]);
                gridColumns.add(label, j, i);
            }
        }

        for (int row = 0, diff = 0; row < rows; row++, diff++) {
            for (int col = diff; col < columns; col++) {
                if (row == col) {
                    javafx.scene.control.Label label = new Label(vertices[row] + " з: ");
                    gridConnections.add(label, col, row);
                } else {
                    Spinner<Integer> spinner = new Spinner<>();
                    SpinnerValueFactory<Integer> valueFactory =
                            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 0);
                    spinner.setValueFactory(valueFactory);
                    spinner.setEditable(false);
                    gridConnections.add(spinner, col, row);
                    spinners[row][col] = spinner;
                }
            }
        }
    }

    private int[][] getConnectionParts() {

        int rows = spinners.length;
        int columns = spinners[0].length;


        int[][] connectionParts = new int[rows][];

        for (int i = 0, diff = 1; i < spinners.length; i++, diff++) {
            connectionParts[i] = new int[columns - diff];
            for (int j = diff; j < spinners[i].length; j++) {
                int value = spinners[i][j].getValue();
                
                connectionParts[i][j - diff] = value;
            }
        }

        return connectionParts;
    }

    private void initCellGrid() {
        int rows = 11;
        int cols = 9;
        textFields = new TextField[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                TextField textField = new TextField();
                textField.setPrefSize(40, 40);
                textField.setAlignment(Pos.CENTER);
                textField.setFont(Font.font("Arial", 16));
                textField.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
                textFields[i][j] = textField;
                gridSchema.add(textField, j, i);
            }
        }
    }

    private ArrayList<char[]> getCellsData() {
        ArrayList<char[]> cells = new ArrayList<>();

        for (int i = 0; i < textFields.length; i++) {
            for (int j = 0; j < textFields[i].length; j++) {
                String text = textFields[i][j].getText();

                if (!text.isEmpty()) {
                    try {
                        char value = (char) text.charAt(0);
                        cells.add(new char[]{(char) i, (char) j, value});
                    } catch (NumberFormatException e) {
                        System.out.println("\nНевірний формат числа в полі [" + i + "][" + j + "]");
                    }
                }
            }
        }

        return cells;
    }
}