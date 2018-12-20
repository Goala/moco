package topgrost.mocoquizer.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.model.Device;
import topgrost.mocoquizer.model.Game;
import topgrost.mocoquizer.model.Quiz;

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
        game.setUserIds(new ArrayList<>(Arrays.asList("currentUser (todo tobi)", "", "", "")));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s");
        gameRef.push().setValue(game);

        startActivity(new Intent(getApplicationContext(), LobbyActivity.class));
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
