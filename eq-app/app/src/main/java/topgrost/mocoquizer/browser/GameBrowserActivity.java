package topgrost.mocoquizer.browser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;
import topgrost.mocoquizer.BaseActivity;
import topgrost.mocoquizer.MainActivity;
import topgrost.mocoquizer.R;
import topgrost.mocoquizer.browser.view.GameBrowserListAdapter;
import topgrost.mocoquizer.browser.view.GameNameComparator;
import topgrost.mocoquizer.browser.view.GamePasswordComparator;
import topgrost.mocoquizer.browser.view.GamePlayersComparator;
import topgrost.mocoquizer.browser.view.GameRunningComparator;
import topgrost.mocoquizer.lobby.LobbyActivity;
import topgrost.mocoquizer.model.Game;

public class GameBrowserActivity extends BaseActivity implements TableDataClickListener<Game> {

    private String user;

    private static final String[] TABLE_HEADERS = {"Name", "Status", "Spieler", "Passwort"};



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user = sharedPref.getString("user", "");

        setContentView(R.layout.game_browser);

        setupGameBrowserTable();
        loadGames();
    }

    void setupGameBrowserTable() {
        final SortableTableView<Game> tableView = findViewById(R.id.gameBrowserTable);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));

        TableColumnWeightModel columnModel = new TableColumnWeightModel(TABLE_HEADERS.length);
        columnModel.setColumnWeight(0, 3);
        columnModel.setColumnWeight(1, 2);
        columnModel.setColumnWeight(2, 2);
        columnModel.setColumnWeight(3,2);
        tableView.setColumnModel(columnModel);

        // setup coloring of rows
        int colorEvenRows = getResources().getColor(R.color.colorPrimaryDark);
        int colorOddRows = getResources().getColor(R.color.colorPrimary);
        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(colorEvenRows, colorOddRows));

        // Set comparators to allow sorting
        tableView.setColumnComparator(0, new GameNameComparator());
        tableView.setColumnComparator(1, new GameRunningComparator());
        tableView.setColumnComparator(2, new GamePlayersComparator());
        tableView.setColumnComparator(3, new GamePasswordComparator());

        tableView.addDataClickListener(this);
    }

    private void loadGames() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child(Game.class.getSimpleName().toLowerCase() + "s").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    final List<Game> games = new LinkedList<>();
                    for (DataSnapshot gameDataSnapshot : dataSnapshot.getChildren()) {
                        final Game gameToAdd = gameDataSnapshot.getValue(Game.class);
                        //Todo disable running games instead of not showing them
                        if(!gameToAdd.isRunning()) {
                            gameToAdd.setFirebaseKey(gameDataSnapshot.getKey());
                            games.add(gameToAdd);
                        }
                    }

                    final SortableTableView<Game> tableView = findViewById(R.id.gameBrowserTable);
                    tableView.setDataAdapter(new GameBrowserListAdapter(getBaseContext(), games));
                }catch(Exception e){
                    Toast.makeText(GameBrowserActivity.this, "Fehler beim Laden der Spieleliste", Toast.LENGTH_LONG).show();
                    Log.d(GameBrowserActivity.class.getSimpleName(), e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GameBrowserActivity.this, "Fehler beim Laden der Spieleliste", Toast.LENGTH_LONG).show();
                Log.d(GameBrowserActivity.class.getSimpleName(), databaseError.getMessage());
            }
        });

        final SortableTableView<Game> tableView = findViewById(R.id.gameBrowserTable);
        tableView.setDataAdapter(new GameBrowserListAdapter(getBaseContext(), new LinkedList<Game>()));
    }

    @Override
    public void onDataClicked(int rowIndex, Game selectedGame) {
        if(isGameFull(selectedGame)) {
            Toast.makeText(GameBrowserActivity.this, "Spiel ist voll", Toast.LENGTH_SHORT).show();
            Log.d(GameBrowserActivity.class.getSimpleName(), "User " + user + " tried to join full game " + selectedGame.getName());
            return;
        }
        final String password = selectedGame.getPassword();
        if(password != null && !password.trim().isEmpty()) {
            checkPassword(selectedGame);
            return;
        }
        showLobby(selectedGame);
    }

    private void showLobby(Game game) {
        Intent intent = new Intent(getApplicationContext(), LobbyActivity.class);
        intent.putExtra(Game.class.getSimpleName().toLowerCase(), game);
        startActivity(intent);
    }

    private void checkPassword(final Game game) {
        final EditText input = new EditText(GameBrowserActivity.this);
        input.requestFocus();

        new AlertDialog.Builder(GameBrowserActivity.this)
                .setTitle("Passwort benötigt")
                .setMessage("Bitte geben Sie das Passwort ein")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        if(game.getPassword().equals(value.toString())) {
                            showLobby(game);
                        } else {
                            Toast.makeText(GameBrowserActivity.this, "Passwort falsch", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    private boolean isGameFull(Game gameToCheck) {
        List<String> players = new LinkedList<>();
        players.add(gameToCheck.getPlayer1());
        players.add(gameToCheck.getPlayer2());
        players.add(gameToCheck.getPlayer3());
        players.add(gameToCheck.getPlayer4());

        int playerCount = 0;
        for(int i = 0; i < players.size(); i++) {
            if (isPlayerSet(players.get(i))) {
                playerCount++;
            }
        }
        return playerCount >= 4;
    }

    private boolean isPlayerSet(String player) {
        if(user.equals(player) || player == null || player.trim().isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
