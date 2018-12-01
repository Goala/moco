package topgrost.mocoquizer.quiz;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.quiz.model.Quiz;

public class QuizActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_quiz);

        Button btnAddQuestion = findViewById(R.id.btnAddQuiz);
        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO add Question Container to layout
            }
        });
    }

    private void save(){
        Quiz quiz = new Quiz();

        final TextView tvName = findViewById(R.id.name);
        quiz.setName(tvName.getText().toString());

        // TODO generate json and save in firebase
    }
}
