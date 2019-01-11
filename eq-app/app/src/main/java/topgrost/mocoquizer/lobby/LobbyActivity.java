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

    private static final String[] TABLE_HEADERS = {"Player", "#"};
    private String firebaseGameKey;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user = sharedPref.getString("user", "NoUser");

        final Game game = (Game) getIntent().getSerializableExtra(Game.class.getSimpleName().toLowerCase());
        firebaseGameKey = game.getFirebaseKey();


        TextView title = findViewById(R.id.lobbyTitle);
        title.setText(game.getName());

        final SortableTableView<Game> tableView = findViewById(R.id.lobbyTable);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));

        TableColumnWeightModel columnModel = new TableColumnWeightModel(TABLE_HEADERS.length);
        columnModel.setColumnWeight(0, 5);
        columnModel.setColumnWeight(1, 1);
        tableView.setColumnModel(columnModel);


        int colorEvenRows = getResources().getColor(R.color.colorPrimaryDark);
        int colorOddRows = getResources().getColor(R.color.colorPrimary);
        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(colorEvenRows, colorOddRows));
        savePlayer();
        loadLobby();

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
                        intent.putExtra(QUESTION_COUNT_KEY, quiz.getQuestions().size());
                        intent.putExtra(PLAYER_NUMBER_KEY, 1);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(LobbyActivity.this, "Spiel konnte nicht gestartet werden. Fehler beim Laden der Quiz-Daten", Toast.LENGTH_LONG).show();
                        Log.d(LobbyActivity.class.getSimpleName(), databaseError.getMessage());
                    }
                });
            }
        });
    }

    private void loadLobby(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(firebaseGameKey);
        gameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> players = new ArrayList<>();
                final Game selectedGame = dataSnapshot.getValue(Game.class);
                players.add(selectedGame.getPlayer1());
                players.add(selectedGame.getPlayer2());
                players.add(selectedGame.getPlayer3());
                players.add(selectedGame.getPlayer4());
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

    private void savePlayer(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(firebaseGameKey);
        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("player1").exists()) {
                    gameRef.child("player1").setValue(user);
                }else if(!dataSnapshot.child("player2").exists()){
                    gameRef.child("player2").setValue(user);
                }else if(!dataSnapshot.child("player3").exists()){
                    gameRef.child("player3").setValue(user);
                }else if(!dataSnapshot.child("player4").exists()){
                    gameRef.child("player4").setValue(user);
                }else{
                    Toast.makeText(LobbyActivity.this, "Die Lobby ist voll", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),GameBrowserActivity.class);
                    startActivity(intent);
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
                .setPositiveButton("Ja", new DialogInterface.OnClickListener()
                {
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

    private void deletePlayer(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(firebaseGameKey);
        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Game game = dataSnapshot.getValue(Game.class);
                if(game.getPlayer1().equals(user)) {
                    dataSnapshot.child("player1").getRef().removeValue();
                }else if(game.getPlayer2().equals(user)) {
                    dataSnapshot.child("player2").getRef().removeValue();
                }else if(game.getPlayer3().equals(user)) {
                    dataSnapshot.child("player3").getRef().removeValue();
                }else if(game.getPlayer4().equals(user)) {
                    dataSnapshot.child("player4").getRef().removeValue();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(LobbyActivity.class.getSimpleName(), databaseError.getMessage());
            }
        });
    }
}