package topgrost.mocoquizer.quiz;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.quiz.view.QuestionEditorFragment;

public class QuizEditorActivity extends AppCompatActivity {

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
                transaction.replace(R.id.quizEditorFragmentContainer, new QuestionEditorFragment());
                transaction.commit();
            }
        });
    }
}
