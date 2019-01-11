package topgrost.mocoquizer.lobby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

        NumberPicker npTimeSeconds = findViewById(R.id.lobbySetupTimeSeconds);
        npTimeSeconds.setMinValue(6);
        npTimeSeconds.setMaxValue(30);
        npTimeSeconds.setValue(8);

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
                Log.d(LobbySetupActivity.class.getSimpleName(), databaseError.getMessage());
            }
        });

        setDeviceSpinnerData(new String[]{"Loading..."});
    }

    private void loadAndSetQuizList() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child(Quiz.class.getSimpleName().toLowerCase() + "s").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Quiz> quizzes = new ArrayList<>();

                for (DataSnapshot quizDataSnapshot : dataSnapshot.getChildren()) {
                    Quiz quiz = quizDataSnapshot.getValue(Quiz.class);
                    quizzes.add(quiz);
                }

                Quiz[] primQuizzes = new Quiz[quizzes.size()];
                primQuizzes = quizzes.toArray(primQuizzes);

                setQuizSpinnerData(primQuizzes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LobbySetupActivity.this, "Fehler beim Laden der verfügbaren Quiz-Daten", Toast.LENGTH_LONG).show();
                Log.d(LobbySetupActivity.class.getSimpleName(), databaseError.getMessage());
            }
        });

        // TODO fix in rederer
        //setQuizSpinnerData(new Quiz[]{"Loading..."});
    }

    @Override
    public void onClick(View view) {
        Spinner spinner = findViewById(R.id.lobbySetupQuiz);
        Quiz selectedQuiz = (Quiz) spinner.getSelectedItem();

        Game game = new Game();
        game.setRunning(false);
        game.setName(((TextView) findViewById(R.id.lobbySetupName)).getText().toString());
        game.setPassword(((TextView) findViewById(R.id.lobbySetupPassword)).getText().toString());
        game.setDeviceId(((Spinner) findViewById(R.id.lobbySetupDevice)).getSelectedItem().toString());
        game.setQuizId(selectedQuiz.getName());
        game.setPlayer1(user);
        game.setFeed1(false);
        game.setQuestionNr(0);
        game.setQuestionCount(selectedQuiz.getQuestions().size());
        game.setQuestionTime(((NumberPicker) findViewById(R.id.lobbySetupTimeSeconds)).getValue());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s");

        String gameID = gameRef.push().getKey();
        game.setFirebaseKey(gameID);
        gameRef.child(gameID).setValue(game);

        Intent lobbyIntent = new Intent(getApplicationContext(), LobbyActivity.class);
        lobbyIntent.putExtra(Game.class.getSimpleName().toLowerCase(), game);
        startActivity(lobbyIntent);
    }

    private void setQuizSpinnerData(Quiz[] quizzes) {
        Spinner spinner = findViewById(R.id.lobbySetupQuiz);
        ArrayAdapter<Quiz> adapter = new ArrayAdapter<>(getBaseContext(),
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
