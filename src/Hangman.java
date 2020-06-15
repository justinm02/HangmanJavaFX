import javafx.application.Application;

import javafx.stage.Stage;

public class Hangman extends Application {

    @Override
    public void start(Stage primaryStage) {
        Gameplay gameplay = new Gameplay();
        HangmanUI hangmanUI = new HangmanUI(primaryStage, gameplay);
    }

    public static void main(String[] args) {
        launch();
    }
}

