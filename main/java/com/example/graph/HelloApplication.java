package com.example.graph;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Arrays;


import static java.nio.ByteBuffer.wrap;

public class HelloApplication extends Application {
    private final int WIDTH = 1440;
    private final int HEIGHT = 785;
    private final int BUTTON_HEIGHT = 750;
    private final int BUTTON_WIDTH = 20;
    private final int LABEL_HEIGHT = 150;
    private final int LABEL_WIDTH = 10;
    private final int DISTANCE_OF_LABEL = 80;
    private final int DISTANCE_OF_BUTTON_WIDTH = 130;
    private final int SIZE_PREF_BUTTON_OF_WIDTH = 120;

    @Override
    public void start(Stage stage) throws IOException {
        Group group = new Group();
        Scene scene = new Scene(group, WIDTH, HEIGHT);

        Button button = new Button("Исходный сигнал");
        button.setLayoutX(BUTTON_WIDTH);
        button.setLayoutY(BUTTON_HEIGHT);
        button.setPrefWidth(SIZE_PREF_BUTTON_OF_WIDTH);

        Button button1 = new Button("Спектр");
        button1.setLayoutX(BUTTON_WIDTH + DISTANCE_OF_BUTTON_WIDTH);
        button1.setLayoutY(BUTTON_HEIGHT);
        button1.setPrefWidth(SIZE_PREF_BUTTON_OF_WIDTH);

        Button button2 = new Button("Сигнал после ЦФ");
        button2.setLayoutX(BUTTON_WIDTH + 2 * DISTANCE_OF_BUTTON_WIDTH);
        button2.setLayoutY(BUTTON_HEIGHT);
        button2.setPrefWidth(SIZE_PREF_BUTTON_OF_WIDTH);

        TextField textField = new TextField("Исходный сигнал");
        textField.setLayoutX(LABEL_WIDTH);
        textField.setLayoutY(LABEL_WIDTH + LABEL_HEIGHT);

        TextField textField1 = new TextField("Спектр");
        textField1.setLayoutX(LABEL_WIDTH);
        textField1.setLayoutY(LABEL_WIDTH + 2 * LABEL_HEIGHT + DISTANCE_OF_LABEL);

        TextField textField2 = new TextField("Сигнал после ЦФ");
        textField2.setLayoutX(LABEL_WIDTH);
        textField2.setLayoutY(LABEL_WIDTH + 3 * LABEL_HEIGHT + 2 * DISTANCE_OF_LABEL);

        //Array of numbers
        FileReader fileReader = new FileReader();
        float[] array = fileReader.Reader();

        for (float i : array) {
            System.out.print(i + " ");
        }

        TextArea textArea = new TextArea();
        textArea.setText(Arrays.toString(array));

        textArea.setLayoutX(LABEL_WIDTH - 3);
        textArea.setLayoutY(650);
        textArea.setPrefColumnCount(116);
        textArea.setPrefRowCount(1);

        //button reles
        button.setOnAction(actionEvent -> {
            // connect in interface in lambda
            for (int i = 0; i < array.length - 1; i++) {
                group.getChildren().addAll(new Line((i * 2.84) + LABEL_WIDTH, -(array[i] * 10) + LABEL_HEIGHT / 2, ((i + 1) * 2.84) + LABEL_WIDTH, -(array[i + 1] * 10) + LABEL_HEIGHT / 2));
            }
        });

        float[] a = new float[array.length];
        float[] b = new float[array.length];
        float[] c = new float[array.length];
        for (int n = 0; n < array.length; n++) {
            float countOfArrayA = 0;
            float countOfArrayB = 0;
            for (int k = 0; k < array.length; k++) {
                countOfArrayA += array[k] * Math.cos((2 * Math.PI / array.length) * n * k);
                countOfArrayB += array[k] * Math.sin((2 * Math.PI / array.length) * n * k);
            }
            countOfArrayA *= 1.0 / array.length;
            countOfArrayB *= 1.0 / array.length;
            a[n] = countOfArrayA;
            b[n] = countOfArrayB;
            c[n] = (float) Math.sqrt(Math.pow(a[n], 2) + Math.pow(b[n], 2));
        }

        TextArea textArea1 = new TextArea();
        textArea1.setText(Arrays.toString(c));

        textArea1.setLayoutX(LABEL_WIDTH - 3);
        textArea1.setLayoutY(700);
        textArea1.setPrefColumnCount(116);
        textArea1.setPrefRowCount(1);


        //button1 reles
        button1.setOnAction(ActionEvent -> {
            for (int i = 0; i <= array.length / 2; i++) {
                group.getChildren().addAll(new Line((i * 2.84) + LABEL_WIDTH, -(c[i] * 200) + LABEL_HEIGHT * 2 + 10, (i) * 2.84 + LABEL_WIDTH, HEIGHT / 3 + 50));
            }
        });

        float C0 = (float) 0;

        for (int i = 0; i <= array.length - 1; i++) {
            C0 += array[i];
        }

        C0 *= (float) 1 / array.length;

        for (int k = 140; k <= 180; k++) {
            float sum = (float) 0;
            for (int n = 1; n <= array.length / 2; n++) {
                sum += (a[n] * Math.cos((2 * Math.PI / array.length) * n * k)) + (b[n] * Math.sin((2 * Math.PI / array.length) * n * k));
            }
            sum *= 2;
            array[k] = C0 + sum;
        }


        //Fill signal cypher function
        button2.setOnAction(actionEvent -> {
            Step step = x -> Math.sin(x[0]);
            for (int i = 140; i <= 180; i++) {  // two variance four canal
                group.getChildren().addAll(new Line((i * 6) + LABEL_WIDTH - 840, -(array[i]) * 15 + LABEL_HEIGHT * 4 - 60, ((i + 1) * 6) + LABEL_WIDTH - 840, -(array[i + 1]) * 15 + LABEL_HEIGHT * 4 - 60));
            }
        });

        System.out.println();
        for (float i : array) {
            System.out.print(i + " ");
        }

        //First label
        group.getChildren().addAll(new Line(LABEL_WIDTH, LABEL_WIDTH, LABEL_WIDTH, LABEL_HEIGHT));
        group.getChildren().addAll(new Line(LABEL_WIDTH, LABEL_HEIGHT, WIDTH - 10, LABEL_HEIGHT));
        group.getChildren().addAll(new Line(WIDTH - 10, LABEL_HEIGHT, WIDTH - 10, LABEL_WIDTH));
        group.getChildren().addAll(new Line(LABEL_WIDTH, LABEL_WIDTH, WIDTH - 10, LABEL_WIDTH));
        group.getChildren().addAll(new Line(LABEL_WIDTH, HEIGHT / 10, WIDTH - 10, HEIGHT / 10)); // Begin (StartX, StartY, EndX, EndY)

        //Second label
        group.getChildren().addAll(new Line(LABEL_WIDTH, LABEL_HEIGHT + DISTANCE_OF_LABEL + 10, LABEL_WIDTH, 2 * LABEL_HEIGHT + DISTANCE_OF_LABEL));
        group.getChildren().addAll(new Line(LABEL_WIDTH, 2 * LABEL_HEIGHT + DISTANCE_OF_LABEL, WIDTH - 10, 2 * LABEL_HEIGHT + DISTANCE_OF_LABEL));
        group.getChildren().addAll(new Line(WIDTH - 10, 2 * LABEL_HEIGHT + DISTANCE_OF_LABEL, WIDTH - 10, LABEL_HEIGHT + DISTANCE_OF_LABEL + 10));
        group.getChildren().addAll(new Line(WIDTH - 10, LABEL_HEIGHT + DISTANCE_OF_LABEL + 10, LABEL_WIDTH, LABEL_HEIGHT + DISTANCE_OF_LABEL + 10));
        group.getChildren().addAll(new Line(LABEL_WIDTH, HEIGHT / 3 + 50, WIDTH - 10, HEIGHT / 3 + 50));

        //Third label
        group.getChildren().addAll(new Line(LABEL_WIDTH, 2 * LABEL_HEIGHT + 2 * DISTANCE_OF_LABEL + 10, LABEL_WIDTH, 3 * LABEL_HEIGHT + 2 * DISTANCE_OF_LABEL));
        group.getChildren().addAll(new Line(LABEL_WIDTH, 3 * LABEL_HEIGHT + 2 * DISTANCE_OF_LABEL, WIDTH - 10, 3 * LABEL_HEIGHT + 2 * DISTANCE_OF_LABEL));
        group.getChildren().addAll(new Line(WIDTH - 10, 3 * LABEL_HEIGHT + 2 * DISTANCE_OF_LABEL, WIDTH - 10, 2 * LABEL_HEIGHT + 2 * DISTANCE_OF_LABEL + 10));
        group.getChildren().addAll(new Line(WIDTH - 10, 2 * LABEL_HEIGHT + 2 * DISTANCE_OF_LABEL + 10, LABEL_WIDTH, 2 * LABEL_HEIGHT + 2 * DISTANCE_OF_LABEL + 10));
        group.getChildren().addAll(new Line(LABEL_WIDTH, HEIGHT / 2 + 150, WIDTH - 10, HEIGHT / 2 + 150));

        //add in scene
        group.getChildren().addAll(button);
        group.getChildren().addAll(button1);
        group.getChildren().addAll(button2);
        group.getChildren().addAll(textField);
        group.getChildren().addAll(textField1);
        group.getChildren().addAll(textField2);
        group.getChildren().addAll(textArea);
        group.getChildren().addAll(textArea1);
        stage.setScene(scene);
        stage.show();
    }

    @FunctionalInterface
    public interface Step {
        double paint(double... array);
    }

    public static void main(String[] args) {
        launch();
    }
}

class FileReader extends HelloApplication {
    float[] Reader() {
        final String s = "/Users/amir/Downloads/Graph/src/JavaFXNew/main/resources/filekr2.dat";
        float[] array = new float[500];

        try (FileInputStream fileInputStream = new FileInputStream((s))) {
            byte[] buffer = new byte[4];
            int i = 0;
            while ((fileInputStream.read(buffer)) != -1) {
                float number = wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                array[i] = number;
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }
}
