import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    private static final int MAX_MISTAKES = 6;
    private static boolean isWin;
    private static boolean isLose;
    private static String usedLetters;
    private static final int EASY = 1;

    public static void main(String[] args) throws FileNotFoundException {
        List<String> words = readWords();

        boolean isPlayAgain;
        do {
            startGameLoop(words);

            System.out.println("Хотите сыграть еще раз? д/н");
            Scanner scanner = new Scanner(System.in);
            char answer = scanner.next().charAt(0);
            isPlayAgain = answer == 'д';
        } while (isPlayAgain);
    }

    private static void startGameLoop(List<String> words) {
        System.out.println("Выберите уровень сложности 1 или 2");
        System.out.println("1 - легкий, будут открыты две случайные буквы в маске слова, 2 - сложный, без подсказок:");
        Scanner sc = new Scanner(System.in);
        int gameLevel = sc.nextInt();

        String word = getRandomWord(words);
        String mask = "*".repeat(word.length());
        usedLetters = "";

        if (gameLevel == EASY) {
            mask = openLettersForEasyLevel(word, mask, 2);
        }

        System.out.println("Добро пожаловать на игру виселица!");
        int mistakes = 0;
        isWin = false;
        isLose = false;

        do {
            printHangman(mistakes);

            System.out.println("Количество ошибок: " + mistakes);
            System.out.println(mask);

            Scanner scanner = new Scanner(System.in);
            char letter = inputValidLetter(scanner);

            if (word.indexOf(letter) != -1) {
                mask = openLetter(word, mask, letter);
            } else {
                mistakes++;
            }

            System.out.println("Использованные буквы:" + Arrays.toString(usedLetters.toCharArray()));

            checkGameState(word, mask, mistakes);
        } while (!isWin && !isLose);

        printHangman(mistakes);
        printGameResult(word);
    }

    private static String openLettersForEasyLevel(String word, String mask, int letterCount) {
        Random random = new Random();
        for (int i = 0; i < letterCount; i++) {
            int randomIndex = random.nextInt(word.length());
            char letter = word.charAt(randomIndex);
            mask = openLetter(word, mask, letter);
        }
        return mask;
    }

    private static char inputValidLetter(Scanner scanner) {
        System.out.println("Введите одну букву русского алфавита:");
        while (true) {
            String input = scanner.nextLine().toLowerCase();
            char letter = input.charAt(0);
            if (input.length() > 1) {
                System.out.println("Некорректный ввод! Введите только одну букву.");
                continue;
            }
            if (!input.matches("[а-яё]")) {
                System.out.println("Некорректный ввод! Введите одну букву русского алфавита.");
                continue;
            }
            if (usedLetters.contains(input)) {
                System.out.println("Вы уже вводили эту букву: " + input + ". Введите другую букву.");
                continue;
            }

            usedLetters += input;
            return letter;
        }
    }

    private static String openLetter(String word, String mask, char letter) {
        char[] maskCharArray = mask.toCharArray();

        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter) {
                maskCharArray[i] = letter;
            }
        }
        return new String(maskCharArray);
    }

    private static void checkGameState(String word, String mask, int mistakes) {
        if (mistakes == MAX_MISTAKES) {
            isLose = true;
        } else if (mask.equals(word)) {
            isWin = true;
        }
    }

    private static void printGameResult(String word) {
        if (isLose) {
            System.out.println("Увы, Вы проиграли. Загаданное слово: " + word);
        }
        if (isWin) {
            System.out.println("Поздравляем, Вы победили!");
        }
    }

    private static List<String> readWords() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("words.txt"));

        List<String> words = new ArrayList<>();

        while (scanner.hasNext()) {
            words.add(scanner.nextLine().toLowerCase().trim());
        }

        return words;
    }

    private static String getRandomWord(List<String> words) {
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    private static void printHangman(int mistakes) {
        String[] stages = new String[7];
        stages[0] = """
                     ________
                     |      |
                     |
                     |
                     |
                     |
                    _|_
                    """;
        stages[1] = """
                     ________
                     |      |
                     |      o
                     |
                     |
                     |
                    _|_
                    """;

        stages[2] = """
                     ________
                     |      |
                     |      o
                     |      |
                     |      |
                     |
                    _|_
                    """;
        stages[3] = """
                     ________
                     |      |
                     |      o
                     |      |/
                     |      |
                     |
                    _|_
                    """;
        stages[4] = """
                     ________
                     |      |
                     |      o
                     |     \\|/
                     |      |
                     |
                    _|_
                    """;
        stages[5] = """
                     ________
                     |      |
                     |      o
                     |     \\|/
                     |      |
                     |       \\
                    _|_
                    """;
        stages[6] = """
                     ________
                     |      |
                     |      o
                     |     \\|/
                     |      |
                     |     / \\
                    _|_
                    """;
        System.out.println(stages[mistakes]);
    }
}
