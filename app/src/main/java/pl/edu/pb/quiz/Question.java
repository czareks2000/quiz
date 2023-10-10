package pl.edu.pb.quiz;

public class Question {
    private int questionId;
    private boolean answer;

    public Question(int questionId, boolean trueAnswer) {
        this.questionId = questionId;
        this.answer = trueAnswer;
    }

    public boolean isTrueAnswer() {
        return answer;
    }

    public int getQuestionId() {
        return questionId;
    }
}
