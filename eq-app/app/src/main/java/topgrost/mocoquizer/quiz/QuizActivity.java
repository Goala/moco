package topgrost.mocoquizer.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.model.Quiz;

public class QuizActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quiz);

        Intent intent = getIntent();
        Quiz quiz = (Quiz) intent.getSerializableExtra(Quiz.class.getSimpleName().toLowerCase());

        ProgressBar pbTimeOut = findViewById(R.id.quizTimeProgressBar);
        pbTimeOut.setMax(30);
    }
}
