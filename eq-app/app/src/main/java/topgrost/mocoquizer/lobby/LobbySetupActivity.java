package topgrost.mocoquizer.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import topgrost.mocoquizer.R;
import topgrost.mocoquizer.lobby.model.Device;
import topgrost.mocoquizer.lobby.model.Game;
import topgrost.mocoquizer.quiz.model.Quiz;

public class LobbySetupActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lobby_setup);

        loadAndSetDeviceList();
        loadAndSetQuizList();

        Button btnStartGame = findViewById(R.id.lobbySetupCreate);
        btnStartGame.setOnClickListener(this);
    }

    private void loadAndSetDeviceList() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference deviceRef = database.getReference(Device.class.getSimpleName().toLowerCase());

        Spinner spinner = findViewById(R.id.lobbySetupDevice);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item, new String[]{"wer23489_Arduino Mega"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    private void loadAndSetQuizList() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference quizRef = database.getReference(Quiz.class.getSimpleName().toLowerCase());

        Spinner spinner = findViewById(R.id.lobbySetupDevice);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),
            android.R.layout.simple_spinner_dropdown_item, new String[]{"df234ert_Quiz 1"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
