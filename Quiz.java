
import java.util.ArrayList;
import java.util.Scanner;

public class Quiz {

    ArrayList<Question> questions = new ArrayList<>();
    int score = 0;

    // Add a question
    public void addQuestion(Question question) {
        questions.add(question);
    }

    // Display all questions
    public void startQuiz(Scanner scanner) {

        score = 0;

        if (questions.isEmpty()) {
            System.out.println("No questions available!");
            return;
        }

        System.out.println("\n===== QUIZ STARTED =====");

        for (Question question : questions) {

            question.displayQuestion();

            System.out.print("Enter your answer (1-4): ");
            int answer = scanner.nextInt();

            if (question.checkAnswer(answer)) {
                System.out.println("Correct Answer!");
                score++;
            } else {
                System.out.println("Wrong Answer!");
            }
        }

        System.out.println("\n===== QUIZ RESULT =====");
        System.out.println("Total Questions: " + questions.size());
        System.out.println("Correct Answers: " + score);
        System.out.println("Wrong Answers: " + (questions.size() - score));
        System.out.println("Final Score: " + score + "/" + questions.size());
    }
}
