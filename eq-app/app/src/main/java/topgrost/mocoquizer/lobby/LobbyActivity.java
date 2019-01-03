package topgrost.mocoquizer.lobby;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.browser.GameBrowserActivity;
import topgrost.mocoquizer.model.Game;
import topgrost.mocoquizer.model.Question;
import topgrost.mocoquizer.model.Quiz;
import topgrost.mocoquizer.quiz.QuizActivity;

public class LobbyActivity extends AppCompatActivity {

    public static final String GAME_ID_KEY = "gameId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        final Game game = (Game) getIntent().getSerializableExtra(Game.class.getSimpleName().toLowerCase());

        TextView title = findViewById(R.id.lobbyTitle);
        title.setText(game.getName());

        Button lobbyPlayer1 = findViewById(R.id.lobbyPlayer1);
        lobbyPlayer1.setText(game.getPlayers().get(0).getName());

        Button btnStartGame = findViewById(R.id.lobbyStartGame);
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference quizRef = database.getReference(Quiz.class.getSimpleName().toLowerCase() + "s");
                quizRef.orderByChild("name").equalTo(game.getQuizId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Quiz quiz = dataSnapshot.getChildren().iterator().next().getValue(Quiz.class);

                        Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                        intent.putExtra(Quiz.class.getSimpleName().toLowerCase(),quiz);
                        intent.putExtra(GAME_ID_KEY, game.getFirebaseKey());
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(LobbyActivity.this, "Spiel konnte nicht gestartet werden. Fehler beim Laden der Quiz-Daten", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
