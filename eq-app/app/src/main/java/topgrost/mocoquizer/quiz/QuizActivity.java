package topgrost.mocoquizer.quiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import topgrost.mocoquizer.R;

public class QuizActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quiz);

        ProgressBar pbTimeOut = findViewById(R.id.quizTimeProgressBar);
        pbTimeOut.setMax(30);
    }
}
