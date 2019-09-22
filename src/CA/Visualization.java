package CA;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class Visualization {
    public static final int SCENE_WIDTH = 800;
    public static final int SCENE_WIDTH_WITH_INPUT_BAR = 1000;
    public static final int SCENE_HEIGHT = 800;

    private Game myCurrentGame;
    private Stage myStage;
    private Pane myRoot;
    private Scene myScene;
    private HashMap<String, String> mySimulationsSupported;
    private ArrayList<String> mySimulationButtons;

    public Visualization(Game currentGame, Stage stage, HashMap<String, String> simulationsSupported, ArrayList<String> simulationButtons) {
        mySimulationsSupported = simulationsSupported;
        mySimulationButtons = simulationButtons;
        myCurrentGame = currentGame;
        myStage = stage;
        myStage.setTitle("Cellular Automata"); //TODO: Read from XML File
        myStage.setResizable(false);
        stage.show();
    }

    public void showIntroScene() {
        myRoot = new Pane();
        setBackground();
        myScene = new Scene(myRoot, SCENE_WIDTH_WITH_INPUT_BAR, SCENE_HEIGHT);
        myRoot.getChildren().add(createButtonsForIntro());
        myStage.setScene(myScene);
    }

    private void setBackground() {
        Image imageForBackground = new Image(this.getClass().getClassLoader().getResourceAsStream("background.jpg"));
        BackgroundImage backgroundImage = new BackgroundImage(imageForBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        myRoot.setBackground(background);
    }

    private VBox createButtonsForIntro() {
        int buttonsSpacing = 15;
        int vBoxX = 835;
        int vBoxY = 300;
        VBox buttonsBox = new VBox(buttonsSpacing);
        buttonsBox.setLayoutX(vBoxX);
        buttonsBox.setLayoutY(vBoxY);
        buttonsBox.setAlignment(Pos.CENTER);
        int buttonWidth = 130;
        for (String key : mySimulationsSupported.keySet()) {
            Button simulationButton = new Button(key);
            simulationButton.setPrefWidth(buttonWidth);
            simulationButton.setOnAction(e -> myCurrentGame.loadSimulation(mySimulationsSupported.get(key)));
            buttonsBox.getChildren().add(simulationButton);
        }
        return buttonsBox;
    }

    public void showSimulationScene(Grid grid) {
        myRoot = new Pane();
        setBackground();
        myScene = new Scene(myRoot, SCENE_WIDTH_WITH_INPUT_BAR, SCENE_HEIGHT);
        myStage.setScene(myScene);
        myRoot.getChildren().add(createButtonsForSimulation());
        displayGrid(grid);
    }

    private VBox createButtonsForSimulation() {
        int buttonsSpacing = 15;
        VBox buttonsBox = new VBox(buttonsSpacing);
        double xPos = 850 ;
        double yPos = 80;
        buttonsBox.setLayoutX(xPos);
        buttonsBox.setLayoutY(yPos);
        buttonsBox.setAlignment(Pos.CENTER);
        int buttonWidth = 100;
        for (int label=0; label<mySimulationButtons.size(); label++) {
            Button simulationButton = new Button(mySimulationButtons.get(label));
            simulationButton.setPrefWidth(buttonWidth);
            int finalLabel = label;
            simulationButton.setOnAction(e -> {
                if (finalLabel == 0) {
                    myCurrentGame.loadIntro();
                } else if (finalLabel == 1) {
                    myCurrentGame.playSimulation();
                } else if (finalLabel == 2) {
                    myCurrentGame.pauseSimulation();
                } else if (finalLabel == 3) {
                    myCurrentGame.skipStep();
                } else if (finalLabel == 4) {
                    myCurrentGame.fastForwardSimulation();
                } else if (finalLabel == 5) {
                    myCurrentGame.slowDownSimulation();
                }
            });
            buttonsBox.getChildren().add(simulationButton);
        }
        return buttonsBox;
    }

    public void displayGrid(Grid grid){ //TODO: Temporary stroke; we have to redo logic for rectangle borders (old logic doesn't work if we spread rectangles out across scene)
        myRoot.getChildren().clear();
        myRoot.getChildren().add(createButtonsForSimulation());
        Cell[][] cells = grid.getCells();
        Rectangle rectangle;
        for (int i = 0; i < grid.getNumRows(); i++) {
            for (int j=0; j< grid.getNumCols(); j++) {
                int cellSize = (SCENE_WIDTH / grid.getNumRows());
                rectangle = new Rectangle(cellSize, cellSize);
                rectangle.setStroke(Color.BLACK);
                rectangle.setX((j) * (cellSize));
                rectangle.setY((i) * (cellSize));
                if(cells[i][j].getState() == 0){
                    rectangle.setFill(Color.BLACK);
                }
                else if(cells[i][j].getState() == 1){
                    rectangle.setFill(Color.GREEN);
                }
                else{
                    rectangle.setFill(Color.RED);
                }
                myRoot.getChildren().add(rectangle);
            }
        }
    }

}

