package topgrost.mocoquizer.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.*;
import topgrost.mocoquizer.BaseActivity;
import topgrost.mocoquizer.MainActivity;
import topgrost.mocoquizer.R;
import topgrost.mocoquizer.lobby.LobbyActivity;
import topgrost.mocoquizer.model.Game;
import topgrost.mocoquizer.model.Quiz;

public class QuizResultActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quiz_result);

        loadResults();

        findViewById(R.id.quizResultClose).setOnClickListener(this);
    }

    private void loadResults() {
        for(int i = 1; i <= 4; i++) {
            registerValueEventListener(i);
        }
    }

    private void registerValueEventListener(final int playerNumber) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(getIntent().getStringExtra(LobbyActivity.GAME_ID_KEY)).child("score" + playerNumber);
        gameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Long scoreValue = (Long) dataSnapshot.getValue();
                    switch (playerNumber) {
                        case 1:
                            ((TextView)findViewById(R.id.quizResultScore1)).setText("Punkte Spieler1: " + scoreValue);
                            break;
                        case 2:
                            ((TextView)findViewById(R.id.quizResultScore2)).setText("Punkte Spieler2: " + scoreValue);
                            break;
                        case 3:
                            ((TextView)findViewById(R.id.quizResultScore3)).setText("Punkte Spieler3: " + scoreValue);
                            break;
                        case 4:
                            ((TextView)findViewById(R.id.quizResultScore4)).setText("Punkte Spieler4: " + scoreValue);
                            break;
                    }
                } catch (Exception e) {
                    Toast.makeText(QuizResultActivity.this, "Fehler beim Aktualisieren der Highscore", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuizResultActivity.this, "Fehler beim Aktualisieren der Highscore", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
