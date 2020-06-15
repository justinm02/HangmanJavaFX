import java.util.ArrayList;

public class Gameplay {
    private int remainingTurns;
    private String[] potentialWords;
    private String gameWord;
    private ArrayList<String> correctUsedLetters;

    public Gameplay() {
        remainingTurns = 6;
        potentialWords = new String[]{"TIGER", "LION", "ELEPHANT", "CHEETAH", "RHINOCEROS", "PANDA", "CHIMPANZEE", "STING RAY", "SHARK", "COMPUTER", "MOUSE", "WATER BOTTLE", "BINDER", "PENCIL", "MONITOR",
                "CALCULATOR", "CHAPSTICK", "CONTAINER", "FRYING PAN", "ICECREAM", "STRAWBERRY", "GLASSES", "MANILA FOLDER", "HEADPHONES", "LANDLINE", "UNIVERSITY", "ELEMENTARY"};
        gameWord = potentialWords[(int)(Math.random()*potentialWords.length)];
        correctUsedLetters = new ArrayList<String>();

        System.out.println(gameWord);
    }

    public ArrayList<Integer> useTurn(String guessedLetter) {
        ArrayList<Integer> correctLetterIndices = new ArrayList<Integer>();

        for (int i = 0; i < gameWord.length(); i++) {
            if (gameWord.substring(i,i+1).equalsIgnoreCase(guessedLetter)) {
                correctLetterIndices.add(i);
            }
        }

        if (correctLetterIndices.size() == 0) {
            remainingTurns--;
        }
        else {
            correctUsedLetters.add(guessedLetter);
        }

        return correctLetterIndices;
    }

    public boolean wonGame() {
        boolean wonGame = true;

        for (int i = 0; i < gameWord.length(); i++) {
            String currentLetter = gameWord.substring(i, i+1);

            if (!correctUsedLetters.contains(currentLetter) && !currentLetter.equals(" ")) {
                wonGame = false;
            }
        }

        return wonGame;
    }

    public String getGameWord() {
        return gameWord;
    }

    public int getRemainingTurns() {
        return remainingTurns;
    }
}
