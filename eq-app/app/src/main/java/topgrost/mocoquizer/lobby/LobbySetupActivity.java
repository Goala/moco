package topgrost.mocoquizer.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import topgrost.mocoquizer.R;
import topgrost.mocoquizer.lobby.model.Game;

public class LobbySetupActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lobby_setup);

        Button btnStartGame = findViewById(R.id.lobbySetupCreate);
        btnStartGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Game game = new Game();
        game.setRunning(false);
        game.setName(((TextView) findViewById(R.id.lobbySetupName)).getText().toString());
        game.setPassword(((TextView) findViewById(R.id.lobbySetupPassword)).getText().toString());
        game.setDeviceId(((Spinner) findViewById(R.id.lobbySetupDevice)).getSelectedItem().toString());
        game.setQuizId(((Spinner) findViewById(R.id.lobbySetupQuiz)).getSelectedItem().toString());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase());
        gameRef.push().setValue(game);

        startActivity(new Intent(getApplicationContext(), LobbyActivity.class));
    }
}
