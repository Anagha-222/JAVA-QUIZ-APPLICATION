
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Quiz quiz = new Quiz();

        System.out.println("===== JAVA QUIZ APPLICATION =====");

        System.out.print("How many questions do you want to add? ");
        int totalQuestions = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= totalQuestions; i++) {

            System.out.println("\nEnter Question " + i);

            System.out.print("Enter question: ");
            String questionText = scanner.nextLine();

            String[] options = new String[4];

            for (int j = 0; j < 4; j++) {
                System.out.print("Enter Option " + (j + 1) + ": ");
                options[j] = scanner.nextLine();
            }

            System.out.print("Enter correct answer number (1-4): ");
            int correctAnswer = scanner.nextInt();
            scanner.nextLine();

            Question question = new Question(
                i, questionText, options, correctAnswer
            );

            quiz.addQuestion(question);
        }

        System.out.println("\nAll questions added successfully!");

        quiz.startQuiz(scanner);

        scanner.close();
    }
}
