package topgrost.mocoquizer.quiz;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import topgrost.mocoquizer.BaseActivity;
import java.util.Timer;
import java.util.TimerTask;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.browser.GameBrowserActivity;
import topgrost.mocoquizer.lobby.LobbyActivity;
import topgrost.mocoquizer.model.Game;
import topgrost.mocoquizer.model.Question;
import topgrost.mocoquizer.model.Quiz;

public class QuizActivity extends BaseActivity implements ValueEventListener, View.OnClickListener {

    private int score = 0;
    private Timer timer = new Timer();
    private Question currentQuestion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quiz);

        findViewById(R.id.quizSendAnswer).setOnClickListener(this);
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(getIntent().getStringExtra(LobbyActivity.GAME_ID_KEY)).child("questionNr");
            gameRef.addValueEventListener(this);
        } catch (Exception e) {
            Toast.makeText(QuizActivity.this, "Fehler beim Laden der Quiz-Daten", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        try {
            Long questionNr = (Long) dataSnapshot.getValue();
            Quiz quiz = (Quiz) getIntent().getSerializableExtra(Quiz.class.getSimpleName().toLowerCase());

            ProgressBar progressBar = findViewById(R.id.quizTimeProgressBar);
            if(currentQuestion != null && progressBar.getProgress() < progressBar.getMax()) {
                evaluateAnswer();
            }
            currentQuestion = quiz.getQuestions().get(questionNr.intValue());
            updateQuestionData();
            startTimer();
        } catch (Exception e) {
            Toast.makeText(QuizActivity.this, "Fehler beim Aktualisieren der Frage", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(QuizActivity.this, "Fehler beim Aktualisieren der Frage", Toast.LENGTH_LONG).show();
        System.out.println(databaseError.getMessage());
    }

    @Override
    public void onClick(View v) {
        // Set progress to max and disable edit of answer
        ProgressBar progressBar = findViewById(R.id.quizTimeProgressBar);
        progressBar.setProgress(progressBar.getMax());
        updateEnablement(false);
    }

    private void evaluateAnswer() {
        boolean correctAnswer = true;
        if(!currentQuestion.getAnswers().get(0).getCorrect().equals(((CheckBox) findViewById(R.id.quizAnswer1)).isChecked())){
            correctAnswer = false;
        }
        if(!currentQuestion.getAnswers().get(1).getCorrect().equals(((CheckBox) findViewById(R.id.quizAnswer2)).isChecked())){
            correctAnswer = false;
        }
        if(!currentQuestion.getAnswers().get(2).getCorrect().equals(((CheckBox) findViewById(R.id.quizAnswer3)).isChecked())){
            correctAnswer = false;
        }

        if(correctAnswer) {
            Toast.makeText(QuizActivity.this, "Richtig!", Toast.LENGTH_SHORT).show();
            score += 30;
        } else {
            // TODO LG set feedback required to true for current user
            Toast.makeText(QuizActivity.this, "Falsch!", Toast.LENGTH_SHORT).show();
            score -= 10;
        }
        ((TextView) findViewById(R.id.quizScore)).setText(String.valueOf(score));
    }

    private void updateQuestionData() {
        ((TextView) findViewById(R.id.quizQuestionText)).setText(currentQuestion.getText());
        ((TextView) findViewById(R.id.quizAnswerText1)).setText(currentQuestion.getAnswers().get(0).getText());
        ((TextView) findViewById(R.id.quizAnswerText2)).setText(currentQuestion.getAnswers().get(1).getText());
        ((TextView) findViewById(R.id.quizAnswerText3)).setText(currentQuestion.getAnswers().get(2).getText());
    }

    private void updateEnablement(boolean enable) {
        findViewById(R.id.quizAnswer1).setEnabled(enable);
        findViewById(R.id.quizAnswer2).setEnabled(enable);
        findViewById(R.id.quizAnswer3).setEnabled(enable);
        findViewById(R.id.quizSendAnswer).setEnabled(enable);
    }

    private void startTimer() {
        ProgressBar progressBar = findViewById(R.id.quizTimeProgressBar);
        progressBar.setMax(currentQuestion.getTime_seconds());
        progressBar.setProgress(0);

        timer.scheduleAtFixedRate(new ProgressUpdateTask(), 0, DateUtils.SECOND_IN_MILLIS * currentQuestion.getTime_seconds());
        updateEnablement(true);
    }

    private class ProgressUpdateTask extends TimerTask {

        @Override
        public void run() {
            QuizActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    ProgressBar progressBar = findViewById(R.id.quizTimeProgressBar);
                    if (progressBar.getProgress() >= progressBar.getMax()) {
                        evaluateAnswer();
                        cancel();
                    } else {
                        progressBar.setProgress(progressBar.getProgress() + 1);
                    }
                }
            });
        }
    }
}
