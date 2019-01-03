package topgrost.mocoquizer.quiz;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.browser.GameBrowserActivity;
import topgrost.mocoquizer.lobby.LobbyActivity;
import topgrost.mocoquizer.model.Game;
import topgrost.mocoquizer.model.Question;
import topgrost.mocoquizer.model.Quiz;

public class QuizActivity extends AppCompatActivity implements ValueEventListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quiz);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(getIntent().getStringExtra(LobbyActivity.GAME_ID_KEY)).child("questionNr");
        gameRef.addValueEventListener(this);
    }

    private void updateQuestionData(Question question) {
        ((ProgressBar)findViewById(R.id.quizTimeProgressBar)).setMax(question.getTime_seconds());
        ((TextView)findViewById(R.id.quizQuestionText)).setText(question.getText());
        ((TextView)findViewById(R.id.quizAnswerText1)).setText(question.getAnswers().get(0).getText());
        ((TextView)findViewById(R.id.quizAnswerText2)).setText(question.getAnswers().get(1).getText());
        ((TextView)findViewById(R.id.quizAnswerText3)).setText(question.getAnswers().get(2).getText());
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        try {
            int questionNr = (int) dataSnapshot.getValue();
            Quiz quiz = (Quiz) getIntent().getSerializableExtra(Quiz.class.getSimpleName().toLowerCase());
            final Question currentQuestion = quiz.getQuestions().get(questionNr);
            updateQuestionData(currentQuestion);
        } catch (Exception e){
            Toast.makeText(QuizActivity.this, "Fehler beim Aktualisieren der Frage", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(QuizActivity.this, "Fehler beim Aktualisieren der Frage", Toast.LENGTH_LONG).show();
    }
}
