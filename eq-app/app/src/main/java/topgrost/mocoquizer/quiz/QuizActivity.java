package topgrost.mocoquizer.quiz;

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
import topgrost.mocoquizer.quiz.model.Quiz;
import topgrost.mocoquizer.quiz.view.QuestionContainer;

public class QuizActivity extends AppCompatActivity {

    private List<QuestionContainer> questionContainers = new LinkedList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        init();
    }

    private void init() {
        Button btnAddQuestion = findViewById(R.id.quizBtnAddQuestion);
        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final QuestionContainer containerToAdd = new QuestionContainer(getBaseContext());
                questionContainers.add(containerToAdd);

                LinearLayout layout = findViewById(R.id.quizContentLayout);
                layout.addView(containerToAdd);
                layout.invalidate();
            }
        });
    }

    private void save(){
        Quiz quiz = new Quiz();

        final TextView tvName = findViewById(R.id.quizName);
        quiz.setName(tvName.getText().toString());

        for (QuestionContainer questionContainer : questionContainers) {
            quiz.getQuestions().add(questionContainer.getQuestion());
        }

        // TODO generate json and save in firebase
    }
}
