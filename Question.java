
public class Question {

    int questionId;
    String questionText;
    String[] options;
    int correctAnswer;

    public Question(int questionId, String questionText,
                    String[] options, int correctAnswer) {

        this.questionId = questionId;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public void displayQuestion() {

        System.out.println("\n" + questionId + ". " + questionText);

        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
    }

    public boolean checkAnswer(int answer) {
        return answer == correctAnswer;
    }
}
