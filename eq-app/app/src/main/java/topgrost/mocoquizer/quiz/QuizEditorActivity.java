package topgrost.mocoquizer.quiz;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

import topgrost.mocoquizer.MainActivity;
import topgrost.mocoquizer.R;
import topgrost.mocoquizer.model.Question;
import topgrost.mocoquizer.model.Quiz;
import topgrost.mocoquizer.quiz.view.QuestionEditorFragment;

public class QuizEditorActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Question> questions = new LinkedList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quiz_editor);

        Button btnAddQuestion = findViewById(R.id.quizEditorAddQuestion);
        btnAddQuestion.setOnClickListener(this);

        Button btnSave = findViewById(R.id.quizEditorSave);
        btnSave.setOnClickListener(this);

        Button btnReset = findViewById(R.id.quizEditorReset);
        btnReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quizEditorAddQuestion:
                showQuestionEditor();
                break;
            case R.id.quizEditorSave:
                saveQuiz();
                break;
            case R.id.quizEditorReset:
                restQuiz();
                break;
        }
    }

    private void showQuestionEditor() {
        FragmentManager fm = getFragmentManager();
        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.add(R.id.quizEditorFragmentContainer, new QuestionEditorFragment());
        transaction.commit();
    }

    private void saveQuiz() {
        final Quiz quiz = new Quiz();
        quiz.setName(((TextView) findViewById(R.id.quizEditorName)).getText().toString());
        quiz.setQuestions(new LinkedList<>(questions));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference quizRef = database.getReference(Quiz.class.getSimpleName().toLowerCase() + "s");
        quizRef.push().setValue(quiz);

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private void restQuiz() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void addQuestion(Question questionToAdd) {
        questions.add(questionToAdd);

        final TextView tvQuestionAdded = new TextView(getBaseContext());
        tvQuestionAdded.setText(formatQuestionText(questionToAdd));

        LinearLayout lyContentContainer = findViewById(R.id.quizEditorContentContainer);
        lyContentContainer.addView(tvQuestionAdded);
    }

    private String formatQuestionText(Question question) {
        String questionText = question.getText();
        if (questionText == null) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("Frage ");
        sb.append(questions.size());
        sb.append(": ");
        if (questionText.length() > 30) {
            questionText = questionText.substring(0, 30) + "...";
        }
        sb.append(questionText);
        sb.append(" (");
        sb.append(question.getTime_seconds());
        sb.append(" Sekunden Zeit)");
        return sb.toString();
    }
}
