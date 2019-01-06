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

import java.util.Timer;
import java.util.TimerTask;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.browser.GameBrowserActivity;
import topgrost.mocoquizer.lobby.LobbyActivity;
import topgrost.mocoquizer.model.Game;
import topgrost.mocoquizer.model.Question;
import topgrost.mocoquizer.model.Quiz;

public class QuizActivity extends AppCompatActivity implements ValueEventListener {

    private Timer timer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quiz);

        timer = new Timer();
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
            final Question currentQuestion = quiz.getQuestions().get(questionNr.intValue());
            updateQuestionData(currentQuestion);
            startTimer(currentQuestion);
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

    private void updateQuestionData(Question question) {
        ((TextView) findViewById(R.id.quizQuestionText)).setText(question.getText());
        ((TextView) findViewById(R.id.quizAnswerText1)).setText(question.getAnswers().get(0).getText());
        ((TextView) findViewById(R.id.quizAnswerText2)).setText(question.getAnswers().get(1).getText());
        ((TextView) findViewById(R.id.quizAnswerText3)).setText(question.getAnswers().get(2).getText());
    }

    private void updateEnablement(boolean enable) {
        findViewById(R.id.quizAnswer1).setEnabled(enable);
        findViewById(R.id.quizAnswer2).setEnabled(enable);
        findViewById(R.id.quizAnswer3).setEnabled(enable);
        findViewById(R.id.quizSendAnswer).setEnabled(enable);
    }

    private void startTimer(Question question) {
        ProgressBar progressBar = findViewById(R.id.quizTimeProgressBar);
        progressBar.setMax(question.getTime_seconds());
        progressBar.setProgress(0);

        timer.scheduleAtFixedRate(new ProgressUpdateTask(), DateUtils.SECOND_IN_MILLIS, DateUtils.SECOND_IN_MILLIS * question.getTime_seconds());
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
                        updateEnablement(false);
                        cancel();
                    } else {
                        progressBar.setProgress(progressBar.getProgress() + 1);
                    }
                }
            });
        }
    }
}
