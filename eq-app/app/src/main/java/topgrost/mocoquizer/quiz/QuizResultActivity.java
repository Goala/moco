package topgrost.mocoquizer.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import topgrost.mocoquizer.BaseActivity;
import topgrost.mocoquizer.MainActivity;
import topgrost.mocoquizer.R;

public class QuizResultActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quiz_result);

        findViewById(R.id.quizResultClose).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
