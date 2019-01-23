package topgrost.mocoquizer.quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import topgrost.mocoquizer.BaseActivity;
import topgrost.mocoquizer.R;
import topgrost.mocoquizer.browser.GameBrowserActivity;
import topgrost.mocoquizer.lobby.LobbyActivity;
import topgrost.mocoquizer.model.Game;
import topgrost.mocoquizer.model.Question;
import topgrost.mocoquizer.model.Quiz;

public class QuizActivity extends BaseActivity implements View.OnClickListener {

    private int score = 0;
    private Question currentQuestion;
    private Quiz quiz;
    private int playerNumber;
    private int questionCount;
    private String gameId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quiz);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String user = sharedPref.getString("user", "NoUser");

        findViewById(R.id.quizSendAnswer).setOnClickListener(this);

        playerNumber = getIntent().getIntExtra(LobbyActivity.PLAYER_NUMBER_KEY, 0);
        questionCount = getIntent().getIntExtra(LobbyActivity.QUESTION_COUNT_KEY, 0);
        gameId = getIntent().getStringExtra(LobbyActivity.GAME_ID_KEY);

        ((TextView) findViewById(R.id.quizPlayerText)).setText("Spieler" + playerNumber + ": " + user);

        quiz = (Quiz) getIntent().getSerializableExtra(Quiz.class.getSimpleName().toLowerCase());
        ProgressBar progressBar = findViewById(R.id.quizTimeProgressBar);
        progressBar.setMax(getIntent().getIntExtra(LobbyActivity.QUESTION_TIME_KEY, 0));

        registerListeners();
    }

    @Override
    public void onClick(View v) {
        evaluateAnswer();
    }

    private void registerListeners() {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference questionNrRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(gameId).child("questionNr");
            questionNrRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Long questionIndex = (Long) dataSnapshot.getValue();
                        if (questionIndex == null) {
                            Log.d(QuizActivity.class.getSimpleName(), "Got invalid question number");
                            return;
                        }

                        currentQuestion = quiz.getQuestions().get(questionIndex.intValue());
                        int questionNumber = questionIndex.intValue() + 1;
                        updateQuestionData(questionNumber);
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

            DatabaseReference timeLeftRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(gameId).child("currentTime");
            timeLeftRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Long remainingTime = (Long) dataSnapshot.getValue();

                    ProgressBar progressBar = findViewById(R.id.quizTimeProgressBar);
                    progressBar.setProgress(remainingTime.intValue());

                    View btnSendAnswer = findViewById(R.id.quizSendAnswer);
                    if (remainingTime >= progressBar.getMax() && btnSendAnswer.isEnabled()) {
                        btnSendAnswer.callOnClick();
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
        updateEnablement(false);

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
        String scoreOfPlayer = "score" + playerNumber;
        if(correctAnswer) {
            Toast.makeText(QuizActivity.this, "Richtig!", Toast.LENGTH_SHORT).show();
            score += 30;
        } else {
            String player = "feed" + playerNumber;
            database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(gameId).child(player).setValue(true);
            Toast.makeText(QuizActivity.this, "Falsch!", Toast.LENGTH_SHORT).show();
            score -= 10;
        }
        database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(gameId).child(scoreOfPlayer).setValue(score);
        ((TextView) findViewById(R.id.quizScore)).setText("Punkte: " + String.valueOf(score));
        checkGameIsOver();
    }

    private void checkGameIsOver() {
        if(quiz.getQuestions().indexOf(currentQuestion) + 1 >= quiz.getQuestions().size()) {
            Intent intent = new Intent(getApplicationContext(), QuizResultActivity.class);
            intent.putExtra(LobbyActivity.GAME_ID_KEY, getIntent().getStringExtra(LobbyActivity.GAME_ID_KEY));
            startActivity(intent);
        }
    }

    private void updateQuestionData(int questionNumber) {
        ((TextView) findViewById(R.id.quizQuestionCount)).setText("Frage " + questionNumber + " von " + questionCount);
        ((TextView) findViewById(R.id.quizQuestionText)).setText(currentQuestion.getText());

        ((TextView) findViewById(R.id.quizAnswerText1)).setText(currentQuestion.getAnswers().get(0).getText());
        ((CheckBox) findViewById(R.id.quizAnswer1)).setChecked(false);

        ((TextView) findViewById(R.id.quizAnswerText2)).setText(currentQuestion.getAnswers().get(1).getText());
        ((CheckBox) findViewById(R.id.quizAnswer2)).setChecked(false);

        ((TextView) findViewById(R.id.quizAnswerText3)).setText(currentQuestion.getAnswers().get(2).getText());
        ((CheckBox) findViewById(R.id.quizAnswer3)).setChecked(false);
    }

    private void updateEnablement(boolean enable) {
        findViewById(R.id.quizAnswer1).setEnabled(enable);
        findViewById(R.id.quizAnswer2).setEnabled(enable);
        findViewById(R.id.quizAnswer3).setEnabled(enable);
        findViewById(R.id.quizSendAnswer).setEnabled(enable);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit Game")
                .setMessage("Möchten Sie die das Spiel verlassen?")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), GameBrowserActivity.class);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("Nein", null)
                .show();
    }
}
