package topgrost.mocoquizer.browser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    //Todo User kann nur beitreten, wenn er noch nicht im Spiel ist
    //Catch Blöcke bei Datenbankanwendungen

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
                final List<Game> games = new LinkedList<>();
                for (DataSnapshot gameDataSnapshot : dataSnapshot.getChildren()) {
                    final Game gameToAdd = gameDataSnapshot.getValue(Game.class);
                    gameToAdd.setFirebaseKey(gameDataSnapshot.getKey());
                    games.add(gameToAdd);
                }

                final SortableTableView<Game> tableView = findViewById(R.id.gameBrowserTable);
                tableView.setDataAdapter(new GameBrowserListAdapter(getBaseContext(), games));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GameBrowserActivity.this, "Fehler beim Laden der Spieleliste", Toast.LENGTH_LONG).show();
            }
        });

        final SortableTableView<Game> tableView = findViewById(R.id.gameBrowserTable);
        tableView.setDataAdapter(new GameBrowserListAdapter(getBaseContext(), new LinkedList<Game>()));
    }

    @Override
    public void onDataClicked(int rowIndex, Game selectedGame) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s");

        gameRef.child(selectedGame.getFirebaseKey()).child("player1").setValue(user);
        gameRef.child(selectedGame.getFirebaseKey()).child("feed1").setValue(false);

        Intent intent = new Intent(getApplicationContext(), LobbyActivity.class);
        intent.putExtra(Game.class.getSimpleName().toLowerCase(), selectedGame);
        startActivity(intent);
    }

}
