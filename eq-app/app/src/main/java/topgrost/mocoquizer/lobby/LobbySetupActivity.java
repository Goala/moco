package topgrost.mocoquizer.lobby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import topgrost.mocoquizer.BaseActivity;
import topgrost.mocoquizer.R;
import topgrost.mocoquizer.model.Device;
import topgrost.mocoquizer.model.Game;
import topgrost.mocoquizer.model.Quiz;

public class LobbySetupActivity extends BaseActivity implements View.OnClickListener {
    private String user;

    //Catch Blöcke bei Datenbankzugriffen

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lobby_setup);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user = sharedPref.getString("user", "");

        loadAndSetDeviceList();
        loadAndSetQuizList();

        Button btnStartGame = findViewById(R.id.lobbySetupCreate);
        btnStartGame.setOnClickListener(this);
    }

    private void loadAndSetDeviceList() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child(Device.class.getSimpleName().toLowerCase() + "s").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> devices = new ArrayList<>();

                for (DataSnapshot deviceDataSnapshot : dataSnapshot.getChildren()) {
                    Device device = deviceDataSnapshot.getValue(Device.class);
                    devices.add(device.getName());
                }

                String[] primDevices = new String[devices.size()];
                primDevices = devices.toArray(primDevices);

                setDeviceSpinnerData(primDevices);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LobbySetupActivity.this, "Fehler beim Laden der verfügbaren Geräte", Toast.LENGTH_LONG).show();
            }
        });

        setDeviceSpinnerData(new String[]{"Loading..."});
    }

    private void loadAndSetQuizList() {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child(Quiz.class.getSimpleName().toLowerCase() + "s").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> quizzes = new ArrayList<>();

                for (DataSnapshot quizDataSnapshot : dataSnapshot.getChildren()) {
                    Quiz quiz = quizDataSnapshot.getValue(Quiz.class);
                    quizzes.add(quiz.getName());
                }

                String[] primQuizzes = new String[quizzes.size()];
                primQuizzes = quizzes.toArray(primQuizzes);

                setQuizSpinnerData(primQuizzes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LobbySetupActivity.this, "Fehler beim Laden der verfügbaren Quiz-Daten", Toast.LENGTH_LONG).show();
            }
        });

        setQuizSpinnerData(new String[]{"Loading..."});
    }

    @Override
    public void onClick(View view) {
        Game game = new Game();
        game.setRunning(false);
        game.setName(((TextView) findViewById(R.id.lobbySetupName)).getText().toString());
        game.setPassword(((TextView) findViewById(R.id.lobbySetupPassword)).getText().toString());
        game.setDeviceId(((Spinner) findViewById(R.id.lobbySetupDevice)).getSelectedItem().toString());
        game.setQuizId(((Spinner) findViewById(R.id.lobbySetupQuiz)).getSelectedItem().toString());
        game.setPlayer1(user);
        game.setFeed1(false);
        game.setQuestionNr(0);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s");

        String gameID = gameRef.push().getKey();
        game.setFirebaseKey(gameID);
        gameRef.child(gameID).setValue(game);

        Intent lobbyIntent = new Intent(getApplicationContext(), LobbyActivity.class);
        lobbyIntent.putExtra(Game.class.getSimpleName().toLowerCase(), game);
        startActivity(lobbyIntent);
    }

    private void setQuizSpinnerData(String[] quizzes) {
        Spinner spinner = findViewById(R.id.lobbySetupQuiz);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item, quizzes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setDeviceSpinnerData(String[] devices) {
        Spinner spinner = findViewById(R.id.lobbySetupDevice);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item, devices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }
}
