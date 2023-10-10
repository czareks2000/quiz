package pl.edu.pb.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private TextView questionTextView;
    private int currentIndex = 0;
    private int score = 0;
    private Question[] questions = new Question[] {
            new Question(R.string.q_etf_definition, true),
            new Question(R.string.q_bond_risk, true),
            new Question(R.string.q_diversification, true),
            new Question(R.string.q_etf_expenses, false),
            new Question(R.string.q_stocks, false)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        questionTextView = findViewById(R.id.question_text_view);

        trueButton.setOnClickListener(v -> checkAnswerCorrectness(true));

        falseButton.setOnClickListener(v -> checkAnswerCorrectness(false));

        nextButton.setOnClickListener(v -> {
            currentIndex++;
            setNextQuestion();
        });

        setNextQuestion();
    }

    private void checkAnswerCorrectness(boolean userAnswer) {
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId = 0;
        if (userAnswer == correctAnswer) {
            resultMessageId = R.string.correct_answer;
            score++;
        } else {
            resultMessageId = R.string.incorrect_answer;
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
        enableButtons(false);
    }

    private void setNextQuestion() {
        if (currentIndex == questions.length)
        {
            enableButtons(false);
            questionTextView.setText(String.format("Koniec pytań. Twój wynik: %s / %s", score, questions.length));
            score = 0;
            currentIndex = 0;
        } else {
            enableButtons(true);
            questionTextView.setText(questions[currentIndex].getQuestionId());
        }
    }

    private void enableButtons(boolean state) {
        trueButton.setEnabled(state);
        falseButton.setEnabled(state);
    }
}
