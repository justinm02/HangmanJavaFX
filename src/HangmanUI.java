import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class HangmanUI {
    private Stage primaryStage;
    private Gameplay gameplay;

    private ArrayList<Image> hangmanImages;
    private ImageView hangmanView;
    private ArrayList<VBox> correctUsedLetters;

    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public HangmanUI(Stage primaryStage, Gameplay gameplay) {
        this.primaryStage = primaryStage;
        this.gameplay = gameplay;

        hangmanImages = new ArrayList<Image>(Arrays.asList(
                new Image("HangmanBoardImages/HangmanStart.jpeg"),
                new Image("HangmanBoardImages/Hangman1.jpeg"),
                new Image("HangmanBoardImages/Hangman2.jpeg"),
                new Image("HangmanBoardImages/Hangman3.jpeg"),
                new Image("HangmanBoardImages/Hangman4.jpeg"),
                new Image("HangmanBoardImages/Hangman5.jpeg"),
                new Image("HangmanBoardImages/Hangman6.jpeg")));

        hangmanView = new ImageView(hangmanImages.get(0));

        correctUsedLetters = new ArrayList<VBox>();

        StackPane hangmanSetup = new StackPane();

        Scene scene = new Scene(hangmanSetup);

        BorderPane letterPane = new BorderPane();
        setUpLetterPane(letterPane);

        hangmanSetup.getChildren().addAll(hangmanView, letterPane);

        hangmanView.fitWidthProperty().bind(hangmanSetup.widthProperty());
        hangmanView.fitHeightProperty().bind(hangmanSetup.heightProperty());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setUpLetterPane(BorderPane letterPane) {
        setUpAvailableLetters(letterPane);
        setUpGameWord(letterPane);
    }

    private void setUpAvailableLetters(BorderPane letterPane) {
        ArrayList<HBox> hangmanBoxes = new ArrayList<HBox>();

        for (int i = 0; i < alphabet.length(); i++) {
            String letter = alphabet.substring(i, i+1);
            hangmanBoxes.add(createLetterButton("LettersImages/Hangman" + letter + ".png", i));
        }

        HBox alphabetBox1 = createAlphabetBox(new ArrayList<HBox>(Arrays.asList(hangmanBoxes.get(0), hangmanBoxes.get(1), hangmanBoxes.get(2), hangmanBoxes.get(3),
                hangmanBoxes.get(4), hangmanBoxes.get(5), hangmanBoxes.get(6))), 0);
        HBox alphabetBox2 = createAlphabetBox(new ArrayList<HBox>(Arrays.asList(hangmanBoxes.get(7), hangmanBoxes.get(8), hangmanBoxes.get(9), hangmanBoxes.get(10),
                hangmanBoxes.get(11), hangmanBoxes.get(12), hangmanBoxes.get(13))), 0);
        HBox alphabetBox3 = createAlphabetBox(new ArrayList<HBox>(Arrays.asList(hangmanBoxes.get(14), hangmanBoxes.get(15), hangmanBoxes.get(16),
                hangmanBoxes.get(17), hangmanBoxes.get(18), hangmanBoxes.get(19))), 30);
        HBox alphabetBox4 = createAlphabetBox(new ArrayList<HBox>(Arrays.asList(hangmanBoxes.get(20), hangmanBoxes.get(21), hangmanBoxes.get(22),
                hangmanBoxes.get(23), hangmanBoxes.get(24), hangmanBoxes.get(25))), 30);

        VBox fullAlphabetBox = new VBox();
        fullAlphabetBox.getChildren().addAll(alphabetBox1, alphabetBox2, alphabetBox3, alphabetBox4);
        fullAlphabetBox.setAlignment(Pos.BOTTOM_RIGHT);
        fullAlphabetBox.setPadding(new Insets(0, 80, 60, 0));
        fullAlphabetBox.setPrefSize(100, 100);

        letterPane.setRight(fullAlphabetBox);
    }

    private void setUpGameWord(BorderPane letterPane) {
        String gameWord = gameplay.getGameWord();
        HBox gameWordLetters = new HBox();

        for (int i = 0; i < gameWord.length(); i++) {
            String letter = gameWord.substring(i, i+1);
            String imageURL = "";

            if (!letter.equals(" ")) {
                imageURL = "LettersImages/letterPlaceHolder.png";
            }
            else {
                imageURL = "LettersImages/letterSpace.png";
            }

            ImageView letterPlaceHolder = new ImageView(new Image(imageURL));
            VBox letterPlaceHolderBox = new VBox(letterPlaceHolder);

            correctUsedLetters.add(letterPlaceHolderBox);
            gameWordLetters.getChildren().add(letterPlaceHolderBox);
        }

        gameWordLetters.setAlignment(Pos.BASELINE_CENTER);
        gameWordLetters.setSpacing(10);
        gameWordLetters.setPadding(new Insets(135, 30, 0, 30));
        letterPane.setTop(gameWordLetters);
    }

    private HBox createLetterButton(String imageURL, int letterIndex) {
        ImageView imageView = new ImageView(new Image(imageURL));
        Button letterButton = new Button();
        letterButton.setGraphic(imageView);
        letterButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        String IDLE_BUTTON_STYLE = "-fx-background-color: transparent;";
        String HOVERED_BUTTON_STYLE = "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;";

        letterButton.setStyle(IDLE_BUTTON_STYLE);
        letterButton.setOnMouseEntered(e -> letterButton.setStyle(HOVERED_BUTTON_STYLE));
        letterButton.setOnMouseExited(e -> letterButton.setStyle(IDLE_BUTTON_STYLE));

        letterButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String guessedLetter = alphabet.substring(letterIndex, letterIndex+1);

                        letterButton.setDisable(true);
                        ArrayList<Integer> correctLetterIndices = gameplay.useTurn(guessedLetter);

                        for (Integer index : correctLetterIndices) {
                            ImageView correctLetter = new ImageView(new Image("LettersImages/Hangman" + guessedLetter + ".png"));

                            correctUsedLetters.get(index).getChildren().add(0, correctLetter);
                            correctUsedLetters.get(index).setPadding(new Insets(0,0,135,0));
                        }

                        hangmanView.setImage(hangmanImages.get(6 - gameplay.getRemainingTurns()));

                        if (gameplay.wonGame()) {
                            Alert wonGameMessage = new Alert(Alert.AlertType.INFORMATION);
                            wonGameMessage.setHeaderText("Congratulations! You won!");
                            wonGameMessage.setContentText("You correctly guessed the word: " + gameplay.getGameWord() + "\nPress \"OK\" to end the game!");

                            wonGameMessage.showAndWait();

                            primaryStage.close();
                        }
                        else if (gameplay.getRemainingTurns() == 0) {
                            Alert lostGameMessage = new Alert(Alert.AlertType.INFORMATION);
                            lostGameMessage.setHeaderText("Game Over!");
                            lostGameMessage.setContentText("Nice try, but the word was " + gameplay.getGameWord() + "\nPress \"OK\" to end the game!");

                            lostGameMessage.showAndWait();

                            primaryStage.close();
                        }
                    }
                }
        );
        HBox letterHBox = new HBox(letterButton);

        return letterHBox;
    }

    private HBox createAlphabetBox(ArrayList<HBox> hangmanBoxes, int rightInset) {
        HBox alphabetBox = new HBox();
        alphabetBox.getChildren().addAll(hangmanBoxes);
        alphabetBox.setAlignment(Pos.CENTER_RIGHT);
        alphabetBox.setPadding(new Insets(0, rightInset, 0, 0));

        return alphabetBox;
    }
}
