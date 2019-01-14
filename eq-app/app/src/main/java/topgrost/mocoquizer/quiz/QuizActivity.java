package topgrost.mocoquizer.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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

import topgrost.mocoquizer.BaseActivity;
import topgrost.mocoquizer.R;
import topgrost.mocoquizer.lobby.LobbyActivity;
import topgrost.mocoquizer.model.Game;
import topgrost.mocoquizer.model.Question;
import topgrost.mocoquizer.model.Quiz;

public class QuizActivity extends BaseActivity implements View.OnClickListener {

    private int score = 0;
    private Question currentQuestion;
    private Quiz quiz;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quiz);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String user = sharedPref.getString("user", "NoUser");

        findViewById(R.id.quizSendAnswer).setOnClickListener(this);
        ((TextView) findViewById(R.id.quizPlayerText)).setText("Spieler" + getIntent().getIntExtra(LobbyActivity.PLAYER_NUMBER_KEY, 0) + ": " + user);

        registerListeners();
    }

    @Override
    public void onClick(View v) {
        // Set progress to max and disable edit of answer
        ProgressBar progressBar = findViewById(R.id.quizTimeProgressBar);
        progressBar.setProgress(progressBar.getMax());
        updateEnablement(false);
    }

    private void registerListeners() {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(getIntent().getStringExtra(LobbyActivity.GAME_ID_KEY)).child("questionNr");
            gameRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Long questionNr = (Long) dataSnapshot.getValue();
                        quiz = (Quiz) getIntent().getSerializableExtra(Quiz.class.getSimpleName().toLowerCase());

                        ProgressBar progressBar = findViewById(R.id.quizTimeProgressBar);
                        if(currentQuestion != null && progressBar.getProgress() < progressBar.getMax()) {
                            evaluateAnswer();
                        }
                        currentQuestion = quiz.getQuestions().get(questionNr.intValue());
                        updateQuestionData();
                        updateEnablement(true);
                    } catch (Exception e) {
                        Toast.makeText(QuizActivity.this, "Fehler beim Aktualisieren der Frage", Toast.LENGTH_SHORT).show();
                        Log.d(QuizActivity.class.getSimpleName(), e.getMessage());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(QuizActivity.this, "Fehler beim Aktualisieren der Frage", Toast.LENGTH_SHORT).show();
                    Log.d(QuizActivity.class.getSimpleName(), databaseError.getMessage());
                }
            });

            DatabaseReference remainingTimeRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(getIntent().getStringExtra(LobbyActivity.GAME_ID_KEY)).child("remainingTime");
            remainingTimeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Long remainingTime = (Long) dataSnapshot.getValue();

                    ProgressBar progressBar = findViewById(R.id.quizTimeProgressBar);
                    progressBar.setProgress(remainingTime.intValue());

                    if (remainingTime >= progressBar.getMax()) {
                        updateEnablement(false);
                        evaluateAnswer();
                        checkGameIsOver();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(QuizActivity.this, "Fehler beim Aktualisieren der Frage", Toast.LENGTH_SHORT).show();
                    Log.d(QuizActivity.class.getSimpleName(), databaseError.getMessage());
                }
            });
        } catch (Exception e) {
            Toast.makeText(QuizActivity.this, "Fehler beim Laden der Quiz-Daten", Toast.LENGTH_SHORT).show();
            Log.d(QuizActivity.class.getSimpleName(), e.getMessage());
        }
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String game = getIntent().getStringExtra(LobbyActivity.GAME_ID_KEY);
        String scoreOfPlayer = "score" + getIntent().getIntExtra(LobbyActivity.PLAYER_NUMBER_KEY, 0);
        if(correctAnswer) {
            Toast.makeText(QuizActivity.this, "Richtig!", Toast.LENGTH_SHORT).show();
            score += 30;
        } else {
            String player = "feed" + getIntent().getIntExtra(LobbyActivity.PLAYER_NUMBER_KEY, 0);
            database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(game).child(player).setValue(true);
            Toast.makeText(QuizActivity.this, "Falsch!", Toast.LENGTH_SHORT).show();
            score -= 10;
        }
        database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(game).child(scoreOfPlayer).setValue(score);
        ((TextView) findViewById(R.id.quizScore)).setText(String.valueOf(score));
        ((CheckBox) findViewById(R.id.quizAnswer1)).setChecked(false);
        ((CheckBox) findViewById(R.id.quizAnswer2)).setChecked(false);
        ((CheckBox) findViewById(R.id.quizAnswer2)).setChecked(false);
    }

    private void checkGameIsOver() {
        if(quiz == null || currentQuestion == null) {
            return;
        }

        if(quiz.getQuestions().indexOf(currentQuestion) + 1 >= quiz.getQuestions().size()) {
            Intent intent = new Intent(getApplicationContext(), QuizResultActivity.class);
            intent.putExtra(LobbyActivity.GAME_ID_KEY, getIntent().getStringExtra(LobbyActivity.GAME_ID_KEY));
            startActivity(intent);
        }
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
}
