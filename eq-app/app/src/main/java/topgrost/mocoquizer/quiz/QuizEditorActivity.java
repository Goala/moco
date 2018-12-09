package topgrost.mocoquizer.quiz;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.quiz.model.Question;
import topgrost.mocoquizer.quiz.view.QuestionEditorFragment;

public class QuizEditorActivity extends AppCompatActivity {

    private List<Question> questions = new LinkedList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quiz_editor);

        Button btnAddQuestion = findViewById(R.id.quizEditorAddQuestion);
        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                final FragmentTransaction transaction = fm.beginTransaction();
                transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                transaction.add(R.id.quizEditorFragmentContainer, new QuestionEditorFragment());
                transaction.commit();
            }
        });
    }

    public void addQuestion(Question questionToAdd){
        questions.add(questionToAdd);

        final TextView tvQuestionAdded = new TextView(getBaseContext());
        tvQuestionAdded.setText(formatQuestionText(questionToAdd));

        LinearLayout lyContentContainer = findViewById(R.id.quizEditorContentContainer);
        lyContentContainer.addView(tvQuestionAdded);
    }

    private String formatQuestionText(Question question){
        String questionText = question.getText();
        if(questionText == null){
            return questionText;
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("Frage ");
        sb.append(questions.size());
        sb.append(": ");

        if(questionText.length() > 30) {
            questionText = questionText.substring(0, 30) + "...";
        }
        sb.append(questionText);
        sb.append(" (");
        sb.append(question.getTime_seconds());
        sb.append(" Sekunden Zeit)");
        return sb.toString();
    }
}
