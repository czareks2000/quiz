package pl.edu.pb.quiz;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    private static final String KEY_SCORE = "score";
    private static final String QUIZ_TAG = "MainActivity";
    public static final String KEY_EXTRA_ANSWER = "pl.edu.pb.wi.quiz.correctAnswer";
    private ActivityResultLauncher<Intent> promptLauncher;
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button hintButton;
    private TextView questionTextView;
    private int currentIndex = 0;
    private int score = 0;
    private boolean answerWasShown = false;
    private Question[] questions = new Question[] {
            new Question(R.string.q_etf_definition, true),
            new Question(R.string.q_bond_risk, true),
            new Question(R.string.q_diversification, true),
            new Question(R.string.q_etf_expenses, false),
            new Question(R.string.q_stocks, false)
    };

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(QUIZ_TAG, "Wywołana została metoda onSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
        outState.putInt(KEY_SCORE, score);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(QUIZ_TAG, "Wywołana została metoda onCreate");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
            score = savedInstanceState.getInt(KEY_SCORE);
        }

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        hintButton = findViewById(R.id.hint_button);
        questionTextView = findViewById(R.id.question_text_view);

        trueButton.setOnClickListener(v -> checkAnswerCorrectness(true));

        falseButton.setOnClickListener(v -> checkAnswerCorrectness(false));

        promptLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
                        }
                    }
                }
        );

        hintButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PromptActivity.class);
            boolean correctAnswer = questions[currentIndex].isTrueAnswer();
            intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
            promptLauncher.launch(intent);
        });

        nextButton.setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % (questions.length+1);
            answerWasShown = false;
            setNextQuestion();
        });

        setNextQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(QUIZ_TAG, "Wywołana została metoda onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(QUIZ_TAG, "Wywołana została metoda onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(QUIZ_TAG, "Wywołana została metoda onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(QUIZ_TAG, "Wywołana została metoda onDestroy");
    }

    private void checkAnswerCorrectness(boolean userAnswer) {
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId = 0;
        if (answerWasShown) {
            resultMessageId = R.string.answer_was_shown;
        } else {
            if (userAnswer == correctAnswer) {
                resultMessageId = R.string.correct_answer;
                score++;
            } else {
                resultMessageId = R.string.incorrect_answer;
            }
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
            currentIndex = -1;
        } else {
            enableButtons(true);
            questionTextView.setText(questions[currentIndex].getQuestionId());
        }
    }

    private void enableButtons(boolean state) {
        trueButton.setEnabled(state);
        falseButton.setEnabled(state);
        hintButton.setEnabled(state);
    }
}
