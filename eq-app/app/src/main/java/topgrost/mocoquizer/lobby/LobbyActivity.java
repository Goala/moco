package topgrost.mocoquizer.lobby;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;
import topgrost.mocoquizer.BaseActivity;
import topgrost.mocoquizer.R;
import topgrost.mocoquizer.browser.GameBrowserActivity;
import topgrost.mocoquizer.lobby.view.LobbyListAdapter;
import topgrost.mocoquizer.model.Game;
import topgrost.mocoquizer.model.Quiz;
import topgrost.mocoquizer.quiz.QuizActivity;

public class LobbyActivity extends BaseActivity {

    public static final String GAME_ID_KEY = "gameId";
    public static final String QUESTION_COUNT_KEY = "questionCount";
    public static final String PLAYER_NUMBER_KEY = "playerNumber";
    public static final String QUESTION_TIME_KEY = "questionTime";

    private static final String[] TABLE_HEADERS = {"#", "Player"};
    private String fireBaseGameKey;
    private String user;
    private FirebaseDatabase database;
    private Button btnStartGame;
    private int playerNumber;

    //Todo Event Listener zusammenfassen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        database = FirebaseDatabase.getInstance();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user = sharedPref.getString("user", "NoUser");

        final Game game = (Game) getIntent().getSerializableExtra(Game.class.getSimpleName().toLowerCase());
        fireBaseGameKey = game.getFirebaseKey();

        TextView title = findViewById(R.id.lobbyTitle);
        title.setText(game.getName());

        final SortableTableView<String> tableView = findViewById(R.id.lobbyTable);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));

        TableColumnWeightModel columnModel = new TableColumnWeightModel(TABLE_HEADERS.length);
        columnModel.setColumnWeight(0, 1);
        columnModel.setColumnWeight(1, 6);
        tableView.setColumnModel(columnModel);

        int colorEvenRows = getResources().getColor(R.color.colorPrimaryDark);
        int colorOddRows = getResources().getColor(R.color.colorPrimary);
        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(colorEvenRows, colorOddRows));
        savePlayer();
        loadLobby();
        autoStart();

        btnStartGame = findViewById(R.id.lobbyStartGame);
        btnStartGame.setEnabled(false);
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(fireBaseGameKey);
                try {
                    gameRef.child("running").setValue(true);
                } catch (Exception e) {
                    Toast.makeText(LobbyActivity.this, "Das Spiel konnte nicht gestartet werden.(Running Value)", Toast.LENGTH_LONG).show();
                    Log.e(LobbyActivity.class.getSimpleName(), e.getMessage());
                }
            }
        });
    }

    private void loadLobby() {
        final DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(fireBaseGameKey);
        gameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Game game = dataSnapshot.getValue(Game.class);
                if (game.isRunning()) {
                    gameRef.removeEventListener(this);
                    return;
                }

                List<String> players = new ArrayList<>();
                players.add(game.getPlayer1());
                players.add(game.getPlayer2());
                players.add(game.getPlayer3());
                players.add(game.getPlayer4());
                if (game.getPlayer1() != null) {
                    if (game.getPlayer1().equals(user)) {
                        btnStartGame.setEnabled(true);
                    }
                }
                final SortableTableView<String> tableView = findViewById(R.id.lobbyTable);
                tableView.setDataAdapter(new LobbyListAdapter(getBaseContext(), players));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LobbyActivity.this, "Fehler beim Laden der Lobby", Toast.LENGTH_LONG).show();
                Log.d(LobbyActivity.class.getSimpleName(), databaseError.getMessage());
            }
        });
    }

    private void savePlayer() {
        final DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(fireBaseGameKey);
        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("player1").exists()) {
                    gameRef.child("player1").setValue(user);
                    playerNumber = 1;
                    btnStartGame.setEnabled(true);
                } else if (!dataSnapshot.child("player2").exists()) {
                    playerNumber = 2;
                    gameRef.child("player2").setValue(user);
                } else if (!dataSnapshot.child("player3").exists()) {
                    playerNumber = 3;
                    gameRef.child("player3").setValue(user);
                } else if (!dataSnapshot.child("player4").exists()) {
                    playerNumber = 4;
                    gameRef.child("player4").setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LobbyActivity.this, "Fehler Betreten der Lobby", Toast.LENGTH_LONG).show();
                Log.d(LobbyActivity.class.getSimpleName(), databaseError.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit Lobby")
                .setMessage("Möchten Sie die Lobby verlassen?")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePlayer();
                        Intent intent = new Intent(getApplicationContext(), GameBrowserActivity.class);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("Nein", null)
                .show();
    }

    private void deletePlayer() {
        final DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(fireBaseGameKey);
        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    final Game game = dataSnapshot.getValue(Game.class);
                    if (game.getPlayer1().equals(user)) {
                        dataSnapshot.child("player1").getRef().removeValue();
                        //Set new player one to start game
                        if (dataSnapshot.child("player2").exists()) {
                            gameRef.child("player1").setValue(game.getPlayer2());
                            dataSnapshot.child("player2").getRef().removeValue();
                        } else if (dataSnapshot.child("player3").exists()) {
                            gameRef.child("player1").setValue(game.getPlayer3());
                            dataSnapshot.child("player3").getRef().removeValue();
                        } else if (dataSnapshot.child("player4").exists()) {
                            gameRef.child("player1").setValue(game.getPlayer4());
                            dataSnapshot.child("player4").getRef().removeValue();
                        }
                    } else if (game.getPlayer2().equals(user)) {
                        dataSnapshot.child("player2").getRef().removeValue();
                    } else if (game.getPlayer3().equals(user)) {
                        dataSnapshot.child("player3").getRef().removeValue();
                    } else if (game.getPlayer4().equals(user)) {
                        dataSnapshot.child("player4").getRef().removeValue();
                    }
                } catch (Exception e) {
                    Toast.makeText(LobbyActivity.this, "Fehler beim Verlassen der Lobby", Toast.LENGTH_LONG).show();
                    Log.d(LobbyActivity.class.getSimpleName(), e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LobbyActivity.this, "Fehler beim Verlassen der Lobby", Toast.LENGTH_LONG).show();
                Log.d(LobbyActivity.class.getSimpleName(), databaseError.getMessage());
            }
        });
    }

    public void autoStart() {
        final DatabaseReference runningRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(fireBaseGameKey).child("running");
        final ValueEventListener autoStartListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    final Boolean running = dataSnapshot.getValue(Boolean.class);
                    if (running == null || !running.booleanValue()) {
                        return;
                    }
                    startGame();
                    runningRef.removeEventListener(this);
                } catch (Exception e) {
                    Toast.makeText(LobbyActivity.this, "Fehler beim Starten des Spiels", Toast.LENGTH_LONG).show();
                    Log.e(LobbyActivity.class.getSimpleName(), e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LobbyActivity.this, "Fehler beim Starten des Spiels", Toast.LENGTH_LONG).show();
                Log.e(LobbyActivity.class.getSimpleName(), databaseError.getMessage());
            }
        };
        runningRef.addValueEventListener(autoStartListener);
    }

    public void startGame() {
        final Game game = (Game) getIntent().getSerializableExtra(Game.class.getSimpleName().toLowerCase());
        DatabaseReference quizRef = database.getReference(Quiz.class.getSimpleName().toLowerCase() + "s");
        quizRef.orderByChild("name").equalTo(game.getQuizId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Quiz quiz = dataSnapshot.getChildren().iterator().next().getValue(Quiz.class);
                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                    intent.putExtra(Quiz.class.getSimpleName().toLowerCase(), quiz);
                    intent.putExtra(GAME_ID_KEY, game.getFirebaseKey());
                    intent.putExtra(QUESTION_COUNT_KEY, game.getQuestionCount());
                    intent.putExtra(PLAYER_NUMBER_KEY, playerNumber);
                    intent.putExtra(QUESTION_TIME_KEY, game.getQuestionTime());
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(LobbyActivity.this, "Das Spiel konnte nicht gestartet werden.", Toast.LENGTH_LONG).show();
                    Log.e(LobbyActivity.class.getSimpleName(), e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LobbyActivity.this, "Das Spiel konnte nicht gestartet werden.", Toast.LENGTH_LONG).show();
                Log.e(LobbyActivity.class.getSimpleName(), databaseError.getMessage());
            }
        });
    }
}